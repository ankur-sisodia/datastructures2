/**
 * Created by asisodia on 8/4/2016.
 */
public class Node {

    //private variables
    private String myID;
    private double myLon;
    private double myLat;
    private String myName;
    private double myHeuristic;

    public double getMyHeuristic() {
        return myHeuristic;
    }

    // Added by Jason
    public void setMyID(String myID) {
        this.myID = myID;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

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
    // --------------

    public Node() {

    }

    public Node(String id, double lon, double lat) {
        myID = id;
        myLon = lon;
        myLat = lat;
    }

    public double euclidianDistance(Node n2) {
        return (double) Math.sqrt((n2.myLon - this.myLon)
                * (n2.myLon - this.myLon) + (n2.myLat - this.myLat) * (n2.myLat - this.myLat));
    }
}
