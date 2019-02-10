package cs455.overlay.wireformats;

//This will be the format for sending the messages for the rounds
public  class Message implements Event {
  private int type;
  public int getType(){return 0;}
  public byte[] getBytes(){return null;}
}
