package cs455.scaling.server;

import cs455.scaling.protocol.Payload;
import cs455.scaling.protocol.Task;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

//This will be the Link list of connections that is maintained of
public class ClientNode {

    private Batch msgBatch;
    private String whoAmI; //TODO fix this but this needs to be the identity of the client being accessed

    public ClientNode(int batchSize, int batchTime){
        this.msgBatch = new Batch(batchSize, batchTime); //TODO confirm what would be an example of the cmd line args in use
        this.whoAmI = null;
    }

    //This will remove the set of hashed messages
    public synchronized LinkedList<Payload> detach(){
        return msgBatch.detach();
    }

    //TODO create the payload out of the passed task
    public synchronized void append(Task task){
        //TODO convert a task or extrapolate the payload
        Payload temp = null;
        try {
            temp = new Payload();
        }catch (NoSuchAlgorithmException ne){
            System.err.println("Unable to create a payload in Client.append() : " + ne.getMessage());
        }
        this.msgBatch.append(temp);
    }

    public synchronized int length(){
        return msgBatch.length;
    }

}
