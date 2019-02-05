package cs455.overlay.wireformats;

public class DeregisterRequest extends Message {

  public int getType(){
    return Protocol.DEREGISTER_RQ;
  }

  public byte[] getBytes(){
    return null;//TODO implement this
  }
}
