package oswego.csc365.a3;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class SimpleService {
    static final int PORT = 8000;

	// as per https://stackoverflow.com/questions/24709769/java-using-system-getpropertyuser-dir-to-get-the-home-directory
    File headerFile = new File("header.hdr");
    File btreeFile = new File("btree.btr");
    File flraf = new File("nodes.flraf");

	// as per professor wenderholms fancy serialization demo on her sites resources
	FileInputStream fis = null;
    FileOutputStream fos = null;
    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;
    BTree btree = null;
    Header header = null;
	
	// constructor
    SimpleService() {
		checkPersistance();
		execute();
		save();
    }


	// adds words from words.txt to persistant btree
    void addWordsToBTree() {
		try {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream is = classloader.getResourceAsStream("words.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;

			while ((line = reader.readLine()) != null)
				btree.add(line);
			is.close();	

					
		} catch (Exception e){
			System.out.println(e + " Problem creating dictionary backend");
		}
    }


	// loads/creates .hdr & .btr files from home directory 
    public void checkPersistance() {
		try { // try to load header
			fis = new FileInputStream(headerFile);
			ois = new ObjectInputStream(fis);
			System.out.println("Loading an existing header file\n");
			header = (Header) ois.readObject();
		} catch(Exception e) { // create new ones
	    	System.out.println("Creating a new header file\n");
	    	header = new Header(flraf, 0, 8, 0, new ArrayList<Integer>(), 0);
		}
		
		try { // try to load persistent btree
            fis = new FileInputStream(btreeFile);
            ois = new ObjectInputStream(fis);
			System.out.println("Loading an existing btree file\n");
            btree = (BTree) ois.readObject();
			try { // try to load cache from header if exists
				btree.cache = new Cache(header.file, "rw", 29, 4);
			} catch(Exception e) {
				System.out.println(e);
			}
		
			// update rest of btree's attributes
			btree.loadHeader(header);

		} catch(Exception e) { 	
			System.out.println(e.getMessage() + "\nBTree persistent data not found, attempting" +
												" to make a new persistent btree\n");
			btree = new BTree(header);
			try {
				btree.cache = new Cache(header.file, "rw", 29, 4);
			} catch(Exception e2){
				System.out.println(e2);
			}
			addWordsToBTree();
		}		
    }


	// returns HTML string of root node and total node count
    String printNodeInfo() {
		return "<br/><br/>" +
			   "<h4>~~~ROOT~~~~~<br><font color=\"#eee\"></h4>"
				+ btree.root + "<br/><br/><br/>"
				+ "<br/>Amount of Nodes: = <font color=\"#eee\">" + btree.blockAmount + "</font><br/>";
    }


	// sockets infinite listening loop
    void execute() {
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
		
			while (true) {
				Socket client = serverSocket.accept();
				
				PrintWriter out = new PrintWriter(client.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
				String cmd = in.readLine();  
				String queryParameter = "";

				// get query param after url and between end of full http request string
				queryParameter = cmd.substring(cmd.lastIndexOf("GET /") + 5, cmd.lastIndexOf(" HTTP/"));
				
				// notify user of page
				String reply = "<html>\n" +
					"<head><title>Webster 365 API</title></head>\n" + 
					"<body bgcolor=\"#001f3f\">" + 
					"<h1><font color=\"#7FDBFF\">Webster 365 API</font></h1>" 
					+ "</body><br><font color=\"#7FDBFF\"><h2>"; 
					
					
				// check if removing or looking/adding word
				if(queryParameter.indexOf("-") == 0){
					if(btree.delete(queryParameter.substring(1))) // successfully removed
						reply += "<br/>\"<font color=\"red\">" + queryParameter.substring(1) 
							  + "</font>\" was removed from the dictionary btree.\n</html>\n";
					else // deletion couldn't take place, not found
						reply += "<br/>\"<font color=\"red\">" + queryParameter.substring(1) 
							  + "</font>\" was not found in the dictionary btree.\n</html>\n";
				} else { // trying to add or look up word
					if(btree.add(queryParameter)) // word doesnt already exist, add it
						reply += "<br>\"<font color=\"white\">" + queryParameter + "</font>\" was added into the btree dictionary.\n</html>\n";
					else // already exist, let user know that
						reply += "<br>\"<font color=\"orange\">"+ queryParameter + "</font>\" exists in the webster dictionary.\n</html>\n";
				}

				// show user the current state of the cache management system
				reply += printNodeInfo() + "<br/><br/><br/><br/><br/>"
				      + "<h4>~~~~~~CACHE~~~~~~~</h4><textarea cols=\"201\" rows=\"4\">"
					  + btree.cache + "</textarea>";

				// update any changes to persistant btree per the request
				save();
				reply += "<br><h3><font color=\"white\">" + btree.count + "</font> words in the dictionary btree.</h3></font>\n";
				reply += "<br><h3><font color=\"white\">" + btree.height + "</font> height of the tree.</h3></font>\n";

					int len = reply.length();
					
					out.println("HTTP/1.0 200 OK");
					out.println("Content-Length: " + len);
					out.println("Content-Type: text/html\n");
					out.println(reply);
						
					out.close();
					in.close();
					client.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
    }

	
	// saves changes from user to persistant data on server
    void save() {
		btree.cache.dump();
		header = new Header(flraf, btree.count, BTree.order, btree.blockAmount, btree.cache.emptyBlocks, btree.height);
		header.setRoot(btree.root);
		try { // try updating header file serialized data
			fos = new FileOutputStream(headerFile);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(header);
			oos.close();
		} catch(Exception e){
			System.out.println(e + " Header file unfortunately could not be saved.");
		}

		try { // try updating btree serialized data
			fos = new FileOutputStream(btreeFile);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(btree);
			oos.close();
		} catch(Exception e){
			System.out.println(e + " Tree file unfortunately could not be saved.");
		}
    }

	public static void main(String[] args) {
		new SimpleService();
    }
}