package oswego.csc365.a3;

import java.io.Serializable;

public class BTree implements Serializable {
    
    public static int order;
    public int minKeys;
    public Node root;
    public Node latterSplit;
    public int count = 0;
    public Cache cache = null;
    public static int blockAmount = 0;

    // Constructor for local in-memory tree
    BTree(int order) {
        root = null;
        BTree.order = order;
        minKeys = order / 2;		
    }

    // Constructor to load BTree from .hdr file ADT
    BTree(Header header) {
        BTree.order = header.order;
        minKeys = order / 2;
        count = header.totalWords;
        blockAmount = header.nodeAmount;
        cache = header.cache;
        cache.emptyBlocks = header.emptyBlocks;
    }

    // sets BTree attributes in accordance to param .hdr ADT values
    void loadHeader (Header header) {
        BTree.order = header.order;
        minKeys = order / 2;
        count = header.totalWords;
        blockAmount = header.nodeAmount;
        cache.emptyBlocks = header.emptyBlocks;
    }

    // adds word param to BTree
    boolean add(String word) {
        // if tree DNE
        if(root == null) {
            root = new Node(0); // add node at position 0 of persistent data as root & add to cache
            count++;
            blockAmount++;
            root.add(word);
            cache.add(root, true);
            return true;
        } else if(insert(word, root)) { // else use helper insert method to add word to btree
            count++;
            return true;
        }
        return false;
    }

    // adds word param into node param
    boolean insert(String word, Node node) {
        // already exist --> break	
        if(node.contains(word))  
            return false;
        
        // there's room for it and DNE --> add it
        if(!node.isFull() && node.isLeaf()) { 
            node.add(word);
            cache.add(node, true);
            return true;
        // need to add to already full node, make new one
        } else if(node.isFull() && node == root) {
            // allocate new root and update nodes address
            root = new Node(0);
            node.setAddress(blockAmount++);
            String tmp = split(node);

            // add mid indenode of node as new root
            root.add(tmp);
                
            // set children of new root to 
            root.children[0] = node.getAddress();
            root.children[1] = latterSplit.getAddress();
                
            // see if there's addresses to write new nodes to that were cleared and 
            cache.emptyAddrInCache(node, root);
            cache.emptyAddrInCache(latterSplit, root);
            cache.add(root, true);
            cache.add(node, true);
            cache.add(latterSplit, true);
                
            
            // determine which child of root to add word param to
            if(word.compareTo(tmp) < 0)
                return insert(word, node); 
            else if(word.compareTo(tmp)>0)
                return insert(word, latterSplit);
                    
            return false;
        } else { // node is full but node != root
            Node next = null;
            try { // load appropriate child for word to go into to the cache
                next = cache.get(node.getChild(word));
            } catch(Exception e) {
                e.printStackTrace();
            }

            // if child word belongs in is full too, split it as we did to root ^^^
            if(next.isFull()) {  

                String tmp = split(next);
                node.add(tmp);
                int index = node.getIndex(tmp);
                        
                node.children[index] = next.getAddress();
                node.children[index + 1] = latterSplit.getAddress();
                        
                cache.emptyAddrInCache(next, node);
                cache.emptyAddrInCache(latterSplit, node);
                cache.add(node, true);
                cache.add(next, true);
                cache.add(latterSplit, true);
                    
                // find right grandchild for word to go into
                if(word.compareTo(tmp) < 0)
                    return insert(word, next);
                if(word.compareTo(tmp) > 0)
                    return insert(word, latterSplit);
                        
                return false;
            }
            return insert(word, next);
        }
    }

    // Returns word node is being split at for full node insertion
    String split(Node node) {
        latterSplit = new Node(blockAmount++);
        int mid = order / 2 - 1;
        String tmp = node.words[mid];
            
        // add latter half of words to right slibling
        for (int i = mid + 1; i < order - 1; i++) {
            latterSplit.add(node.words[i]);
            node.words[i] = null;
        }
        
        // add latter half of corresponding children to right slibling
        for (int i = mid + 1; i < order; i++) {
            latterSplit.children[(i - (mid + 1))] = node.children[i];
            node.children[i]= -1;
        }
            
        node.words[mid] = null;	
            
        return tmp;
    }

    // removes word parameter from btree if in root
    boolean delete(String word) {
        if(delete(word, root, null)) {  
            count--;
            return true;
        }
        return false;
    }


    // removes word parameter from btree 
    // helper function to ^^^^
    boolean delete(String word, Node node, Node parent) {
        String tmp = null;
        
        if(node == null || node.words[0] == null)
            return false;
        if(node != null) // if cursor node exists check if it can handle deleteetion
            mergeOrBorrow(node, parent);
        
        // next node in case word not in cursor node
        Node next = null;
        
        try { // get child of node from cache
            next = cache.get(node.getChild(word));
        } catch(Exception e) {
            System.out.println(e);
        }
                
        // word in cursor node?
        if(node.contains(word)) {
            // if simply a leaf delete word and add to cache
            if(node.isLeaf()) { 
                node.delete(word);
                cache.add(node, true);
            // cursor not leaf --> rotate next word from right subtree into x's place
            } else {
                tmp = successor(word, node, null); // finds 
                    
                if(node.contains(word) && tmp != null)
                    node.words[node.getIndex(word)] = tmp;
            }
        
            // cursor size is permissible after deletion?
            if(node.size() >= minKeys - 1 || node == root)
                return true;
        } else
            return delete(word, next, node);
        return true;
    }

    // determines whether borrow or merge is necessary
    boolean mergeOrBorrow(Node node, Node parent) {
        // cursor is any node other than root that doesnt possess enough words
        if(node.size() < minKeys && parent != null) {
            // if you cant borrow a word from parent --> need to merge
            if(!borrow(node, parent)) {
                // get index of cursor from children of parent
                int index = parent.getIndex(node.getAddress());
                        
                // clear persistent address in cache
                cache.removeBlock(node.getAddress());

                // merge cursor and parent
                Node mergedNode = merge(node, parent);
                        
                // add updated post merge node as left child
                if(index > 0)
                    parent.children[index - 1] = node.getAddress();
                else
                    parent.children[index] = node.getAddress();
                        
                // if took key from root for merge, make merged node root
                // update cache to root update and cleared merged node block
                if(root.size() == 0) {	
                    root = mergedNode;
                    root.setAddress(0); 
                    cache.add(root, true);
                    cache.removeBlock(mergedNode.getAddress());
                    return true;			
                }
                        
                // make sure we're writing nodes in correct addresses
                // parent and updated merged cursor node to cache
                cache.emptyAddrInCache(node, parent);
                cache.add(parent, true);
                cache.add(mergedNode, true);
                        
                return true;
            } else
                return true;
        }
        return false;
    }

    // true if successfully borrowed word from parent otherwise false
    boolean borrow(Node node, Node parent) {
        // find position of cursor in children array of parent
        int index = parent.getIndex(node.getAddress());
        Node tmp = null;
            
        // try getting left or right slibling from cache
        if(index > 0) {
            try {
                tmp = cache.get(parent.children[index - 1]);
            } catch(Exception e) {
                System.out.println(e);
            }
        } else if (index == 0) {
            try {
                tmp = cache.get(parent.children[index + 1]);
            } catch(Exception e) {
                System.out.println(e);
            }
        }
            
        // borrow from left sibling if cursor not leftmost child
        if(index > 0 && tmp.size() >= minKeys) {
            String largest = tmp.largestWord(); 
            int largestIndex = tmp.getIndex(largest); // get greatest word from left sibling
            Node child1 = null;
            Node child2 = null;
            try { // get children of word we're going to rotate
                child1 = cache.get(tmp.children[largestIndex + 1]);
                child2 = cache.get(tmp.children[largestIndex]);
            } catch(Exception e) {
                System.out.println(e);
            }

            // get word from parent to rotate to cursor
            String replacement = parent.words[index - 1];

            // rotate corrsponding parent word into cursor node
            node.shiftWordsR(0);
            node.words[0] = replacement;

            // rotate right child largest lexographically sorted word into parent nodes
            parent.words[index - 1] = largest;
                
            tmp.delete(largest);
                
            // if x isn't a leaf rotate children 
            if(!node.isLeaf()) {
                node.shiftChildrenR(0); // shift children to make room for rotated children
                node.children[0] = child1.getAddress();
                tmp.children[largestIndex] = child2.getAddress(); // update siblings children array
                tmp.children[largestIndex + 1] = -1;
            }


            // update cache	
            cache.add(tmp, true);
            cache.add(node, true);
            cache.add(parent, true);
            return true;
        // borrow from right sibling if cursor is leftmost child
        } else if(index == 0 && tmp.size() >= minKeys) {
            // get smallest lexographically sorted child of sibling
            String smallest = tmp.words[0];
            Node child = null;
            try { // get corresponding child
                child = cache.get(tmp.children[0]);
            } catch(Exception e) {
                System.out.println(e);
            } // get smallest word in parent
            String replacement = parent.words[index];
                
            // rotate smallest parent word into cursor 
            node.add(replacement);
            // rotate smallest child of sibling into parent
            parent.words[index] = smallest;
                
            tmp.delete(smallest);
                
            // if cursor isnt a lead, rotate corresponding children & update caches
            if(!node.isLeaf())
                node.children[node.getIndex(replacement) + 1] = child.getAddress();
                
            cache.add(node, true);
            cache.add(parent, true);
            cache.add(tmp, true);

            return true;
        }	
        return false;
    }

    // true if merge executed otherwise false
    Node merge(Node node, Node parent) {
        // index of cursor in parent
        int index = parent.getIndex(node.getAddress());
            
        // try to get right or left sibling based off of index for merge
        Node tmp = null;
        if(index == 0) {
            try {
                tmp = cache.get(parent.children[1]);
            } catch(Exception e) {
                System.out.println(e);
            }
        } else {
            try {
                tmp = cache.get(parent.children[index - 1]);
            } catch(Exception e) {
                System.out.println(e);
            }
        }
            
        // if there is a sibling to merge with
        if(tmp != null) {
            if(index == 0) { // cursor is leftmost child
                int size = node.size();
                // rotate parent into cursor 
                node.words[size] = parent.words[0];
                size++;
                        
                // add siblings words & children to cursor for merge
                for (int i = 0; i < tmp.size(); i++)
                    node.words[size + i] = tmp.words[i];
                for (int i = 0; i <= tmp.size(); i++)
                    node.children[size + i] = tmp.children[i];
                        
                // get rid of sibling node
                parent.delete(parent.words[0]);
            } else { // cursor not leftmost child --> rotate parents greatest word to cursor
                node.shiftWordsR(0);
                node.words[0] = parent.words[index - 1];
                int size = node.size();
                        
                // merge sibling with cursor
                for (int i = tmp.size() - 1; i >= 0; i--) {
                    node.shiftWordsR(0);
                    node.words[0] = tmp.words[i];
                }

                for (int i = tmp.size(); i >= 0; i--) {
                    node.shiftChildrenR(0);
                    node.children[0] = tmp.children[i];
                }

                // get rid of sibling
                parent.delete(parent.words[index - 1]);
            }
        }
            
        // update cache per siblings deletion
        cache.removeBlock(tmp.getAddress());
        return node;	
    }

    // Finds smallest string in the right subtree of the cursor
    String successor(String word, Node node, Node parent) {
        String tmp = null;
        Node child = null;

        try { // smallest child
            child = cache.get(node.children[0]);
        } catch(Exception e) {
            System.out.println(e);
        }
            
        if (node.contains(word)) {  // if word in cursor, get its right child
            int index = node.getIndex(word);  
            try {  
                child = cache.get(node.children[index + 1]);
            } catch(Exception e) {
                System.out.println(e);
            }
            tmp = successor(word, child, node);  // smallest word is now right child recursion result
        } else if (node.children[0] != -1 && !child.isEmpty())
            tmp = successor(word, child, node); // recurse down left child

        // there is no smallest word in subtree
        if (tmp == null) {
            tmp = node.words[0];  // get smallest word from cursor & delete it
            node.delete(tmp);  
        }

        // see if cursor require borrow or merge after removal of smallest word
        mergeOrBorrow(node, parent);
            
        // if we've successfully gotten a replacement, update word slot post deletion
        if (node.contains(word) && tmp != null)
            node.words[node.getIndex(word)] = tmp;
    
        cache.add(node, true);
        return tmp;
    }

    // Stringify BTree for demoing purposes
    public String toString() {
        String result = "";
        result += root.toString();
        for (int i = 0; i < order; i++)
            result += "\nChild #" + i + "\t" + root.children[i];
                
        return result;
    }
}