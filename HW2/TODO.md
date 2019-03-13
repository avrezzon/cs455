Here is what needs to be done:
* Right now neither of the server throughput or the client is correct
   * Server throughput is low due to the fact that Im sure there are a bunch of messages that have a null payload ptr that is wasting time
    to determine this
      * this will indirectly affect the message mean throughput per client giving them a lower value than the correct number
      * Server is still getting screwed
      * Im noticing that in situations where the client size is 100, out of those clients the majority of them are not getting the hashed messages sent back
 * Revision suggested solution would be to remove the filled batch and dispatch immediately instead of putting something back into the task queue
      * The server throughput is getting closer to what it needs to be but the client throughput is not being achieved
      * The Mean throughput per client is low are around 1.0
        *I need to go through a simulation to see how big the task queue gets because sometimes it doesnt connect all of the clients 
               
 * Double check what keys have objects on them because this could easily be an issue
 * I am now getting through put from the clients but a high std deviation
 
 