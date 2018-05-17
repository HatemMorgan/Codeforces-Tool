package models;

import java.util.ArrayList;

public class User {
	private String handle;
	private int rating;
	private ArrayList<Submission> submissions;
	
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
}
