package oswego.csc365.a3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;

public class Cache implements Serializable {
    
    public transient RandomAccessFile file = null;
    public int maxBlocks;
    public int blockSize;
    public Node[] cache;
    public int[] addresses;
    public boolean[] dirty; 
    public ArrayList<Integer> emptyBlocks;
    

    // Constructor
    public Cache(File flraf, String mode, int _blockSize, int _maxBlocks) throws FileNotFoundException {
        file = new RandomAccessFile(flraf, mode);
        maxBlocks = _maxBlocks;
        blockSize = _blockSize;
        cache = new Node[maxBlocks];
        dirty = new boolean[maxBlocks];
        emptyBlocks = new ArrayList<Integer>();
        for (int i = 0; i < maxBlocks; i++)
            dirty[i] = false;
        addresses = new int[maxBlocks];
        for (int i = 0; i < maxBlocks; i++)
            addresses[i] = -1;	
    }


    // Adds node to cache
    public boolean add(Node node, boolean isDirty) {
        if(!isFull()) {  // room in cache
            for (int i = 0; i < cache.length; i++) { 
                // if node in cache is same as one being added
                if(cache[i] != null && cache[i].getAddress() == node.getAddress()) { 
                    cache[i] = node; // update cache
                    dirty[i] = true; // toggle dirty arr value
                    addresses[i] = node.getAddress();  // update pointers for nodes in cache
                    return true;
                // if room in cache and its identical to persistent data
                } else if(cache[i] == null || !dirty[i]) {
                    cache[i] = node;
                    addresses[i] = node.getAddress();
                    dirty[i] = isDirty; // state possibly different from persistent data
                    return true;
                }
            }
        } else { // if cache full make room and re-run 
            dump();
            add(node, isDirty);
        }
        return true;
    }

    // check if block in cache
    public boolean contains(int address) {
        for (int i = 0; i < maxBlocks; i++)
            if(cache[i] != null && addresses[i] == address)
                return true;    
        return false;
    }


    // empties cache
    public void dump() {
        for (int i = 0; i < maxBlocks; i++) {
            if(cache[i] != null) {  // update entry in persistent  data and flush cache 
                put(cache[i], cache[i].getAddress());
                cache[i] = null;
                addresses[i] = -1;
                dirty[i] = false;            
            }
        }
    }

    // true if empty block in persistent memory to write to otherwise false
    public boolean emptyAddrInCache(Node cursor, Node parent) {
        // there is an emparentty block
        if(emptyBlocks.size() > 0 && parent != null) {
            int empty = emptyBlocks.get(0);
            int addresses;
            if(!parent.isEmpty()) // if parent then find addresses of child
                addresses = parent.getIndex(cursor.getAddress());
            else		
                addresses = 0;
                
            cursor.setAddress(empty);  // update child address
            parent.children[addresses] = empty;  // update pointer in parent
            emptyBlocks.remove(0);  // update cache state
            return true;
        }
        return false;
    }

    // deletes node from cache
    public boolean removeBlock(int offset) {
        if(offset == 0)  // dont delete root, leave empty
            return false;

        // update empty and dirty arrays in cache
        emptyBlocks.add(offset);
        Node empty = new Node(offset);	
        add(empty, true);	
        return true;
    }


    // reads from persistent data at given address
    public Node get(int offset) throws IOException{
        if(offset <= -1) // non-natural number addresses address
            return null;

        // make byte array of size (max_words + max_children) * bytes_in_longest_possible_string
        byte[] block = new byte[((2 * BTree.order) - 1) * blockSize];
        Node retVal = new Node();

        // if node already loaded up into cache
        if(contains(offset)) {
            for (int i = 0; i < maxBlocks; i++)
                if(addresses[i] == offset) {  // find node, make cache dirty, and return it
                    dirty[i] = true;
                    return cache[i];
                }
        }
                

        // if node not in cache seek to its position in persistent data
        file.seek(offset * blockSize * ((2 * BTree.order) - 1));
            
        // if address exists in file
        if(file.read(block) != -1) {
            String stringified = new String(block); 
            String[] separatedWords = stringified.split(" ");
            ArrayList<String> values = new ArrayList<String>();

            for (int i = 0; i < separatedWords.length; i++)
                if(!separatedWords[i].equals(""))
                    values.add(separatedWords[i]); // get string values from bytes
            
            for (int i = 0; i < values.size() - BTree.order; i++)
                retVal.add(values.get(i)); // populate in-memory node with values
            
            // go to end of byte array and populate children of in-memory node
            for (int i = values.size() - BTree.order; i < values.size(); i++) {
                try { 
                    retVal.children[i - (values.size() - BTree.order)] = Integer.parseInt(values.get(i));
                } catch(Exception e) {
                    System.out.println(e);
                }
            }
            
            retVal.setAddress(offset);  // set in-memory node address to offest and add to cache
            add(retVal, false);
            return retVal;
        }
        return null;
    }
    

     // Checks if cache has room for more cache
    public boolean isFull() {
        for (int i = 0; i < maxBlocks; i++) {
            if(cache[i] == null || !dirty[i])
                return false;
        }
        return true;
    }

    // writes node to persistent data at given address
    public boolean put(Node node, int offset) {
        int paddingNeeded;
        String paddingFill;
        // make byte array of size (max_words + max_children) * bytes_in_longest_possible_string
        byte[] block = new byte[((2 * BTree.order) - 1) * blockSize];
            
        // for word in keys
        for (int i = 0; i < BTree.order - 1; i++) {
            String tmp; // get word to be serialized from node param
            if(node.words[i] == null)
                tmp = "";
            else
                tmp = node.words[i];
                
            // figure out padding length --> 28 - len(tmp) bytes
            paddingNeeded = blockSize - tmp.length();
            paddingFill = "";

            for (int p = 0; p < paddingNeeded; p++)
                paddingFill += " ";
        
            tmp += paddingFill;

            byte[] tmpByte = tmp.getBytes();  // serialize final word
            // append final byte array with <tmp>'s bytes
            for (int j = 0; j < blockSize; j++)
                block[(i * blockSize) + j] = tmpByte[j]; 
        }

        // REPEAT ^^ for children
        for (int i = BTree.order - 1; i < ((2 * BTree.order) - 1); i++) {
            String tmp = Integer.toString(node.children[i - (BTree.order - 1)]);
                
            paddingNeeded = blockSize - tmp.length();
            paddingFill = "";

            for (int j = 0; j < paddingNeeded; j++)
                paddingFill += " ";
        
            tmp += paddingFill;

            byte[] tmpByte = tmp.getBytes();
            for (int j = 0; j < blockSize; j++)
                block[(i * blockSize) + j] = tmpByte[j];
        } 
        
        try { // trying adding final byte[] to persistent data store
            file.seek(offset * blockSize * ((2 * BTree.order) - 1));
            file.write(block);
        } catch(Exception e) {
            return false;
        }
        return true;
    }

    // stringify the cache for demoing purposes
    public String toString() {
        String result = "";
        for (int i = 0; i < maxBlocks; i++) {
            if(cache[i] != null) {
                result += "\nCache Index " + i + ": " + cache[i].toString() + 
                          "\t\t\t@ address: " + cache[i].getAddress() + "\t\t\tisDirty? : " + dirty[i];
            }
        }
        return result;
    }
}