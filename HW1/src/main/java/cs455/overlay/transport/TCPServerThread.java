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

  public int getPortnumber(){return this.port_number;}

  public TCPServerThread(int port_number) throws IOException {
    this.port_number = port_number;
    server = new ServerSocket(this.port_number);
  }

  public TCPServerThread() throws IOException{
    this.server = new ServerSocket(0);
    this.port_number = this.server.getLocalPort();
  }

  public void run(){

    Socket inc_socket;
    DataInputStream inputStream;

    while(true){
      try{

        System.out.println("SERVER THREAD: WAITING FOR CONNECTION");  //Will need to remove these later
        inc_socket = server.accept();
        inputStream = new DataInputStream(inc_socket.getInputStream());

        int packet_length = inputStream.readInt();
        byte[] byteString = new byte[packet_length];

        inputStream.readFully(byteString, 0 ,packet_length);
        inputStream.close();  //TODO revise and look at
        EventFactory.getInstance().createEvent(byteString.clone());

      }catch(IOException e){
        System.err.println(e.getMessage());
        break;
      }
    }

  }



}
