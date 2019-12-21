

import java.util.ArrayList;

//an object which contains a string wordName and an ArrayList with value of the word in all documents.
//This object is used for indexObjectArray, weightedtfidfObjectArray, normalisedWeightedtfidfObjectArray as all of them have the same structure.
public class IndexObject {
	private String wordName;
	private ArrayList<Double> termFrequencyArray;

	public IndexObject(String wordName, ArrayList<Double> termFrequencyArray) {
		super();
		this.wordName = wordName;
		this.termFrequencyArray = termFrequencyArray;
	}

	public String getWordName() {
		return wordName;
	}

	public void setWordName(String wordName) {
		this.wordName = wordName;
	}

	public ArrayList<Double> getTermFrequencyArray() {
		return termFrequencyArray;
	}

	public void setTermFrequencyArray(ArrayList<Double> termFrequencyArray) {
		this.termFrequencyArray = termFrequencyArray;
	}

}
