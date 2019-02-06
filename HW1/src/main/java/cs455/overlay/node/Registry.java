package cs455.overlay.node;

import cs455.overlay.transport.TCPServerThread;
import cs455.overlay.wireformats.*;
import java.io.IOException;

//TODO Create this
public class Registry implements Node {

  private TCPServerThread server;
  private EventFactory eventFactory_instance;

  public Registry(int port_number) throws IOException {
    server = new TCPServerThread(port_number);
    eventFactory_instance = EventFactory.getInstance();
    eventFactory_instance.addListener(this);
  }

  public void onEvent(Event event){
    switch (event.getType()){
      case Protocol.REGISTER_RQ:
        System.out.println(event.getType());

        break;
    }
  }

  private void startup(){
    new Thread(this.server).start();
  }

  public static void main(String[] args){

    Registry registry = null;
    int registry_portnumber;

    if(args.length != 1){
      System.err.println("ERROR: Recieved " + args.length + " args. Expected 1");
      System.err.println("Usage: cs455.overlay.node.Registry [portnumber]");
    }

    registry_portnumber = Integer.parseInt(args[0]);

    try{
      registry = new Registry(registry_portnumber);
      registry.startup();
    }catch (Exception e){
      System.err.println(e.getMessage());
    }

    //TODO this portion will be dedicated to receiving input from the console

  }
}
