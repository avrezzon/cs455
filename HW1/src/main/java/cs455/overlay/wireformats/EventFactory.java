package cs455.overlay.wireformats;

public class EventFactory {

  private static EventFactory eventFactory_instance  = null;

  public static EventFactory getInstance(){
    if(eventFactory_instance == null){
      eventFactory_instance = new EventFactory();
    }
    return eventFactory_instance;
  }

  public void createEvent(byte[] byteString){
    //TODO
  }


}
