import socket
import database_go
import json


def go():
    a = database_go.Go()
    while True:
            data_r = a.return_data()
            print data_r
            data_dict = json.loads(data)
            for key, value in data_dict.iteritems():
                check = False
                for key2, value2 in data_dict.iteritems():
                    if key != key2 and value[::-1] == value2:
                        del data_dict[key]
                        del data_dict[key2]
                        a.delete_row(key)
                        a.delete_row(key2)
                        check = True
                        for socket_user, name in open_sockets_names.iteritems():
                            if name == key:
                                socket_user.send(key2)
                            elif name == key2:
                                socket_user.send(key)
                        break
                if check:
                    break


s = socket.socket()
s.connect(("127.0.0.1", 1234))

s.send('Login: {"username": "tom2", "password": "hashed_pass"}')
data = s.recv(1024)
print data

s.send('Go: {"tom": ["happy", "sad"]}')
s.send('Go: {"tom2": ["sad", "happy"]}')
data = s.recv(1024)
print data

