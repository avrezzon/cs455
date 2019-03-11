package cs455.scaling.server;

public class StatsThread implements Runnable {

  public void run() {

    //TODO this needs to be fixed along with all of the other classes that used the timer
    long timesUp = System.currentTimeMillis() / 1000 + 5;

    while (true) {
      if (System.currentTimeMillis() / 1000 == timesUp) {
        System.out.println(Server.stats.toString());
        timesUp = System.currentTimeMillis() / 1000 + 5;
      }
    }
  }
}
