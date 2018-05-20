package models;

import java.io.Serializable;
import java.util.Arrays;

import utilities.CONTEST_TYPE;

public class Contest implements Serializable {
	public static class Submission implements Serializable {
		public int rejectedCount;
		public int points;

		public Submission(int rejectedCount, int points) {
			this.rejectedCount = rejectedCount;
			this.points = points;
		}

		@Override
		public String toString() {
			return "Submission [rejectedCount=" + rejectedCount + ", points=" + points + "]";
		}

	}

	public static class Row implements Serializable {
		public int points, penalty, rank;
		public String handle;
		public Submission[] submissions;

		public Row(int points, int penalty, int rank, String handle, Submission[] submissions) {
			this.points = points;
			this.penalty = penalty;
			this.rank = rank;
			this.handle = handle;
			this.submissions = submissions;
		}

		@Override
		public String toString() {
			return "Row [points=" + points + ", penalty=" + penalty + ", rank=" + rank + ", handle=" + handle
					+ ", submissions=" + Arrays.toString(submissions) + "]";
		}

	}

	private CONTEST_TYPE type;
	private Row[] rows;

	public Contest(CONTEST_TYPE type, Row[] rows) {
		this.type = type;
		this.rows = rows;
	}

	public CONTEST_TYPE getType() {
		return type;
	}

	public Row[] getRows() {
		return rows;
	}

	@Override
	public String toString() {
		return "Contest [type=" + type + ", rows=" + Arrays.toString(rows) + "]";
	}

}
