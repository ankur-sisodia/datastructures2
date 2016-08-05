import java.util.HashMap;

public class TrieNode {
    char c;
    TrieNode parent;
    HashMap<Character, TrieNode> children = new HashMap<Character, TrieNode>();
    boolean isLeaf;

    public TrieNode() {

    }

    public TrieNode(char c) {
        this.c = c;
    }
}
