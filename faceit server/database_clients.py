import sqlite3


class Clients:
    def __init__(self):
        self.conn = sqlite3.connect("clients.db")
        self.cursor = self.conn.cursor()

        self.cursor.execute("CREATE TABLE IF NOT EXISTS clients "
                            "(name TEXT PRIMARY KEY, hashed_password TEXT, Email TEXT)")

    def insert(self, name, password, email):
        try:
            self.cursor.execute("INSERT INTO clients VALUES "
                                "('{}', '{}', '{}')".format(name, password, email))
            self.conn.commit()
            return True  # added successfully
        except sqlite3.IntegrityError:
            return False  # this name was already taken

    def return_data(self):
        table = ""
        for row in self.cursor.execute("SELECT * FROM clients ORDER BY name"):
            table += str(row) + "\n"
        self.conn.commit()
        return table

    def return_pass(self, name):
        cur = self.cursor.execute("SELECT hashed_password FROM clients WHERE name=?", (name,))
        fetch = cur.fetchall()
        f = fetch[0]
        return f[0]