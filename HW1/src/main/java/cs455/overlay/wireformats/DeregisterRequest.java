package cs455.overlay.wireformats;

import cs455.overlay.node.Registry;
import cs455.overlay.transport.TCPRegularSocket;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DeregisterRequest implements Event{

  private final int type = Protocol.DEREGISTER_RQ;
  private String ip_addr;
  private int port_number;

  public DeregisterRequest(String ip_addr, int port_number) throws IOException {
    this.ip_addr = ip_addr;
    this.port_number = port_number;
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
    dout.flush();

    marshalledBytes = baOutputStream.toByteArray();

    baOutputStream.close();
    dout.close();
    return marshalledBytes;
  }

  public void resolve(String origin){

    byte success;
    String additional_info;
    DeregisterResponse drs = null;

    String key = new String(this.ip_addr + ":" + this.getPort());
    TCPRegularSocket socket = Registry.getTCPSocket(key);
    if(socket != null){
      drs = new DeregisterResponse((byte) 1);
      try {
        socket.getSender().sendData(drs.getBytes());
        Registry.removeConnection(key);
      }catch(IOException ie){
        System.out.println("Could not successfully deregister");
      }
    }else{
        //TODO Im not sure how this happens
    }


  }
}
