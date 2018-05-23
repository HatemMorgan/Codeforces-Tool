package utilities;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.PriorityQueue;

import models.User;
import models.User.Submission;

public class Task3 {
	static ArrayList<User> users;

	private static class Pair {
		int hightRange, lowRange;

		public Pair(int lowRange, int hightRange) {
			this.hightRange = hightRange;
			this.lowRange = lowRange;
		}
	}

	private static class UserActivity implements Comparable<UserActivity> {
		String handle;
		int activity;

		public UserActivity(String handle, int activity) {
			this.handle = handle;
			this.activity = activity;
		}

		@Override
		public String toString() {
			return "UserActivity [handle=" + handle + ", activity=" + activity + "]";
		}

		@Override
		public int compareTo(UserActivity o) {
			if (o.activity != this.activity)
				return o.activity - this.activity;
			return this.handle.compareToIgnoreCase(o.handle);
		}
	}

	public static void prepare() throws FileNotFoundException {
		users = (ArrayList<User>) Seed.deserialize("./data/filtered/users_activity");
	}

	public static ArrayList<String> getActiveUsers(int t1, int t2, int rLo, int rHi, int cnt) {
		Pair usersRange = ratingBSearch(rLo, rHi);

		User currUser;
		PriorityQueue<UserActivity> queue = new PriorityQueue<UserActivity>();
		for (int i = usersRange.lowRange; i < usersRange.hightRange; ++i) {
			currUser = users.get(i);
			Pair range = timeBSearch(currUser.getSubmissions(), t1, t2);

			queue.add(new UserActivity(currUser.getHandle(), range.hightRange - range.lowRange + 1));
		}

		ArrayList<String> res = new ArrayList<String>();
		for (int i = 0; i < cnt && !queue.isEmpty(); ++i)
			res.add(queue.poll().handle);

		return res;
	}

	private static Pair ratingBSearch(int rLo, int rHi) {
		int low = 0;
		int high = users.size() - 1;
		int highAns = -1;
		int lowAns = -1;

		// get low idx of target range
		while (low <= high) {
			int mid = (high + low) >> 1;
			if (users.get(mid).getRating() < rLo)
				low = mid + 1;
			else {
				lowAns = mid;
				high = mid - 1;
			}
		}

		low = 0;
		high = users.size() - 1;

		// get high idx of target range
		while (low <= high) {
			int mid = (high + low) >> 1;
			if (users.get(mid).getRating() > rHi)
				high = mid - 1;
			else {
				highAns = mid;
				low = mid + 1;
			}
		}
		return new Pair(lowAns, highAns);
	}

	private static Pair timeBSearch(ArrayList<Submission> submissions, int t1, int t2) {
		int low = 0;
		int high = submissions.size() - 1;
		int highAns = -1;
		int lowAns = -1;

		// get low idx of target range
		while (low <= high) {
			int mid = (high + low) >> 1;
			if (submissions.get(mid).creationTime > t2)
				low = mid + 1;
			else {
				lowAns = mid;
				high = mid - 1;
			}
		}

		low = 0;
		high = submissions.size() - 1;

		// get high idx of target range
		while (low <= high) {
			int mid = (high + low) >> 1;
			if (submissions.get(mid).creationTime < t1)
				high = mid - 1;
			else {
				highAns = mid;
				low = mid + 1;
			}
		}
		return new Pair(lowAns, highAns);
	}

}
