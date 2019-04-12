package oswego.csc365.a3;

import java.util.*;

public class BTree {
    public BTNode root;
    public static int branchingFactor;
    public static int order;
    public int height;


    public BTree() {
        branchingFactor = 5;  
        root = null;
        order = 8;
        height = 0;
    }

    public static class BTNode {
        public ArrayList<String> keys;
        public ArrayList<BTNode> children;
        public boolean isLeaf;
        public int capacity;

        public BTNode(boolean leaf, int maxSize) {
            keys = new ArrayList<String>();
            children = new ArrayList<BTNode>();
            isLeaf = leaf;
            capacity = maxSize;
        }

        public void insertWord(String word) {
            keys.add(word);
            Collections.sort(keys);
        }

        public void removeWord(int idx) {
            keys.remove(idx);
        }

        public void insertChild(BTNode node) {
            children.add(node);
        }


        public void removeChild(int idx) {
            children.remove(idx);
        }


        public int midWordIdx() {
            return keys.size() / 2;
        }

        public boolean isFull() {
            if (capacity % 2 == 0)
                return keys.size() >= 2 * branchingFactor - 2;
            else
                return keys.size() >= 2 * branchingFactor - 1;
        }
    }
}