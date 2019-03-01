package cs455.scaling.server;

public class WorkerThread implements Runnable {

  private String clientIP;


  public WorkerThread() {

  }

  //TODO this object is the task queue
    //If the queue is empty after a notifyall then sleep

    //The worker thread will need to obtain a lock on Object X
//    synchronized( lockObject )
//    {
//        while( ! condition )
//        {
//            lockObject.wait();
//        }
//        //take the action here;
//    }

//    //synchronized(lockObject)
//    {
//        //establish_the_condition;
//
//        lockObject.notify();
//
//        //any additional code if needed
//    }

  public void run() {
    //Do work
  }
}
