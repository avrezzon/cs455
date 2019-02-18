package cs455.overlay.wireformats;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TaskComplete implements Event {

  private final int type = Protocol.TASK_COMPLETE;
  private String nodeIp;
  private int nodePort;

  public TaskComplete(String nodeIp, int nodePort) {
    this.nodeIp = nodeIp;
    this.nodePort = nodePort;
  }

  public int getType(){
    return this.type;
  }

  public byte[] getBytes() throws IOException {
    byte[] marshalledBytes;
    ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

    dout.writeInt(this.type);
    dout.writeInt(this.nodeIp.length());
    byte[] msg = this.nodeIp.getBytes();
    ;
    dout.write(msg, 0, nodeIp.length());
    dout.writeInt(this.nodePort);
    dout.flush();

    marshalledBytes = baOutputStream.toByteArray();

    baOutputStream.close();
    dout.close();

    return marshalledBytes;
  }

  public void resolve(String origin) {
    //TODO implement the resolve
    System.out.println("need to implement the resolve function for the taskComplete msg");
  }
}
