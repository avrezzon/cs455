package cs455.scaling.server;

import cs455.scaling.protocol.Task;

import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadPoolManager {

    private ArrayList<WorkerThread> threadPool;
    private final static ConcurrentLinkedQueue<Task> taskQueue = new ConcurrentLinkedQueue<>();
    private static BatchMessages batchMessages;
    //TODO add the BatchMessage class implementation

    public ThreadPoolManager(int threadPoolSize, int maxBatchSize, int maxBatchTime) {

        //This will create the container that stores all of the worker threads
        this.threadPool = new ArrayList<>();
        for (int i = 0; i < threadPoolSize; i++) {
            threadPool.add(new WorkerThread());
        }

        this.batchMessages = new BatchMessages(maxBatchSize, maxBatchTime);
    }

    //This method is so that the worker threads have a reference to be notifyed on
    public static ConcurrentLinkedQueue<Task> getTaskQueueRef() {
        return taskQueue;
    }

    //This is will spin up all of the threads in the thread pool
    public void bootup() {
        for (int i = 0; i < threadPool.size(); i++) {
            WorkerThread worker = threadPool.get(i);
            new Thread(worker).start();
        }
    }

    //The worker threads will query this to determine if they need to sleep
    public synchronized static boolean isTaskQueueEmpty() {
        synchronized (taskQueue) {
            if (taskQueue.isEmpty()) {
                return true;
            }
            return false;
        }
    }

    //Threads will retrieve the task from the queue when notified
    public static Task getNextTask() {
        synchronized (taskQueue) {
            return taskQueue.poll();
        }
    }

    //This is invoked once the Manager accepts the task that is current in his queue
    public synchronized static void addTask(Task task) {
        synchronized (taskQueue) {
            taskQueue.add(task);
            taskQueue.notifyAll();
        }
    }

    //TODO review white board
    public static void addMsgKey(SelectionKey key) {

        if (key.attachment() != null) {
            Server.stats.receivedMsg();
        }

    }

    //TODO review white board
    public synchronized static Batch removeBatch() {
        return batchMessages.getDispatchBatch();
    }
}
