import database_go
import json
import socket


def main():
    while True:
        a = database_go.Go()
        data = a.return_data()
        if data != "}":
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
                        s = socket.socket()
                        s.connect(("127.0.0.1", 1234))
                        s.send("Thread: " + "{\"" + key + "\": \"" + value[0] + "\", \"" + key2 + "\": \"" +
                               value2[0] + "\"}")
                        s.close()
                        break
                if check:
                    break


if __name__ == '__main__':
    main()