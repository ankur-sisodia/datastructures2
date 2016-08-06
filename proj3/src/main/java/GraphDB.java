import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.events.Attribute;
import java.util.ArrayList;
import java.util.*;


/**
 * Wraps the parsing functionality of the MapDBHandler as an example.
 * You may choose to add to the functionality of this class if you wish.
 * @author Alan Yao
 */
public class GraphDB {
    public static HashMap<String, Node> nodeList;
    public static HashMap<String, ArrayList<Node>> adjHashMap;

    public void addNodeToGraph(String id, double lon, double lat) {
        adjHashMap.put(id, new ArrayList<Node>());
        nodeList.put(id, new Node(id, lon, lat));
    }

    /**
     * Example constructor shows how to create and start an XML parser.
     *
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            nodeList = new HashMap<String, Node>();
            adjHashMap = new HashMap<String, ArrayList<Node>>();
            File inputFile = new File(dbPath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            MapDBHandler maphandler = new MapDBHandler(this);
            saxParser.parse(inputFile, maphandler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        System.out.println("size of hash map prior to clean: " + nodeList.size() + ", adjlist:" + adjHashMap.size());
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     * Remove nodes with no connections from the graph.
     * While this does not guarantee that any two nodes in the remaining graph are connected,
     * we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        HashMap<String, Node> tempHashMap_NodeList = new HashMap<String, Node>();
        HashMap<String, ArrayList<Node>> tempHashMap_AdjList = new HashMap<String, ArrayList<Node>>();

        for (String k : adjHashMap.keySet()) {
            if (adjHashMap.get(k).size() != 0) {
                tempHashMap_AdjList.put(k, adjHashMap.get(k));
                tempHashMap_NodeList.put(k, nodeList.get(k));
            }
        }
        adjHashMap = tempHashMap_AdjList;
        nodeList = tempHashMap_NodeList;
    }

    public static ArrayList<Long> shortestPath(String startVertex, String endVertex) {
        //your code here...
//      Maintain a mapping, from vertices to their distance from the start vertex
        HashMap<String, Double> pathDistances = new HashMap<String, Double>();
        HashSet<String> visited = new HashSet<String>();
        HashMap<String, String> fullPath = new HashMap<String, String>();
        ArrayList<Long> path = new ArrayList<Long>();


//      Add the start vertex to the fringe (a queue ordered on the distance mapping) with distance zero.
        PriorityQueue<String> fringe = new PriorityQueue<String>(10, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if(pathDistances.get(o1) + nodeList.get(o1).EuclidianDistance(nodeList.get(endVertex))
                        < pathDistances.get(o2) + nodeList.get(o2).EuclidianDistance(nodeList.get(endVertex))) {
                    return -1;
                }
                if(pathDistances.get(o1) + nodeList.get(o1).EuclidianDistance(nodeList.get(endVertex))
                        > pathDistances.get(o2) + nodeList.get(o2).EuclidianDistance(nodeList.get(endVertex))) {
                    return 1;
                }
                return 0;
            }
        }
        );
        //HashSet<Edge> visited = new

        pathDistances.put(startVertex, 0.0);
        fringe.add(startVertex);
//      All other nodes can be left out of the fringe. If a node is not in the fringe, assume it has distance infinity.
//        For each vertex, keep track of which other node is the predecessor for the node along the shortest path found.

        while (!fringe.isEmpty()) {
            String v = fringe.poll();
            if (v == endVertex) {
                break;
            }
            if (visited.contains(v)) {
                continue;
            }
            visited.add(v);
           // neighbors = (ArrayList<Integer>) neighbors(v);
            //adjHashMap.get(v);

            //for (String w : adjHashMap.get(v)) {
            for (Node q : adjHashMap.get(v)) {
                String w = q.getMyID();
                if (visited.contains(w)) {
                    continue;
                }
                if (!fringe.contains(w)) {
                    double distance = pathDistances.get(v);
                    distance += (double) nodeList.get(v).EuclidianDistance(nodeList.get(w));
                    pathDistances.put(w, distance);
                    fringe.add(w);
                    fullPath.put(w,v);
                } else {
                    double pred = pathDistances.get(w);
                    double distance = pathDistances.get(v);
                    distance += (double) nodeList.get(v).EuclidianDistance(nodeList.get(w));
                    if (distance < pred) {
                        pathDistances.put(w, distance);
                        fullPath.put(w,v);
                    } else {
                        continue;
                    }
                }
            }
        }
        String curr = endVertex;
        while (curr != startVertex) {
            path.add(Long.parseLong(curr));
            curr = fullPath.get(curr);
        }
        path.add(Long.parseLong(startVertex));
        Collections.reverse(path);
        return path;
    }
//        While-Loop
//        Loop until the fringe is empty.
//
//        Remove and hold onto the vertex v in the fringe for which the distance from start is minimal. (One can prove that for this vertex, the shortest path from the start vertex to it is known for sure.)
//        If v is the destination vertex (optional), terminate now. Otherwise, mark it as visited; likewise, any visited vertices should not be visited again.
//                Then, for each neighbor w of v, do the following:
//
//        As an optimization, if w has been visited already, skip it (as we have no way of finding a shorter path anyways
//                If w is not in the fringe (or another way to think of it - it's distance is infinity or undefined in our distance mapping), add it (with the appropriate distance and previous pointers).
//        Otherwise, w's distance might need updating if the path through v to w is a shorter path than what we've seen so far.
// In that case, replace the distance for w's fringe entry with the distance from start to v plus the weight of the edge (v, w), and replace its predecessor with v.
// If you are using a java.util.PriorityQueue, you will need to call add or offer again so that the priority updates correctly - do not call remove as this takes linear time.


    public static String[] closestNode(double start_lon, double start_lat, double end_lon, double end_lat) {
        //your code here...
        String[] closeNode = new String[2];
        Node startExact = new Node("", start_lon, start_lat);
        Node endExact = new Node("", end_lon, end_lat);

        closeNode[0] = nodeList.keySet().iterator().next();
        closeNode[1] = nodeList.keySet().iterator().next();
        for(String s: nodeList.keySet()) {
            if(nodeList.get(s).EuclidianDistance(startExact) < nodeList.get(closeNode[0]).EuclidianDistance(startExact)) {
                closeNode[0] = s;
            }
            if(nodeList.get(s).EuclidianDistance(endExact) < nodeList.get(closeNode[1]).EuclidianDistance(endExact)) {
                closeNode[1] = s;
            }
        }
        return closeNode;
    }
}

// add - adding a neighbor
//    public void addNeighbor(String id, Attribute attributes) {
//        if (adjHashMap.get(id) == null) {
//            this.addNodeToGraph(id);
//        }
//        Node n = new Node();
//        adjHashMap.get(id).add(n);
//    }