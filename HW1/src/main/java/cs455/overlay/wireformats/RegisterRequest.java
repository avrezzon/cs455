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
    ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

    dout.writeInt(this.type);
    dout.writeInt(this.ip_addr.length());
    byte[] msg = this.ip_addr.getBytes();
    dout.write(msg, 0 ,ip_addr.length());
    dout.writeInt(this.port_number);
    dout.flush();

    marshalledBytes = baOutputStream.toByteArray();

    baOutputStream.close();
    dout.close();

    int packet_size = marshalledBytes.length;
    ByteArrayOutputStream final_packet = new ByteArrayOutputStream();
    final_packet.write(packet_size);
    final_packet.write(marshalledBytes,0, packet_size);

    marshalledBytes = final_packet.toByteArray();

    final_packet.close();

    return marshalledBytes;
  }
}
