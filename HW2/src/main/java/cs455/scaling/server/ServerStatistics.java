package cs455.scaling.server;

import java.util.concurrent.atomic.AtomicInteger;

public class ServerStatistics {

    private AtomicInteger clientConnections;
    private AtomicInteger sentMsg;
    private AtomicInteger receivedMsg;
  private long lastTime;

  public ServerStatistics() {
      this.clientConnections = new AtomicInteger(0);
      this.sentMsg = new AtomicInteger(0);
      this.receivedMsg = new AtomicInteger(0);
    this.lastTime = System.currentTimeMillis() / 1000;
  }

  public synchronized void addConnection() {
      this.clientConnections.getAndIncrement();
  }

  public synchronized void dropConnection() {
      this.clientConnections.getAndDecrement();
  }

  public synchronized void receivedMsg() {
      this.receivedMsg.getAndIncrement();
  }

  public synchronized void sendMsg() {
      this.sentMsg.getAndIncrement();
  }

  public synchronized String toString() {
    long now = System.currentTimeMillis() / 1000;
      double serverThroughput = this.sentMsg.get() / 20.0;
    double meanClientThroughput =
            (clientConnections.get() == 0) ? (0) : (this.receivedMsg.get() / clientConnections.get()) / 20.0;
    double stdevPerClientThroughput = 0;

      String result = "[" + now + "] Server Throughput: " + serverThroughput +
              " messages/s, Active Client Connections: " + this.clientConnections +
              ", Mean Per-client Throughput: " + meanClientThroughput
              + " messages/s, Std. Dev. Of Per-client Throughput: " + stdevPerClientThroughput
              + " messages/s";

    lastTime = now;
      this.receivedMsg.set(0);
      this.sentMsg.set(0);

      return result;
  }

}
