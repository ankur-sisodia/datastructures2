import java.util.HashMap;

/**
 * Created by asisodia on 8/4/2016.
 */
public class Node {
    //private variables
    private String myID;
    private double myLon, myLat;
    private double myHeuristic;

    public String getMyID() {
        return myID;
    }

    public double getMyLon() {
        return myLon;
    }

    public double getMyLat() {
        return myLat;
    }

    public double getMyHeuristic() {
        return myHeuristic;
    }

    public Node() {
    }

    public Node(String id, double lon, double lat) {
            myID = id;
            myLon = lon;
            myLat = lat;
        }

        public double EuclidianDistance(Node n2) {
            return (double) Math.hypot(n2.myLon-this.myLon, n2.myLat-this.myLat);
        }
    }
