package cs455.overlay.wireformats;

import cs455.overlay.node.Node;
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
      case Protocol.MESSAGING_NODE_LIST:
        event = createMessagingNodeList(din);
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
}
