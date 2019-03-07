package cs455.scaling.protocol;

import cs455.scaling.server.Server;
import cs455.scaling.server.ThreadPoolManager;
import java.io.IOException;
import java.nio.channels.SelectionKey;

public class Task {

  public enum Type {
    Accept,
    Work,
    Write
  }

  private SelectionKey key;

  //This is the task that is created for registering
  public Task(SelectionKey key) {
    this.key = key;
  }

  //This will do the function based upon the type of action so that the worker thread can just call the resolve fn
  public void resolve() {
    try {
      if (this.key
          .isValid()) {  //Need to validate that we aren't trying to read from an already closed channel
        if (this.key
            .isAcceptable()) {  //An acceptable flag will show that we need to connect to a new client
          Server.register();
        }

        if (this.key.isReadable()) {
          //This will extract the key from the task and pass it into the linked list of batches
          ThreadPoolManager.addMsgKey(this.key);
        }
      }

    } catch (IOException ie) {
      System.err.println(ie.getMessage());
    }
  }


}
