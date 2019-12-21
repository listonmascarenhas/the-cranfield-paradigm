

//object used for part 2. it contains the document id and its similarity score.
public class OutputQueryObject {
	private int documentId;
	private double similarityScore;

	public OutputQueryObject(int documentId, double similarityScore) {
		super();
		this.documentId = documentId;
		this.similarityScore = similarityScore;
	}

	public int getDocumentId() {
		return documentId;
	}

	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}

	public double getSimilarityScore() {
		return similarityScore;
	}

	public void setSimilarityScore(double similarityScore) {
		this.similarityScore = similarityScore;
	}

}
