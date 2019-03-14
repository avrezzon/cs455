package cs455.scaling.server;

//In relation to the producer consumer problem lets make the worker threads the consumers of the
//task queue and the Thread pool manager the producer in which tasks must go through him in order
//to notify the worker threads

//This thread needs to add the new tasks to the queue instead of the main thread for throughput purposes

import cs455.scaling.protocol.Task;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadPoolManagerThread implements Runnable {

  //This will be where all the pending tasks are waiting to be added to the taskQueue
  private ThreadPoolManager threadPoolManager;

  public ThreadPoolManagerThread(int threadPoolSize, int batchSize, int batchTime) {
    threadPoolManager = new ThreadPoolManager(threadPoolSize, batchSize, batchTime);
  }

  public synchronized void addPendingTask(SelectionKey key) {

    if (key.isValid()) {

        if ((key.isAcceptable() || key.isReadable()) && key.attachment() == null) {
              key.attach(new Object());
      }
        threadPoolManager.addTask(new Task(key));
    }
  }

  //This run function will be the thread to notify the worker threads about the
  public void run() {
    threadPoolManager.bootup();
    while (true) {
        if (!threadPoolManager.isTaskQueueEmpty()) {
            threadPoolManager.wakeupWorkers();
      }
    }
  }

}
