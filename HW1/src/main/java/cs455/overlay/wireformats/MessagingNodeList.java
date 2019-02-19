package cs455.overlay.wireformats;

import cs455.overlay.node.MessagingNode;
import cs455.overlay.transport.TCPRegularSocket;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MessagingNodeList implements Event {

  private final int type = Protocol.MESSAGING_NODE_LIST;
  private ArrayList<String> connections;
  private int numberOfPeers;


  public MessagingNodeList(ArrayList<String> connections) {
    this.connections = (ArrayList<String>) connections.clone();
    this.numberOfPeers = connections.size();
  }

  public byte[] getBytes() throws IOException {
    byte[] marshalledBytes;
    byte[] peer; //byte representation of an individual IP:Port connection
    ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

    dout.writeInt(this.type);
    dout.writeInt(this.numberOfPeers);
    for (String IP_Port : this.connections) {
      dout.writeInt(IP_Port.length());
      peer = IP_Port.getBytes();
      dout.write(peer, 0, IP_Port.length());
    }
    dout.flush();

    marshalledBytes = baOutputStream.toByteArray();

    baOutputStream.close();
    dout.close();

    return marshalledBytes;
  }

  public int getType() {
    return this.type;
  }

  //FIXME this is the place where it appears to be that Im not making the connections
  public void resolve(String origin) {

    TCPRegularSocket socket;
    RegisterRequest rrq;

    for (String IP_port : connections) {

      if (MessagingNode.isMessagingNodePresent(IP_port)) {
        //Nothing should happen in this instance
        System.out.println("It appears that the messing node " + IP_port + " is already there" );
      } else {
        //This means that this is the first time registering the Messaging node
        System.out.println("Messaging Node list has reached else");
        MessagingNode.addServerMapping(IP_port, origin);
        socket = MessagingNode.getTCPSocket(IP_port);

        String[] elements = IP_port.split(":");
        rrq = new RegisterRequest(elements[0], Integer.parseInt(elements[1]),
            Protocol.messagingNode);
        try {
          socket.getSender().sendData(rrq.getBytes());
        } catch (IOException ie) {
          System.err.println("IOException occured in MessagingNodeList ln 78: " + ie.getMessage());
        }
      }
    }

  }
}
