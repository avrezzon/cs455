package cs455.overlay.transport;

import cs455.overlay.node.Registry;
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

    while(true){
      try{

        System.out.println("SERVER THREAD: WAITING FOR CONNECTION");
        inc_socket = server.accept();
        System.out.println("SERVER THREAD: ACCEPTED CONNECTION, SPAWNED NEW REGULAR SOCKET");
        TCPRegularSocket socket = new TCPRegularSocket(inc_socket);
        new Thread(socket.getReceiverThread()).start();
        //TODO add this regular socket into the dictionary ex: {"192.23.032.123:3551" : new TCPRegularSocket(inc_socket)}

      }catch(IOException e){
        System.err.println(e.getMessage());
        break;
      }
    }

  }



}
