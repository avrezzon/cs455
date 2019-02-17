package cs455.overlay.wireformats;

public class Protocol {

  //Used to setup
  public static final int REGISTER_RQ = 11;
  public static final int REGISTER_RS = 12;
  public static final int DEREGISTER_RQ = 13;
  public static final int DEREGISTER_RS = 14;

  //Protocols that contain the message format
  public static final int MESSAGING_NODE_LIST = 15;
  public static final int LINK_WEIGHTS = 16;
  public static final int TASK_INIT = 17;
  public static final int TASK_COMPLETE = 18;
  public static final int TASK_SUMMARY_RQ = 19;
  public static final int TASK_SUMMARY_RS = 20;

  public static final byte success = 1;
  public static final byte failure = 0;

  public static final int registry = 4;
  public static final int messagingNode = 5;


}

