package models;

import java.io.Serializable;

public class Solution implements Serializable {
	public long solvedTime;
	public Problem problem;

	public Solution(long solvedTime, Problem problem) {
		this.solvedTime = solvedTime;
		this.problem = problem;
	}

	@Override
	public String toString() {
		return "Solution [solvedTime=" + solvedTime + ", problem=" + problem + "]";
	}

}
