package cs455.overlay.wireformats;

public abstract class Message implements Event {
  private int type;
  private byte[] source;
  //TODO there has to be a simpler way of doing this I shouldnt be fighting the event handler
}
