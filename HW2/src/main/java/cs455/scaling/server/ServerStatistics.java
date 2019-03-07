package cs455.scaling.server;

public class ServerStatistics {

  private int clientConnections;
  private int sentMsg;
  private int receivedMsg;
  private long lastTime;

  public ServerStatistics() {
    this.clientConnections = 0;
    this.sentMsg = 0;
    this.receivedMsg = 0;
    this.lastTime = System.currentTimeMillis() / 1000;
  }

  public synchronized void addConnection() {
    this.clientConnections += 1;
  }

  public synchronized void dropConnection() {
    this.clientConnections -= 1;
  }

  public synchronized void receivedMsg() {
    this.receivedMsg += 1;
  }

  public synchronized void sendMsg() {
    this.sentMsg += 1;
  }

  public synchronized String toString() {
    long now = System.currentTimeMillis() / 1000;
    double serverThroughput = this.sentMsg / 5.0;
    double meanClientThroughput = (this.receivedMsg / clientConnections) / 5.0;
    double stdevPerClientThroughput = 0;
    lastTime = now;
    this.receivedMsg = 0;
    this.sentMsg = 0;

    return "[" + now + "] Server Throughput: " + serverThroughput +
        " messages/s, Active Client Connections: " + this.clientConnections +
        ", Mean Per-client Throughput: " + meanClientThroughput
        + " messages/s, Std. Dev. Of Per-client Throughput: " + stdevPerClientThroughput
        + " messages/s";
  }

}
