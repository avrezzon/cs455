package cs455.overlay.wireformats;

import java.io.IOException;

public interface IEvent {
    int getType(); //TODO
    byte[] getBytes() throws IOException;
}
