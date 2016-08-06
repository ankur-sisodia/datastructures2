import java.util.*;

public class TrieNode {
    char c;
    TrieNode parent;
    HashMap<Character, TrieNode> children;
    boolean isLeaf;
    String name;
    ArrayList<Node> nodes;

    public TrieNode() {
        nodes = new ArrayList<>();
        children = new HashMap<>();
    }

    public TrieNode(char c) {
        this.c = c;
        nodes = new ArrayList<>();
        children = new HashMap<>();
    }
}
