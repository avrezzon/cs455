package cs455.overlay.transport;

import cs455.overlay.wireformats.EventFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.*;

import java.io.IOException;
import java.net.ServerSocket;

public class TCPServerThread implements Runnable{

  private ServerSocket server;
  private int port_number;

  public TCPServerThread(int port_number) throws IOException {
    this.port_number = port_number;
    //This should really be in a loop that determines if a port is open and the loops through the possible options
    server = new ServerSocket(port_number);
  }

  public void run(){

    Socket inc_socket;
    DataInputStream inputStream;

    while(true){
      try{

        inc_socket = server.accept();

        inputStream = new DataInputStream(inc_socket.getInputStream());
        int packet_length = inputStream.readInt();
        byte[] byteString = new byte[packet_length];
        inputStream.readFully(byteString, 0 ,packet_length);
        inputStream.close();
        EventFactory.getInstance().createEvent(byteString);

      }catch(IOException e){
        System.err.println(e.getMessage());
        break;
      }
    }

  }



}
