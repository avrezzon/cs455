package cs455.overlay.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//This should be a class object to make it easier for managing the types of overlays
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

  //With this, consider this later when Registry is acutally creating a copy

  private HashMap<String, ArrayList<String>> fullOverlay;
  private HashMap<String, ArrayList<String>> rmvDuplicatesOverlay; //used for communictions
  private String[] ipConnections;
  private int k; //number of connections
  private int len;

  public OverlayCreator(ArrayList<String> connections, int k) throws IllegalArgumentException{

    this.ipConnections = connections.toArray(new String[0]);

    this.k = k;

    this.len = connections.size();

    this.fullOverlay = createOverlay();

    //TODO see comment made below in regards to the function
    //this.rmvDuplicatesOverlay = RmvDuplicatesOverlay();
  }

  public HashMap<String, ArrayList<String>> getFullOverlay(){return (HashMap<String, ArrayList<String>>) this.fullOverlay.clone();}

  //This will take in the the number of connectiions and the possible connections that the
  private HashMap<String, ArrayList<String>> createOverlay() throws IllegalArgumentException{

    if((this.k < 2) || ((this.len*this.k) %2 ==1)){
      throw new IllegalArgumentException("N k violation, Nk must be even and k >= 2");
    }

    //In this problem there is only two situations to evaluate
    //N is even and k is odd, and N -> even k->even or N->odd k->even

    if(this.k%2==0){
      //radius from node
      return performKEvenConnect();
    }else{
      //radius from focal point and node
      return performKOddConnect();
    }

  }

  //Precondition is that input will be of valid conditions k >= 2 k is 4 by default and Nk is even
  private HashMap<String, ArrayList<String>> performKOddConnect(){
    HashMap<String, ArrayList<String>> overlay = new HashMap<>();
    int half = len /  2;
    int radius = k / 2;
    int translatedNode;
    String connectingTo;
    String mainNode;

    ArrayList<String> connectToIPs = new ArrayList<String>();

    for(int mainNodeIdx = 0; mainNodeIdx < len; mainNodeIdx++){

      translatedNode = translateNode(mainNodeIdx + half);

      for(int i = translatedNode - radius; i <= translatedNode + radius; i++){
        connectingTo = this.ipConnections[translateNode(i)];
        connectToIPs.add(connectingTo);
      }
      mainNode = this.ipConnections[mainNodeIdx];
      overlay.put(mainNode, (ArrayList<String>) connectToIPs.clone());
      connectToIPs.clear();
    }

    return overlay;
  }

  private HashMap<String, ArrayList<String>> performKEvenConnect(){
    HashMap<String, ArrayList<String>> overlay = new HashMap<>();

    int radius = k/2;
    String connectingTo;
    String mainNode;

    ArrayList<String> connectToIPs = new ArrayList<String>();

    for(int i = 0; i < len; i++){
      for(int c = i - radius; c <= (i+radius); c++){
        if(c == i){
          //This is the case for if the idx is the origin node
          //In this case we don't want to be sending connections to itself
          continue;
        }else{
          connectingTo = this.ipConnections[translateNode(c)];
          connectToIPs.add(connectingTo);
        }
      }

      mainNode = this.ipConnections[i];
      overlay.put(mainNode, (ArrayList<String>) connectToIPs.clone());
      connectToIPs.clear();
    }

    return overlay;
  }

  //This is used to help map the negative indexes
  private int translateNode(int c){
    int idx;
    if(c < 0){
      //Connection with end of array
      return this.len + c;
    }else if(c >= this.len){
      //Connection with the beginning of the array
      idx = c - this.len;
    }else{
      //Connection with the main node
      idx = c;
    }
    return idx;
  }

  //TODO later if i feel like that this is important to implement then we can approach
  private HashMap<String, ArrayList<String>> RmvDuplicatesOverlay(){

    HashMap<String, ArrayList<String>> rmvDup = new HashMap<>();
    ArrayList<String> tempConnetions;


    for(String key : this.fullOverlay.keySet()){

      ArrayList<String> tempConnections = this.fullOverlay.get(key);

      //FIXME this is where i am getting a null error I think that this is when i remove all of one nodes connection
      //This will be where we iterate over all of the values from the subsequent arrays
      for(String node : tempConnections){
          //go to that node as the key of the dict and remove the orig key
        //System.out.println("Removing element " + key + " from " + fullOverlay.get(node));
        this.fullOverlay.get(node).remove(key);
      }

    }

    return rmvDup;
  }

  public void printMap(Map mp) {
    Iterator it = mp.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry pair = (Map.Entry)it.next();
      System.out.println(pair.getKey() + " = " + pair.getValue());
      it.remove(); // avoids a ConcurrentModificationException
    }
  }

  public static void main(String[] args){

    ArrayList<String> nodes = new ArrayList<>();

    int N = Integer.parseInt(args[0]);
    int k = Integer.parseInt(args[1]);

    for(int i = 0; i < N; i++){
      nodes.add("node"+i);
    }

    OverlayCreator temp = new OverlayCreator(nodes, k);

  }

}


