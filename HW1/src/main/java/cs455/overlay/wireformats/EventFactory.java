package cs455.overlay.wireformats;

import cs455.overlay.node.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class EventFactory {

  private static EventFactory eventFactory_instance  = null;
  private Node listening_node;

  public static EventFactory getInstance(){
    if(eventFactory_instance == null){
      eventFactory_instance = new EventFactory();
    }
    return eventFactory_instance;
  }

  public synchronized void addListener(Node node){
    this.listening_node = node;
  }

  public synchronized void removeListener(){
    this.listening_node = null;
  }


  //This class is responsible for holding the type of the message
  public synchronized void createEvent(byte[] byteString) throws IOException {

    //TODO extract the type
    int type = Protocol.REGISTER_RQ;
    System.out.println(byteString);
    switch (type){
      case Protocol.REGISTER_RQ:
        listening_node.onEvent(new RegisterRequest(byteString));
        break;
    }

  }

  //Precondition is that the message type has already been striped
  private RegisterRequest createRegisterRQ(byte[] byteString) throws IOException {
    return new RegisterRequest(byteString); //TODO there is probably a better approach
  }


}
