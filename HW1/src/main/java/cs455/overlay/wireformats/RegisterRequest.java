package cs455.overlay.wireformats;

import cs455.overlay.node.MessagingNode;
import cs455.overlay.node.Registry;
import cs455.overlay.transport.TCPRegularSocket;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class RegisterRequest implements Event {

  private final int type = Protocol.REGISTER_RQ;
  private String ip_addr;
  private int port_number;
  private int originType;

  public String key;
  public String origin;

  public RegisterRequest(String ip_addr, int port_number, int originType) {
    this.ip_addr = ip_addr;
    this.port_number = port_number;
    this.originType = originType;
  }

  public String getIP() {
    return this.ip_addr;
  }

  public int getPort() {
    return this.port_number;
  }

  public int getType() {
    return this.type;
  }

  public byte[] getBytes() throws IOException {
    byte[] marshalledBytes;
    ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

    dout.writeInt(this.type);
    dout.writeInt(this.ip_addr.length());
    byte[] msg = this.ip_addr.getBytes();
    ;
    dout.write(msg, 0, ip_addr.length());
    dout.writeInt(this.port_number);
    dout.writeInt(this.originType);
    dout.flush();

    marshalledBytes = baOutputStream.toByteArray();

    baOutputStream.close();
    dout.close();

    return marshalledBytes;
  }

  public void resolve(String origin) {

    byte success;
    String additional_info = null;
    RegisterResponse rrs = null;
    boolean present;
    String key = this.ip_addr + ":" + this.port_number;
    TCPRegularSocket socket = null;

    if (this.originType == Protocol.registry) {
      if (Registry.isMessagingNodePresent(key)) {
        //This portion says that we are done and sending a a failed response back
        socket = Registry.getTCPSocket(key);
        try {
          socket.getSender().sendData(new RegisterResponse((byte) 0,
              "A connection already exists in the table for this entry").getBytes());
        } catch (IOException ie) {
          System.err.println("Exception at ln 72 in RegisterRQ : " + ie.getMessage());
        }
      } else {
        //This is the registry portion that sends back a successful registration rq
        Registry.addServerMapping(key, origin);
        socket = Registry.getTCPSocket(key);
        rrs = new RegisterResponse((byte) 1);
        try {
          socket.getSender().sendData(rrs.getBytes());
        } catch (IOException ie) {
          System.err.println(
              "Unable to get the bytes from the register response at RegisterRQ ln 80");
        }
      }
    }
    if (this.originType == Protocol.messagingNode) {
      //FIXME
      //Note that this does NOT actually get to register with other peer nodes

      //This would be the section that is dedicated to the messaging node protocol for registration
//      if (MessagingNode.isMessagingNodePresent(key)) {
//        //This case means that we don't need to do anything
//      } else {
//
//        //This means that we need to do some setup
//        //The first thing that we need to do is create a regular socket for the connection that we want to create since it doesnt exist atm
//        try {
//          socket = new TCPRegularSocket(new Socket(ip_addr,
//              port_number)); //This creates the regular socket for the server port that we want to ping
//        } catch (IOException ie) {
//          System.out
//              .println("Could not correctly open the socket for " + ip_addr + ":" + port_number);
//          System.out.println(ie.getMessage());
//        }
//        MessagingNode.addServerMapping(ip_addr + ":" + port_number, socket.getIPPort());
//        rrs = new RegisterResponse((byte) 1);
//        try {
//          socket.getSender().sendData(rrs.getBytes());
//        } catch (IOException ie) {
//          System.err.println(
//              "Unable to get the bytes from the register response at RegisterRQ ln 80");
//        }
//      }
        MessagingNode.addServerMapping(key, origin);
        socket = MessagingNode.getTCPSocket(key);
        try {
            socket.getSender().sendData(new RegisterResponse((byte) 1).getBytes());
        }catch (IOException ie){
            System.out.println(ie.getMessage());
        }
    }

  }

}
