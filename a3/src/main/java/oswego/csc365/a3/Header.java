
package oswego.csc365.a3;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

// ADT for easier interfacing with .hdr file
class Header implements Serializable {
    public Node root;
    public int order;
    public int totalWords;
    public int nodeAmount;
    
    public File file;
    public Cache cache = null;
    public ArrayList<Integer> emptyBlocks;

    
    // Constructor
    Header(File filePath, int wordTotal, int _order,  int _nodeAmount, ArrayList<Integer> _emptyBlocks) {
        file = filePath;
        totalWords = wordTotal;
        order = _order;
        nodeAmount = _nodeAmount;
        emptyBlocks = _emptyBlocks;
        try {
            cache = new Cache(filePath, "rw", 28, 4);
        } catch(Exception e) {
            System.out.println(e);
        }
        root = null;
    }

    public void setRoot(Node node) {
        root = node;
    }
}