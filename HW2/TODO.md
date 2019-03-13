Here is what needs to be done:
* Right now neither of the server throughput or the client is correct
   * Server throughput is low due to the fact that Im sure there are a bunch of messages that have a null payload ptr that is wasting time
    to determine this
      * this will indirectly affect the message mean throughput per client giving them a lower value than the correct number
      * Server is still getting screwed
      * Im noticing that in situations where the client size is 100, out of those clients the majority of them are not getting the hashed messages sent back