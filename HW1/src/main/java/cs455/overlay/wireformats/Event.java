package cs455.overlay.wireformats;

import java.io.IOException;

public interface Event {
    int getType();
    byte[] getBytes() throws IOException;
    void resolve();
    //TODO Consider adding a .resolve so that it can do the things that it needs to do

}
