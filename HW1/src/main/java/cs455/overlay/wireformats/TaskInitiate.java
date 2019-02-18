package cs455.overlay.wireformats;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TaskInitiate implements Event {

  private final int type = Protocol.TASK_INIT;
  private int rounds;

  public TaskInitiate(int rounds) {
    this.rounds = rounds;
  }

  public int getType(){
    return Protocol.TASK_INIT;
  }

  public byte[] getBytes() throws IOException {

    byte[] marshalledBytes;
    ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

    dout.writeInt(this.type);
    dout.writeInt(this.rounds);
    dout.flush();

    marshalledBytes = baOutputStream.toByteArray();

    baOutputStream.close();
    dout.close();

    return marshalledBytes;
  }

  public void resolve(String origin) {
    //TODO Resolve what this means
    System.out.println("Task Initiated");
  }

}
