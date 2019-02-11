package cs455.overlay.wireformats;

import cs455.overlay.node.Registry;
import cs455.overlay.transport.TCPRegularSocket;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RegisterRequest implements Event {

    private final int type = Protocol.REGISTER_RQ;
    private String ip_addr;
    private int port_number;

    public RegisterRequest(String ip_addr, int port_number) throws IOException {
        this.ip_addr = ip_addr;
        this.port_number = port_number;
    }

    public String getIP() {
        return this.ip_addr;
    }

    public int getPort() {
        return this.port_number;
    }

    public int getType() {
        return this.type;
    }

    public byte[] getBytes() throws IOException {
        byte[] marshalledBytes;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

        dout.writeInt(this.type);
        dout.writeInt(this.ip_addr.length());
        byte[] msg = this.ip_addr.getBytes();
        ;
        dout.write(msg, 0, ip_addr.length());
        dout.writeInt(this.port_number);
        dout.flush();

        marshalledBytes = baOutputStream.toByteArray();

        baOutputStream.close();
        dout.close();

        return marshalledBytes;
    }

    public void resolve(){

        String key = new String(this.ip_addr + ":" + this.getPort());
        System.out.println("The key for this node is  " + key);
        TCPRegularSocket socket;

        try{
            socket = Registry.connections.get(key);
        }catch(NullPointerException ne){
            //If we reach this portion of the block that means the node has not already registered with the Registry
            try {
                socket = new TCPRegularSocket(new Socket(this.ip_addr, this.port_number));
                new Thread(socket.getReceiverThread()).start();
                Registry.connections.put(key, socket);
                Event rrs = new RegisterResponse( (byte)1 );
                socket.getSender().sendData(rrs.getBytes());
            }catch (IOException ie){
                System.err.println("IOException occured within REGISTER RQ .resolve(): " + ie.getMessage());
            }
        }
    }

}
