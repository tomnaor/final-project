import database_go
import json
import socket


def run_go():
    while True:
        a = database_go.Go()
        data = a.return_data()
        if data != "}":
            data_dict = json.loads(data)
            for key, value in data_dict.iteritems():
                time = a.return_time(key)
                a.update_time(key, time-1)
                check = False
                for key2, value2 in data_dict.iteritems():
                    time2 = a.return_time(key2)
                    a.update_time(key2, time2-1)
                    if key != key2 and value[::-1] == value2 and time2 > 0 and time > 0:
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
                    else:
                        if time <= 0:
                            s = socket.socket()
                            s.connect(("127.0.0.1", 1234))
                            s.send("ThreadNo: " + key)
                            a.delete_row(key)
                        if time2 <= 0:
                            s = socket.socket()
                            s.connect(("127.0.0.1", 1234))
                            s.send("ThreadNo: " + key2)
                            a.delete_row(key2)
                if check:
                    break

if __name__ == "__main__":
    run_go()