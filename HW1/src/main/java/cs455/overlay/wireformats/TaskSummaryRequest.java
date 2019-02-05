package cs455.overlay.wireformats;

public class TaskSummaryRequest extends Message{

  public int getType(){
    return Protocol.TASK_SUMMARY_RQ;
  }

  public byte[] getBytes(){
    return null; //TODO
  }

}
