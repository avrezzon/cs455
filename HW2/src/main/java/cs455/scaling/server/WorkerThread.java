package cs455.scaling.server;

import cs455.scaling.protocol.Task;

public class WorkerThread implements Runnable {

  //This is the beginning state of the thread
  public WorkerThread() {

  }

  private void doWork(Task job) {
    job.resolve();
  }

  public synchronized void run() {
    try {
      while (true) {
        synchronized (ThreadPoolManager.taskQueue) {
          if (ThreadPoolManager.taskQueue.isEmpty()) {
            ThreadPoolManager.taskQueue.wait();
          }
          if (!ThreadPoolManager.taskQueue.isEmpty()) {
            //System.out.println("\t" + Thread.currentThread().getName()
            //    + " Has received an event from the task queue");
            try {
              doWork(ThreadPoolManager.taskQueue.poll());
            } catch (NullPointerException ne) {
              //System.out.println(
              //    Thread.currentThread().getName() + " received a null ptr exception" + ne
              //        .getMessage() + ne.getStackTrace());
            }
          }
          //System.out.println(Thread.currentThread().getName() + " is going back to sleep");
        }
      }
    } catch (InterruptedException ie) {
      System.out.println(ie.getMessage());
    }
  }
}
