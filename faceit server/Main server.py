import socket
import select
import signup
import login
import re
import json
import face_recognition

IP = "0.0.0.0"
PORT = 1234
LISTEN_NUM = 5
open_client_sockets = []
messages_to_send = []
go_users = {}


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
        return res

    def signup(self, data):
        regex = r"SignUp:(.*)"
        match = re.search(regex, data)
        s_dict = match.group(1)
        sign_dict = json.loads(s_dict)
        user = signup.SignUp(sign_dict['ip_address'], sign_dict['username'],
                             sign_dict['password'], sign_dict['email'])
        res = user.put_in_database()
        return res

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
                        data = current_socket.recv(1024)
                    except socket.error:
                        print "Connection with client closed."
                    print data
                    if data == "":
                        open_client_sockets.remove(current_socket)
                        print "Connection with client closed."
                    elif "SignUp:" in data:
                        try:
                            res = self.signup(data)
                            if res:
                                print "added successfully"
                                current_socket.send("True")
                            else:
                                print "this name was already taken"
                                current_socket.send("False")
                        except Exception as e:
                            print e.args
                            current_socket.send("False")
                    elif "Login" in data:
                        try:
                            res = self.login(data)
                            if res:
                                print "correct password"
                                current_socket.send("True")
                            else:
                                print "not the same password"
                                current_socket.send("False")
                        except Exception as e:
                            print e.args
                            current_socket.send("False")
                    elif "Image_face" in data:
                        regex = r"Image_face:(.*)"
                        match = re.search(regex, data)
                        f_dict = match.group(1)
                        face_dict = json.loads(f_dict)
                        face_data = face_dict["data"]
                        face = face_recognition.Face(face_data)
                        res = face.recognition()
                        if "False" in res:
                            print res
                        else:
                            print "successful face recognition!"
                            print "The emotion is: " + res
                        current_socket.send(res)
                    elif "GO" in data:
                        regex = r"Go:(.*)"
                        match = re.search(regex, data)
                        this_data = match.group(1)
                        emotions = json.loads(this_data)
                        go_users[current_socket] = emotions
                        # for user, emotions in go_users.iteritems():
                        #     if emotions[1] ==
                    else:
                        pass

if __name__ == '__main__':
    a = Server("0.0.0.0", 1234, 5)
    a.server_connection()
    a.check_if_client()
