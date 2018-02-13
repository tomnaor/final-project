import sqlite3


class Clients:
    def __init__(self):
        self.conn = sqlite3.connect("clients.db")
        self.cursor = self.conn.cursor()

        self.cursor.execute("CREATE TABLE IF NOT EXISTs clients "
                            "(ip_address TEXT, name TEXT PRIMARY KEY, hashed_password TEXT, Email TEXT)")

    def Insert(self, ip, name, password, email):
        try:
            self.cursor.execute("INSERT INTO clients VALUES "
                                "('{}', '{}', '{}', '{}')".format(ip, name, password, email))
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
        return self.cursor.execute("SELECT hashed_password FROM clients WHERE name = {}".format(name))