package cs455.scaling.protocol;

import cs455.scaling.server.Server;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;

//This will just be a container that holds the SocketChannel peeled from the Selection key
public class ClientMessage {

  private String IP;
  private SocketChannel socket;
  private Payload payload;
  private ByteBuffer buffer;
  //TODO ill add more variables as needed

  //This will only be created once the task has been accepted by the worker thread
  //upon initalizaton, the class will extract the message from the buffer so that when
  //the object has been finished being created that the hash is already computed and read to send
  //back when the batch is finished
  public ClientMessage(SelectionKey key) throws IOException {
    this.socket = (SocketChannel) key.channel();
    this.IP = socket.getRemoteAddress().toString();
    this.buffer = ByteBuffer.allocate(8000);

    int bytesRead = socket.read(buffer);
    synchronized (socket) {
      if (bytesRead == -1) {
        socket.close();
        Server.stats.dropConnection();
      } else {
        if (bytesRead == 8000) {
          this.payload = new Payload(buffer.array());
          Server.stats.receivedMsg(socket);
        } else {
          this.payload = null;
        }
      }
    }
    key.attach(null);
  }

  //Returns True if the payload is not null
  public boolean verifyPayload(SelectionKey key) {
    if (this.payload == null) {
      key.attach(null);
      return false;
    }
    return true;
  }

  public void sendMsgToSender() throws IOException {
    synchronized (socket) {
      if (this.payload != null) {
        try {
          this.payload.calculateMsgHash();
        } catch (NoSuchAlgorithmException noa) {
          System.out.println(noa.getMessage());
        }
        buffer = ByteBuffer.wrap(this.payload.getHash().getBytes());
        socket.write(buffer);
        buffer.clear();
        Server.stats.sendMsg(socket);
      } else {
        System.out.println("sendMsgToSender() Null entry processed!!!!!!!!!!!");
      }
    }
  }

  public String toString() {
    return "Message coming from " + this.IP;
  }

}
