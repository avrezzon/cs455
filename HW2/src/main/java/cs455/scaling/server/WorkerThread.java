package cs455.scaling.server;

import cs455.scaling.protocol.Task;

import java.util.concurrent.ConcurrentLinkedQueue;

public class WorkerThread implements Runnable {

    //This is the beginning state of the thread
    public WorkerThread() {

    }


    public void run() {

        ConcurrentLinkedQueue<Task> queue = ThreadPoolManager.getTaskQueueRef();

        try {
            while (true) {
                synchronized (queue) {
                    //Only when the task queue is empty should a thread be put to sleep
                    while (!ThreadPoolManager.availableTasks()) {
                        queue.wait();
                    }
                }
                //If the thread has received a notify or there is a task in the queue should the thread
                //then check to see if it can poll a task, other threads may of gotten to the queue before
                //another thread could get there

                //This getNextTask will poll from the ThreadPoolManager's taskQueue
                Task job = ThreadPoolManager.getNextTask();
                if (job != null) {
                    job.resolve();
                }


            }
        } catch (InterruptedException ie) {
            System.out.println(ie.getMessage());
        }
    }
}
