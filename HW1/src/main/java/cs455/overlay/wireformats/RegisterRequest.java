package cs455.overlay.wireformats;

import cs455.overlay.node.Registry;
import cs455.overlay.transport.TCPRegularSocket;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;


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

        System.out.println("REGISTER RQ IP: " + this.getIP());
        System.out.println("REGISTER RQ Port: " + this.getPort());
        return marshalledBytes;
    }

    //TODO this will require the port that it was actually sent from
    public void resolve(String origin){

        byte success;
        String additional_info = null;
        RegisterResponse rrs = null;

        String key = this.ip_addr + ":" + this.getPort();
        System.out.println("Connection key " + key);
        System.out.println("This request was recieved on this port: " + origin);
        Registry.printConnections();

        if(Registry.isMessagingNodePresent(key)){
            //TODO this is an error case and registration should not happen
        }else{
            //This means that this is the first time registering the Messaging node
            Registry.addServerMapping(key, origin);
            TCPRegularSocket socket = Registry.getTCPSocket(key);
            rrs = new RegisterResponse((byte)0);
            try {
                socket.getSender().sendData(rrs.getBytes());
            }catch(IOException ie){
                System.err.println("Unable to get the bytes from the register response at RegisterRQ ln 80");
            }
            //TODO notify the registry to print the number of connections made now
        }
        Registry.printConnections();

    }

}
