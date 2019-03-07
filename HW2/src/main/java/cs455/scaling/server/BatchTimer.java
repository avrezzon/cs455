package cs455.scaling.server;

public class BatchTimer implements Runnable {

  private int duration;

  public BatchTimer(int maxBatchTime) {
    this.duration = maxBatchTime;
  }

  public void run() {
    long end = System.currentTimeMillis() / 1000 + this.duration;
    while (System.currentTimeMillis() / 1000 != end) {
      continue;
    }
    //TODO figure the logic that should fall behind this
    System.out.println("BATCH LAUNCHING --> max batch time ");
  }
}
