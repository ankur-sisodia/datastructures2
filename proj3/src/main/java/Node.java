import java.util.HashMap;

/**
 * Created by asisodia on 8/4/2016.
 */
public class Node {
    //private variables
    private String myID;
    private float myLon, myLat;
    public Node() {
    }

    public Node(String id, float lon, float lat) {
            myID = id;
            myLon = lon;
            myLat = lat;
        }

        public double findDistance(Node n2) {
            return Math.hypot(n2.myLon-this.myLon, n2.myLat-this.myLat);
        }
    }
