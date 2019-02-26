package csc365.projects.one;

import javafx.application.Application; 
import javafx.event.EventHandler; 
import javafx.scene.Group; 
import javafx.scene.Scene; 
import javafx.scene.control.TextField; 
import javafx.scene.control.Button; 
import javafx.scene.input.MouseEvent; 
import javafx.event.ActionEvent;
import javafx.scene.paint.Color; 
import javafx.scene.text.Font; 
import javafx.scene.text.FontWeight; 
import javafx.scene.text.Text;  
import javafx.stage.Stage; 

import java.util.ArrayList;
import java.util.Arrays;
import java.net.UnknownHostException;
import java.net.MalformedURLException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
         
public class Main extends Application { 
   @Override 
   public void start(Stage stage) {       
      // Text configurations
      Text text = new Text("Enter a COMPLETE URL you'd like to compare against the data: "); 
      text.setFont(Font.font(null, FontWeight.BOLD, 15));     
      text.setFill(Color.DODGERBLUE); 
      text.setX(20); 
      text.setY(50);
      
      // TextField configurations
      TextField textField = new TextField();   
      textField.setLayoutX(50); 
      textField.setLayoutY(100); 

      // Button Configurations
      Button button = new Button("Submit"); 
      button.setLayoutX(300); 
      button.setLayoutY(100);
       
      // Button Event Listener
      button.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            if (!(textField.getText() != null && !textField.getText().isEmpty())) // if empty
                System.out.println("action required");
            else {
                try {
                  String url = textField.getText(); //get url from textfield
                  URL urlObj = new URL(url); // try validating it
                  Parsing parser = new Parsing();
                  String[] userWords = parser.makeWordArray(url);
                  ArrayList<String> userMeta = parser.makeMetaTagWords(url);
                //   System.out.println(Arrays.toString(userWords));
                //   System.out.println(userMeta);
                  if (userWords.length > 0) { // if arr > 0, aka some vaid content reached, close
                    stage.close();
                    Results r = new Results(url);
                    Stage s = new Stage();
                    r.start(s);
                  } else  // UnknownHostException probably, need to enter existing URL
                    System.out.println("URL entered might not exist, try another.");
                } catch(java.net.MalformedURLException err) {
                    err.printStackTrace();
                }        
            }
        }
      });
       
      // Finish window configurations
      Group root = new Group(textField, text, button); 
      Scene scene = new Scene(root, 500, 200);      
      stage.setTitle("CSc 365 Assignment 1 - Dor Rondel"); 
      stage.setScene(scene); 
      stage.show(); 
   } 

   public static void main(String args[]){ 
      launch(args); 
   } 
}