Here is what needs to be done:
* Right now neither of the server throughput or the client is correct
   * Server throughput is low due to the fact that Im sure there are a bunch of messages that have a null payload ptr that is wasting time
    to determine this
      * this will indirectly affect the message mean throughput per client giving them a lower value than the correct number