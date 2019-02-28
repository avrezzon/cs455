package cs455.scaling.wireformats;

import cs455.scaling.hash.Hash;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

//This is the data packet that will be sent to the server
//This class might be able to be used through both the server and the client
//i.e. That this class could self contain the hash of what the message was -> Brilliant
public class Payload {

  private byte[] msg;

  //This will be used when creating the message from the client side
  public Payload() throws NoSuchAlgorithmException {
    msg = new byte[8000];
    SecureRandom.getInstanceStrong().nextBytes(msg);
  }

  //This will be the constructor that the server will use to make sure that the msg is consistant
  public Payload(byte[] msg) {
    this.msg = msg.clone();
  }

  public String calculateMsgHash() throws NoSuchAlgorithmException {
    return Hash.SHA1FromBytes(msg);
  }

  public byte[] getBytes() {
    return this.msg.clone();
  }
}
