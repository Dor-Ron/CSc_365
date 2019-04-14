package oswego.csc365.a3;

/* 
Author: Dor Rondel
Course: CSc 365
Instructor: Prof. Ioaona Coman
Program: String BTree

Big thanks to https://www.geeksforgeeks.org/b-tree-set-1-introduction-2/ 
For providing C++ code for Integer BTrees, that was translated to Java and modified
to make up for programming language differences and program specifications.
*/

public class BTree {
    public BTNode root;
    public static int branchingFactor;
    public int height;


    // constructor
    public BTree(int bf) {
        branchingFactor = bf;  
        root = null;
        height = 0;
    }

    // btree wrapper for btnode traverse
    public void traverse() {
        if (root != null) 
            root.traverse();
    }

    // btree wrapper for btnode search
    public BTNode search(String word) {
        return root == null ? null : root.search(word);
    }

    // inserts word into BTree
    public void insert(String word) {

        // first entry
        if (root == null) {
            root = new BTNode(true);
            root.words[0] = word;
            root.n++;
            height++;
        } else { // tree not empty
            if (root.n == 2 * branchingFactor - 1) { // root full? grow tree
                BTNode node2 = new BTNode(false);

                // add old root as child to new root
                node2.children[0] = root;

                // split old root and populate new root
                node2.splitChild(0, root);

                // Decide which child of new root gets final key
                int i = 0;
                if (node2.words[0].compareTo(word) < 0) 
                    i++;
                node2.children[i].insertNonFull(word);
                root = node2;
                height++;
            } else //root not full? proceed to regular insertion
                root.insertNonFull(word);
        }
    }

    // Removes word from BTree
    public void remove(String word) {
        if (root == null) {
            System.out.println("The tree is empty");
            return ;
        }

        // call btnode.remove on word
        root.remove(word);

        if (root.n == 0) {  // root has no keys
            BTNode tmp = root;
            if (root.isLeaf)  // if leaf, make it null
                root = null;
            else
                root = root.children[0]; // make first child root
        }
    }

    public static class BTNode {
        public String[] words;
        public BTNode[] children;
        public boolean isLeaf;
        public int n;

        // node constructor
        public BTNode(boolean leaf) {
            words = new String[2 * branchingFactor - 1];
            children = new BTNode[2 * branchingFactor];
            isLeaf = leaf;
            n = 0;
        }

        // returns BTNode containing word if found
        public BTNode search(String word) {
            int i = 0;

            // find first key greater than word
            while (i < n && word.compareTo(words[i]) > 0)
                i++;

            // key is word, return node
            if (words[i].equals(word))
                return this;

            // if leaf and word not equals key, word does not exist in tree
            if (isLeaf == true)
                return null;
            
            // find next possible child node for key to be in
            return children[i].search(word);
        }

        // in order traversal of btree
        public void traverse() {
            int i = 0;
            // traverse first size(keys) children
            for (i = 0; i < n; i++) {
                if (!isLeaf) // if not leaf traverse subtree before printing key
                    children[i].traverse();
                System.out.println(words[i]);
            }

            // traverse last subtree due to size(children) - 1 ^^^
            if (!isLeaf)
                children[i].traverse();
        }

        // utility fn for insertion, precondition is node isnt full when called
        public void insertNonFull(String word) {
            int i = n - 1; // greatest key index

            if (isLeaf) {
                // while keys are greater than word param
                while (i >= 0 && words[i].compareTo(word) > 0) {
                    words[i + 1] = words[i]; // shift keys
                    i--; // update insertion index
                }

                if (i < 0) // second insertion
                    words[i + 2] = word;
                else // subsequent ones
                    words[i + 1] = word;

                n++;
            } else { // not leaf node

                // find child for new insertion
                while (i >= 0 && words[i].compareTo(word) > 0)
                    i--;
                
                // if child is full
                if (children[i + 1].n == 2 * branchingFactor - 1) {
                    splitChild(i + 1, children[i + 1]); // split it

                    // figure out which child is getting new word
                    if (words[i + 1].compareTo(word) < 0)
                        i++;
                }
                children[i + 1].insertNonFull(word);
            }
        }

        // utility function for splitting node at index i
        // precondition: i valid index and node is a full btnode
        public void splitChild(int i, BTNode node) {
            BTNode tmp = new BTNode(node.isLeaf);
            tmp.n = branchingFactor - 1;

            // copy second half of node into tmp
            for (int j = 0; j < branchingFactor - 1; j++) 
                tmp.words[j] = node.words[j + branchingFactor];
            
            if (!node.isLeaf) { // if node not leaf copy over corresponding children ^^^
                for (int j = 0; j < branchingFactor; j++) 
                    tmp.children[j] = node.children[j + branchingFactor];
            }

            // update node's word count
            node.n = branchingFactor - 1;

            // shift cursors children to make room for new child
            for (int j = n; j >= i + 1; j--)
                children[j + 1] = children[j];


            // add tmp to children
            children[i + 1] = tmp;

            // make a location for a word from node to be inserted
            for (int j = n - 1; j >= i; j--)
                words[j + 1] = words[j];

            // add word from node to cursor
            words[i] = node.words[branchingFactor - 1];

            n++;
        }

        // returns index of first key >= word
        public int findKey(String word) {
            int idx = 0;
            while (idx < n && words[idx].compareTo(word) < 0)
                ++idx;
            return idx;
        }

        // remove word from subtree of cursor
        public void remove(String word) {
            int idx = findKey(word);

            // word in current cursor
            if (idx < n && words[idx].equals(word)) {
                if (isLeaf)
                    removeFromLeaf(idx);
                else
                    removeFromNonLeaf(idx);
            } else {
                if (isLeaf) { // node leaf and not in node --> DNE
                    System.out.println("The key " + word + " does not exist in the BTree");
                    return;
                }

                // up to last child?
                boolean flag = (idx == n) ? true : false;

                // fill child with word if needed
                if (children[idx].n < branchingFactor)
                    fill(idx);

                // has btnode been merged?
                if (flag && idx > n) // recurse merged node
                    children[idx - 1].remove(word);
                else // recurse filled child
                    children[idx].remove(word);
            }
        }

        // remove word from leaf btnode's idx
        public void removeFromLeaf(int idx) {
            for (int i = idx + 1; i < n; ++i)
                words[i - 1] = words[i];

            n--;
        }

        // remove word at index idx of nonleaf btnode
        public void removeFromNonLeaf(int idx) {
            String word = words[idx];

            // pred can donate, recurse down it and replace donatable with word
            if (children[idx].n >= branchingFactor) {
                String pred = getPred(idx);
                words[idx] = pred;
                children[idx].remove(pred);
            } else if (children[idx + 1].n >= branchingFactor) {
                // pred cant donate same logic with succesor sibling
                String succ = getSucc(idx);
                words[idx] = succ;
                children[idx + 1].remove(succ);
            } else { // neither sibling can donate then merge
                merge(idx);
                children[idx].remove(word);
            }
        }

        // find pred word as left btnode siblings donation
        public String getPred(int idx) {
            BTNode cursor = children[idx];

            // travese until get to leaf
            while(!cursor.isLeaf)
                cursor = cursor.children[cursor.n];

            // return last word of left sibling leaf btnode
            return cursor.words[cursor.n - 1];
        }

        // find succ word as right btnode siblings donation
        public String getSucc(int idx) {
            BTNode cursor = children[idx + 1];

            while(!cursor.isLeaf)
                cursor = cursor.children[0];

            return cursor.words[0];
        }

        // fill child whos n violates btree invariants
        public void fill(int idx) {
            // if prev child has enough words borrow from them
            if (idx != 0 && children[idx - 1].n >= branchingFactor)
                borrowFromPrev(idx);

            // if prev doesnt and next does borrow from them
            else if (idx != n && children[idx + 1].n >= branchingFactor)
                borrowFromNext(idx);

            else {
                if (idx != n) // not last child
                    merge(idx); // merge with next sibling
                else // is last child
                    merge(idx - 1); // merge with prev sibling
            }
        }

        // borrows word from left sibling and inserts into cursor btnode
        public void borrowFromPrev(int idx) {
            BTNode child = children[idx];
            BTNode sibling = children[idx - 1];

            // shift all of keys in child  right
            for (int i = child.n - 1; i >=0; --i)
                child.words[i + 1] = child.words[i];

            if (!child.isLeaf) { // if child not leaf update child pointer indices
                for (int i = child.n; i >= 0; --i)
                    child.children[i + 1] = child.children[i];
            }

            // set child first key based off current node words
            child.words[0] = words[idx - 1];

            // update children of childs first index if necessary
            if (!child.isLeaf) 
                child.children[0] = sibling.children[sibling.n];

            // move key from sibling to parent
            words[idx - 1] = sibling.words[sibling.n - 1];

            child.n++;
            sibling.n--;
        }

        // 
        public void borrowFromNext(int idx) {
            BTNode child = children[idx];
            BTNode sibling = children[idx + 1];

            // insert last word from cursor to child
            child.words[child.n] = words[idx];

            // update children too if necessary
            if (!child.isLeaf)
                child.children[child.n + 1] = sibling.children[0];

            // cursor gets siblings first word
            words[idx] = sibling.words[0];

            // shifting siblings keys left
            for (int i = 1; i < sibling.n; ++i)
                sibling.words[i - 1] = sibling.words[i];

            // updated children pointers in sibling if necessary
            if (!sibling.isLeaf) {
                for (int i = 1; i < sibling.n; ++i)
                    sibling.children[i - 1] = sibling.children[i];
            }

            child.n++;
            sibling.n--;
        }

        // merges two children of word if necessary during deletion
        public void merge(int idx) {
            BTNode child = children[idx];
            BTNode sibling = children[idx + 1];

            // get middle word and add to child
            child.words[branchingFactor - 1] = words[idx];

            // copy over keys between siblings
            for (int i = 0; i < sibling.n; ++i)
                child.words[i + branchingFactor] = sibling.words[i];
            
            // copy pointers too if necessary
            if (!child.isLeaf) {
                for (int i = 0; i <= sibling.n; ++i)
                    child.children[i + branchingFactor] = sibling.children[i];
            }

            // update cursors words indices to fill gap created ^^
            for (int i = idx + 1; i < n; ++i)
                words[i - 1] = words[i];

            // update child pointers left as per ^^
            for (int i = idx + 2; i <= n; ++i)
                children[i - 1] = children[i];

            child.n += sibling.n + 1;
            n--;
        }
    }
}