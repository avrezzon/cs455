package cs455.scaling.server;


import cs455.scaling.protocol.Task;

import java.nio.channels.SelectionKey;
import java.util.LinkedList;

public class BatchMessages {

    private LinkedList<Batch> batchLL;
    private volatile int headNodePtr;
    private int batchSize;
    private int batchTime;

    public BatchMessages(int batchSize, int batchTime){

        this.batchLL = new LinkedList<>();
        this.batchLL.add(new Batch(batchSize, batchTime));

        this.headNodePtr = 0;
        this.batchSize = batchSize;
        this.batchTime = batchTime;
    }

    public synchronized void append(SelectionKey key){

        Batch currentBatch = null;
        synchronized (batchLL){
            currentBatch = this.batchLL.get(headNodePtr);
            if(currentBatch.readyToDispatch()){

                //Create a new link in the batchLL for the append currently happening
                headNodePtr = headNodePtr + 1;

                //create the new batch that the next current key will be able to append to
                batchLL.add(new Batch(batchSize, batchTime));

                //TODO signal batch for dispatch by creating a method to add the new type of task to taskQueue
                ThreadPoolManager.addTask(new Task(true));
            }

            //Add the current key now to the selected batch that
            currentBatch.append(key);
        }
    }

    //This method will be invoked once the task that contains the dispatch info calls the .resolve()
    public synchronized Batch getDispatchBatch(){
        Batch dispatchBatch = null;
        synchronized (batchLL){
            //All that I need to do is retrieve the batch and decrement the headNode
            dispatchBatch = this.batchLL.removeFirst();
            headNodePtr = headNodePtr - 1;
        }
        return dispatchBatch;
    }

}
