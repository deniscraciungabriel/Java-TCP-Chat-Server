# Java-TCP-Chat-Server (backend)
---
This is a TCP Chat Server made with Java. It's pretty simple so there are no security verifications. Therea are no verifications for the username, but that would just imply some if statements and nothing too hard.
This is also just the backend so there is no GUI created for it.
---
## Features
- With "/name" + the username, the user can change his nickname and the server will broadcast the change to everyone in the room
- With "/quit" the client can quit the server
More features can easily be added in the while loop of the server just by adding some logical conditions.
`while (!clientSocket.isClosed()) {
                    message = in.readLine();
                    if (message.startsWith("/name")) {
                        temp = this.nickname;
                        this.nickname = message.split(" ")[1];
                        broadcast(temp + " has changed the username to " + this.nickname, "Server");
                    } else if (message.equals("/quit")) {
                        stop();
                    } else {
                        broadcast(message, this.nickname);
                    }
                }`
