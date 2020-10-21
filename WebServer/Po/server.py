import base64
from socket import *
import _thread
import xml.etree.ElementTree as ET
from xml.dom import minidom

serverSocket = socket(AF_INET, SOCK_STREAM)

serverPort = 8083

serverSocket.bind(("", serverPort))

serverSocket.listen(5)
print('The server is running')


# Server should be up and running and listening to the incoming connections


def process(connectionSocket):
    try:
        # Receives the request message from the client
        message = connectionSocket.recv(1024)

        message = message.decode(encoding='UTF-8')
        print(message)

        # Only continues if the message is not blank
        if message:
            filename = message.split()[1][1:]
            method = message.split()[0]
            print('File name is: ' + filename)
            print("Method is " + method)

            if method == "GET":
                if filename.endswith("html") or filename.endswith(('png', 'jpg', 'jpeg')):
                    # Calls the function to send the response as there is no additional checking required
                    send_file(filename)

                elif filename == "status.xml":
                    # The referer determines where the message is being sent from, another server or from inside html
                    referer = message.split("Referer: ")[1].split("\n")[0].split("/")[-1].strip()

                    if referer == "friends.html":
                        # Sending the returned xml from 'show_friends' of all the status posts back to the html
                        r = show_friends()
                        connectionSocket.send(("HTTP/1.1 200 OK\r\nContent-Type:document/xml\r\n\r\n").encode() + r)
                        connectionSocket.close()

                    else:
                        # This is the request send from another server asking for the status file
                        # Checking whether the server who requested the file is in the friends list
                        friend = False
                        # Parsing the friends list of this server
                        xmlDoc = minidom.parse("friends.xml")
                        friends = xmlDoc.getElementsByTagName("name")
                        for i in range(friends.length):
                            name = friends[i].childNodes[0].nodeValue
                            if name == referer:
                                # When they are in fact friends
                                friend = True
                        if friend:
                            # Access to the status file is granted and therefore the status file is sent as normal
                            send_file(filename)
                        else:
                            # Access is not granted and a 403 access error is sent instead of the requested file
                            print("Forbidden Access: " + referer + " not in friends list")
                            print()
                            connectionSocket.send("HTTP/1.1 403 Forbidden Access\r\n\r\n".encode() + (
                                    "<html><head></head><body><h1>403 Forbidden Access</h1><br><h3>" + referer + " is not friends with you</h3></body></html>\r\n").encode())
                            connectionSocket.close()
            else:
                # It is a POST request
                # Works out the sender as above
                referer = message.split("Referer: ")[1].split("\n")[0].split("/")[-1].strip()
                if referer == "update.html":
                    # This is the request to update the status file
                    # The post message include the contents for the post split with '///'
                    status = message.split("///")[1:]

                    # Parses the status file into an ElementTree
                    tree = ET.parse(filename)
                    root = tree.getroot()

                    # Creating a new post element with the appropriate SubElements
                    newPost = ET.Element("post")
                    newText = ET.SubElement(newPost, "text")
                    newDate = ET.SubElement(newPost, "timestamp")
                    newLikes = ET.SubElement(newPost, "likes")

                    # The status text and status date are populated with the data that was sent in the request
                    # The status likes are left blank as there are not yet any likes
                    newText.text = status[0]
                    newDate.text = status[1]

                    # Appends the post element to the current status tree
                    root.append(newPost)
                    # Makes sure the indents are correct
                    indent(root)
                    # Writes the updates tree to the file, making sure to include the xml_declaration
                    tree.write(filename, encoding='UTF-8', xml_declaration=True)
                    send_file(filename)
                elif referer == "friends.html":
                    # This is the request to send a request to add a like to the correct status
                    name = message.split("///")[1:][0]

                    # Finding the port number to communicate to the correct server to update their status file
                    # The friend's xml is parsed and the friends elements are iterated through
                    xmlDoc = minidom.parse("friends.xml")
                    friends = xmlDoc.getElementsByTagName("name")
                    for i in range(friends.length):
                        # Checking if the current friend is the one we're looking for
                        if friends[i].childNodes[0].nodeValue == name:
                            # The correct friend is found and their port number is set
                            port = xmlDoc.getElementsByTagName("server_port")[i].childNodes[0].nodeValue
                            # The client function is called for this server to act as the client to contact another server
                            client(filename, int(port), "POST")

                    send_file(filename)
                else:
                    # This is the request to add a like to the recent status
                    # The status file is parsed into an ElementTree
                    tree = ET.parse(filename)
                    root = tree.getroot()
                    # The number of posts in the file to find the most resent one
                    length = len(root.findall("post")) - 1

                    # Finds the like element of the most recent post
                    likesElement = root[length].find("likes")

                    # Creates a new like element and sets its name to be the name of the server who requested
                    newLike = ET.SubElement(likesElement, "like")
                    newLike.text = referer
                    # Indent the file correctly again after the addition of a like
                    indent(root)
                    # Write to file to save the changes
                    tree.write(filename, encoding='UTF-8', xml_declaration=True)
                    send_file(filename)

    except (IOError, IndexError):
        # Send HTTP response message for file not found
        connectionSocket.send("HTTP/1.1 404 Not Found\r\n\r\n".encode())
        connectionSocket.send("<html><head></head><body><h1>404 Not Found</h1></body></html>\r\n".encode())

        connectionSocket.close()

    print()
    print("------------------")  # Just to identify each new request
    print()


def send_file(filename):
    # Opens and reads the requested file
    f = open(filename, "rb")
    r = f.read()

    contentType = ""
    if filename.endswith(('png', 'jpg', 'jpeg')):
        # Setting the content type to bytes as it will be sent in base 64
        contentType = "text/bytes"

        # Reads the image into a base64 string so it can be decoded within the html
        with open(filename, "rb") as image:
            b64string = base64.b64encode(image.read())

        # Send the response
        connectionSocket.send(("HTTP/1.1 200 OK\r\nContent-Type:" + contentType + "\r\n\r\n").encode() + b64string)

        # Close the connection socket
        connectionSocket.close()
    else:
        # Setting the content type to either html or xml
        if filename.endswith("html"):
            contentType = "text/html"
            print(r)
        if filename.endswith("xml"):
            contentType = "document/xml"

        # Sending the response
        connectionSocket.send(("HTTP/1.1 200 OK\r\nContent-Type:" + contentType + "\r\n\r\n").encode() + r)

        # Close the connection socket
        connectionSocket.close()


def show_friends():
    # Parsing the friends file to minidom to find each of the friends
    xmlDoc = minidom.parse("friends.xml")

    # Creating a list of the friends elements
    friends = xmlDoc.getElementsByTagName("friend")

    # Creating a new root element called status in order to create a new xml file with the posts being the recent posts of each friend
    root = ET.Element("status")

    # Iterating through each friend to gather the information from their server
    for i in range(friends.length):
        name = xmlDoc.getElementsByTagName("name")[i].childNodes[0].nodeValue
        port = xmlDoc.getElementsByTagName("server_port")[i].childNodes[0].nodeValue

        # Using the client function to request for the friend's status file
        friendFile = client("status.xml", int(port))

        if friendFile:
            # If they were friends something was returned and the recent post can be added to the xml
            friendFileRoot = ET.fromstring(friendFile.encode())

            # Finding the length of all of the posts to find the most recent one
            length = len(friendFileRoot.findall("post")) - 1

            # As they are friends the profile photo can be requested
            photo = client("profile.jpg", int(port))

            # The name and photo are added to the status post of the most recent status to send to the html
            newName = ET.SubElement(friendFileRoot[length], "name")
            newPhoto = ET.SubElement(friendFileRoot[length], "photo")

            # Populating the elements with the name and photo
            newName.text = name
            newPhoto.text = photo

            # Append the most recent post (with the added information) to the response status xml
            root.append(friendFileRoot[length])

    # Indent the xml text
    indent(root)

    # Return the created xml (converted to string) back to be sent as a response to the html
    xmlstr = ET.tostring(root)
    return xmlstr


def client(file, port, method="GET"):
    # This function allows the server to speak to the other servers
    # Creating the socket to request and receive
    clientSocket = socket(AF_INET, SOCK_STREAM)

    # Connecting to the friends server port
    clientSocket.connect(("localhost", port))

    # The referer lets the server know who is sending the request
    request = method + " /" + file + " HTTP/1.0\nReferer: server/Po\n\n"

    # Send the message
    clientSocket.send(request.encode())

    # Receive the reply, the receive characters are 60000 to allocate enough space for the image bytes or if the status file gets really long
    response = clientSocket.recv(60000).decode()

    content = ""
    # Checking the reponse was successful and not Forbidden or Not Found
    if response:
        if response.split("\r\n")[0].split(" ")[1] == "200":
            content = response.split("\r\n\r\n")[1]

    # Close the socket
    clientSocket.close()

    # Returning the content received, this might be blank if the response was not OK
    return content


## Function a resource from http://effbot.org/zone/element-lib.htm
def indent(elem, level=0):
    i = "\n" + level * "  "
    if len(elem):
        if not elem.text or not elem.text.strip():
            elem.text = i + "  "
        if not elem.tail or not elem.tail.strip():
            elem.tail = i
        for elem in elem:
            indent(elem, level + 1)
        if not elem.tail or not elem.tail.strip():
            elem.tail = i
    else:
        if level and (not elem.tail or not elem.tail.strip()):
            elem.tail = i


while True:
    # Set up a new connection from the client
    connectionSocket, addr = serverSocket.accept()

    # Clients timeout after 60 seconds of inactivity and must reconnect.
    connectionSocket.settimeout(60)

    # start new thread to handle incoming request
    _thread.start_new_thread(process, (connectionSocket,))

serverSocket.close()