/**
 * Created by asisodia on 8/4/2016.
 */
public class Edge {
    private String from, to;
    private double myDistance;
    public Edge() {
    }

    public Edge(String f, String t) {
        from = f;
        to = t;
        myDistance = Math.sqrt(Math.pow(GraphDB.getNodeList().get(f).getMyLon() +
                GraphDB.getNodeList().get(t).getMyLon(), 2)
                + Math.pow(GraphDB.getNodeList().get(f).getMyLat() +
                GraphDB.getNodeList().get(t).getMyLat(), 2));
    }

    public void setDistance(double dist) {
        myDistance = dist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (Double.compare(edge.myDistance, myDistance) != 0) return false;
        if (from != null ? !from.equals(edge.from) : edge.from != null) return false;
        return to != null ? to.equals(edge.to) : edge.to == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        temp = Double.doubleToLongBits(myDistance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public double getMyDistance() {
        return myDistance;
    }
}
