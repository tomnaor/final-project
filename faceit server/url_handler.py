import socket

server_socket = socket.socket()
server_socket.bind(('0.0.0.0', 1234))
server_socket.listen(1)
(client_socket, client_address) = server_socket.accept()
img_len = client_socket.recv(1024)
print "image length is: " + img_len
img = ""
while True:
    img += client_socket.recv(4096)
    if len(img) == int(img_len):
        break

print "image received successfully"
file_img = open("img.jpg", "wb")
file_img.write(img)
file_img.close()


server_socket.close()
