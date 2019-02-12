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
  private volatile boolean alive;
  public int getPortnumber(){return this.port_number;}

  public TCPServerThread(int port_number) throws IOException {
    this.port_number = port_number;
    server = new ServerSocket(this.port_number);
    this.alive = true;
  }

  public TCPServerThread() throws IOException{
    this.server = new ServerSocket(0);
    this.port_number = this.server.getLocalPort();
    this.alive = true;
  }

  public void killThread(){alive = false;}

  //private boolean getAlive(){return alive;}

  public void run(){

    Socket inc_socket;

    while(alive){
      try{
        inc_socket = server.accept();
        TCPRegularSocket socket = new TCPRegularSocket(inc_socket);
        new Thread(socket.getReceiverThread()).start();
        //TODO there is an issue where two of the same threads are created and this one isnt destroyed
        //TODO add this regular socket into the dictionary ex: {"192.23.032.123:3551" : new TCPRegularSocket(inc_socket)}

      }catch(IOException e){
        System.err.println(e.getMessage());
        break;
      }
    }
    System.out.println("SERVER THREAD WINDING DOWN");
  }

}
