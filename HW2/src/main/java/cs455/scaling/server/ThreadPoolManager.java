package cs455.scaling.server;

import cs455.scaling.protocol.Task;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadPoolManager {

  private ArrayList<WorkerThread> threadPool; //An ArrayList was used instead of a queue because there should be no order in which the workers should be able to access the tasks
  private static ConcurrentLinkedQueue<Task> taskQueue;
  private static LinkedList<Batch> messageBatch;
  private int maxBatchSize;
  private int maxBatchTime;
  private ThreadPoolManagerThread managerThread;

  //thread-pool-size batch-size batch-time
  public ThreadPoolManager(int threadPoolSize, int maxBatchSize, int maxBatchTime) {

    this.threadPool = new ArrayList<>(); //Creates the storage unit for all of the threads in the pool

    for (int i = 0; i < threadPoolSize; i++) {
      threadPool.add(new WorkerThread());
    }

    this.maxBatchSize = maxBatchSize;
    this.maxBatchTime = maxBatchTime;

    messageBatch = new LinkedList<Batch>();
    taskQueue = new ConcurrentLinkedQueue<>(); //TODO verify this data structure

    this.managerThread = new ThreadPoolManagerThread();
  }

  //FIXME I know that taskQueue is a ConcurrentLinkedQueue is the behavior thread safe?
  public static void addTask(Task task) {
    taskQueue.add(task);
  }

  //This needs to be called after the ctor, This will launch all of the threads that need to be active
  private void bootup() {

    for (WorkerThread worker : threadPool) {
      new Thread(worker).start();
    }

    new Thread(this.managerThread).start();
  }




}
