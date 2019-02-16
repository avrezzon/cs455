package cs455.overlay.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OverlayCreator {

  //This will receive an array of strings of Messaging nodes
  //[a,b,c,d,e,f,g,h,i,j,k,l] length of 12

  //INPUT is an array of ip
  //Output would get a hashmap of the things below

  //[A : [c,d]]
  //[B : [e,h]]
  //[C : [f,b]]

  //TODO I know that Im not going to have enough time to implement this
  //Idea: within the generation of the overlay, if i keep track of what node i visit
  //I can eleminate duplicate messages.
  //if A connects to C, should i have to specify in C's nodes if A is a connection rq since
  //these sockets are bi directional


  //This will take in the the number of connectiions and the possible connections that the
  public static HashMap<String, ArrayList<String>> createOverlay(int numConnections, ArrayList<String> Connections) throws IllegalArgumentException{

    Map<String, ArrayList<String>> overlay = new HashMap<>();
    //This was translated into an array to help with easier accesses
    String[] connections = Connections.toArray(new String[0]);

    int N = Connections.size();
    int k = numConnections;

    if((k < 2) || ((N*k) %2 ==1)){
      throw new IllegalArgumentException("N k violation, Nk must be even and k >= 2");
    }

    //In this problem there is only two situations to evaluate
    //N is even and k is odd, and N -> even k->even or N->odd k->even
    if(k%2==0){
      //radius from node
      return performKEvenConnect(connections, k);
    }else{
      //radius from focal point and node
      return performKOddConnect(connections, k);
    }
  }

  //FIXME This is getting the accros node correctly but it does not grab the node adjacent in the radius
  //Precondition is that input will be of valid conditions k >= 2 k is 4 by default and Nk is even
  private static HashMap<String, ArrayList<String>> performKOddConnect(String[] connections, int k){
    Map<String, ArrayList<String>> overlay = new HashMap<>();
    int len = connections.length;
    int half = len /  2;
    int radius = k / 2;
    String connectingTo;
    String mainNode;

    ArrayList<String> connectToIPs = new ArrayList<String>();

    for(int mainNodeIdx = 0; mainNodeIdx < len; mainNodeIdx++){
      int translatedNode = mainNodeIdx + half;
      for(int i = translatedNode - radius; i < translatedNode + radius; i++){
        connectingTo = connections[translateNode(mainNodeIdx,i,len)];
        connectToIPs.add(connectingTo);
      }
      mainNode = connections[mainNodeIdx];
      overlay.put(mainNode, (ArrayList<String>) connectToIPs.clone());
      connectToIPs.clear();
    }

    return (HashMap<String, ArrayList<String>>) overlay;
  }

  private static HashMap<String, ArrayList<String>> performKEvenConnect(String[] connections, int k){
    Map<String, ArrayList<String>> overlay = new HashMap<>();

    int radius = k/2;
    int len = connections.length;
    String connectingTo;
    String mainNode;

    ArrayList<String> connectToIPs = new ArrayList<String>();

    for(int i = 0; i < connections.length; i++){
      for(int c = i - radius; c <= (i+radius); c++){
        if(c == i){
          //This is the case for if the idx is the origin node
          //In this case we don't want to be sending connections to itself
          continue;
        }else{
          connectingTo = connections[translateNode(c,i,len)];
          connectToIPs.add(connectingTo);
        }
      }

      mainNode = connections[i];
      overlay.put(mainNode, (ArrayList<String>) connectToIPs.clone());
      connectToIPs.clear();
    }

    return (HashMap<String, ArrayList<String>>) overlay;
  }

  //This is used to help map the negative indexes
  private static int translateNode(int c, int i, int len){
    int idx;
    if(c < 0){
      //Connection with end of array
      return len + c;
    }else if(c >= len){
      //Connection with the beginning of the array
      idx = c - len;
    }else{
      //Connection with the main node
      idx = c;
    }
    return idx;
  }

  public static void printMap(Map mp) {
    Iterator it = mp.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry pair = (Map.Entry)it.next();
      System.out.println(pair.getKey() + " = " + pair.getValue());
      it.remove(); // avoids a ConcurrentModificationException
    }
  }

  public static void main(String[] args){
    //Note that this is simply a testing environment to verify how the overlay is being set up

    //Even, Even Case
    ArrayList<String> temp = new ArrayList<String>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j"));
    int k = 4;
    Map<String, ArrayList<String>> overlay = createOverlay(k, temp);
    System.out.println("The Overlay: ");
    printMap(overlay);

    System.out.println("\n\n\nThe Odd even case");
    temp = new ArrayList<String>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"));
    overlay = createOverlay(k, temp);
    System.out.println("the Overlay");
    printMap(overlay);

    System.out.println("\n\n\nThe Even odd case");
    temp = new ArrayList<String>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j"));
    overlay = createOverlay(5, temp);
    System.out.println("the Overlay");
    printMap(overlay);

  }

}
