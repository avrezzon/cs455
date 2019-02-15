package cs455.overlay.transport;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import cs455.overlay.wireformats.*;
import java.util.concurrent.LinkedBlockingQueue;

public class EventQueueThread implements Runnable {

    //This is blocking for the case that the queue might go faster in performing events
    //rather then elements can be placed into the queue
    //http://tutorials.jenkov.com/java-util-concurrent/blockingqueue.html
    //The link will contain enough info above to explain more of the blocking queue
    private static BlockingQueue<EventInstance> eventBuffer;
    private static volatile boolean alive;

    public EventQueueThread(){

        eventBuffer = new LinkedBlockingQueue<>();
        alive = true;
    }

    public synchronized void addEvent(EventInstance event) throws InterruptedException{
        eventBuffer.put(event);
    }

    public static void killThread(){ alive = false;}

    private static boolean getAlive(){return alive;}

    public void run(){
        while(getAlive()){
            //each event should have a .resolve() to make this easier when dealing with the context
            //eventBuffer.
            EventInstance eventInstance;
            try {
                eventInstance = eventBuffer.take();
                Event event = eventInstance.event;
                String origin = eventInstance.origin;
                event.resolve(origin);
            }catch (InterruptedException ie){
                System.err.println("ERROR OCCURED IN EVENT_QUEUE_THREAD: " + ie.getMessage());
            }
        }
        System.out.println("EVENT QUEUE THREAD WINDING DOWN");
    }
}
