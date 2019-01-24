# Client-Server-application

SERVER ALGORITHM EXPLANATION:
For every new client connection the server save record for it.
Then the it iterates over all records and processes client's queries if there are any.
In case of no client queries the server goes in sleep mode for 500 milliseconds and repeate the above procedure again.
If new queries has been received the server sumbit them in the thread pool and wait for all task to be completed
with timeout 1000 miliseconds(1 second).Then move on next iteration over all registered clients. 
When the client want to disconnect from the server it has to send "END" message. 
Thus the server will remove the record for the client.

TEST CASE:
The test case is simple echo program which take as a input the text below and for every word 
it creates new Client which send the word to Server and the server send it back to the client.

TEXT:
An int, a char and a string walk into a bar and order some drinks. 
A short while later, the int and char start hitting on the waitress who gets very uncomfortable and walks away.
The string walks up to the waitress and says “You’ll have to forgive them, they’re primitive types.


RUN THE CLIENT SERVER APPLICATION

1.Run the server by the terminal with command: java -jar Server.jar
2.Run the test by the terminal with command: java -jar Test.jar

EXPECTED RESULTS:
In oder for the test to pass sucessfully in the console has to be printed folling message:

Expected received packets: 52
Actual received packets: 52
