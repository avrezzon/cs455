package cs455.overlay.node;

import java.io.IOException;
import java.net.*;
import cs455.overlay.transport.*;

public class MessagingNode {
  private String ipAddr;
  private TCPServerThread server; //This will spawn at runtime to accept messages from other nodes
  private TCPSender senderSock; //This will be done to initate a message with the Registry

  public MessagingNode(String hostname, int port_number) throws UnknownHostException , IOException {
    this.ipAddr = InetAddress.getLocalHost().toString();
    this.server = new TCPServerThread(); //TODO create constructor
    this.senderSock = new TCPSender(new Socket(hostname, port_number));
  }

  public static void main(String[] args){
    //Does
    System.out.print("Hello from messaginh");
  }
}
