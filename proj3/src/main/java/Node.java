import java.util.HashMap;

/**
 * Created by asisodia on 8/4/2016.
 */
public class Node {
    //private variables
    private String myID, myName;
    private float myLon, myLat;
    public Node() {

    }

    public Node(String id, float lon, float lat) {
            myID = id;
            myLon = lon;
            myLat = lat;
    }

    public Node(String id, float lon, float lat, String name) {
        myID = id;
        myLon = lon;
        myLat = lat;
        myName = name;
    }

        public double findDistance(Node n2) {
            return Math.hypot(n2.myLon-this.myLon, n2.myLat-this.myLat);
        }
    }
