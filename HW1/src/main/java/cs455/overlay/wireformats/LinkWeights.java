package cs455.overlay.wireformats;

import java.util.ArrayList;

public class LinkWeights implements Event {

  private final int type = Protocol.LINK_WEIGHTS;
  private ArrayList<LinkInfo> links;

  //PRECONDITION: going into this better be
  public LinkWeights(ArrayList<LinkInfo> links) {
    this.links = (ArrayList<LinkInfo>) links.clone();
  }

  public int getType() {
    return this.type;
  }

  public byte[] getBytes() {
    return null;
  }

  public void resolve(String origin) {
    //TODO this will fill the nodes primary connection table
  }
}
