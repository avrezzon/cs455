package cs455.overlay.wireformats;

public class TaskInitiate extends Message {

  public int getType(){
    return Protocol.TASK_INIT;
  }

  public byte[] getBytes(){
    return null; //TODO
  }

}
