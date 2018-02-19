import sqlite3
import json

class Go:
    def __init__(self):
        self.conn = sqlite3.connect("Go.db")
        self.cursor = self.conn.cursor()

        self.cursor.execute("CREATE TABLE IF NOT EXISTS Go "
                            "(name TEXT PRIMARY KEY, my_emotion TEXT, i_want TEXT)")

    def insert(self, name, my_emo, i_want_emo):
        try:
            self.cursor.execute("INSERT INTO Go VALUES "
                                "('{}', '{}', '{}')".format(name, my_emo, i_want_emo))
            self.conn.commit()
            return True  # added successfully
        except sqlite3.IntegrityError:
            return False  # this name was already taken

    def return_data(self):
        table = ""
        for row in self.cursor.execute("SELECT * FROM Go ORDER BY name"):
            table += str(row)
        self.conn.commit()
        return table

    def return_want(self, name):
        cur = self.cursor.execute("SELECT i_want FROM Go WHERE name=?", (name,))
        fetch = cur.fetchall()
        f = fetch[0]
        return f[0]


if __name__ == "__main__":
    a = Go()
    y_emo = 'sad'
    i_want = 'happy'
    b = a.insert("dani", y_emo, i_want)
    print b
    data = a.return_data()
    print data
    d = json.load(data)
    print d