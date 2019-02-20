package cs455.overlay.wireformats;

import cs455.overlay.node.MessagingNode;
import cs455.overlay.transport.TCPRegularSocket;
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
      System.out.println("RECEIVED A TASK INITIATE MESSAGE");
      TCPRegularSocket receivingSocket;
      Message msg;
    String receivingNode;

    System.out.println("Connections size is " + MessagingNode.getConnectionsList().size());

    for (int i = 1; i < MessagingNode.getConnectionsList().size(); i++) {
          //FIXME he isnt getting populated so then ln 54 has a null reference to the socket
      receivingNode = MessagingNode.getConnectionsList().get(i);
      System.out.println("Trying to message " + receivingNode);
          receivingSocket = MessagingNode.getTCPSocket(receivingNode);

          msg = new Message(MessagingNode.getIPport(), receivingNode, 0);
          try {
              receivingSocket.getSender().sendData(msg.getBytes());
          }catch(IOException ie){
              System.err.println("Issue when sending a message in task initiate: " + ie.getMessage());
          }
      }
  }

}
