package cs455.scaling.server;

import cs455.scaling.protocol.Task;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadPoolManager {

  private ArrayList<WorkerThread> threadPool; //An ArrayList was used instead of a queue because there should be no order in which the workers should be able to access the tasks
  public static ConcurrentLinkedQueue<Task> taskQueue; //TODO this seems wrong to do
  private static LinkedList<Batch> messageBatch;
  private int maxBatchSize;
  private int maxBatchTime;
  //private ThreadPoolManagerThread managerThread;

  //thread-pool-size batch-size batch-time
  public ThreadPoolManager(int threadPoolSize, int maxBatchSize, int maxBatchTime) {

    this.threadPool = new ArrayList<>(
        threadPoolSize); //Creates the storage unit for all of the threads in the pool

    for (int i = 0; i < threadPoolSize; i++) {
      threadPool.add(new WorkerThread());
    }

    this.maxBatchSize = maxBatchSize;
    this.maxBatchTime = maxBatchTime;

    messageBatch = new LinkedList<Batch>();
    taskQueue = new ConcurrentLinkedQueue<>(); //TODO verify this data structure

    //this.managerThread = new ThreadPoolManagerThread();
  }

  //FIXME I know that taskQueue is a ConcurrentLinkedQueue is the behavior thread safe?
  public synchronized void addTask(Task task) {
    System.out.println("Adding the task at the threadpoolmanager");

    taskQueue.add(task);
    synchronized (taskQueue) {
      taskQueue.notifyAll();
    }
  }

  //This needs to be called after the ctor, This will launch all of the threads that need to be active
  public void bootup() {
    System.out.println("ThreadPoolManager: Thread pool is spinning up now...");
    for (int i = 0; i < threadPool.size(); i++) {
      WorkerThread worker = threadPool.get(i);
      new Thread(worker).start();
    }
    //new Thread(this.managerThread).start();
    System.out.println("ThreadPoolManager: Thread pool is alive and workers are waiting");
  }



}
