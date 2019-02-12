package cs455.overlay.transport;

import cs455.overlay.wireformats.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class TCPReceiverThread implements Runnable {

  private Socket socket;
  private DataInputStream din;
  private EventFactory eventFactory = EventFactory.getInstance();

  public TCPReceiverThread(Socket socket) throws IOException {
    this.socket = socket;
    din = new DataInputStream(socket.getInputStream());
  }

  public void run(){
    System.out.println("RECEIVER THREAD IS ALIVE AND WELL");
    int dataLength;
    while(socket != null){
      try{

        dataLength = din.readInt();
        byte[] data = new byte[dataLength];
        din.readFully(data, 0, dataLength);
        System.out.println("RECEIVING THREAD: JUST RECEIVED A PACKET!");
        eventFactory.createEvent(data);

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
