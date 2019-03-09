package cs455.scaling.client;

import cs455.scaling.protocol.Payload;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SenderThread implements Runnable {

  private int msgRate; // This is the rate in which the thread needs to put out messages to the server between 2 and 4
  private SocketChannel socket; // This should be the connection to the server who this node is sending messages to
  private static ByteBuffer buffer; // This is the buffer that writes the info to the socket

  public SenderThread(SocketChannel socket, int msgRate) {
    this.msgRate = msgRate;
    this.socket = socket; //This was the channel that associated to the server
  }

  public void run() {
    System.out.println("The sender thread is up and running");
    while (socket != null) {
      try {

        Payload msg = new Payload();
        System.out.println("MSG: " + msg.getBytes());
        buffer = ByteBuffer.wrap(msg.getBytes());
        socket.write(buffer);
        Thread.sleep(1000 / this.msgRate);
        buffer.clear();

        //TODO add all of the necessary fields for the stats collector.
        //FIXME add some of the other maintenance associated with sending a message

      } catch (InterruptedException ie) {
        System.out.println("The client sender thread is winding down: " + ie.getMessage());
        break;
      } catch (IOException ie) {
        System.err.println("Issue trying to write the buffer to the socket: " + ie.getMessage());
        break;
      }
    }
    System.out.println("The send thread is shutting down");
  }

}
