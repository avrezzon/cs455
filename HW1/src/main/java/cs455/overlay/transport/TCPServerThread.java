package cs455.overlay.transport;

import cs455.overlay.node.Registry;
import java.net.*;

import java.io.IOException;
import java.net.ServerSocket;

public class TCPServerThread implements Runnable {

    private ServerSocket server;
    private int port_number;
    private String IP_addr;
    private volatile boolean alive;

    public int getPortnumber() {
        return this.port_number;
    }

    public String getIP() {
        return this.IP_addr;
    }

    //This creates the server socket on a specified port
    public TCPServerThread(int port_number) throws IOException {
        server = new ServerSocket(port_number);
        this.IP_addr = InetAddress.getLocalHost().getHostAddress();
        this.port_number = server.getLocalPort();
        this.alive = true;
    }

    //This creates the server socket on the first free port -> used with the MessagingNodes
    public TCPServerThread() throws IOException {
        this.server = new ServerSocket(0);
        this.port_number = this.server.getLocalPort();
        this.IP_addr = InetAddress.getLocalHost().getHostAddress();
        this.alive = true;
    }

    //This is used to kill the server socket run() method
    public void killThread() {
        alive = false;
    }

    public void run() {

        Socket inc_socket;

        while (alive) {
            try {
                //Accepts the connections from incoming nodes and spawns the regular socket
                inc_socket = server.accept();
                //Adds the new connection into the connections map
                TCPRegularSocket connection = new TCPRegularSocket(inc_socket);
                Registry.receivedConnection(connection);

                new Thread(connection.getReceiverThread()).start();

            } catch (IOException ie) {
                System.out.println(ie.getMessage());
                break;
            }
        }
        System.out.println("SERVER THREAD WINDING DOWN");
    }

}
