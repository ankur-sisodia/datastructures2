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
    public static HashMap<String, ArrayList<Edge>> adjHashMap;

    public void addNodeToGraph(String id, double lon, double lat) {
        adjHashMap.put(id, new ArrayList<Edge>());
        nodeList.put(id, new Node(id, lon, lat));
    }
    /**
     * Example constructor shows how to create and start an XML parser.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            nodeList = new HashMap<String, Node>();
            adjHashMap = new HashMap<String, ArrayList<Edge>>();
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
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        HashMap<String, Node> tempHashMap_NodeList = new HashMap<String, Node>();
        HashMap<String, ArrayList<Edge>> tempHashMap_AdjList = new HashMap<String, ArrayList<Edge>>();

        for(String k: adjHashMap.keySet()) {
            if(adjHashMap.get(k).size() != 0) {
                tempHashMap_AdjList.put(k,adjHashMap.get(k));
                tempHashMap_NodeList.put(k,nodeList.get(k));
            }
        }
        adjHashMap = tempHashMap_AdjList;
        nodeList = tempHashMap_NodeList;

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