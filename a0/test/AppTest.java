package csc365.projects.one;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*; // Arrays


/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Ascii("Oswego") % 100 == 28
     */
    /*
    @Test
    public void hashTest() {
        HashTable table = new HashTable();
        int idx = table.hash("Oswego");
        assertTrue(idx == 28);  
    }

    @Test
    public void putGetTest() {
        HashTable ht = new HashTable();
        HashNode[] table = ht.getTable();
        ht.put("Year", 2017);
        ht.put("Year", 2016);
        ht.put("Ydbr", 8);
        assertEquals(ht.get("Year"), 2017);  
        assertNotEquals(ht.get("Year"), 2016); // no repeats
        assertEquals(ht.get("Ydbr"), 8); // chaining works
        assertEquals(ht.size(), 1); // size incrementing works properly
    }

    @Test 
    public void elongateTest() {
        HashTable ht = new HashTable();
        ht.put("a", 1);
        assertEquals(ht.length(), 4);
        System.out.println(Arrays.toString(ht.getTable()));         
        ht.put("e", 1);       
        assertEquals(ht.length(), 4);
        System.out.println(Arrays.toString(ht.getTable()));
        ht.put("b", 1);
        assertEquals(ht.length(), 4);
        System.out.println(Arrays.toString(ht.getTable()));        
        ht.put("c", 1);
        assertEquals(ht.length(), 6);
        System.out.println(Arrays.toString(ht.getTable())); 
    }*/

    /////// Parsing.java //////
    // @Test
    // public void makeWordArrayTest() {
    //     Parsing parser = new Parsing();
    //     parser.makeWordArray("https://en.wikipedia.org/wiki/1");
    //     assertTrue(true);
    // }

    // @Test
    // public void makeMetaTest() {
    //     Parsing parser = new Parsing();
    //     ArrayList<String> p = parser.makeMetaTagWords("https://en.wikipedia.org/wiki/1");
    //     assertTrue(true);
    // }

    // @Test
    // public void populateBodyTest() {
    //     Parsing parser = new Parsing();
    //     HashTable ht = new HashTable();

    //     String[] arr = parser.makeWordArray("https://en.wikipedia.org/wiki/1");
    //     parser.populateTableNormal(ht, arr);
    //     assertEquals(ht.get("a"), 41);
    //     assertEquals(ht.get("statement"), 1);
    //     assertEquals(ht.get("cookie"), 1);
    //     assertEquals(ht.get("privacy"), 2);
    //     assertEquals(ht.get("Privacy"), -1);
    // }

    // @Test
    // public void populateMetaTest() {
    //     Parsing parser = new Parsing();
    //     HashTable ht = new HashTable();

    //     ArrayList<String> arr = parser.makeMetaTagWords("https://en.wikipedia.org/wiki/1");
    //     parser.populateTableMeta(ht, arr);
    //     assertEquals(ht.get("utf-8"), 2); // 1 occurence, double weight
    //     assertEquals(ht.get("referrer"), 2);
    //     assertEquals(ht.get("cookie"), -1);
    // }

    // @Test
    // public void getTableKeysTest() {
    //     Parsing parser = new Parsing();
    //     HashTable ht = new HashTable();

    //     String[] arr = parser.makeWordArray("https://en.wikipedia.org/wiki/1");
    //     parser.populateTableNormal(ht, arr);
    //     assertEquals(1,1);
    // }

    // @Test
    // public void cosineTest() {
    //     Parsing parser = new Parsing();
    //     HashTable ht1 = new HashTable();
    //     HashTable ht2 = new HashTable();
    //     String[] arr = parser.makeWordArray("https://en.wikipedia.org/wiki/1");
    //     String[] arr2 = parser.makeWordArray("https://en.wikipedia.org/wiki/2");
    //     parser.populateTableNormal(ht1, arr);
    //     parser.populateTableNormal(ht2, arr2);
    //     System.out.println(parser.cosineVectorSimilarity(ht1, ht2));
    //     assertTrue(true);
    // }

    // @Test
    // public void metaTest() {
    //     Parsing parser = new Parsing();
    //     HashTable ht = new HashTable();

    //     ArrayList<String> arr = parser.makeMetaTagWords("https://en.wikipedia.org/wiki/1");
    //     System.out.println(arr);
    //     assertTrue(true);
    // }

    // @Test
    // public void qa() {
    //     Parsing parser = new Parsing();
    //     HashTable ht = new HashTable();
    //     HashTable ht2 = new HashTable();

    //     ht.put("lessThan0", -81);
    //     ArrayList<String> arr = parser.makeMetaTagWords("https://en.wikipedia.org/wiki/1");
    //     String[] arr2 = parser.makeWordArray("https://en.wikipedia.org/wiki/1");
    //     parser.populateTable(ht2, arr.toArray(new String[arr.size()]), true);
    //     parser.populateTable(ht2, arr2, false);
    //     System.out.println(parser.cosineVectorSimilarity(ht, ht2));
    //     assertTrue(true);
    // }
}
