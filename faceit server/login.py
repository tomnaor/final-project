import hashlib
import database_clients


class Login():
    def __init__(self, username, password):
        self.username = username
        self.password = password
        self.database = database_clients.Clients()
        self.real_pass = self.pull_from_database()

    def pull_from_database(self):
        return self.database.return_pass(self.username)

    def check_password(self):
        this_pass, salt = self.real_pass.split(':')
        return this_pass == hashlib.sha256(salt.encode() + self.password.encode()).hexdigest()