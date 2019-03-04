package cs455.scaling.protocol;

import java.nio.channels.SelectionKey;
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
  private Payload payload;

  public Task(SelectionKey key) {
    //TODO I dont think that this is correct double check this
    this.client = (SocketChannel) key.channel();
    this.type = Type.Accept; //When a new Task is created it needs to start in a waiting state
    this.payload = null; //This will be assigned once the worker thread has read the message
  }

  //This will do the function based upon the type of action so that the worker thread can just call the resolve fn
  public void resolve() {
    switch (this.type) {
      case Accept:
        //TODO this is the registration phase
        break;
      case Read:
        break;
      case Work:
        break;
      case Write:
        break;
    }
  }


}
