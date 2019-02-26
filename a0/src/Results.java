package csc365.projects.one;

import javafx.application.Application; 
import javafx.scene.Group; 
import javafx.scene.Scene; 
import javafx.scene.control.TextField; 
import javafx.scene.control.Button; 
import javafx.scene.control.Label;
import javafx.scene.paint.Color; 
import javafx.scene.text.Font; 
import javafx.scene.text.FontWeight; 
import javafx.scene.text.Text;  
import javafx.stage.Stage; 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
         
public class Results extends Application { 
   public static final ObservableList data = FXCollections.observableArrayList();
   private String userUrl = "";
   Parsing parser = new Parsing();

   public Results(String usrUrl) {
       userUrl = usrUrl;
   }

   /**
     * Window configurations setter
     * 
     * @param stage to configure
     */
   @Override 
   public void start(Stage stage) {           
      // window setup
      stage.setTitle("Results!");
      stage.setWidth(850);
      stage.setHeight(350);

      // table definition
      final ListView listView = new ListView(data);
      listView.setPrefSize(540, 230);
      listView.setEditable(true);
       
      // initialize data analysis
      String[] urls = this.makeUrls();
      HashTable[] tables = this.makeTables(urls);
      double[] cosineAngles = this.makeAngles(tables);
      ArrayList<String> entries = this.dataValues(urls, cosineAngles);

      // fill table data
      for (int i = 1; i < entries.size(); i++) 
          data.add(entries.get(i));
         
      listView.setItems(data);
      listView.relocate(300, 70);

      // add table label
      Label label = new Label("Rest of data:");     
      label.setTranslateY(100);
      label.setTranslateX(200);

      // result text
      final Text text = new Text("Closest Website: " + entries.get(0).substring(14));
      text.setFont(Font.font(null, FontWeight.BOLD, 17));     
      text.setFill(Color.DODGERBLUE); 
      text.setX(10); 
      text.setY(50);
       
      // Finish window configurations
      Group root = new Group(text, label, listView); 
      Scene scene = new Scene(root, 350, 600);      
      stage.setTitle("CSc 365 Assignment 1 - Dor Rondel"); 
      stage.setScene(scene); 
      stage.show(); 
   } 

   /**
     * Makes array of wikipedia strings for 1-10
     * 
     * @return string array 10 wikipedia urls
     */
   public String[] makeUrls() {
      String[] urls = new String[10];
      for (int i = 0; i < 10; i++) 
        urls[i] = "https://en.wikipedia.org/wiki/" + Integer.toString(i+1);
      return urls;
   }

   /**
     * Makes array of wikipedia strings for 1-10
     * 
     * @param array of 10 url strings
     * @return hashtable of word frequencies for each of the urls, size 10
     */
   public HashTable[] makeTables(String[] urls) {
      boolean isMeta = true;
      String[] metaArr;
      HashTable[] tables = new HashTable[10];
      HashTable tmp = new HashTable();
      for (int i = 0; i < urls.length; i++) {
        String[] arr1 = parser.makeWordArray(urls[i]);
        ArrayList<String> arr2 = parser.makeMetaTagWords(urls[i]);
        metaArr = new String[arr2.size()];
        parser.populateTable(tmp, arr1, !isMeta);
        parser.populateTable(tmp, arr2.toArray(metaArr), isMeta);
        tables[i] = tmp;
        tmp = new HashTable();
      }
      return tables;
   }

   /**
     * Makes array of wikipedia strings for 1-10
     * 
     * @param array of 10 url hashtables
     * @return double array of cosine vector values for each url, size 10
     */
   public double[] makeAngles(HashTable[] tables) {
      boolean isMeta = true;
      String[] metaArr;
      HashTable userTable = new HashTable();
      String[] arr1 = parser.makeWordArray(userUrl);
      ArrayList<String> arr2 = parser.makeMetaTagWords(userUrl);
      metaArr = new String[arr2.size()];
      parser.populateTable(userTable, arr1, !isMeta);
      parser.populateTable(userTable, arr2.toArray(metaArr), isMeta);
      double[] values = new double[10];
      for (int i = 0; i < tables.length; i++) 
         values[i] = parser.cosineVectorSimilarity(userTable, tables[i]);
      return values;
   }

   /**
     * Finds largest double from array of doubles
     * 
     * @param array of 10 url hashtables
     * @return double array of cosine vector values for each url, size 10
     */
   public double getMaxValue(double[] values) {
       ArrayList<Double> list = new ArrayList<Double>();
       for (int i = 0; i < values.length; i++) 
         list.add(values[i]);
       return Collections.max(list);
   }

   /**
     * Makes arraylist of formatted result strings to put in listview
     * 
     * @param string of urls used as data
     * @param double cosine vector values of the urls
     * @return arraylist of formatted result strings with largest at index 0
     */
   public ArrayList<String> dataValues(String[] urls, double[] values) {
       ArrayList<String> entries = new ArrayList<String>();
       double closest = this.getMaxValue(values);
       int idx = -1;
       for(int i = 0; i < values.length; i++) 
          if(values[i] == closest) idx = i;

       String template = "URL: %s \t Weighted Cosine Vector Similarity: %f";
       for (int i = 0; i < values.length; i++) {
           if (i == idx) {
              entries.add("CLOSEST! " + String.format(template, urls[i], (float) values[i]));
              Collections.swap(entries, 0, i);
           } else 
              entries.add(String.format(template, urls[i], (float) values[i]));
       }
       return entries;
   }
}