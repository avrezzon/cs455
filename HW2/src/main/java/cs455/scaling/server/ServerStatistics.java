package cs455.scaling.server;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerStatistics {

    private AtomicInteger clientConnections;
    private long lastTime;
    private ConcurrentHashMap<SocketChannel, Stats> connectionStats;
    private ArrayList<SocketChannel> connections;

    public class Stats {
        private int receivedMsg;
        private int totalReceivedMsg;

        public Stats() {
            this.receivedMsg = 0;
            this.totalReceivedMsg = 0;
        }

        public double meanThroughput() {
            System.out.println(this.toString());
            double val = this.receivedMsg / 20.0;
            this.receivedMsg = 0;
            return val;
        }

        public void recievedMsg() {
            this.receivedMsg += 1;
            this.totalReceivedMsg += 1;
        }

        public String toString() {
            return "Total Received: " + totalReceivedMsg + ", Recieved: " + this.receivedMsg;
        }
    }

    public ServerStatistics() {
        this.clientConnections = new AtomicInteger(0);
        this.connections = new ArrayList<>();
        this.connectionStats = new ConcurrentHashMap<>();
        this.lastTime = System.currentTimeMillis() / 1000;
    }

    public synchronized void addConnection(SocketChannel client) {
        this.clientConnections.getAndIncrement();
        this.connections.add(client);
        this.connectionStats.put(client, new Stats());
    }

    //FIXME!!!!!
    public synchronized void dropConnection() {
        this.clientConnections.getAndDecrement();
    }

    public synchronized void receivedMsg(SocketChannel client) {
        Stats s = this.connectionStats.get(client);
        s.recievedMsg();
        //System.out.println(s.toString() );
    }

    private double meanClientThroughput() {
        double sumMeans = 0.0;
        synchronized (connectionStats) {
            synchronized (connections) {
                for (SocketChannel client : connections) {
                    sumMeans += connectionStats.get(client).meanThroughput();
                }
                sumMeans = sumMeans / clientConnections.get();
            }
        }
        return sumMeans;
    }
//
//    public synchronized void sendMsg() {
//        this.sentMsg.getAndIncrement();
//    }

    public synchronized String toString() {
        long now = System.currentTimeMillis() / 1000;
        double serverThroughput = 0.0;

        double meanClientThroughput = meanClientThroughput();
        double stdevPerClientThroughput = 0;

        String result = "[" + now + "] Server Throughput: " + serverThroughput +
                " messages/s, Active Client Connections: " + this.clientConnections +
                ", Mean Per-client Throughput: " + meanClientThroughput
                + " messages/s, Std. Dev. Of Per-client Throughput: " + stdevPerClientThroughput
                + " messages/s";

        lastTime = now;


        return result;
    }

}
