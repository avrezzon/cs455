package cs455.overlay.wireformats;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

//This will be the format for sending the messages for the rounds
public  class Message implements Event {

  private final int type = Protocol.MESSAGE;
  private String originNode; // This will be an IP:Port paring
  private String sinkNode;
  private int msgWeight;

  public Message(String originNode, String sinkNode, int msgWeight) {
    this.originNode = originNode;
    this.sinkNode = sinkNode;
    this.msgWeight = msgWeight;
  }

  public int getType() {
    return this.type;
  }

  public byte[] getBytes() throws IOException {
    byte[] marshalledBytes;
    ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

    dout.writeInt(this.type);
    dout.writeInt(this.originNode.length());
    byte[] msg = this.originNode.getBytes();
    dout.write(msg, 0, originNode.length());

    dout.writeInt(this.sinkNode.length());
    msg = this.sinkNode.getBytes();
    dout.write(msg, 0, this.sinkNode.length());

    dout.writeInt(this.msgWeight);
    dout.flush();

    marshalledBytes = baOutputStream.toByteArray();

    baOutputStream.close();
    dout.close();

    return marshalledBytes;
  }

  //TODO Implement this function
  //This will be responsible for doing all of the stats collections and relaying sinking the message so on and on
  public void resolve(String origin) {
    System.out.println("Recieved a message from my buddy " + this.originNode);
  }
}
