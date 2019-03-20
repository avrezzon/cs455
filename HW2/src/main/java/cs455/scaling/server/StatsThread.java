package cs455.scaling.server;

public class StatsThread implements Runnable {

  public void run() {

    long timesUp = System.currentTimeMillis() / 1000 + 20;

    while (true) {
      if (System.currentTimeMillis() / 1000 == timesUp) {
        System.out.println(Server.stats.toString());
        timesUp = System.currentTimeMillis() / 1000 + 20;
      }
    }
  }
}
