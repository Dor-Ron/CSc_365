package oswego.csc365.a3;

import java.io.*;

public class Main {
    public static void main( String[] args ) throws java.io.IOException {

        BTree bt = new BTree(5); 

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("words.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;

        while ((line = reader.readLine()) != null)
            bt.insert(line);
        is.close();

        bt.traverse();
    }
}
