package cs455.overlay.wireformats;

import cs455.overlay.util.StatisticsCollectorAndDisplay;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TaskSummaryResponse implements Event {

  private final int type = Protocol.TASK_SUMMARY_RS;
  private String nodeIp;
  private int nodePort;
  private int msgSent;
  private int sumMsgSent;
  private int msgReceived;
  private int sumMsgReceived;
  private int msgRelayed;

  public TaskSummaryResponse(String IP_addr, int portnumber, StatisticsCollectorAndDisplay stats) {
    this.nodeIp = IP_addr;
    this.nodePort = portnumber;
    this.msgSent = stats.getMsgSent();
    this.sumMsgSent = stats.getMsgSumSent();
    this.msgReceived = stats.getMsgReceived();
    this.sumMsgReceived = stats.getMsgSumReceived();
    this.msgRelayed = stats.getMsgRelayed();
  }

  public int getType() {
    return this.type;
  }

  public byte[] getBytes() throws IOException {
    byte[] marshalledBytes = null;
    ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

    dout.writeInt(this.type);
    dout.writeInt(this.nodeIp.length());
    byte[] msg = this.nodeIp.getBytes();
    dout.write(msg, 0, this.nodeIp.length());

    dout.writeInt(this.nodePort);
    dout.writeInt(this.msgSent);
    dout.writeInt(this.sumMsgSent);
    dout.writeInt(this.msgReceived);
    dout.writeInt(this.sumMsgReceived);
    dout.writeInt(this.msgRelayed);

    dout.flush();

    marshalledBytes = baOutputStream.toByteArray();

    baOutputStream.close();
    dout.close();

    return marshalledBytes;
  }

  public void resolve(String origin) {
    //TODO Implement this portion of the function
    System.out.println("Need to implement Task summary response resolve()");
  }
}