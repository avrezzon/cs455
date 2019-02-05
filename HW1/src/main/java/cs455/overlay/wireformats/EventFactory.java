package cs455.overlay.wireformats;

import cs455.overlay.node.*;

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

  public synchronized  void removeListener(){
    this.listening_node = null;
  }

  public void createEvent(byte[] byteString){

    Event temp = new RegisterRequest("192.87.0.0", 90);
    listening_node.onEvent(temp); //TODO Verify the validity of this statement

  }

  //Precondition is that the message type has already been striped
  private RegisterRequest createRegisterRQ(byte[] byteString){
    return null;
  }


}
