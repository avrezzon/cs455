package cs455.overlay.wireformats;

public class DeregisterResponse extends Message{

  public int getType(){
    return Protocol.DEREGISTER_RS;
  }

  public byte[] getBytes(){
    return null; //TODO
  }

}
