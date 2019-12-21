

import java.util.ArrayList;

//an object for the array of individual documents.it contains a string to store the word and an integer to store the occurence of the word.
public class DictionaryObject {
	private ArrayList<String> dictionary;
	private ArrayList<Integer> count;

	public DictionaryObject(ArrayList<String> dictionary, ArrayList<Integer> count) {
		super();
		this.dictionary = dictionary;
		this.count = count;
	}

	public int getSize() {
		return dictionary.size();
	}

	public ArrayList<String> getDictionary() {
		return dictionary;
	}

	public void setDictionary(ArrayList<String> dictionary) {
		this.dictionary = dictionary;
	}

	public ArrayList<Integer> getCount() {
		return count;
	}

	public void setCount(ArrayList<Integer> count) {
		this.count = count;
	}
}
