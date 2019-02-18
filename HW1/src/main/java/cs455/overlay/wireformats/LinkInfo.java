package cs455.overlay.wireformats;

public class LinkInfo {

  private String sendingNode;
  private String receivingNode;
  private int connectionWeight;

  //This class is strictly to pass a bunch of data together
  public LinkInfo(String sendingNode, String receivingNode, int connectionWeight) {
    this.sendingNode = sendingNode;
    this.receivingNode = receivingNode;
    this.connectionWeight = connectionWeight;
  }

  public String getSendingNode() {
    return this.sendingNode;
  }

  public String getReceivingNode() {
    return this.receivingNode;
  }

  public int getConnectionWeight() {
    return this.connectionWeight;
  }
}
