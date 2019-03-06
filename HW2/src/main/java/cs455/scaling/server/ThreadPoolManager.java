package cs455.scaling.server;

import cs455.scaling.protocol.Task;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadPoolManager {

  private ArrayList<WorkerThread> threadPool;
  public static ConcurrentLinkedQueue<Task> taskQueue;
  private static LinkedList<Batch> messageBatch;
  private int maxBatchSize;
  private int maxBatchTime;

  public ThreadPoolManager(int threadPoolSize, int maxBatchSize, int maxBatchTime) {

    this.threadPool = new ArrayList<>(); //Creates the storage unit for all of the threads in the pool
    for (int i = 0; i < threadPoolSize; i++) {
      threadPool.add(new WorkerThread());
    }
    this.maxBatchSize = maxBatchSize;
    this.maxBatchTime = maxBatchTime;
    messageBatch = new LinkedList<Batch>();
    taskQueue = new ConcurrentLinkedQueue<>(); //TODO verify this data structure
  }

  //This is invoked once the server gets activity on a channel
  public void addTask(Task task) {
    synchronized (taskQueue) {
      taskQueue.add(task);
      taskQueue.notify();
    }
  }

  //This needs to be called after the ctor, This will launch all of the threads that need to be active
  public void bootup() {
    for (int i = 0; i < threadPool.size(); i++) {
      WorkerThread worker = threadPool.get(i);
      new Thread(worker).start();
    }
  }



}
