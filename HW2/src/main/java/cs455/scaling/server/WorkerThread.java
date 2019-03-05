package cs455.scaling.server;

import cs455.scaling.protocol.Task;
import java.nio.channels.SocketChannel;

public class WorkerThread implements Runnable {

  private String clientIP; //This can be used as the key for the information for the client to be accessed in the
  //concurrentHashMap
  private boolean working;  //Determines whether or not the thread is sleeping but i doubt Ill use this
  private SocketChannel taskChannel;

  //This is the beginning state of the thread
  public WorkerThread() {
    clientIP = null;
    working = false;
    taskChannel = null;
  }

  private void doWork(Task job) {
    job.resolve();
  }



  //TODO This should be used when a thread needs to detach from the main chain
  private void attachBatch(Batch msgBatch) {

  }

//  public synchronized void addTask(Task task) {
//    ThreadPoolManager.addTask(task);
//    //TODO need to notify the thread pool of a job
//  }


  //TODO this object is the task queue
    //If the queue is empty after a notifyall then sleep

//    The worker thread will need to obtain a lock on Object X
//    synchronized( lockObject )
//    {
//        while( ! condition )
//        {
//            lockObject.wait();
//        }
//        //take the action here;
//    }


  public synchronized void run() {
    System.out.println("Entered in run for the ");
    try {
      while (true) {
        synchronized (ThreadPoolManager.taskQueue) {
          while (ThreadPoolManager.taskQueue.isEmpty()) {
            ThreadPoolManager.taskQueue.wait();
          }
          if (!ThreadPoolManager.taskQueue.isEmpty()) {
            doWork(ThreadPoolManager.taskQueue.poll());
          }
        }
      }
    } catch (InterruptedException ie) {
      System.out.println(ie.getMessage());
    }
  }
}
