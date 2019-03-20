package cs455.scaling.client;

import java.util.concurrent.atomic.AtomicInteger;

public class ClientStatistics {

    private AtomicInteger msgSent;
    private AtomicInteger msgReceived;
    private AtomicInteger mismatch;

  public ClientStatistics() {
      this.msgSent = new AtomicInteger(0);
      this.msgReceived = new AtomicInteger(0);
      this.mismatch = new AtomicInteger(0);
  }

    public void sentMsg() {
        msgSent.getAndIncrement();
  }

    public void receivedMsg() {
        msgReceived.getAndIncrement();

  }

    public void updateMismatch() {
        mismatch.getAndIncrement();

    }

    public String toString() {
        synchronized (msgSent) {
            synchronized (msgReceived) {
                synchronized (mismatch) {
                    return "[" + (System.currentTimeMillis() / 1000) + "] Total Sent Count: " + this.msgSent
                        + ", Total Received Count: " + this.msgReceived
                        + ", Total Bad messages received: " + this.mismatch;
                }
            }
        }
  }
}
