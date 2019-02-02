package cs455.overlay.wireformats;

public class RegisterResponse extends Event {

  public int getType(){
    return Protocol.REGISTER_RS;
  }

  public byte[] getBytes(){
    return null; //TODO
  }
}
