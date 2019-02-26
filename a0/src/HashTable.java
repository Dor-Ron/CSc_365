package csc365.projects.one;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class HashTable {
    private int size;
    private final double loadFactor;
    private HashNode[] table;

    public HashTable() {
        table = new HashNode[1000];
        size = 0;
        loadFactor = 0.75;
    }
    
    /**
     * Returns index for corresponding value to key parameter
     *
     * @param key input for hash function
     * @return the index of the ArrayList for the input to the function
     */
    public int hash(String key) {
        int asciiSum = 0;
        for (int idx = 0; idx < key.length(); idx++) 
            asciiSum += (int) key.charAt(idx);
        return asciiSum % table.length;
    }


    /**
     * adds k,v pair to hashtable
     *
     * @param key entry's key
     * @param value value to add to entry
     */
    public void put(String key, int value) {
        int idx = hash(key);
        HashNode listHead = table[idx];
        // no collision, simply add
        if (listHead == null) {
            table[idx] = new HashNode(key, value);
            size++;
        }
        // if collision, add to end of linked hashnode list
        else {
            for(HashNode tmp = listHead; tmp.next != null; tmp = tmp.next) {
                if (tmp.key() != key)  // key doesn't already exist
                    listHead = tmp;
                else {
                    System.out.println("Key already exists.");
                    return ; // exit function 
                }
            }
            listHead.next = new HashNode(key, value);
        }

        // check if need to resize
        if (size >= (table.length * loadFactor)) elongate();
    }

    /**
     * finds matching value for given key
     *
     * @param key entry's key
     * @return the value associated with key argument
     */
    public int get(String key) {
        int idx = hash(key);

        for (HashNode tmp = table[idx]; tmp != null; tmp = tmp.next) 
            if (tmp.key().equals(key)) return tmp.value();
        return -1;
    }

    /**
     * updates value for given key
     *
     * @param key entry's key
     * @param the value to update the value field of entry with
     */
    public void updateValue(String key, int update) {
        int idx = hash(key);
        
        for (HashNode tmp = table[idx]; tmp != null; tmp = tmp.next) 
            if (tmp.key().equals(key)) tmp.setValue(update);
    }

    /**
     * Adds buckets to hashtable 
     */
    public void elongate() {
        // adjust object attributes
        size = 0;
        HashNode[] tempTable = table; // hold onto current entries in tmp array
        table = new HashNode[table.length*2]; // new array to fill
      
        for(HashNode element : tempTable) {  // each elem of original array
            for (HashNode tmp = element; tmp != null; tmp = tmp.next) // traverse list
                this.put(tmp.key(), tmp.value()); // add to new list
        }
    }

    /**
     * gives back string representation of hashtable
     * 
     * @return string version of hashtable array
     */
    public String stringify() {
        return Arrays.toString(table);
    }

    /**
     * getter for size attribute
     * 
     * @return current amount of taken up buckets
     */
    public int size() {
        return size;
    }

    /**
     * getter for table attribute
     * 
     * @return current version of hashtable array
     */
    public HashNode[] getTable() {
        return this.table;
    }

    /**
     * creates arraylist of distinct words in website
     * 
     * @return arraylist of distinct keys
     */
    public HashSet<String> getTableKeys() {
        HashSet<String> set = new HashSet<String>();
        for(HashNode node: table) {
            for (HashNode tmp = node; tmp != null; tmp = tmp.next) 
                set.add(tmp.key());
        }
        return set;
    }
}