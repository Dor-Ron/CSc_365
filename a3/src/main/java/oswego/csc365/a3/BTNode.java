package oswego.csc365.a3;

import java.util.*;

public class BTNode {
    public ArrayList<String> words;
    public ArrayList<BTNode> children;
    public boolean isLeaf;
    public int capacity;

    public BTNode(boolean leaf, int maxSize) {
        words = new ArrayList<String>();
        children = new ArrayList<BTNode>();
        capacity = maxSize;
    }

    public void insertWord(String word) {
        words.add(word);
        Collections.sort(words);
    }

    public void removeWord(int idx) {
        words.remove(idx);
    }

    public void insertChild(BTNode node) {
        children.add(node);
    }


    public void removeChild(int idx) {
        children.remove(idx);
    }


    public int midWordIdx() {
        return words.size() / 2;
    }

    public boolean isFull() {
        return words.size() >= capacity;
    }

}