package cs455.overlay.wireformats;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegisterRequest extends Message {

  private final int type = Protocol.REGISTER_RQ;
  private String ip_addr;
  private int port_number;

  public RegisterRequest(String ip_addr, int port_number){
    this.ip_addr = ip_addr;
    this.port_number = port_number;
  }

  public int getType(){
    return this.type;
  }

  public byte[] getBytes() throws IOException {
    byte[] marshalledBytes = null;
    byte[] finalMsg = null;
    ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

    dout.writeInt(this.type);
    dout.writeInt(this.ip_addr.length());
    byte[] msg = this.ip_addr.getBytes();
    dout.write(msg, 0 ,ip_addr.length());
    dout.writeInt(this.port_number);
    dout.flush();

    marshalledBytes = baOutputStream.toByteArray();

    //TODO verify that this works
    baOutputStream.reset();
    dout.write(marshalledBytes.length);
    dout.write(marshalledBytes,0, marshalledBytes.length);

    finalMsg = baOutputStream.toByteArray();

    baOutputStream.close();
    dout.close();

    return finalMsg;
  }
}
