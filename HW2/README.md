# HW2-PC README

## *View the project description CS455-Spring19-HW2-PC and the written components CS455-Spring19-HW2-WC*

Description of the extra classes:
- cs455.scaling.protocol

	- ClientMessage:

		This is the message that is sent from the client.  When a task is pulled from the task queue by the worker thread
		it will read the message so that more activity can happen on the client.  This bundles up the socket so it is garunteed
		that we send the message to the correct client.
		
	- Payload:

		This is what is received and sent between the client and the server.  This will contain the message of random bytes and has functions
		incorperated to hash the messages.

- cs455.scaling.server

	- Batch:
		
		This is the batch that holds a linked list of Client messages that are to be processed once the batch is full.

	- BatchMessages:

		This is the container that will hold the linked list of batches.  This class is responsible for determining if
		the batch is ready to be sent or not.

	- ThreadPoolManagerThread:

		This thread was responsible for adding the incoming selections keys and converting them into tasks that could be
		processed by the worker threads.  This thread in addition would also constantly check to see if there was anything
		in the taskQueue and would notify the workers for the work that was ready to be completed.
