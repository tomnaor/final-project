import re
import json
import database_clients


class Chat():
    def __init__(self, data):
        self.data = data
        self.database_clients = database_clients.Clients()

    def build_message(self):
        """
         take the data - 'Chat: {"username_of_sender": ["username_of_receiver", "message"]}'
         and return a message for the receiver client
         the message is  - 'Chat: username_of_sender, message'
        """
        regex = r"Chat:(.*)"
        match = re.search(regex, self.data)
        this_data = match.group(1)
        msg_dict = json.loads(this_data)
        sender_name = msg_dict.items()[0][0]
        access = self.database_clients.check_if_in_database(sender_name)
        if access:
            receiver_name = msg_dict.items()[0][1][0]
            message = msg_dict.items()[0][1][1]
            new_message = receiver_name, "Chat: " + sender_name + ", " + message
            return new_message
        else:
            return False