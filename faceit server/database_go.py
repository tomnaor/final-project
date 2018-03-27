import sqlite3
import json


class Go:
    def __init__(self):
        self.conn = sqlite3.connect("Go.db")
        self.cursor = self.conn.cursor()

        self.cursor.execute("CREATE TABLE IF NOT EXISTS Go "
                            "(name TEXT PRIMARY KEY, my_emotion TEXT, i_want TEXT, time INTEGER)")

    def insert(self, name, my_emo, i_want_emo):
        try:
            self.cursor.execute("INSERT INTO Go VALUES "
                                "('{}', '{}', '{}', {})".format(name, my_emo, i_want_emo, 900))
            self.conn.commit()
            return True  # added successfully
        except sqlite3.IntegrityError:
            return False  # this name was already taken

    def return_data(self):
        table = "{"
        for row in self.cursor.execute("SELECT * FROM Go ORDER BY name"):
            table += '\"' + row[0] + '\"' + ": [" + '\"' + row[1] + '\"' + ", " + '\"' + row[2] + "\"], "
        self.conn.commit()
        return table[:-2] + "}"

    def return_time(self, name):
        cur = self.cursor.execute("SELECT time FROM Go WHERE name=?", (name,))
        fetch = cur.fetchall()
        f = fetch[0]
        return f[0]

    def update_time(self, name, time):
        self.cursor.execute("UPDATE Go SET time=? WHERE name=?", (time, name,))
        self.conn.commit()

    def return_want(self, name):
        cur = self.cursor.execute("SELECT i_want FROM Go WHERE name=?", (name,))
        fetch = cur.fetchall()
        f = fetch[0]
        return f[0]

    def delete_row(self, name):
        self.cursor.execute("DELETE FROM Go WHERE name=?", (name,))
        self.conn.commit()



# if __name__ == "__main__":
#     a = Go()
#     y_emo = 'sad'
#     i_want = 'sad'
#     b = a.insert("Erez", y_emo, i_want)
#     print b
#     data = a.return_data()
#     print(data)
#     data_dict = json.loads(data)
#     a = False
#     print type(data_dict)
#     for z in "ab":
#         for key, value in data_dict.iteritems():
#             for key2, value2 in data_dict.iteritems():
#                 if key != key2 and value[::-1] == value2:
#                     print("True: " + key + " and " + key2)
#                     del data_dict[key]
#                     del data_dict[key2]
#                     a = True
#                     break
#             if a:
#                 break