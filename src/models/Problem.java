package models;

import java.io.Serializable;
import java.util.TreeSet;

public class Problem implements Serializable {
	public String problemCode;
	public int solvedCount;
	public TreeSet<String> tags;

	public Problem(String problemCode, int solvedCount, TreeSet<String> tags) {
		this.problemCode = problemCode;
		this.solvedCount = solvedCount;
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "Problem [problemCode=" + problemCode + ", solvedCount=" + solvedCount + ", tags=" + tags + "]";
	}

}
