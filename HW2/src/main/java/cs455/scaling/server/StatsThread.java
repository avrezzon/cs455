package cs455.scaling.server;

public class StatsThread implements Runnable {

  public void run() {

    //FIXME ensure that the elapsed time i.e. 5 seconds or whatever the specified time is consistent throughout the timers
    long timesUp = System.currentTimeMillis() / 1000 + 5;

    while (true) {
      if (System.currentTimeMillis() / 1000 == timesUp) {
        System.out.println(Server.stats.toString());
        timesUp = System.currentTimeMillis() / 1000 + 5;
      }
    }
  }
}
