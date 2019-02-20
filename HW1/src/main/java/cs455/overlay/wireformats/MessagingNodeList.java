package cs455.overlay.wireformats;

import cs455.overlay.node.MessagingNode;
import cs455.overlay.transport.TCPRegularSocket;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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

    System.out.println("Resolve method for the Messagingnode list");
    for (String node : connections) {
      System.out.println("Connected to " + node);
    }

    TCPRegularSocket socket = null;
    RegisterRequest rrq;
    String[] ip_port;

    for (String IP_port : connections) {
      //TODO try making this a try catch block with cathch inserting


      if (MessagingNode.isMessagingNodePresent(IP_port)) {
        //Nothing should happen in this instance
        System.out.println("It appears that the messing node " + IP_port + " is already there" );
      } else {
        //This means that this is the first time registering the Messaging node
        ip_port = IP_port.split(":");
        System.out.println("Messaging Node list has reached else");
        try {
          socket = new TCPRegularSocket(new Socket(ip_port[0], Integer.parseInt(ip_port[1])));
        } catch (IOException ie) {
          System.out.println("Could not create a socket");
        }
        MessagingNode.receivedConnection(socket);

        String[] elements = IP_port.split(":");
        rrq = new RegisterRequest(elements[0], Integer.parseInt(elements[1]),
            Protocol.messagingNode);
        try {
          socket.getSender().sendData(rrq.getBytes());
        } catch (IOException ie) {
          System.err.println("IOException occured in MessagingNodeList ln 78: " + ie.getMessage());
        } catch (NullPointerException ne) {
          System.out.println("YOu still clucked up"); //TODO this might actually be ok
        }
      }
    }

  }
}
