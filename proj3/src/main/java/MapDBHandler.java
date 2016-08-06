import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;


/**
 *  Parses OSM XML files using an XML SAX parser. Used to construct the graph of roads for
 *  pathfinding, under some constraints.
 *  See OSM documentation on
 *  <a href="http://wiki.openstreetmap.org/wiki/Key:highway">the highway tag</a>,
 *  <a href="http://wiki.openstreetmap.org/wiki/Way">the way XML element</a>,
 *  <a href="http://wiki.openstreetmap.org/wiki/Node">the node XML element</a>,
 *  and the java
 *  <a href="https://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html">SAX parser tutorial</a>.
 *  @author Alan Yao
 */
public class MapDBHandler extends DefaultHandler {
    /**
     * Only allow for non-service roads; this prevents going on pedestrian streets as much as
     * possible. Note that in Berkeley, many of the campus roads are tagged as motor vehicle
     * roads, but in practice we walk all over them with such impunity that we forget cars can
     * actually drive on them.
     */
    private static final Set<String> ALLOWED_HIGHWAY_TYPES = new HashSet<>(Arrays.asList
            ("motorway", "trunk", "primary", "secondary", "tertiary", "unclassified",
                    "residential", "living_street", "motorway_link", "trunk_link", "primary_link",
                    "secondary_link", "tertiary_link"));
    private String activeState = "";
    private boolean activeWAY_ALLOWED;
    private ArrayList<String> activeWAY_NODES = new ArrayList<String>();;

    private final GraphDB g;
    public static Trie prefixTree;
    // Added by Jason
    public static HashMap<String, Object> locationMap;


    public MapDBHandler(GraphDB g) {
        this.g = g;
        prefixTree = new Trie();
        // Added by Jason
        locationMap = new HashMap<>();
    }

    /**
     * Called at the beginning of an element. Typically, you will want to handle each element in
     * here, and you may want to track the parent element.
     * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
     *            if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace
     *                  processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if qualified names are
     *              not available. This tells us which element we're looking at.
     * @param attributes The attributes attached to the element. If there are no attributes, it
     *                   shall be an empty Attributes object.
     * @throws SAXException Any SAX exception, possibly wrapping another exception.
     * @see Attributes
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        /* Some example code on how you might begin to parse XML files. */

        if (qName.equals("node")) {
            activeState = "node";
            if (!g.adjHashMap.containsKey(attributes.getValue("id"))) {
                String id = attributes.getValue("id");
                float lon = Float.valueOf(attributes.getValue("lon"));
                float lat = Float.valueOf(attributes.getValue("lat"));

                g.addNodeToGraph(id, lon, lat);
                // check to see in my graph
                // if not add to graph
            }

        } else if (qName.equals("way")) {
            activeState = "way";
            //System.out.println("Beginning a way...");

        } else if (activeState.equals("way") && qName.equals("tag")) {
                String k = attributes.getValue("k");
                String v = attributes.getValue("v");
                if (k.equals("highway")) {
                    if (ALLOWED_HIGHWAY_TYPES.contains(v)) {
                        activeWAY_ALLOWED = true;
                    }
                }
               // System.out.println("Tag with k=" + k + ", v=" + v + ".");
        } else if (activeState.equals("node") && qName.equals("tag") && attributes.getValue("k")
                    .equals("name")) {

            // Added by Jason
            String cleanWord = attributes.getValue("v").replaceAll("[^a-zA-Z ]", "").toLowerCase();
            prefixTree.insert(attributes.getValue("v"));

//            Node locationNode = new Node(attributes.getValue("id"), Float.valueOf(attributes.getValue("lon")), Float.valueOf(attributes.getValue("lat")), attributes.getValue("v"));
//
//            locationMap.put(cleanWord, locationNode);
            // --------------

                //System.out.println("Node with name: " + attributes.getValue("v"));
        } else if (qName.equals("nd")) {
            String ref = attributes.getValue("ref");
            activeWAY_NODES.add(ref);
        }
    }

    /**
     * Receive notification of the end of an element. You may want to take specific terminating
     * actions here, like finalizing vertices or edges found.
     * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
     *            if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace
     *                  processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if qualified names are
     *              not available.
     * @throws SAXException  Any SAX exception, possibly wrapping another exception.
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("way")) {
            if (activeWAY_ALLOWED == true) {
                for(int i = 0; i < activeWAY_NODES.size()-1; i++){
                    g.adjHashMap.get(activeWAY_NODES.get(i)).add(activeWAY_NODES.get(i+1));
                    g.adjHashMap.get(activeWAY_NODES.get(i+1)).add(activeWAY_NODES.get(i));
                }
            }

            // System.out.println("Finishing a way...");
            activeWAY_ALLOWED = false;
            activeWAY_NODES.clear();
        }
    }

}
