package models;

import java.io.Serializable;
import java.util.TreeSet;

public class Problem implements Serializable, Comparable<Problem> {
	public String problemCode;
	public int solvedCount;
	public TreeSet<String> tags;
	public int points;

	public Problem(String problemCode, int solvedCount, TreeSet<String> tags, int points) {
		this.problemCode = problemCode;
		this.solvedCount = solvedCount;
		this.tags = tags;
		this.points = points;
	}

	@Override
	public String toString() {
		return "Problem [problemCode=" + problemCode + ", solvedCount=" + solvedCount + ", tags=" + tags + ", points="
				+ points + "]";
	}

	@Override
	public int compareTo(Problem o) {
		return problemCode.compareTo(o.problemCode);
	}

}
