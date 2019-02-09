package cs455.overlay.wireformats;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegisterResponse{

  private final int type = Protocol.REGISTER_RS;
  private byte status_code;
  private String Additional_Info;

  public RegisterResponse(byte success, String info){
    this.status_code = success;
    this.Additional_Info = info;
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
    dout.write(this.Additional_Info.length());
    if(Additional_Info.length() != 0){
      byte[] msg = this.Additional_Info.getBytes();
      dout.write(msg, 0, Additional_Info.length());
    }

    dout.flush();
    marshalledBytes = baOutputStream.toByteArray();
    baOutputStream.close();
    dout.close();

    return marshalledBytes;
  }
}
