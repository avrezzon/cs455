package cs455.overlay.transport;

import cs455.overlay.node.Node;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class TCPRecieverThread implements Runnable {

  private Socket socket;
  private DataInputStream din;

  public TCPRecieverThread(Socket socket) throws IOException {
    this.socket = socket;
    din = new DataInputStream(socket.getInputStream());
  }

  public void run(){
    int dataLength;
    while(socket != null){
      try{

        dataLength = din.readInt();
        byte[] data = new byte[dataLength];
        din.readFully(data, 0, dataLength);

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
