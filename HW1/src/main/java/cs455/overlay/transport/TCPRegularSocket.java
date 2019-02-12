package cs455.overlay.transport;

import java.io.IOException;
import java.net.Socket;

public class TCPRegularSocket {

  private Socket socket;
  private TCPReceiverThread receiverThread;
  private TCPSender sender;
  private String IP_Addr_Port;
  private boolean verified; //TODO implement this so that when we do have an incsocket that we can add to the connections and if the request isnt verified its gets pulled

  public TCPRegularSocket(Socket socket) throws IOException {
    this.socket = socket;
    this.receiverThread = new TCPReceiverThread(socket);
    this.sender = new TCPSender(socket);
    this.verified = false;
  }

  public void verifyConnection() {this.verified = true;}

  public boolean getVerficationStatus(){return this.verified;}

  public String getConnectionInfo() {return this.IP_Addr_Port;}

  public void setConnectionInfo(String IP_Addr_Port){ this.IP_Addr_Port = IP_Addr_Port; }

  public Socket getMainSocket(){  return this.socket; }

  public TCPReceiverThread getReceiverThread() {  return this.receiverThread; }

  public TCPSender getSender() {  return this.sender; }

  //This should be used to terminate a socket that either is invalid or needs to deregister
  public void killSocket() { this.socket = null; }

}
