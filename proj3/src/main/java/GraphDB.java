import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.*;


/**
 * Wraps the parsing functionality of the MapDBHandler as an example.
 * You may choose to add to the functionality of this class if you wish.
 * @author Alan Yao
 */
public class GraphDB {
    private HashMap<String, ArrayList<String>> adjHashMap;
    private HashMap<String, Node> nodeList;

    public void addNodeToGraph(String id, double lon, double lat) {
        adjHashMap.put(id, new ArrayList<>());
        nodeList.put(id, new Node(id, lon, lat));
    }
    /**
     * Example constructor shows how to create and start an XML parser.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            adjHashMap = new HashMap<>();
            nodeList = new HashMap<>();
            File inputFile = new File(dbPath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            MapDBHandler maphandler = new MapDBHandler(this);
            saxParser.parse(inputFile, maphandler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
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
        HashMap<String, ArrayList<String>> tempHashMapAdjList = new HashMap<>();
        HashMap<String, Node> tempHashMapNodeList = new HashMap<>();
        for (String k: adjHashMap.keySet()) {
            if (adjHashMap.get(k).size() != 0) {
                tempHashMapAdjList.put(k, adjHashMap.get(k));
                tempHashMapNodeList.put(k, nodeList.get(k));
            }
        }
        adjHashMap = tempHashMapAdjList;
        nodeList = tempHashMapNodeList;

    }
}
