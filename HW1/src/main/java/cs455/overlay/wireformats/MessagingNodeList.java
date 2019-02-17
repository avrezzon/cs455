package cs455.overlay.wireformats;

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
    System.out.println("MSL type: " + this.type);

    dout.writeInt(this.numberOfPeers);
    System.out.println("MSL number of peers: " + this.numberOfPeers);

    for(String IP_Port : this.connections){
      dout.writeInt(IP_Port.length());
      System.out.printf("MSL peer ip length :%d, ip: %s\n", IP_Port.length(), IP_Port);
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

  public void resolve(String origin) {
    System.out.println("You have recieved a reguest to initiate connections with some other nodes");
    //iterate through the list of the IP:Port connections that we need to get
    //This might mimic the way that register rq behaves

    //TODO tommorow we need to implement who we are sending the messages out to
    for(String IP_port : connections){
      //Send connection to this IP

    }

  }

  /**
   * if(MessagingNode.isMessagingNodePresent(key)){
   *         //Nothing should happen in this instance
   *       }else{
   *         //This means that this is the first time registering the Messaging node
   *         MessagingNode.addServerMapping(key, origin);
   *       }*/

}
