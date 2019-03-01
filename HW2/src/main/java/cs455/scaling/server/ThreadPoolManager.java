package cs455.scaling.server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadPoolManager {

    private static ArrayList<WorkerThread> threadPool; //FIXME this could be a queue of threads
    private static ConcurrentLinkedQueue<Task> taskQueue;
    private static LinkedList<ClientNode> clients; //FIXME Im not sure if access is synchronized
    private static ConcurrentHashMap<String, String> clientConnections; //TODO fix the key value pairing

    private int maxBatchSize;
    private int maxBatchTime;

    //thread-pool-size batch-size batch-time
    public ThreadPoolManager(int threadPoolSize, int maxBatchSize, int maxBatchTime){
        for(int i = 0; i < threadPoolSize; i++){
            threadPool.add(new WorkerThread());
        }
        clientConnections = new ConcurrentHashMap<>(16); //Not sure how many keys i should allow up to
        this.maxBatchSize = maxBatchSize;
        this.maxBatchTime = maxBatchTime;
    }

    private void bootup(){
        for(WorkerThread worker : threadPool){
            new Thread(worker).start();
        }
    }

    public synchronized void register(String clientInfo){
        clients.add(new ClientNode(this.maxBatchSize, this.maxBatchTime));
    }

    public synchronized static void addTask(Task task){
        taskQueue.add(task);
        //TODO need to notify the thread pool of a job
    }

}
