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

        byte success;
        String additional_info = null;
        RegisterResponse rrs = null;

        String key = new String(this.ip_addr + ":" + this.getPort());
        TCPRegularSocket socket = Registry.getConnections().get(key);

        if(socket.getVerficationStatus() == false){
            if(socket.getIPPort() == key){
                success = 1;
                socket.verifyConnection();
                rrs = new RegisterResponse(success);
            }
            else{
                success = 0;
                additional_info = "Socket IP address and port does not match the request";
                rrs = new RegisterResponse(success, additional_info);
            }
        }

        try {
            socket.getSender().sendData(rrs.getBytes());
        }catch (IOException ie){
            System.err.println("ERRROR IN REGISTER_RQ: " + ie.getMessage());
        }
    }

}
