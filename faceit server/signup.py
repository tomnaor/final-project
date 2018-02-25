import uuid
import hashlib
import database_clients
 

class SignUp():
    def __init__(self, username, password, email):
        self.username = username
        self.password = password
        self.hash_password()
        self.email = email
        self.database = database_clients.Clients()

    def hash_password(self):
        # uuid is used to generate a random number
        salt = uuid.uuid4().hex
        self.password = hashlib.sha256(salt.encode() +
                                       self.password.encode()).hexdigest() + ':' + salt

    def put_in_database(self):
        res = self.database.insert(self.username, self.password, self.email)
        return res