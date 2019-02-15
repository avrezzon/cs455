package cs455.overlay.wireformats;

public class EventInstance {
    public Event event;
    public String origin;

    //This is intentionally used so the the EventQueue has an idea of who received the message
    public EventInstance(Event e, String origin){
        this.event = e;
        this.origin = origin;
    }
}
