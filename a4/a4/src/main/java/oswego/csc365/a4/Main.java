package oswego.csc365.a4;

import java.net.*;
import java.io.*;
import java.util.*;

public class Main {
    static final int PORT = 5000;
	
	static final String start = "GET /";
	static final String end = " HTTP/1.1";
	
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
	  		System.out.println("Waiting for indices to be searched in the API\n");
			while (true) {
				Socket client = serverSocket.accept();
			
				PrintWriter out = new PrintWriter(client.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
					
				FLRAF flraf = new FLRAF(32);
				ClassLoader classloader = Thread.currentThread().getContextClassLoader();
				InputStream is = classloader.getResourceAsStream("words.txt");
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				String line;
				int blockNumber = 0;

				// serialize words and add padding when necessary
				while ((line = reader.readLine()) != null) {
					byte[] pre = line.getBytes();
					byte[] post = new byte[32];
					int i = 0;
					while(i < pre.length) {
						post[i] = pre[i];
						i++;
					}

					while(i != post.length) {
						post[i] = '-';
						i++;
					}

					flraf.write(post, blockNumber);
					blockNumber++;
				}

				is.close();

				// get query param
				String cmd = in.readLine();
				if(cmd.contains(start) && cmd.contains(end)) {
					cmd = cmd.replace(start, "");
					cmd = cmd.replace(end, "");
				}

				try {
					if(cmd == null)
						cmd = "Enter a valid index.";
					if(cmd.contains("-")) { // check if range inputted
						String first = cmd.substring(0, cmd.indexOf("-")); // find start
						String last = cmd.substring(cmd.indexOf("-") + 1, cmd.length()); // find end
						if(first != null && last != null) {  // if both exist
							int index1 = Integer.parseInt(first);
							int index2 = Integer.parseInt(last);

							int i = index1;
							while(i < index2) { // try to find word in dicitonary
								String word = new String(flraf.read(i));
								if(i == index1) // if last index simply append
									cmd = deserialize(word);
								else
									cmd += ", " + deserialize(word); // else append with comma prefix
								i++;
							}
						} else  // cannot find word, invalid range
							cmd = "Invalid Range.";
					} else { // simply try to find word at address
						int address = Integer.parseInt(cmd);
						String word = new String(flraf.read(address));
						cmd = deserialize(word);
					}
				} catch(NumberFormatException e) {
					cmd = "Invalid Index.";
				}
				
				String reply = "<html>\n" + "<head><title>CSC 365 Webster Dictionary</title></head>\n" 
										  + "<body><h1>FLRAF Assignment</h1></body>\n" 
										  + "Got request:<br>\n " + cmd + "\n</html>\n";

				out.println("HTTP/1.0 200 OK");
				out.println("Content-Length: " + reply.length());
				out.println("Content-Type: text/html\n");
				out.println(reply);

				out.close();
				in.close();
				client.close();
			}
		}
		catch (IOException e) {
			System.out.println(e);
			System.exit(-1);
		} 
	}

	// deserializes word essentially
	public static String deserialize(String word) {
		int offset = word.indexOf("-");
		String ret;
		if(offset != -1)
			ret = word.substring(0, offset);
		else
			ret = word;
		return ret;
	}
}