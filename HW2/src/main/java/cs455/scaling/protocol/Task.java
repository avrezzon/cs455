package cs455.scaling.protocol;

import cs455.scaling.server.Server;
import java.io.IOException;
import java.nio.channels.SocketChannel;

public class Task {

  public enum Type {
    Accept,
    Read,
    Work,
    Write
  }

  private Type type; // This is the state of the task
  private SocketChannel client;
  private String Id;
  private Payload payload;

  public Task() {
    this.client = null;
    this.type = Type.Accept; //When a new Task is created it needs to start in a waiting state
    this.payload = null; //This will be assigned once the worker thread has read the message
  }

  //This will do the function based upon the type of action so that the worker thread can just call the resolve fn
  public void resolve() {
    try {
      switch (this.type) {
        case Accept:
          this.client = Server.register();
          this.Id = client.getRemoteAddress().toString();
          System.out.println("Successfully registered " + Id + " with the server.");
          break;
        case Read:
          break;
        case Work:
          break;
        case Write:
          break;
      }
    } catch (IOException ie) {
      System.err.println(ie.getMessage());
    }
  }


}
