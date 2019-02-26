package csc365.projects.one;

import java.util.ArrayList;

public class HashNode {
    private String key;
    private int value;
    HashNode next;  // to allow for chaining in case of collision
    
    public HashNode(String key, int value) {
        this.key = key;
        this.value = value;
        this.next = null;
    }

    /**
    * @return value.
    */
    public int value() {
        return value;
    }

    /**
    * @return key.
    */
    public String key() {
        return key;
    }

    /**
    * changes next attribute of object
    */
    public void setNext(HashNode next) {
        this.next = next;
    }

    /**
    * changes value attribute of object
    */
    public void setValue(int value) {
        this.value = value;
    }
}