package cs455.overlay.transport;

import cs455.overlay.node.Node;
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
    DataOutputStream outputStream;

    while(true){
      try{

        inc_socket = server.accept();

        //TODO  add data structure to verify if the connection has been made
        //Socket.getInetAddress() will return the object below
        //InetAddress.getHostAddress() will provide the IP address

        inputStream = new DataInputStream(inc_socket.getInputStream());
        outputStream = new DataOutputStream(inc_socket.getOutputStream());

        //TODO need to establish events and what messages are
        //NOTE all of the elements within the wireformats just define the structure of the messages

        //This is established after the message is verified and connection should continue
        TCPSender sender = new TCPSender(inc_socket);
        TCPRecieverThread recieverThread = new TCPRecieverThread(inc_socket);
        new Thread(recieverThread).start();


      }catch(IOException e){

      }
    }
  }



}
