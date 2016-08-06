/**
 * Created by asisodia on 8/4/2016.
 */
public class Node {
    public void setMyID(String myID) {
        this.myID = myID;
    }

    //private variables
    private String myID;

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    private String myName;

    public void setMyLon(double myLon) {
        this.myLon = myLon;
    }

    public void setMyLat(double myLat) {
        this.myLat = myLat;
    }

    public String getMyID() {
        return myID;
    }

    public double getMyLon() {
        return myLon;
    }

    public double getMyLat() {
        return myLat;
    }

    private double myLon;
    private double myLat;
    public Node() {

    }

    public Node(String id, double lon, double lat) {
            myID = id;
            myLon = lon;
            myLat = lat;
    }

    public Node(String id, double lon, double lat, String name) {
        myID = id;
        myLon = lon;
        myLat = lat;
        myName = name;
    }

        public double findDistance(Node n2) {
            return Math.hypot(n2.myLon-this.myLon, n2.myLat-this.myLat);
        }
    }
