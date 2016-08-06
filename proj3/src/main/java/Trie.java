/**
 * Created by asisodia on 8/4/2016.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

public class Trie {

    private TrieNode root;
    private ArrayList<String> words;
    private TrieNode prefixRoot;
    private String currPrefix;

    public Trie() {
        root = new TrieNode();
        words = new ArrayList<>();
    }

    // Inserts a word into the trie.
    public void insert(String word, Node node) {
        HashMap<Character, TrieNode> children = root.children;

        String origWord = word;
        word = word.replaceAll("[^a-zA-Z ]", "").toLowerCase();

        TrieNode currParent;

        currParent = root;

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);

            TrieNode t;
            if (children.containsKey(c)) {
                t = children.get(c);
            } else {
                t = new TrieNode(c);
                t.parent = currParent;
                children.put(c, t);
            }

            children = t.children;
            currParent = t;

            // set leaf node
            if (i == word.length() - 1) {
                t.isLeaf = true;
                t.name = origWord;
                t.nodes.add(node);
            }
        }
    }

    // Returns if there is any word in the trie
    // that starts with the given prefix.
    public boolean startsWith(String prefix) {
        if (searchNode(prefix) == null) {
            return false;
        } else {
            return true;
        }
    }

    public TrieNode searchNode(String str) {
        HashMap<Character, TrieNode> children = root.children;
        TrieNode t = null;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (children.containsKey(c)) {
                t = children.get(c);
                children = t.children;
            } else {
                return null;
            }
        }

        prefixRoot = t;
        currPrefix = str;
        words.clear();

        return t;
    }

    void wordsHelper(TrieNode node) {

        if (node.isLeaf) {
            words.add(node.name);
        }

        if (node.children.size() > 0) {
            for (char n : node.children.keySet()) {
                wordsHelper(node.children.get(n));
            }
        }

    }

    void wordsFinderTraversal(String prefix) {
        // System.out.println(node);

        char[] prefixList = prefix.toCharArray();

        TrieNode point = root;

        for (int i = 0; i < prefixList.length; i++) {

            //if (point.children.containsKey(prefixList[i])) {
                point = point.children.get(prefixList[i]);
            //}
        }

        wordsHelper(point);

    }

    ArrayList<Node> nodesFinderTraversal(String prefix) {
        // System.out.println(node);

        char[] prefixList = prefix.toCharArray();

        TrieNode point = root;

        for (int i = 0; i < prefixList.length; i++) {

            if (point.children.containsKey(prefixList[i])) {
                point = point.children.get(prefixList[i]);
            } else {
                System.out.println("No children for: " + prefixList[i]);
            }
        }


        return point.nodes;

    }

    ArrayList<String> displayFoundWords() {

        return words;

    }
}
