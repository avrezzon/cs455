package cs455.overlay.wireformats;

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

  //NOTE anytime that we evaluate this we need to check to see if it is null
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

  public void resolve() {
    //TODO exit based upon the response
    System.out.println("Recieved response from exiting the overlay");
  }

}
