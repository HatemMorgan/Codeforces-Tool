package models;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
	public static class Submission implements Serializable {
		public long creationTime;

		public Submission(long creationTime) {
			this.creationTime = creationTime;
		}

		@Override
		public String toString() {
			return "Submission [creationTime=" + creationTime + "]";
		}

	}

	private String handle;
	private int rating;
	private ArrayList<Submission> submissions;

	public User(String handle, int rating, ArrayList<Submission> submissions) {
		this.handle = handle;
		this.rating = rating;
		this.submissions = submissions;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public ArrayList<Submission> getSubmissions() {
		return submissions;
	}

	public void setSubmissions(ArrayList<Submission> submissions) {
		this.submissions = submissions;
	}

	@Override
	public String toString() {
		return "User [handle=" + handle + ", rating=" + rating + ", submissions=" + submissions + "]";
	}

}
