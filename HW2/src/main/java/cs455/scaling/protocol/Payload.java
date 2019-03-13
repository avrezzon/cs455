package cs455.scaling.protocol;

import cs455.scaling.hash.Hash;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

//This is the data packet that will be sent to the server
//This class might be able to be used through both the server and the client
//i.e. That this class could self contain the hash of what the message was -> Brilliant
public class Payload {

  private byte[] msg;
  private String hash;
  private Random random;

  //NOTE: hash is set to null to prevent a message from being passed without the
  //This will be used when creating the message from the client side
  public Payload(){
    random = new Random();
    msg = new byte[8000];
    random.nextBytes(this.msg);
    hash = null;
  }

  //This will be the constructor that the server will use to make sure that the msg is consistant
  public Payload(byte[] msg) {
    this.msg = msg.clone();
  }

  //The hash needs to be saved so that when the server does send a responsed
  public void calculateMsgHash() throws NoSuchAlgorithmException {
    this.hash = Hash.SHA1FromBytes(msg);
  }

  public byte[] getBytes() {
    return this.msg;
  }

    public String getHash() {
        return this.hash;
    }
}
