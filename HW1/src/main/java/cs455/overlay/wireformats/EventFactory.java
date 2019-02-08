package cs455.overlay.wireformats;

import cs455.overlay.node.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class EventFactory {

  private static EventFactory eventFactory_instance  = null;
  private Node listening_node;

  public static EventFactory getInstance(){
    if(eventFactory_instance == null){
      eventFactory_instance = new EventFactory();
    }
    return eventFactory_instance;
  }

  public synchronized void addListener(Node node){
    this.listening_node = node;
  }

  public synchronized void removeListener(){
    this.listening_node = null;
  }


  //This class is responsible for holding the type of the message
  public synchronized void createEvent(byte[] byteString) throws IOException {

    System.out.println("Unaltered bytestring " + byteString);

    ByteArrayInputStream baInputStream = new ByteArrayInputStream(byteString);
    DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

    System.out.println("did i make3 it this far4?");

    //int type = din.readInt(); //This will read the type

    int type = Protocol.REGISTER_RQ;

    switch (type){
      case Protocol.REGISTER_RQ:

        //TODO the request HAS to be incorrect thats why things are messed
        int ip_len = din.readInt();
        byte[] ip_addr_bytes = new byte[ip_len];
        din.readFully(ip_addr_bytes, 0 ,ip_len);
        String ip_addr = new String(ip_addr_bytes);
        int port_number = din.readInt();
        RegisterRequest rrq = new RegisterRequest(ip_addr, port_number);
        System.out.println(rrq.getIP());
        listening_node.onEvent(rrq);
        break;
    }
  }



}
