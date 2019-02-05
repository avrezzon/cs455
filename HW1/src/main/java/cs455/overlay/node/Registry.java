package cs455.overlay.node;

import cs455.overlay.transport.TCPServerThread;
import cs455.overlay.wireformats.*;

//TODO Create this
public class Registry implements Node {

  private TCPServerThread server;

  public void onEvent(Event event){
    switch (event.getType()){
      case Protocol.REGISTER_RQ:
        System.out.println("This actually worked");
        break;
    }
  }

  public static void main(String args[]){
    try{
      TCPServerThread serverThread = new TCPServerThread(8088);
      new Thread(serverThread).run();
      System.out.println("You created the server thread");
    }catch (Exception e){
      System.err.println(e.getMessage());
    }
  }
}
