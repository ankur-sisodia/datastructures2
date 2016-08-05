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

        myDistance = Math.sqrt(Math.pow(GraphDB.nodeList.get(f).myLon + GraphDB.nodeList.get(t).myLon, 2)
                + Math.pow(GraphDB.nodeList.get(f).myLat + GraphDB.nodeList.get(t).myLat, 2));
       // System.out.println("mydistance: " + myDistance);
                //(GraphDB.nodeList.get(f).myLat - GraphDB.nodeList.get(t).myLat) * (GraphDB.nodeList.get(f).myLat - GraphDB.nodeList.get(t).myLat));

//        myDistance = (long) Math.hypot(GraphDB.nodeList.get(f).myLon - GraphDB.nodeList.get(t).myLon,
//                GraphDB.nodeList.get(f).myLat - GraphDB.nodeList.get(t).myLat);
        //myDistance = f.EuclidianDistance(t);
    }
}
