package cs455.overlay.wireformats;

public class RegisterRequest extends Event {

  public int getType(){
    return Protocol.REGISTER_RQ;
  }

  public byte[] getBytes(){
    return null; //TODO
  }
}
