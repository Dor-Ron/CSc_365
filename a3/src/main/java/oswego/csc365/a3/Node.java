package oswego.csc365.a3;

import java.io.Serializable;
import java.util.Arrays;


// Node class to be used for the BTree
public class Node implements Serializable {
	public String[] words;
	public int[] children;
	public int address; // node address in serialized file
	
	// constructor if address not provided
	Node() {
		words = new String[BTree.order - 1];
		children = new int[BTree.order];
		address = -1;
		Arrays.fill(children, -1);
	}

	// constructor for when address provided
	Node(int _address) {
		words = new String[BTree.order - 1];
		children = new int[BTree.order];
		address = _address;
		Arrays.fill(children, -1);
	}

	// adds string param as key of node
	boolean add(String word) {
		if(word == null || word.equals("")) return false;
			
		for(int i = 0; i < words.length; i++) {
			// next available position, place word there
			if(words[i] == null) { 
				words[i] = word;
				return true;
			} 

			// make room for word and append words
			if(word.compareTo(words[i]) < 0) {
				shiftWordsR(i);
				shiftChildrenR(i);
				words[i] = word;
				return true;
			}
		}
		return false;
	}

	// deletes string param from words key array
	boolean delete(String word) {
		if(word != null && this.contains(word)) {  // word in node
			int index = getIndex(word);
			words[index] = null;  // delete it at proper index
			shiftWordsL(index);  // shift words and children to compensate
			shiftChildrenL(index);
			return true;
		}
		return false;
	}	


	// shifts words len(words) - < param > spaces right
	void shiftWordsR(int subtrahend) {
		for(int i = words.length; i >= subtrahend; i--) {
			if((i + 1) < words.length)
				words[i + 1] = words[i];
		}
	}

	// shifts words left
	void shiftWordsL(int startIdx) {
		for(int i = startIdx; i < words.length; i++) {
			if((i + 1) < words.length)
				words[i] = words[i + 1];
			else
				words[i] = null;
		}
	}


	// shifts words len(children) - < param > spaces right
	void shiftChildrenR(int subtrahend) {
		for(int i = children.length; i >= subtrahend; i--) {
			if((i+1) < children.length)
			children[i + 1] = children[i];
		}
	}

	// shifts children left
	void shiftChildrenL(int startIdx) {
		for(int i = startIdx; i < children.length; i++) {
			if((i + 1) < children.length)
				children[i] = children[i + 1];
			else 
				children[i] = -1;
		}
	}


	// finds index of word param in words
	int getIndex(String word) {	
		for(int i = 0; i < words.length; i++) {
			if(words[i].equalsIgnoreCase(word))
				return i;
		}
		return -1;
	}

	// finds index of child in children array
	int getIndex(int address) {
		for(int i = 0; i < children.length; i++) {
			if(children[i] == address)
				return i;
		}
		return -1;
	}


	// gets node address in serialized data 
	int getAddress() {
		return address;
	}
	

	// sets node address in serialized data
	void setAddress(int _address) {
		address = _address;
	}

	// returns largest lexographically sorted word key
	String largestWord() {
		for(int i = words.length - 1; i >= 0; i--) {
			if(words[i] != null)
				return words[i];
		}
		return null;
	}
	
	// Finds appropriate child node for word to go in
	int getChild(String word) {
		int i = 0;
		// check last index
		if(words[words.length - 1] != null && word.compareTo(words[words.length - 1]) > 0)
			return children[words.length];

		// if not at end, find position based off of other words
		while(words[i] != null && i < words.length) {
			if(word.compareTo(words[i]) > 0)
				i++;
			else // if key is greater than word, return left child
				return children[i];
		}
		return children[i];
	}

	// returns amount of word words in node
	int size() {
		int count = 0;
		while(words[count] != null) {
			count++;
			if(count == BTree.order - 1) // if words == BTree order --> break
				return count;
		}
		return count;
	}

	// true if node contains word else false
	boolean contains(String word) {
		for(int i = 0; i < words.length; i++) {
			if(word.equals(words[i]))
				return true;
		}
		return false;
	}


	// true if node is leaf otherwise false
	boolean isLeaf() {
		for(int i = 0; i < children.length; i++) {
			if(children[i] != -1) // if any child position is occupied
				return false;
		}
		return true;
	}


	// true if node is full with words false otherwise
	boolean isFull() {
		if(size() == words.length)
			return true;
		return false;
	}

	// true if node doesnt contain a single key false otherwise
	boolean isEmpty() {
		if(size() == 0)
			return true;
		return false;
	}

	// Stringify node for demoing purposes
	public String toString() {
		String result = "";

		// get all words
		for(int i = 0; i < words.length; i++)
			result += " " + words[i];

		// get all children
		for(int i = 0; i < children.length; i++)
			result += " " + children[i];
				
		return result;
	}
}
	