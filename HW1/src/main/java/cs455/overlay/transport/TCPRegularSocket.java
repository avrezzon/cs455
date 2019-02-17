package cs455.overlay.transport;

import java.io.IOException;
import java.net.Socket;

public class TCPRegularSocket {

    private Socket socket;
    private TCPReceiverThread receiverThread;
    private TCPSender sender;
    private String IP_Addr_Port;

    public TCPRegularSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.sender = new TCPSender(socket);
        this.receiverThread = new TCPReceiverThread(socket);
        this.IP_Addr_Port = socket.getRemoteSocketAddress().toString();
        this.IP_Addr_Port = this.IP_Addr_Port.substring(1);
    }

    //Returns the key that should be used for this regular socket
    public String getIPPort() {
        return this.IP_Addr_Port;
    }

    //Allows interactions to spawn the receiver thread
    public TCPReceiverThread getReceiverThread() {
        return this.receiverThread;
    }

    //allows access to send messages to the socket
    public TCPSender getSender() {
        return this.sender;
    }

    //This should be used to terminate a socket that either is invalid or needs to deregister
    public void killSocket() throws IOException{
        this.socket.close();
        this.socket = null;
    }

}
