package cs455.overlay.wireformats;


public class TaskComplete{

  public int getType(){
    return Protocol.TASK_COMPLETE;
  }

  public byte[] getBytes(){
    return null;
  }

}
