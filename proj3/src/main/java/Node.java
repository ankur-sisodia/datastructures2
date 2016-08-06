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

    public double EuclidianDistance(Node n2) {
        return (double) hypot(n2.myLon - this.myLon, n2.myLat - this.myLat);
    }

    public static double hypot(double x, double y) {

        if (Double.isInfinite(x) || Double.isInfinite(y)) return Double.POSITIVE_INFINITY;
        if (Double.isNaN(x) || Double.isNaN(y)) return Double.NaN;

        x = Math.abs(x);
        y = Math.abs(y);

        if (x < y) {
            double d = x;
            x = y;
            y = d;
        }

        int xi = Math.getExponent(x);
        int yi = Math.getExponent(y);

        if (xi > yi + 27) return x;

        int bias = 0;
        if (xi > 510 || xi < -511) {
            bias = xi;
            x = Math.scalb(x, -bias);
            y = Math.scalb(y, -bias);
        }


        // translated from "Freely Distributable Math Library" e_hypot.c to minimize rounding errors
        double z = 0;
        if (x > 2*y) {
            double x1 = Double.longBitsToDouble(Double.doubleToLongBits(x) & 0xffffffff00000000L);
            double x2 = x - x1;
            z = Math.sqrt(x1*x1 + (y*y + x2*(x+x1)));
        } else {
            double t = 2 * x;
            double t1 = Double.longBitsToDouble(Double.doubleToLongBits(t) & 0xffffffff00000000L);
            double t2 = t - t1;
            double y1 = Double.longBitsToDouble(Double.doubleToLongBits(y) & 0xffffffff00000000L);
            double y2 = y - y1;
            double x_y = x - y;
            z = Math.sqrt(t1*y1 + (x_y*x_y + (t1*y2 + t2*y))); // Note: 2*x*y <= x*x + y*y
        }

        if (bias == 0) {
            return z;
        } else {
            return Math.scalb(z, bias);
        }
    }
}
