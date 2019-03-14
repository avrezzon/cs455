package cs455.scaling.client;

public class ClientStatsThread implements Runnable {
    public void run() {

        long timesUp = System.currentTimeMillis() / 1000 + 5; //FIXME

        while (true) {
            if (System.currentTimeMillis() / 1000 == timesUp) {
                System.out.println(Client.StatsCollector.toString());
                timesUp = System.currentTimeMillis() / 1000 + 5; //FIXME
            }
        }
    }
}
