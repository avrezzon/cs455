package cs455.overlay.wireformats;

import java.io.IOException;

public interface IEvent {
    void getType(); //TODO
    byte[] getBytes() throws IOException;
}
