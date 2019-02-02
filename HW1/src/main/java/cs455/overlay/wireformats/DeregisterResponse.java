package cs455.overlay.wireformats;

public class DeregisterResponse extends Event{

  public int getType(){
    return Protocol.DEREGISTER_RS;
  }

  public byte[] getBytes(){
    return null; //TODO
  }

}
