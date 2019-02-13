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
    int dataLength;
    System.out.println("RECIEVING THREAD IS ALIVE");
    while(socket != null){
      try{

        dataLength = din.readInt();
        byte[] data = new byte[dataLength];
        din.readFully(data, 0, dataLength);
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
    System.out.println("RECEIVING THREAD WINDING DOWN");
  }

}
