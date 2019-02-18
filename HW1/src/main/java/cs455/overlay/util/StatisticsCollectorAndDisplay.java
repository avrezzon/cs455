package cs455.overlay.util;

public class StatisticsCollectorAndDisplay {

  private volatile int msgSent; //Total number of messages sent
  private volatile int msgReceived; //Total number of messages received
  private volatile int msgSumSent; //Summation of sent messages
  private volatile int msgSumReceived; //Summation of messaged recieved
  private volatile int msgRelayed;  //Number of messages relayed

  public StatisticsCollectorAndDisplay() {
    this.msgSent = 0;
    this.msgReceived = 0;
    this.msgSumSent = 0;
    this.msgSumReceived = 0;
    this.msgRelayed = 0;
  }

  public StatisticsCollectorAndDisplay(int msgSent, int msgSumSent, int msgReceived,
      int msgSumReceived, int msgRelayed) {
    this.msgSent = msgSent;
    this.msgSumSent = msgSumSent;
    this.msgReceived = msgReceived;
    this.msgSumReceived = msgSumReceived;
    this.msgRelayed = msgRelayed;
  }

  //TODO Verify that there are no race conditions
  public void SendMsg(int msgWeight) {
    msgSent += 1;
    msgSumSent += msgWeight;
  }

  public void RelayMsg() {
    msgRelayed += 1;
  }

  public void ReceiveMsg(int msgWeight) {
    msgReceived += 1;
    msgSumReceived += msgWeight;
  }

  public void resetStats() {
    this.msgSent = 0;
    this.msgReceived = 0;
    this.msgSumSent = 0;
    this.msgSumReceived = 0;
    this.msgRelayed = 0;
  }

  public int getMsgSent() {
    return msgSent;
  }

  public int getMsgReceived() {
    return msgReceived;
  }

  public int getMsgSumSent() {
    return msgSumSent;
  }

  public int getMsgSumReceived() {
    return msgSumReceived;
  }

  public int getMsgRelayed() {
    return msgRelayed;
  }
}
