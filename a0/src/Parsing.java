package csc365.projects.one;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import java.util.ArrayList;
import java.util.HashSet; 
import java.util.Arrays;
import java.util.List;

public class Parsing {
     /**
     * returns array indexed by each individual word \in <body>
     * 
     * @param url string to be parsed
     * @return current amount of taken up buckets
     */
    public String[] makeWordArray(String url) {
        String[] words = new String[0];
        Document doc = null;
        try {
          doc = Jsoup.connect(url).get();
          String text = doc.body().text();
          words = text.split(" ");
        } catch(java.io.IOException e) {
          e.printStackTrace();
        }       
        return words;
    }

    /**
     * returns array indexed by each individual word \in <meta>
     * 
     * @param url string to be parsed by <meta>
     * @return current amount of taken up buckets
     */
    public ArrayList<String> makeMetaTagWords(String url) {
        ArrayList<String> words = new ArrayList<String>();
        Document doc = null;
        try {
          doc = Jsoup.connect(url).get();
          Elements text = doc.select("meta");
          for (Element el : text) {
              List<Attribute> attrs = el.attributes().asList();
              for (Attribute attr : attrs) {
                  String cont[] = attr.getValue().split(" ");
                  for (String word: cont)
                    if (word != null && !word.isEmpty()) words.add(word);
              }
          }
        } catch(java.io.IOException e) {
          e.printStackTrace();
        }        
        return words;
    }

    /**
     * fills HashTable for individual urls based <meta>
     * 
     * @param hashtable to be populated
     * @param arraylist of words to use for data
     * @param isMeta true if arr contains meta words for word weight purposes
     */
    public void populateTable(HashTable table, String[] arr, boolean isMeta) {
        for (int i = 0; i < arr.length; i++) arr[i] = arr[i].toLowerCase(); 
        for (String word: arr) 
            if (isMeta)
                if (table.get(word) == -1)  // word not in table yet
                    table.put(word, 2);
                else 
                    table.updateValue(word, table.get(word)+2);
            else
                if (table.get(word) == -1)  // word not in table yet
                    table.put(word, 1);
                else 
                    table.updateValue(word, table.get(word)+1);
    }

    /**
     * finds weighted cosine similarity value between 2 word lists
     * 
     * @param first hashtable for comparison
     * @param second hashtable for comparison
     * @return double representation of cosine similarity value
     */
    public double cosineVectorSimilarity(HashTable first, HashTable second) {
        HashSet<String> firstSet = first.getTableKeys();
        HashSet<String> secondSet = second.getTableKeys();
        HashSet<String> combinedSet = firstSet;
        // get all distinct words
        for (String word: secondSet) 
            if (!combinedSet.contains(word)) 
                combinedSet.add(word);
        
        // count occurences of word for all words, in both hashtables
        ArrayList<Integer> firstCounter = new ArrayList<Integer>();
        ArrayList<Integer> secondCounter = new ArrayList<Integer>();
        for (String word: combinedSet) {
            if (firstSet.contains(word)) 
                firstCounter.add(first.get(word));
            else 
                firstCounter.add(0);

            if (secondSet.contains(word))
                secondCounter.add(second.get(word));
            else 
                secondCounter.add(0);
        }

        // Weighted cosine vector value calculation
        int dotProduct = 0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;
        for (int i = 0; i < combinedSet.size(); i++) {
            dotProduct += firstCounter.get(i) * secondCounter.get(i);
            magnitude1 += Math.pow(firstCounter.get(i), 2);
            magnitude2 += Math.pow(secondCounter.get(i), 2);
        }

        return dotProduct / (Math.sqrt(magnitude1) * Math.sqrt(magnitude2));
    }
}