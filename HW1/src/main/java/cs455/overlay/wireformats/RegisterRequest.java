package cs455.overlay.wireformats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegisterRequest extends Message {

  private final int type = Protocol.REGISTER_RQ;
  private String ip_addr;
  private int port_number;
  private byte[] source;

  public RegisterRequest(String ip_addr, int port_number)throws IOException{
    this.ip_addr = ip_addr;
    this.port_number = port_number;
    this.source = this.getBytes().clone();
  }

  public RegisterRequest(byte[] byteString) throws IOException{

    this.source = byteString.clone();
    ByteArrayInputStream baInputStream = new ByteArrayInputStream(byteString);
    DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

    din.readInt(); //This will read the type
    int ip_len = din.readInt();
    byte[] ip_addr_bytes = new byte[ip_len];
    din.readFully(ip_addr_bytes, 0 ,ip_len);
    this.ip_addr = new String(ip_addr_bytes);
    this.port_number = din.readInt();

    baInputStream.close();
    din.close();
  }

  public String getIP(){return this.ip_addr;}

  public int getPort(){return this.port_number;}

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

    int packet_length = baOutputStream.size();
    marshalledBytes = baOutputStream.toByteArray();

    //TODO verify that this works
    baOutputStream.reset();
    dout.write(packet_length);
    dout.write(marshalledBytes,0, packet_length);

    finalMsg = baOutputStream.toByteArray();

    baOutputStream.close();
    dout.close();

    return finalMsg;
  }
}
