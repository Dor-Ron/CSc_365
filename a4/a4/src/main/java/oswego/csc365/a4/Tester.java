package oswego.csc365.a4;

import java.util.*;
import java.io.*;
import java.lang.*;

public class Tester {
	public static void main(String[] args) {
		try {
			FLRAF flraf = new FLRAF(32);
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream is = classloader.getResourceAsStream("words.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			int index = 0;

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
				flraf.write(post, index);
				index++;
			}
		}
		catch(IOException io) {
			System.out.println("File does not exist, or is not within the correct pathway.");
		}		
	}
}