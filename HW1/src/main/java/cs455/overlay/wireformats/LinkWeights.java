package cs455.overlay.wireformats;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class LinkWeights implements Event {

  private final int type = Protocol.LINK_WEIGHTS;
  private int numberLinks;
  private ArrayList<LinkInfo> links;

  public LinkWeights(ArrayList<LinkInfo> links) {
    this.links = (ArrayList<LinkInfo>) links.clone();
    this.numberLinks = this.links.size();
  }

  public int getType() {
    return this.type;
  }

  public byte[] getBytes() throws IOException {
    byte[] marshalledBytes = null;
    byte[] stringRep = null;
    ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

    dout.writeInt(this.type);
    dout.writeInt(this.numberLinks);

    for (LinkInfo link : this.links) {
      //Writes the length of the main node
      dout.writeInt(link.getSendingNode().length());
      stringRep = link.getSendingNode().getBytes();
      dout.write(stringRep, 0, link.getSendingNode().length());

      dout.writeInt(link.getReceivingNode().length());
      stringRep = link.getReceivingNode().getBytes();
      dout.write(stringRep, 0, link.getReceivingNode().length());

      dout.writeInt(link.getConnectionWeight());

    }

    dout.flush();

    marshalledBytes = baOutputStream.toByteArray();

    baOutputStream.close();
    dout.close();

    return marshalledBytes;
  }

  public void resolve(String origin) {
    //TODO this will fill the nodes primary connection table
    System.out.println("Node now just needs to process all of the info");
    for (LinkInfo link : this.links) {
      System.out.printf("MainNode: %s Connected to: %s Weight; %d\n", link.getSendingNode(),
          link.getReceivingNode(), link.getConnectionWeight());
    }
  }
}
