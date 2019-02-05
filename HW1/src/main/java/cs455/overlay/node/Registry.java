package cs455.overlay.node;

import cs455.overlay.transport.TCPServerThread;
import cs455.overlay.wireformats.*;

public class Registry implements Node {

  private TCPServerThread registry_server;

  public void onEvent(Event event){}
}
