Anthony Rezzonico
Description of the classes within

Event (abstract):
** this will include message type??

REGISTER_REQUEST:

  *Message Type (int): REGISTER_REQUEST
  IP address (String):
  Port number (int):

REGISTER_RESPONSE:

  *Message Type (int): REGISTER_RESPONSE
  Status Code (byte): SUCCESS or FAILURE
  Additional Info (String):

DEREGISTER_REQUEST:

  *Message Type (int): DEREGISTER_REQUEST
  Node IP address (String):
  Node Port number (int):

DEREGISTER_RESPONSE:

  *Message Type (int): DEREGISTER_RESPONSE
  Status Code (byte): SUCCESS or FAILURE
  Additional Info (String):

MESSAGE:
  Message Type (int):
  Sink node

MESSAGING_NODE_LIST:

  Message Type (int): MESSAGING_NODES_LIST
  #Number of peer messaging nodes (int): L
  #Linkinfo1
  #Linkinfo2
  #.....
  #LinkinfoL

LINK_WEIGHTS:

  Message Type (int): LINK_WEIGHTS
  Number of links: L
  Linkinfo1
  Linkinfo2
  .....
  LinkinfoL

TASK_INITIATE:

  Message Type (int): TASK_INITIATE
  Rounds (int): X

TASK_COMPLETE:

  Message Type (int): TASK_COMPLETE
  Node IP Address (String):
  Node Port number (int):

TASK_SUMMARY_REQUEST:

  Message Type (int): PULL_TRAFFIC_SUMMARY

TASK_SUMMARY_RESPONSE:

  Message Type (int): TRAFFIC_SUMMARY
  Node IP address (String):
  Node Port number (int):
  Number of messages sent (int):
  Summation of sent messages (int):
  Number of messages received (int):
  Summation of received messages (int):
  Number of messages relayed (int):
