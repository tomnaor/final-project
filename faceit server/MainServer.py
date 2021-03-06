import socket
import select
import signup
import login
import re
import json
import face_recognition
import database_go
import database_clients
import thread
from go_handler import run_go
import client_chat

IP = "0.0.0.0"
PORT = 1234
LISTEN_NUM = 5
open_client_sockets = []
open_sockets_names = {}
messages_to_send = []


class Server():
    """
    a basic server class that connect with the client.
    """
    def __init__(self, ip, port, listen_num):
        """
        the constructor of the server class.
        receive: ip address
                 the port
                 number of the socket can listen
        """
        self.ip = ip
        self.port = port
        self.listen_num = listen_num
        self.server_socket = socket.socket()
        self.go_users = database_go.Go()
        thread.start_new_thread(run_go, ())

    def server_connection(self):
        """
        make the server's socket.
        make the connection between the client.
        """
        self.server_socket.bind((self.ip, self.port))
        self.server_socket.listen(self.listen_num)

    def accept1(self):
        """

        receive: ip address
                 the port
                 number of the socket can listen
        """
        (client_socket, client_address) = self.server_socket.accept()
        return client_socket, client_address

    def login(self, data):
        regex = r"Login:(.*)"
        match = re.search(regex, data)
        l_dict = match.group(1)
        log_dict = json.loads(l_dict)
        user = login.Login(log_dict['username'], log_dict['password'])
        res = user.check_password()
        return res, log_dict['username']

    def signup(self, data):
        regex = r"SignUp:(.*)"
        match = re.search(regex, data)
        s_dict = match.group(1)
        sign_dict = json.loads(s_dict)
        user = signup.SignUp(sign_dict['username'],
                             sign_dict['password'], sign_dict['email'])
        res = user.put_in_database()
        return res, sign_dict['username']

    def add_to_go(self, name, my_emotion, wanted_emotion):
        self.go_users.insert(name, my_emotion, wanted_emotion)

    def thread_go(self):
        while True:
            data = self.go_users.return_data()
            data_dict = json.loads(data)
            for key, value in data_dict.iteritems():
                check = False
                for key2, value2 in data_dict.iteritems():
                    if key != key2 and value[::-1] == value2:
                        del data_dict[key]
                        del data_dict[key2]
                        self.go_users.delete_row(key)
                        self.go_users.delete_row(key2)
                        check = True
                        s = socket.socket()
                        s.connect(("127.0.0.1", 1234))
                        s.send("Thread: " + "[" + key + ", " + key2 + "]")
                        s.close()
                        break
                if check:
                    break

    def handle_msg(self, msg):
        return msg + "\r\n"

    def check_if_client(self):
        while True:
            rlist, wlist, xlist = select.select([self.server_socket] + open_client_sockets,  open_client_sockets, [])
            for current_socket in rlist:
                if current_socket is self.server_socket:
                    print "new client!"
                    (new_socket, address) = self.server_socket.accept()
                    open_client_sockets.append(new_socket)
                else:
                    try:
                        data = current_socket.recv(4096)
                    except socket.error:
                        print "Connection with client closed."
                        try:
                            open_client_sockets.remove(current_socket)
                            del open_sockets_names[current_socket]
                        except Exception as e:
                            print e.args
                    # if "Image_face" not in data:
                    print data
                    if data == "":
                        try:
                            open_client_sockets.remove(current_socket)
                            del open_sockets_names[current_socket]
                        except Exception as e:
                            print e.args
                        print "Connection with client closed."
                    elif "SignUp:" in data:  # 'SignUp: {"username": "tom", "password":
                    # "hashed_pass", "email": "example@gmail.com"}'
                        try:
                            res, username = self.signup(data)
                            if res:
                                open_sockets_names[current_socket] = username  # update name of user -
                                # need to check if it is working
                                print "added successfully"
                                output = "SignUpTrue"
                                current_socket.send(self.handle_msg(output))
                            else:
                                print "this name was already taken"
                                output = "SignUpFalse"
                                current_socket.send(self.handle_msg(output))
                        except Exception as e:
                            print e.args
                            output = "SignUpFalse"
                            current_socket.send(self.handle_msg(output))
                    elif "Login" in data:  # 'Login: {"username": "tom", "password": "hashed_pass"}'
                        try:
                            res, username = self.login(data)
                            if res:
                                open_sockets_names[current_socket] = username
                                print "correct password"
                                output = "LoginTrue"
                                current_socket.send(self.handle_msg(output))
                            else:
                                print "not the same password"
                                output = "LoginFalse"
                                current_socket.send(self.handle_msg(output))
                        except Exception as e:
                            print e.args
                            output = "LoginFalse"
                            current_socket.send(self.handle_msg(output))
                    elif "Image_face" in data:  # 'Image_face'
                        try:
                            regex = r"Image_face:(.*)"
                            match = re.search(regex, data)
                            size = match.group(1)
                            print "size:" + size
                            data = current_socket.recv(int(size))
                            while int(size) != len(data):
                                data += current_socket.recv(int(size))
                            face = face_recognition.Face(data)
                            res = face.recognition()
                            if "False" in res:
                                print res
                                output = "ImageFalse"
                                current_socket.send(self.handle_msg(output))
                            else:
                                print "successful face recognition!"
                                print "The emotion is: " + res
                                output = "Emotion: " + res
                                current_socket.send(self.handle_msg(output))
                        except Exception as e:
                            print e.args
                            output = "ImageFalse"
                            current_socket.send(self.handle_msg(output))
                    elif "GoChat" in data:  # 'GoChat: {"tom": ["my_emotion", "the_emotion_i_want"]}'
                        try:
                            regex = r"GoChat:(.*)"
                            match = re.search(regex, data)
                            this_data = match.group(1)
                            emotions_dict = json.loads(this_data)
                            user_name = emotions_dict.items()[0][0]
                            access = True
                            try:
                                a_data = database_clients.Clients()
                                a_data.return_pass(user_name)
                            except IndexError:
                                access = False
                                print "this client does not exists"
                                print self.go_users.return_data()
                            if access:
                                try:
                                    print open_sockets_names[current_socket]
                                except KeyError:
                                    print "this client was already signed up or logged in"
                                    open_sockets_names[current_socket] = user_name
                                emotions = emotions_dict.items()[0][1]
                                self.add_to_go(user_name, emotions[0], emotions[1])
                        except Exception as e:
                            print e.args
                            output = "GoFalse"
                            current_socket.send(self.handle_msg(output))
                    elif "Thread" in data:
                        if "ThreadNo" in data:
                            user_name3 = data[10:]
                            print user_name3
                            for key, value in open_sockets_names.iteritems():
                                if user_name3 == value:
                                    output = "ThreadFalse"
                                    key.send(self.handle_msg(output))
                        else:
                            try:
                                regex = r"Thread:(.*)"
                                match = re.search(regex, data)
                                this_data = match.group(1)
                                go_dict = json.loads(this_data)
                                user_name1 = go_dict.items()[0][0]
                                emotion1 = go_dict.items()[0][1]
                                user_name2 = go_dict.items()[1][0]
                                emotion2 = go_dict.items()[1][1]
                                for key, value in open_sockets_names.iteritems():
                                    if user_name1 == value:
                                        output = "Thread: " + user_name2 + ", " + emotion2
                                        print output
                                        key.send(self.handle_msg(output))
                                    if user_name2 == value:
                                        output = "Thread: " + user_name1 + ", " + emotion1
                                        print output
                                        key.send(self.handle_msg(output))
                            except Exception as e:
                                print e.args
                    elif "Chat" in data:
                        chat = client_chat.Chat(data)
                        receiver_name, msg = chat.build_message()
                        for key, value in open_sockets_names.iteritems():
                                    if receiver_name == value:
                                        key.send(self.handle_msg(msg).encode('utf8'))
                        print msg
                    else:
                        pass

if __name__ == "__main__":
    a = Server("0.0.0.0", 1234, 5)
    a.server_connection()
    a.check_if_client()
