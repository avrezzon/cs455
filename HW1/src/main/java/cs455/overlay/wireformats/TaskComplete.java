package cs455.overlay.wireformats;


public class TaskComplete extends Event {

  public int getType(){
    return Protocol.TASK_COMPLETE;
  }

  public byte[] getBytes(){
    return null; // TODO
  }

}
