# HW1-PC README

## *View the project description CS455-Spring19-HW1-PC and the written components CS455-Spring19-HW1-WC*

Here is a layout of what is present in the project submission:
Things to note for the submmision I was not able to finish and send the messages to the peer nodes.  This was because I though I had manipulated the RegisterRequest to handle both the MessagingNode to Registry Request and the MessagingNode to MessagingNode.  After attempting to initiate the task message to ensure they were working I realized that I did't set up the connections correctly.  Since this was found close to the project submission, I felt that this was important to explain during the grading process.  I knew how to complete the rest of the project but I tried to focus on getting this to work but I ran out of time.  While some of the things do not work please run and test all of the functions.

- cs455.overlay.dijkstra
	- Unfortunately neither of the files were able to be implemented due to an issue with the MessagingNodes not being able to connect to one another.
	
	- RoutingCache -> This was going to be a singleton instance that was shared among all of the messaging nodes (Mn) so that when the Mn got the message, the node would know who to foward the message to.

- cs455.overlay.node
    
    - MessagingNode: This class can run all of the functions in the foreground process, however read the print statements
    
    - Registry: This class can run all of the functions in the foreground process, however read the print statements
    
- cs455.overlay.transport

    - EventQueueThread -> This was created so that once the TCPReceiverThread recieves an event thatthat thread can still be devoted to receiving messages.  This creates a thread on its own responsible for handling events calling their resolve method.
    
    - TCPRegularSocket -> This is a class that takes in a new Socket created from the TCPServerThread.  This is used to help bundle the connections
    
- cs455.overlay.util
    
    - OverlayCreator -> This works perfect and is able to assign an ArrayList<String> values of the IP's that it needs to connect to.  
    
    - StatisticsCollectorAndDisplay - this is a class that would be held at each of the messagingNodes and would collect stats based upon the actions taken in the message.resolve(origin).  Fully implemented npot used.
    
- cs455.overlay.wireformats

    - EventInstance -> This is just the event wrapped into an object that contains the origin IP and port.
    
    - LinkInfo -> This contains the originNode, the receivingNode, and the link weight
    
    - Message -> This contains where the the message originated from, who is the sink node, and what the weight of the message was.  As mentioned before, Each of the nodes would have an instance of the routing cache and would of been able to look at where the message was in transit and would relay it to their peerNode corresponding with the shortest path.
    
    - Protocol -> This was a class that helped establish consistency when it cameto assigning values to message types.
    
    - RegisterRequest -> This works fine when the MessagingNode is trying to connect to the Registry.  However a bug was found close to the submission date, and prevented the Messaging node to connect to his peer nodes in the overlay.
    
    - TaskInitiate -> Did not get to finish.  this was where I found out that my overlay was not correctly implemented. 
