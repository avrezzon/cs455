package cs455.overlay.transport;

import cs455.overlay.exceptions.ObjectAlreadyExistsException;
import cs455.overlay.node.Registry;
import cs455.overlay.wireformats.RegisterResponse;
import java.net.*;

import java.io.IOException;
import java.net.ServerSocket;

public class TCPServerThread implements Runnable{

  private ServerSocket server;
  private int port_number;
  private String IP_addr;
  private volatile boolean alive;

  public int getPortnumber(){return this.port_number;}

  public String getIP(){return this.IP_addr;}

  //This creates the server socket on a specified port
  public TCPServerThread(int port_number) throws IOException {
    this.port_number = port_number;
    server = new ServerSocket(this.port_number);
    this.IP_addr = server.getInetAddress().getHostAddress();
    this.alive = true;
  }

  //This creates the server socket on the first free port -> used with the MessagingNodes
  public TCPServerThread() throws IOException{
    this.server = new ServerSocket(0);
    this.port_number = this.server.getLocalPort();
    this.IP_addr = InetAddress.getLocalHost().getHostAddress();
    this.alive = true;
  }

  //This is used to kill the server socket run() method
  public void killThread(){alive = false;}

  public void run(){

    Socket inc_socket;

    while(alive){
      try{
        //Accepts the connections from incoming nodes and spawns the regular socket
        inc_socket = server.accept();
        TCPRegularSocket socket = new TCPRegularSocket(inc_socket);

        //FIXME this is genuinely the issue then LAST WROTE ON WEDNESDAY AT DENTIST
        String key = socket.getIPPort();
        System.out.println("SERVER THREAD JUST RECIEVED A CONNECTION OF " + key);

        try {

          //This will attempt to add a connection to the Registry
          //will throw an exception if the key already exists

          Registry.addConnection(key, socket);

          //Upon success of adding the connection, we spin the thread up
          new Thread(socket.getReceiverThread()).start();

        }catch (ObjectAlreadyExistsException oe){

          //Entering this means that the node has already been registered
          //Precondition is that the Messaging node has already spun up the recieving thread for the Registry
          socket.getSender().sendData(new RegisterResponse((byte)0, " Messaging node already registered under that IP and port address").getBytes());

          //Terminate the socket that was spawned from recieving the connection RQ
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
