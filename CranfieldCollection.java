
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

// Name:Liston Lucio Mascarenhas
//Student id:2970414
public class CranfieldCollection{
	static Scanner sc = null;
	static String document = "";
	static String mainDocumentSplit[], individualWordSplit[];
	static Stemmer stemmer;
	static DictionaryObject dictionaryObject;
	static StringBuilder stringBuilder;
	static IndexObject indexObject;
	volatile static boolean runPart2 = true;
	static OutputQueryObject outputQueryObject;
	static ArrayList<String> individualQueryArray;
	static String[] querySplit, authorNameSplit, bibliographySplit;
	static ArrayList<String> wordsInSingleDocumentArray, stopWordArray, allWordsArray;
	static ArrayList<Integer> wordCountArray;
	static ArrayList<IndexObject> indexObjectArray, weightedtfidfObjectArray, normalisedWeightedtfidfObjectArray;
	static ArrayList<Double> idfArray, tfidfArray, indexListArray, lengthOfEachDocumentArray, queryListArray;
	static ArrayList<DictionaryObject> dictionaryObjectArray;
	static ArrayList<OutputQueryObject> outputQueryArray;

	public static void main(String args[]) {
		Runnable part1 = new Part1();
		Runnable part2 = new Part2();
		Thread part2Thread;
		Thread part1Thread = new Thread(part1);

		try {
			part1Thread.start();
			part1Thread.join();
			while (runPart2) {
				part2Thread = new Thread(part2);
				part2Thread.start();
				part2Thread.join();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static class Part1 implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			initiate();
		}

		public void initiate() {
			System.out.println("Creating dictionary...");
			initialiseValues();
			preProcessing();
			creatingDictionary();
			calculateIdf();
			calculateWeightedtfidfObjectArray();
			calculateLength();
			calculateNormalisedWeightedtfidfObjectArray();
			exportLengthDocument();
		}
	}

	// method that initializes values and also calls a method to retrieve the
	// document
	public static void initialiseValues() {
		// TODO Auto-generated method stub
		allWordsArray = new ArrayList<>();
		stopWordArray = new ArrayList<>();
		dictionaryObjectArray = new ArrayList<>();
		idfArray = new ArrayList<>();
		tfidfArray = new ArrayList<>();
		stringBuilder = new StringBuilder("");
		stopWordArray = stopWordsDocument();
		indexObjectArray = new ArrayList<>();
		weightedtfidfObjectArray = new ArrayList<>();
		lengthOfEachDocumentArray = new ArrayList<>();
		normalisedWeightedtfidfObjectArray = new ArrayList<>();

		retrieveDocumentBuffer();
	}

	// method for preprocessing the words to store in the dictionary,stop word
	// removal,stemming and getting an array of all the words are done in this
	// method
	public static void preProcessing() {
		// TODO Auto-generated method stub
		int index = 0;
		mainDocumentSplit = document.split(".I"); // splitting the document at the index .I
		String authorName = "", bibliography = "";
		int authorIndex = 0, bibliographyIndex = 0, wordsIndex = 0;
		String individualString = "";
		int counter;
		for (String s : mainDocumentSplit) { // for each loop for all terms after the split

			if (index != 0) { // the initial term is blank so ignoring it altogether
				authorIndex = s.indexOf(".A"); // getting the position of .A
				bibliographyIndex = s.indexOf(".B"); // getting the position of .B
				wordsIndex = s.indexOf(".W");// getting the position of .W
				authorName = s.substring(authorIndex + 2, bibliographyIndex).trim();// getting the author name by using
																					// substring from after .A to before
																					// .B
				bibliography = s.substring(bibliographyIndex + 2, wordsIndex).trim();// getting the bibliography by
																						// using substring from after .B
																						// to before .W

			}
			individualString = s.replaceAll("\\s.T\\s", " ").replaceAll("\\s.W\\s", " ").replaceAll("\\s.A\\s", " ")
					.replaceAll("\\s.B\\s", " ").replaceAll("[^a-zA-Z0-9,.]", " ").toLowerCase(); // replacing all
																									// .T,.W,.A,.B and
																									// all special
																									// characters except
																									// , and .

			individualWordSplit = individualString.split(" "); // splitting the individual words in every document
			wordsInSingleDocumentArray = new ArrayList<>();
			wordCountArray = new ArrayList<>();

			for (String string : individualWordSplit) { // for each loop
				if (isNumeric(string)) { // checking if the string is numeric,if its not remove all special characters
											// otherwise just remove . and , from the start and end of the number
					if (string.startsWith(".") || string.endsWith(".") || string.startsWith(",")
							|| string.endsWith(",")) {
						if (string.startsWith(".") || string.startsWith(",")) {
							string = string.substring(1);
						}
						if (string.endsWith(".") || string.startsWith(",")) {
							string = string.substring(0, string.length() - 1);
						}
					}
				} else {
					string = string.replaceAll("[^a-zA-Z0-9]", "");
				}
				// more preprocessing steps,allow only words which aren't stopwords or words
				// which aren't empty,just a full stop or comma
				if (!checkIfStopWord(string, stopWordArray) && !string.equals("") && !string.equals(".")
						&& !string.equals(",")) {
					// don't porter stem if it is the first word of a document AND it is a
					// number,this step is done to remove the index no of the document.for eg, the 1
					// after .I
					if (!(wordsInSingleDocumentArray.size() == 0 && string.matches(".*\\d+.*"))) {

						char[] porterStemChar = string.toCharArray();
						stemmer = new Stemmer();
//						for (int i = 0; i < porterStemChar.length; i++) {
//							stemmer.add(porterStemChar[i]);
//						}
						stemmer.add(porterStemChar, porterStemChar.length);
						stemmer.stem();

						counter = 1; // counter to check if a stemmed word is repeated in the // individual
										// document,initially 1 (every term will occur at least once)
						// condition to increment counter if the word exists.
						if (wordsInSingleDocumentArray.size() > 0) {
							counter = counter
									+ checkIfInArray(stemmer.toString(), wordsInSingleDocumentArray, wordCountArray);
						}
						// creating a dictionary array of all stemmed words in the entire corpus,if word
						// doesnt exist,add it to the array
						if (!allWordsArray.contains(stemmer.toString())) {
							allWordsArray.add(stemmer.toString());
						}
						wordsInSingleDocumentArray.add(stemmer.toString()); // add unique words of individual documents
																			// at given index to the array.
						wordCountArray.add(counter); // add counter of the word to the array
					}
				}
			}
			allWordsArray.add(authorName); // add the authors name without preprocessing to the array of all words
			allWordsArray.add(bibliography); // add the bibliography without preprocessing to the array of all words
			wordsInSingleDocumentArray.add(authorName); // add authors name without preprocessing to the array of
														// individual documents
			wordCountArray.add(1); // add 1 to word count array
			wordsInSingleDocumentArray.add(bibliography);// add a counter
			wordCountArray.add(1); // add 1 to word count array
			authorNameSplit = authorName.replaceAll("[^a-zA-Z0-9]", " ").trim().split("\\s+"); // adding individual
																								// terms
			// of authors name
			for (String author : authorNameSplit) {
				if (!author.equals("//s+") || !author.contains("")) {
					if (author.length() != 1) {
						allWordsArray.add(author);
						wordsInSingleDocumentArray.add(author);
						wordCountArray.add(1);
//					System.out.println(index + ":" + author);
					}
				}
			}

			dictionaryObject = new DictionaryObject(wordsInSingleDocumentArray, wordCountArray);
			dictionaryObjectArray.add(dictionaryObject); // This array contains all individual documents(words and
															// occurrence).
			index++;
		}

		dictionaryObjectArray.remove(0); // removing the first element of the array list(the first element becomes blank
											// after splitting)
	}

	// check if the word is numeric
	public static boolean isNumeric(String strNum) {
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException | NullPointerException nfe) {
			return false;
		}
		return true;
	}

	// after preprocessing the next method is creatingDictionary.In this method,we
	// map all the occurrences of a given word to all the documents(if the word
	// doesnt exist in the document,the occurrence is stored as 0)
	public static void creatingDictionary() {
		// TODO Auto-generated method stub
		allWordsArray.sort(String::compareToIgnoreCase);// sort the array in ascending order
		String wordName = "";
		for (int i = 0; i < allWordsArray.size(); i++) {
			wordName = allWordsArray.get(i);
			indexListArray = new ArrayList<>();
			for (int j = 0; j < dictionaryObjectArray.size(); j++) {
				// condition to check if an individual document contains the word.if it does add
				// the occurrence value to the array,else add 0
				if (dictionaryObjectArray.get(j).getDictionary().contains(wordName)) {
					int index = dictionaryObjectArray.get(j).getDictionary().indexOf(wordName);// get the index of the
																								// word
					indexListArray.add((double) dictionaryObjectArray.get(j).getCount().get(index));// add the value of
																									// the term at the
																									// given index
				} else {
					indexListArray.add((double) 0);
				}
			}
			indexObject = new IndexObject(wordName, indexListArray); // this object contains a string and an
																		// ArrayList<Double>. the string is the name of
																		// the word and the ArrayList contains its
																		// occurrence across all documents
			indexObjectArray.add(indexObject); // add it to the array.This array now contains all the words in the
												// entire corpus and their occurrence.
		}
		dictionaryObjectArray.removeAll(dictionaryObjectArray); //remove array to free memory
	}

	// the next method after creating the dictionary is to calculateIdf. it
	// calculates the idf of each word and stores it in an array.
	public static void calculateIdf() {
		// TODO Auto-generated method stub
		int size = indexObjectArray.get(0).getTermFrequencyArray().size();// the no of documents
		int counter;
		double idf;
		for (int i = 0; i < indexObjectArray.size(); i++) {

			counter = 0;
			for (int j = 0; j < indexObjectArray.get(i).getTermFrequencyArray().size(); j++) {
				// if the value at term is not 0,increment the counter(get the no of documents
				// the word occurred in)
				if (indexObjectArray.get(i).getTermFrequencyArray().get(j) != 0) {
					counter++;
				}
			}

			idf = Math.log10(size / counter);// formula idf=log(N/n)
			idfArray.add(idf); // add it to the array
		}
	}

	// after calculating idf,we calculate tfidf(multiplying idf with occurrence of
	// term in a document)
	public static void calculateWeightedtfidfObjectArray() {
		// TODO Auto-generated method stub
		String wordName;
		for (int i = 0; i < indexObjectArray.size(); i++) {
			wordName = indexObjectArray.get(i).getWordName();
			indexListArray = new ArrayList<>();
			// multiply idf with occurrence of term in a document
			for (int j = 0; j < indexObjectArray.get(i).getTermFrequencyArray().size(); j++) {
				indexListArray.add(indexObjectArray.get(i).getTermFrequencyArray().get(j) * idfArray.get(j));
			}
			indexObject = new IndexObject(wordName, indexListArray);
			weightedtfidfObjectArray.add(indexObject); // store the word name and its tfidf accross all documents
		}
		idfArray.removeAll(idfArray); //remove array to free memory
	}

	// calculating the tfidf length of each document in the corpus(root of the sum
	// of the squares)
	public static void calculateLength() {
		// TODO Auto-generated method stub
		int j = 0;
		double length, n;
		int k;
		while (j < weightedtfidfObjectArray.get(j).getTermFrequencyArray().size()) {
			length = 0;
			k = 0;
			while (k < weightedtfidfObjectArray.size()) {
				n = weightedtfidfObjectArray.get(k).getTermFrequencyArray().get(j);
				length = length + (n * n);// adding the sum of the squares
				k++;

			}

			lengthOfEachDocumentArray.add(Math.sqrt(length)); // adding the square root value of sum of the squares of
																// each document to the array
			j++;
		}
	}

	// after calculating tfidf and the length,we normalise it by dividing each tfidf
	// by the length.
	public static void calculateNormalisedWeightedtfidfObjectArray() {
		// TODO Auto-generated method stub
		int j = 0, k;
		double length;
		double normalisedLength;
		normalisedWeightedtfidfObjectArray = weightedtfidfObjectArray; // initially setting the weightedtfidfObjectArray
																		// to normalisedWeightedtfidfObjectArray(instead
																		// of adding elements to the array,just change
																		// the tfidf value)
		while (j < weightedtfidfObjectArray.get(j).getTermFrequencyArray().size()) {
			k = 0;
			length = lengthOfEachDocumentArray.get(j); // since values of lengthOfEachDocumentArray will
														// correspond to weightedtfidfObjectArray,we can get its
														// values from the same loop.
			while (k < weightedtfidfObjectArray.size()) {

				if (weightedtfidfObjectArray.get(k).getTermFrequencyArray().get(j) != 0) { // if the tfidf value is not
																							// equal to 0,divide by
																							// length(when dividing by
																							// 0, it gave NaN values)
					normalisedLength = weightedtfidfObjectArray.get(k).getTermFrequencyArray().get(j) / length;
				} else {
					normalisedLength = 0;
				}
				normalisedWeightedtfidfObjectArray.get(k).getTermFrequencyArray().set(j, normalisedLength); // set the
																											// normalised
																											// value at
																											// given
																											// position.
				k++;
			}
			j++;
		}
	}

	// method to complete part A.exporting the length of each document with its
	// document number
	public static void exportLengthDocument() {
		// TODO Auto-generated method stub
		try {
			PrintWriter out = new PrintWriter("C:\\Assignment_Files\\Length of document.txt");
			for (int i = 0; i < lengthOfEachDocumentArray.size(); i++) {
				out.println(" " + (i + 1) + ":" + lengthOfEachDocumentArray.get(i));
			}
			System.out.println("Length exported to 'Length of document.txt'");
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// method created to add the occurrences of a term in an individual document,if
	// term exists,send back its occurrence and remove the original term from the
	// arrayList(the term occurence counter is added to the new occurrence while
	// deleting the original one) .otherwise return 0.
	public static int checkIfInArray(String string, ArrayList<String> wordsInSingleDocumentArray,
			ArrayList<Integer> wordCountArray) {
		// TODO Auto-generated method stub
		if (wordsInSingleDocumentArray.contains(string)) {
			int index = wordsInSingleDocumentArray.indexOf(string);
			int count = wordCountArray.get(index);
			wordsInSingleDocumentArray.remove(index);
			wordCountArray.remove(index);
			return count;
		} else {

			return 0;
		}
	}

	// method returns true if the given word is a stop word.compares the term with
	// the terms in an arraylist of pre determined stop words.
	public static boolean checkIfStopWord(String string, ArrayList<String> stopWordArray) {
		// TODO Auto-generated method stub
//		for (int i = 0; i < stopWordArray.size(); i++) {
//			if (string.equals(stopWordArray.get(i))) {
//				return true;
//			}
//		}
		if (stopWordArray.contains(string)) {
			return true;
		}
		return false;
	}

	// method to read a text file of the corpus and store it in a string.
	public static void retrieveDocumentBuffer() {
		try {
			FileReader fileReader = new FileReader("C:\\Assignment_Files\\cranfield_collection.txt");

			try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					stringBuilder.append(line);
					stringBuilder.append(" ");
				}
				document = stringBuilder.toString().trim();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// method to read a text file of the stop words.
	@SuppressWarnings("resource")
	public static ArrayList<String> stopWordsDocument() {
		File file = new File("C:\\Assignment_Files\\stopwords.txt");
		ArrayList<String> arrayList = new ArrayList<>();
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				arrayList.add(scanner.nextLine());
			}
			arrayList.add("."); // adding . to the stop word arraylist for precautionary measure
			arrayList.add("\n");// adding \n to the stop word arraylist for precautionary measure
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return arrayList;
	}

	// part 2 deals with query handling,a user inputs the query and the output is
	// exported to a unique text file
	static class Part2 implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			queryHandling();
		}

		public static void queryHandling() {
			Scanner sc = new Scanner(System.in);
//			String query = "free the flow of my name";
			System.out.println("Type query here");
			String query = sc.nextLine();
			individualQueryArray = new ArrayList<>(); // array of individual queries
			outputQueryArray = new ArrayList<>();
			if (query.equals("-1")) {
				runPart2 = false;
			} else {
				if (query.startsWith(".A") || query.startsWith(".B")) {
					individualQueryArray.add(query.substring(2));
					
				} else {
					querySplit = query.split("\\s+");// splitting the query at a white space,querySplit is a stringarray
					splitQuery(); // method to add split words into the individualQueryArray
					// checking if individual word in query exists in array,if it does get its
					// similarity score and add it up with other queries similarity score in each
					// document.Output the first 100 most similar documents.
				}
				for (int i = 0; i < individualQueryArray.size(); i++) {
					// since the values in allWordsArray corresponds to the values in
					// normalisedWeightedtfidfObjectArray,we can use it to check if the query words
					// exists in the array
					if (allWordsArray.contains(individualQueryArray.get(i))) {
						int position = allWordsArray.indexOf(individualQueryArray.get(i));// getting the position of the
																							// word in the arrayList.
						for (int j = 0; j < normalisedWeightedtfidfObjectArray.get(position).getTermFrequencyArray()
								.size(); j++) {

							// condition to check if the normalised tfidf value is not 0
							if (normalisedWeightedtfidfObjectArray.get(position).getTermFrequencyArray().get(j) != 0) {

								// if not the first element.
								if (outputQueryArray.size() != 0) {

									// check if element already exists in the array
									if (containsElement(outputQueryArray, j)) {

										for (int k = 0; k < outputQueryArray.size(); k++) {
											if (outputQueryArray.get(k).getDocumentId() == j) {

												outputQueryArray.get(k)
														.setSimilarityScore(outputQueryArray.get(k).getSimilarityScore()
																+ normalisedWeightedtfidfObjectArray.get(position)
																		.getTermFrequencyArray().get(j)); // if term
																											// exists,add
																											// similarity
																											// score of
																											// term
																											// with the
																											// normalised
																											// tdfidf
																											// value

												break;
											}
										}
									}
									// if it doesnt exist,add normalised tfidf value to the array with the
									// position(document id)
									else {
										outputQueryObject = new OutputQueryObject(j, normalisedWeightedtfidfObjectArray
												.get(position).getTermFrequencyArray().get(j));
										outputQueryArray.add(outputQueryObject);

									}

								}
								// if array size is 0,add normalised tfidf value to the array with the
								// position(document id)
								else {
									outputQueryObject = new OutputQueryObject(j, normalisedWeightedtfidfObjectArray
											.get(position).getTermFrequencyArray().get(j));
									outputQueryArray.add(outputQueryObject);

								}
							}
						}
					}
				}
				// sorting the arraylist in ascending order
				Collections.sort(outputQueryArray,
						(o1, o2) -> Double.compare(o1.getSimilarityScore(), o2.getSimilarityScore()));
				Collections.reverse(outputQueryArray);// reversing the arraylist to get descending order

				exportOutput();
			}
		}

		private static void exportOutput() {
			// TODO Auto-generated method stub
			File file = new File("C:\\Assignment_Files\\Query Output1" + ".txt");

			int increase = 1;
			while (file.exists()) {
				increase++;
				file = new File("C:\\Assignment_Files\\Query Output" + increase + ".txt");

			}

			try {
				PrintWriter out = new PrintWriter(file);

				for (int i = 0; i < outputQueryArray.size(); i++) {
					if (i < 100) {
						out.println(i + 1 + " " + (outputQueryArray.get(i).getDocumentId() + 1) + " "
								+ outputQueryArray.get(i).getSimilarityScore());

					}
				}
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("Output stored in Query Output" + increase + ".txt");
		}

	}

	// method to check if a list contains an element
	private static boolean containsElement(ArrayList<OutputQueryObject> list, int j) {
		// TODO Auto-generated method stub
		return list.stream().filter(x -> x.getDocumentId() == j).findFirst().isPresent();
	}

	// method for splitting query into individual words
	private static void splitQuery() {
		// TODO Auto-generated method stub
		Stemmer stemmer = null;

		for (String s : querySplit) {
			s = s.replaceAll("[^a-zA-Z0-9.,]", "");
			if (!checkIfStopWord(s, stopWordArray)) {
				if (s.startsWith(".") || s.endsWith(".") || s.startsWith(",") || s.endsWith(",")) { // remove full stops
																									// or commas at the
																									// start or end of a
																									// word
					if (s.startsWith(".") || s.startsWith(",")) {
						s = s.substring(1);
					}
					if (s.endsWith(".") || s.endsWith(",")) {
						s = s.substring(0, s.length() - 1);
					}
				}
				char[] porterStemChar = s.toCharArray();
				stemmer = new Stemmer();

				stemmer.add(porterStemChar, porterStemChar.length);
				stemmer.stem();
//				System.out.println(stemmer.toString());
				individualQueryArray.add(stemmer.toString().toLowerCase());

			}

		}
	}

}
