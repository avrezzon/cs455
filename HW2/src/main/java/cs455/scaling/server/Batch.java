package cs455.scaling.server;

import cs455.scaling.protocol.Payload;
import java.util.LinkedList;

//NOTE that this class should be thread safe due to the fact that a worker thread is ONLY allowed to modify the batch of
//  the lock of the client node to be able to access this class
//This is stored within the client node and is what the worker thread will append the new message contents to
public class Batch {

    public int length;
    private int maxBatchSize;
    private double maxBatchTime; //This is declared as volatile so that the time is synchronized among the threads
    private double timesUp;
    private LinkedList<Payload> messages;
    //TODO this class could raise an event that would trigger the java nio if the thing has reached its time

    public Batch(int batchSize, double maxBatchTime) {
        this.maxBatchSize = batchSize;
        this.maxBatchTime = maxBatchTime;
        this.timesUp = System.currentTimeMillis() + this.maxBatchTime; //This will be the time when the message should of been sent
        this.messages = new LinkedList<Payload>();
    }

    //This is called when the batch has been sent and need to reset the state of the batch
    private void reset() {
        this.messages.clear();
        this.length = 0;
        this.timesUp = System.currentTimeMillis() + this.maxBatchTime;
    }

    //NOTE: after this function is called a new batch is created
    //TODO determine who will be accessing this guy
    public LinkedList<Payload> detach() {
        LinkedList<Payload> linkedMsg = (LinkedList<Payload>) this.messages.clone();
        reset();
        return linkedMsg;
    }

    public void append(Payload hashedPayload){
        messages.add(hashedPayload);
    }

}
