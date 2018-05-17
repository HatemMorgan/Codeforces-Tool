package models;

public class Submission {
	private long creationTime;
	private String problemCode;
	private int solvedCount;
	private String[] tags;

	public long getCreationTime() {
		return creationTime;
	}

	public void setProblemCode(String problemCode) {
		this.problemCode = problemCode;
	}

	public void setSolvedCount(int solvedCount) {
		this.solvedCount = solvedCount;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}
}
