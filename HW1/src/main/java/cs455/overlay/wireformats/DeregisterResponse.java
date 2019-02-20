package cs455.overlay.wireformats;

import cs455.overlay.node.MessagingNode;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DeregisterResponse implements Event{

  private final int type = Protocol.DEREGISTER_RS;
  private byte status_code;
  private String Additional_Info;

  public DeregisterResponse(byte success) {
    this.status_code = success;
    this.Additional_Info = null;
  }

  public DeregisterResponse(byte success, String info) {
    this.status_code = success;
    this.Additional_Info = info;
  }

  public boolean getStatus() {
    if (this.status_code == Protocol.success) {
      return true;
    }
    return false;
  }

  public String getAdditionalInfo() {
    return this.Additional_Info;
  }

  public int getType(){
    return this.type;
  }

  public byte[] getBytes() throws IOException {
    byte[] marshalledBytes;
    ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

    dout.writeInt(this.type);
    dout.write(this.status_code);

    if (Additional_Info != null) {
      dout.write(this.Additional_Info.length());
      byte[] msg = this.Additional_Info.getBytes();
      dout.write(msg, 0, Additional_Info.length());
    } else {
      dout.writeInt(0);
    }

    dout.flush();
    marshalledBytes = baOutputStream.toByteArray();
    baOutputStream.close();
    dout.close();

    return marshalledBytes;
  }

  public void resolve(String origin) {
    if(status_code == Protocol.success){
      System.out.println("Messaging node was able to successfully exit the overlay");
      MessagingNode.getEventQueue().killThread();
      MessagingNode.getServer().killThread();
      System.exit(0);
    }else{
        System.out.println("Messaging node was unable to exit the overlay, status code: " + this.status_code);
        System.out.println(this.Additional_Info);
        System.exit(-1);
    }

  }

}
