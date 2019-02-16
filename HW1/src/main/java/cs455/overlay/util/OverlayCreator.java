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

  //This will take in the the number of connectiions and the possible connections that the
  public static HashMap<String, ArrayList<String>> createOverlay(int numConnections, ArrayList<String> Connections) throws IllegalArgumentException{

    Map<String, ArrayList<String>> overlay = new HashMap<>();
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


  //Precondition is that input will be of valid conditions k >= 2 k is 4 by default and Nk is even
  private static HashMap<String, ArrayList<String>> performKOddConnect(String[] connections, int k){
    Map<String, ArrayList<String>> overlay = new HashMap<>();
    return null;
  }

  private static HashMap<String, ArrayList<String>> performKEvenConnect(String[] connections, int k){
    Map<String, ArrayList<String>> overlay = new HashMap<>();

    //array[array.length-1] = lastElement; This will be used for negative indexing

    int radius = k/2;
    int len = connections.length;
    String connectingTo;
    String mainNode;

    ArrayList<String> connectToIPs = new ArrayList<String>();

    for(int i = 0; i < connections.length; i++){

      for(int c = i - radius; c <= (i+radius); c++){
        System.out.println("i: " + i + " c: " + c );

        if(c == i){
          //This is the case for if the idx is the origin node
          //In this case we don't want to be sending connections to itself
          continue;
        }else if(c < 0){
          //Connection with end of array
          connectingTo = connections[len + c];
        }else if(c >= connections.length){
          //Connection with the beginning of the array
          connectingTo = connections[c - len];
        }else{
          //Connection with the main node
          connectingTo = connections[c];
        }
        connectToIPs.add(connectingTo);
      }
      //Clone and clear the main array list
      mainNode = connections[i];
      overlay.put(mainNode, (ArrayList<String>) connectToIPs.clone());
      connectToIPs.clear();
    }

    //This feels weird TODO check this out
    return (HashMap<String, ArrayList<String>>) overlay;
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
  }

}
