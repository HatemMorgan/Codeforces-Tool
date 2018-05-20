package models;

import java.io.Serializable;

public class EnteredContest implements Serializable {

	private int contest_id, rank;

	public EnteredContest(int contest_id, int rank) {
		this.contest_id = contest_id;
		this.rank = rank;
	}

	public int getContest_id() {
		return contest_id;
	}

	public int getRank() {
		return rank;
	}

	@Override
	public String toString() {
		return "EnteredContest [contest_id=" + contest_id + ", rank=" + rank + "]";
	}

}
