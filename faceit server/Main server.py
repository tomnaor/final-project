import socket
import select
import signup
import login
import re
import ast

IP = "0.0.0.0"
PORT = 1234
LISTEN_NUM = 5
open_client_sockets = []
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


    def check_if_client(self):
        rlist, wlist, xlist = select.select([self.server_socket] + open_client_sockets,  open_client_sockets, [])
        for current_socket in rlist:
            if current_socket is self.server_socket:
                print "new client!"
                (new_socket, address) = self.server_socket.accept()
                open_client_sockets.append(new_socket)
            else:
                data = current_socket.recv(1024)
                if data == "":
                    open_client_sockets.remove(current_socket)
                    print "Connection with client closed."
                elif "SignUp:" in data:
                    regex = r"SignUp: (.*)"
                    match = re.search(regex, data)
                    s_dict = match.group(1)
                    dict = ast.literal_eval(s_dict)
                    user = signup.SignUp(dict['ip_address'], dict['username'],
                                         dict['password'], dict['email'])
                    res = user.put_in_database()
                    if res:
                        print "added successfully"
                        current_socket.send(True)
                    else:
                        print "this name was already taken"
                        current_socket.send(False)
                elif "Login" in data:
                    regex = r"Login: (.*)"
                    match = re.search(regex, data)
                    s_dict = match.group(1)
                    dict = ast.literal_eval(s_dict)
                    user = login.Login(dict['username'], dict['password'])
                    res = user.check_password()
                    if res:
                        print "correct password"
                        current_socket.send(True)
                    else:
                        print "not the same password"
                        current_socket.send(False)
                else:
                    pass
