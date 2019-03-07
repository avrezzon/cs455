package cs455.scaling.server;

import cs455.scaling.protocol.Task;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

//FIXME revise why some of the variables have global scope
public class ThreadPoolManager {

  private ArrayList<WorkerThread> threadPool;
  public static ConcurrentLinkedQueue<Task> taskQueue; //The reason why this is public is due to the scope that is needed by the worker threads.  TODO I can revise these calls to helper functions
  public static LinkedList<Batch> messageBatch;//$

  //NOTE that this will not always be 0, this way we can append to other links
  //This was marked as volatile so that every time it is fetched the current thread knows exactly what
  //node to write to
  //TODO this is a large factor that definitely needs to work
  private static volatile int headNodeIdx;

  private static int maxBatchSize;
  private static int maxBatchTime;

  public ThreadPoolManager(int threadPoolSize, int maxBatchSize, int maxBatchTime) {

    this.threadPool = new ArrayList<>(); //Creates the storage unit for all of the threads in the pool
    for (int i = 0; i < threadPoolSize; i++) {
      threadPool.add(new WorkerThread());
    }
    this.maxBatchSize = maxBatchSize;
    this.maxBatchTime = maxBatchTime;
    messageBatch = new LinkedList<Batch>();
    messageBatch.add(new Batch(maxBatchSize, maxBatchTime));
    taskQueue = new ConcurrentLinkedQueue<>();

    headNodeIdx = 0; //Upon initialization the head node should be the first node
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

  //With this method we will check the current head node to determine if the batch needs to be sent out back to
  //The clients.  NOTE upon a successful detatchment, the method will insert a new link as the head node prior to the detachment
  public synchronized static void addMsgKey(SelectionKey key) {

    Batch currentBatch = null;

    //We want to check to see if the head node is max batch size a new
    if (messageBatch.get(headNodeIdx).dispatch()) {

      //This is the case where the batch should be removed from the head of the linked list
      //from the detachment, a new task should be added to the taskQueue with the dispatch batch object
      //that the worker thread will perform work on
      taskQueue.add(new Task(headNodeIdx));
      messageBatch.add(new Batch(maxBatchSize, maxBatchTime));
      headNodeIdx = headNodeIdx + 1; // increments the pointer
      System.out.println("Sending a batch to the queue to be processed");

    } else {

      //This obtains the lock on that specific batch that prevents other threads from directly writing to th e buffer (Weird race condition)
      synchronized (messageBatch.get(headNodeIdx)) {
        currentBatch = messageBatch.get(headNodeIdx);
        currentBatch.append(key);
      }

    }
  }

  //TODO I need to implement a remove batch @ idx so that the headnode IS NOT PUBLIC
  public synchronized static Batch removeBatch(int dispatchIdx) {
    headNodeIdx -= 1;
    return messageBatch.remove(dispatchIdx);
  }
}
