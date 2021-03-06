package cs455.overlay.transport;

import cs455.overlay.node.MessagingNode;
import cs455.overlay.wireformats.EventFactory;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class TCPReceiverThread implements Runnable {

  private Socket socket;
  private DataInputStream din;
  private EventFactory eventFactory = EventFactory.getInstance();
  private String IPPort;

  public TCPReceiverThread(Socket socket) throws IOException {
    this.socket = socket;
    this.IPPort = socket.getRemoteSocketAddress().toString();
    this.IPPort = this.IPPort.substring(1);
    din = new DataInputStream(socket.getInputStream());
  }

  public void run(){
    int dataLength;
    boolean isP2P;
    while(socket != null){
      try{
        dataLength = din.readInt();
        byte[] data = new byte[dataLength];
        din.readFully(data, 0, dataLength);
        eventFactory.createEvent(data, this.IPPort);
      }catch (SocketException se){
        System.err.println(se.getMessage());
        break;
      }catch (IOException ioe){
        //Found an IOException
        System.err.println(ioe.getMessage());
        break;
      }
    }
  }

}
