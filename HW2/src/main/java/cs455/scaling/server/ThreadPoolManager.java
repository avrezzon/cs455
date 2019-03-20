package cs455.scaling.server;

import cs455.scaling.protocol.Task;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadPoolManager {

    private ArrayList<WorkerThread> threadPool;
    private final static ConcurrentLinkedQueue<Task> taskQueue = new ConcurrentLinkedQueue<>();
    private static BatchMessages batchMessages;


    public ThreadPoolManager(int threadPoolSize, int maxBatchSize, int maxBatchTime) {

        //This will create the container that stores all of the worker threads
        this.threadPool = new ArrayList<>();
        for (int i = 0; i < threadPoolSize; i++) {
            threadPool.add(new WorkerThread());
        }

        batchMessages = new BatchMessages(maxBatchSize, maxBatchTime);
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
    public boolean isTaskQueueEmpty() {
        return taskQueue.isEmpty();
    }

    //Static version for worker threads
    public static boolean availableTasks() {
        return !taskQueue.isEmpty();
    }

    //Threads will retrieve the task from the queue when notified
    public static Task getNextTask() {
        return taskQueue.poll();
    }


    public void addTask(Task task) {
        taskQueue.add(task);
    }

    //This is invoked once the Manager accepts the task that is current in his queue
    public void wakeupWorkers() {
        synchronized (taskQueue) {
            taskQueue.notify();
        }
    }

    //This needs to append a client info node along with the message into the 'link'
    public static void addMsgKey(SelectionKey key) {
        if (key.attachment() != null) {
            batchMessages.append(key);
        }
    }

}
