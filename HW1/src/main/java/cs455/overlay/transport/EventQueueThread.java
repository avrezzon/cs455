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
    private static BlockingQueue<Event> eventBuffer;

    public EventQueueThread(){
        //TODO I will need to determine if there is anything else that should be contained within
        eventBuffer = new LinkedBlockingQueue<Event>();
    }

    public synchronized void addEvent(Event event) throws InterruptedException{
        eventBuffer.put(event);
    }

    public void run(){
        System.out.println("THE EVENT QUEUE IS RUNNING");
        while(true){
            //TODO this will need to pull items out of the event queue
            //each event should have a .resolve() to make this easier when dealing with the context
            //eventBuffer.
            Event event;
            try {
                event = eventBuffer.take();
                try {
                    System.out.println("EVENT_QUEUE Received event type: " + event.getType());
                    System.out.println("EVENT_QUEUE Received event bytes[] " + event.getBytes());

                }catch (Exception e){

                }
                event.resolve();
            }catch (InterruptedException ie){
                System.err.println("ERROR OCCURED IN EVENT_QUEUE_THREAD: " + ie.getMessage());
            }
        }
    }
}
