package cs455.scaling.server;

import cs455.scaling.protocol.Task;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

//FIXME revise why some of the variables have global scope
public class ThreadPoolManager {

  private ArrayList<WorkerThread> threadPool;
  private final static ConcurrentLinkedQueue<Task> taskQueue = new ConcurrentLinkedQueue<>(); //The reason why this is public is due to the scope that is needed by the worker threads.  TODO I can revise these calls to helper functions
  private final static LinkedList<Batch> messageBatch = new LinkedList<>();

  //NOTE that this will not always be 0, this way we can append to other links
  //This was marked as volatile so that every time it is fetched the current thread knows exactly what
  //node to write to
  private static volatile int headNodeIdx;

  private static int maxBatchSize;
  private static int maxBatchTime;

  public ThreadPoolManager(int threadPoolSize, int maxBatchSize, int maxBatchTime) {

    //This will create the container that stores all of the worker threads
    this.threadPool = new ArrayList<>();
    for (int i = 0; i < threadPoolSize; i++) {
      threadPool.add(new WorkerThread());
    }

    //Defines the command line arguments that were provided
    this.maxBatchSize = maxBatchSize;
    this.maxBatchTime = maxBatchTime;

    //The thread pool manager will hold a batch of the keys for the clients that have incoming messages
    //The batch has context of how long it should be and when it should be launched back to the clients
    //FIXME should the batch understand the time because once i do the thing below it will be ready to take off

    messageBatch.add(new Batch(maxBatchSize, maxBatchTime));

    //Upon initialization the head node should be the first node
    headNodeIdx = 0;
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
  public synchronized void addTask(Task task) {
    synchronized (taskQueue) {
        taskQueue.add(task);
      taskQueue.notifyAll();
      }
  }

  //TODO The bottom two methods may need to be re-written depending on the new implementation of the manager


  //With this method we will check the current head node to determine if the batch needs to be sent out back to
  //The clients.  NOTE upon a successful detatchment, the method will insert a new link as the head node prior to the detachment
  public static void addMsgKey(SelectionKey key) {
//    Batch currentBatch = messageBatch.get(headNodeIdx);
//    synchronized (currentBatch) {
//      //FIXME there could be a potential concurrency issue dealing with this
////      if(currentBatch.dispatch()){
////        //This is the case where the batch should be removed from the head of the linked list
////        //from the detachment, a new task should be added to the taskQueue with the dispatch batch object
////        //that the worker thread will perform work on
////        taskQueue.add(new Task(headNodeIdx));
////        messageBatch.add(new Batch(maxBatchSize, maxBatchTime));
////        headNodeIdx = headNodeIdx + 1; // increments the pointer
////      }
//      currentBatch.append(key);
//      System.out.println("The current batch size is: " + currentBatch.length);
//    }
//    Server.stats.receivedMsg();
  }

  //FIXME I am not sure that this style of implementation will do me the best with processing the data
  public synchronized static Batch removeBatch(int dispatchIdx) {
//    headNodeIdx -= 1;
//    return messageBatch.remove(dispatchIdx);
    return null;
  }

  public static ConcurrentLinkedQueue<Task> getTaskQueueRef() {
    return taskQueue;
  }
}
