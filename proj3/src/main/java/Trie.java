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
    public void insert(String word) {
        HashMap<Character, TrieNode> children = root.children;

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
            if (i == word.length() - 1)
                t.isLeaf = true;
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

    void wordsFinderTraversal(TrieNode node) {
        // System.out.println(node);

        if (node.isLeaf) {
            // System.out.println("leaf node found");

            TrieNode currWrd = node;

            Stack<String> prefixStack = new Stack<>();

            while (currWrd != prefixRoot) {
                prefixStack.push(Character.toString(currWrd.c));
                currWrd = currWrd.parent;
            }

            String wrd = currPrefix;

            while (!prefixStack.empty()) {
                wrd = wrd + prefixStack.pop();
            }

            // System.out.println(wrd);
            words.add(wrd);

        }

        Set<Character> childKeys = node.children.keySet();
        // System.out.println(node.c); System.out.println(node.isLeaf);System.out.println(kset);
        Iterator<Character> iter = childKeys.iterator();
        ArrayList<Character> prefixList = new ArrayList<>();

        while (iter.hasNext()) {
            Character ch = iter.next();
            prefixList.add(ch);
            // System.out.println(ch);
        }

        for (int i = 0; i < prefixList.size(); i++) {
            wordsFinderTraversal(node.children.get(prefixList.get(i)));
        }

    }

    ArrayList<String> displayFoundWords() {

//        for (String e : words) {
//            System.out.println(e);
//        }

        return words;

    }

//    public static void main(String[] args) {
//        Trie prefixTree;
//
//        prefixTree = new Trie();
//
//        prefixTree.insert("Sushi California");
//        prefixTree.insert("Sushi Sho");
//        prefixTree.insert("Sushi Secrets");
//        prefixTree.insert("Sushi Solano");
//        prefixTree.insert("Sushi Ko");
//
//        if (prefixTree.startsWith("Sushi")) {
//            TrieNode node = prefixTree.searchNode("Sushi");
//            prefixTree.wordsFinderTraversal(node);
//            prefixTree.displayFoundWords();
//        }
//    }
}
