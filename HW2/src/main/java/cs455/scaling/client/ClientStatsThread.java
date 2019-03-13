package cs455.scaling.client;

import cs455.scaling.server.Server;

public class ClientStatsThread implements Runnable {
    public void run() {

        //FIXME ensure that the elapsed time i.e. 5 seconds or whatever the specified time is consistent throughout the timers
        long timesUp = System.currentTimeMillis() / 1000 + 20;

        while (true) {
            if (System.currentTimeMillis() / 1000 == timesUp) {
                System.out.println(Client.StatsCollector.toString());
                timesUp = System.currentTimeMillis() / 1000 + 20;
            }
        }
    }
}
