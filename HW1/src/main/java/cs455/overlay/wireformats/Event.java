package cs455.overlay.wireformats;

import java.io.IOException;

public interface Event {
    int getType(); //TODO
    byte[] getBytes() throws IOException;
}
