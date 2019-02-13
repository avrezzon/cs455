package cs455.overlay.transport;

import cs455.overlay.exceptions.ObjectAlreadyExistsException;
import cs455.overlay.node.Registry;
import cs455.overlay.wireformats.EventFactory;
import cs455.overlay.wireformats.RegisterResponse;
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

        String ip = inc_socket.getInetAddress().getHostAddress();
        int port = inc_socket.getPort();

        String key = ip + ":" + port;
        System.out.println("SERVER SOCKET SPAWNED A NEW CONNECTION WITH KEY: " + key);

        try {
          Registry.addConnection(key, socket);
          new Thread(socket.getReceiverThread()).start();
        }catch (ObjectAlreadyExistsException oe){

          //TODO im having issues again

          System.err.println(oe.getMessage()); //Might need to remove
          new Thread (socket.getReceiverThread()).start();//Just send the response and it doesnt matter if his fills
          socket.getSender().sendData(new RegisterResponse((byte)0, " Messaging node already registered under that IP and port address").getBytes());
          //Within here I should open the message send the response that it was a fail so idont have to deal with this on the resolve level
          socket.killSocket();
        }
      }catch(IOException ie){
        System.out.println(ie.getMessage());
        break;
      }
    }
    System.out.println("SERVER THREAD WINDING DOWN");
  }

}
