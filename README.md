# Multiple Chat Server - Connect and Communicate

## Description
The Multiple Chat Server is a simple chat application implemented in Java. It allows multiple clients to connect and communicate with each other through a server. The server utilizes sockets for listening and establishing connections with clients. It employs executors and multiple threads to enable simultaneous sending and receiving of messages by clients. The server also provides several command functionalities, such as listing connected clients, quitting the connection, changing the client's name, and sending private messages to specific users.

## Table of contents
* [Usage](#usage)
* [Features](#features)
* [Contributing](#contributing)

## Usage
1. Clients can connect to the server using a socket connection.

2. Upon connecting, clients are welcomed into the chat environment and can start sending and receiving messages.

3. Engage in dynamic conversations with multiple users simultaneously, fostering real-time communication and collaboration.

4. Available commands:

  - /list: Retrieves the list of all connected clients, allowing users to see who else is online.
  - /quit: Terminates the connection and gracefully exits the chat session.
  - /name <new-name>: Allows users to change their display name to enhance personalization and identification.
  - /whisper <username> <message>: Enables private messaging by sending a message to a specific user only.

 
## Features
- Socket Programming: Utilizes socket programming to establish network communication between the server and clients, enabling real-time messaging and interaction.
- TCP Usage: Implements the Transmission Control Protocol (TCP) as the underlying protocol for the socket communication, ensuring reliable and ordered data transmission.
- Multiple clients: The server supports connections from multiple clients, allowing them to interact concurrently.
- Real-time messaging: Users can send and receive messages instantly, facilitating seamless communication.
- Command functionality: A set of commands enhances the user experience, offering capabilities such as user listing, name changing, and private messaging.
- User-friendly interface: The chat server provides an intuitive and straightforward interface for users to engage in conversations effortlessly.

## Contributing
Contributions are welcome! If you have suggestions for improvement, please submit raise an issue in the GitHub repository.
