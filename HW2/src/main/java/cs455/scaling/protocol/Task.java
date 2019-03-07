package cs455.scaling.protocol;

import cs455.scaling.server.Server;
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
      if (this.key.isValid()) {
        if (this.key.isAcceptable()) {
          Server.register();
        }

        if (this.key.isReadable()) {
          //FIXME
          Server.stats.receivedMsg();
        }
      }

    } catch (IOException ie) {
      System.err.println(ie.getMessage());
    }
  }


}
