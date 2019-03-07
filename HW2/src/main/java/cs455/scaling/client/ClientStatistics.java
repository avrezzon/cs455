package cs455.scaling.client;

public class ClientStatistics {

  private int msgSent;
  private int msgReceived;

  public ClientStatistics() {
    this.msgSent = 0;
    this.msgReceived = 0;
  }

  public synchronized void sentMsg() {
    this.msgSent += 1;
  }

  public synchronized void receivedMsg() {
    this.msgReceived += 1;
  }

  public synchronized String toString() {
    return "[" + (System.currentTimeMillis() / 1000) + "] Total Sent Count: " + this.msgSent
        + ", Total Received Count: " + this.msgReceived;
  }
}
