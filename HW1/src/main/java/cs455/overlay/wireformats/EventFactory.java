package cs455.overlay.wireformats;

import cs455.overlay.node.Node;
import cs455.overlay.util.StatisticsCollectorAndDisplay;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class EventFactory {

  private static EventFactory eventFactory_instance = null;
  private Node listening_node;

  public static EventFactory getInstance() {
    if (eventFactory_instance == null) {
      eventFactory_instance = new EventFactory();
    }
    return eventFactory_instance;
  }

  public synchronized void addListener(Node node) {
    this.listening_node = node;
  }

  //This needs to be used for deregistraion TODO please implement this so no side affects can happen
  public synchronized void removeListener() {
    this.listening_node = null;
  }

  //This class is responsible for holding the type of the message
  //TODO if i receive a message from another messaging node lets return TRUE and add that to the connections table
  //All of the other functions could return false and still add to the event queue

  public synchronized void createEvent(byte[] byteString, String origin)
      throws IOException {

      ByteArrayInputStream baInputStream = new ByteArrayInputStream(byteString);
      DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

      int type = din.readInt();

      Event event = null;

      switch (type) {
          case Protocol.REGISTER_RQ:
              event = createRegisterRQ(din);
              break;
          case Protocol.REGISTER_RS:
              event = createRegisterRS(din);
              break;
          case Protocol.DEREGISTER_RQ:
              event = createDeregisterRQ(din);
              break;
          case Protocol.DEREGISTER_RS:
              event = createDeregisterRS(din);
              break;
          case Protocol.MESSAGING_NODE_LIST:
              event = createMessagingNodeList(din);
              break;
          case Protocol.LINK_WEIGHTS:
              event = createLinkWeights(din);
              break;
          case Protocol.TASK_INIT:
              event = createTaskInitiate(din);
              break;
          case Protocol.TASK_SUMMARY_RQ:
              //This one only has a data field of type so nothing needs to be retrieved
              event = new TaskSummaryRequest();
              break;
          case Protocol.TASK_SUMMARY_RS:
              event = createTaskSummaryResponse(din);
              break;
          case Protocol.TASK_COMPLETE:
              event = createTaskComplete(din);
              break;
          case Protocol.MESSAGE:
              event = createMessage(din);
      }

      baInputStream.close();
      din.close();
      this.listening_node.onEvent(new EventInstance(event, origin));
  }

  private RegisterRequest createRegisterRQ(DataInputStream din)
      throws IOException {
    int ip_len = din.readInt();
    byte[] ip_addr_bytes = new byte[ip_len];
    din.readFully(ip_addr_bytes, 0, ip_len);
    String ip_addr = new String(ip_addr_bytes);
    int port_number = din.readInt();
    int originType = din.readInt();

    return new RegisterRequest(ip_addr, port_number, originType);
  }

  //TODO RegisterRQ is getting a originType so this might need to be refactored
  private RegisterResponse createRegisterRS(DataInputStream din)
      throws IOException {
    byte success = din.readByte();
    int add_len = din.readInt();
    String add_info;
    RegisterResponse rrs;

    if (add_len == 0) {
      byte[] add_info_b = new byte[add_len];
      add_info = new String(add_info_b);
      rrs = new RegisterResponse(success, add_info);
    } else {
      rrs = new RegisterResponse(success);
    }

    return rrs;
  }

  private DeregisterRequest createDeregisterRQ(DataInputStream din) throws IOException {
    int ip_len = din.readInt();
    byte[] ip_addr_bytes = new byte[ip_len];
    din.readFully(ip_addr_bytes, 0, ip_len);
    String ip_addr = new String(ip_addr_bytes);
    int port_number = din.readInt();
    return new DeregisterRequest(ip_addr, port_number);
  }

  private DeregisterResponse createDeregisterRS(DataInputStream din) throws IOException {
    byte success = din.readByte();
    int add_len = din.readInt();
    String add_info;
    DeregisterResponse drs;

    if (add_len != 0) {
      byte[] add_info_b = new byte[add_len];
      add_info = new String(add_info_b);
      drs = new DeregisterResponse(success, add_info);
    } else {
      drs = new DeregisterResponse(success);
    }

    return drs;
  }

  private MessagingNodeList createMessagingNodeList(DataInputStream din) throws IOException {

    ArrayList<String> connections = new ArrayList<>();
    int peerLength;
    byte[] peerIpPort = null;
    String IP_Port;

    int numOfPeers = din.readInt();

    for (int i = 0; i < numOfPeers; i++) {
      peerLength = din.readInt();
      peerIpPort = new byte[peerLength];
      din.readFully(peerIpPort, 0, peerLength);
      IP_Port = new String(peerIpPort);
      connections.add(IP_Port);
    }

    return new MessagingNodeList(connections);
  }

  private LinkWeights createLinkWeights(DataInputStream din) throws IOException {

    ArrayList<LinkInfo> peerConnections = new ArrayList<>();
    int numConnections;
    int len;
    int weight;
    byte[] IP;
    String sender, receiver;

    numConnections = din.readInt();

    for (int i = 0; i < numConnections; i++) {
      len = din.readInt();
      IP = new byte[len];
      din.readFully(IP, 0, len);
      sender = new String(IP);

      len = din.readInt();
      IP = new byte[len];
      din.readFully(IP, 0, len);
      receiver = new String(IP);

      weight = din.readInt();

      peerConnections.add(new LinkInfo(sender, receiver, weight));
    }

    return new LinkWeights(peerConnections);

  }

  private TaskInitiate createTaskInitiate(DataInputStream din) throws IOException {
    int rounds = din.readInt();
    return new TaskInitiate(rounds);
  }

  private TaskSummaryResponse createTaskSummaryResponse(DataInputStream din) throws IOException {
    String IP_addr;
    byte[] IP;
    int len = din.readInt();
    IP = new byte[len];
    din.readFully(IP, 0, len);
    IP_addr = new String(IP);

    int portnumber = din.readInt();
    int msgSent = din.readInt();
    int msgSumSent = din.readInt();
    int msgReceived = din.readInt();
    int msgSumReceived = din.readInt();
    int msgRelayed = din.readInt();

    return new TaskSummaryResponse(IP_addr, portnumber,
        new StatisticsCollectorAndDisplay(msgSent, msgSumSent, msgReceived, msgSumReceived,
            msgRelayed));
  }

  private TaskComplete createTaskComplete(DataInputStream din) throws IOException {
    int ip_len = din.readInt();
    byte[] ip_addr_bytes = new byte[ip_len];
    din.readFully(ip_addr_bytes, 0, ip_len);
    String ip_addr = new String(ip_addr_bytes);
    int port_number = din.readInt();

    return new TaskComplete(ip_addr, port_number);
  }

  private Message createMessage(DataInputStream din) throws IOException {
    int nodeLen = din.readInt();
    byte[] node_bytes = new byte[nodeLen];
    din.readFully(node_bytes, 0, nodeLen);
    String mainNode = new String(node_bytes);

    nodeLen = din.readInt();
    node_bytes = new byte[nodeLen];
    din.readFully(node_bytes, 0, nodeLen);
    String sinkNode = new String(node_bytes);

    int msgWeight = din.readInt();

    return new Message(mainNode, sinkNode, msgWeight);
  }
}
