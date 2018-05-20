package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import models.Contest;
import models.Contest.Row;
import models.EnteredContest;
import models.User;

public class Seed {
	public static void seedUsersActivity() throws IOException {
		Path path = Paths.get("data/users");

		// holds all users to be written to disk
		ArrayList<User> users = new ArrayList<User>();

		// iterate over users directory and transform each user to a simplified version
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(path)) {
			JSONParser parser = new JSONParser();
			dirStream.forEach(p -> {
				try {
					FileReader reader = new FileReader(p.toString() + "/rating.json");
					JSONArray contests = (JSONArray) parser.parse(reader);
					reader.close();

					reader = new FileReader(p.toString() + "/status.json");
					JSONArray submissions = (JSONArray) parser.parse(reader);
					reader.close();

					// parse a user
					ArrayList<models.User.Submission> userSubmissions = new ArrayList<models.User.Submission>();

					JSONObject submissionJson;
					models.User.Submission submission;

					for (Object o : submissions) {
						submissionJson = (JSONObject) o;

						if (!submissionJson.get("verdict").equals("OK"))
							continue;

						long creationTime = (long) submissionJson.get("creationTimeSeconds");
						submission = new models.User.Submission(creationTime);

						userSubmissions.add(submission);
					}

					String handle = p.getFileName().toString();
					int rating = 1500;
					if (!contests.isEmpty())
						rating = (int) ((long) ((JSONObject) contests.get(contests.size() - 1)).get("newRating"));

					User user = new User(handle, rating, userSubmissions);
					users.add(user);

				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			// sort in ascending order according to rating
			Collections.sort(users, (x, y) -> {
				return x.getRating() - y.getRating();
			});
		}
		
		serialize(users, "./data/filtered/users_activity");
	}

	public static void seedContests() throws IOException {
		Path path = Paths.get("data/contests");

		// holds all contests to be written to disk
		TreeMap<Integer, Contest> tm = new TreeMap<>();

		// iterate over contests directory
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(path)) {
			JSONParser parser = new JSONParser();
			dirStream.forEach(p -> {
				try {
					FileReader reader = new FileReader(p.toString());
					JSONObject contestJson = (JSONObject) parser.parse(reader);
					reader.close();

					int contest_id = (int) ((long) ((JSONObject) contestJson.get("contest")).get("id"));
					String contest_type = ((String) ((JSONObject) contestJson.get("contest")).get("type"));
					if (contest_type.equals("ICPC") || contest_type.equals("CF")) {

						CONTEST_TYPE type = contest_type.equals("ICPC") ? CONTEST_TYPE.ICPC : CONTEST_TYPE.CF;

						JSONArray rowsJson = (JSONArray) contestJson.get("rows");
						Row[] rows = new Row[rowsJson.size()];

						JSONObject row;
						for (int i = 0; i < rowsJson.size(); i++) {
							row = (JSONObject) rowsJson.get(i);
							int points = (int) ((long) row.get("points"));
							int penalty = (int) ((long) row.get("penalty"));
							int rank = (int) ((long) row.get("rank"));
							String handle = (String) ((JSONObject) ((JSONArray) ((JSONObject) row.get("party"))
									.get("members")).get(0)).get("handle");

							JSONArray submissionsJson = (JSONArray) row.get("problemResults");
							models.Contest.Submission[] submissions = new models.Contest.Submission[submissionsJson
									.size()];

							JSONObject submissionJson;
							for (int j = 0; j < submissionsJson.size(); j++) {
								submissionJson = (JSONObject) submissionsJson.get(j);
								int rejectedCount = (int) ((long) submissionJson.get("rejectedAttemptCount"));
								int problemPoints = (int) ((long) submissionJson.get("points"));

								submissions[j] = new models.Contest.Submission(rejectedCount, problemPoints);
							}

							rows[i] = new Row(points, penalty, rank, handle, submissions);
						}

						Contest contest = new Contest(type, rows);
						tm.put(contest_id, contest);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

		}

		serialize(tm, "./data/filtered/contests");
	}

	public static void seedUsersContests() throws IOException {
		Path path = Paths.get("data/users");

		// holds all contests to be written to disk
		TreeMap<String, EnteredContest[]> tm = new TreeMap<>();

		// iterate over contests directory
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(path)) {
			JSONParser parser = new JSONParser();
			dirStream.forEach(p -> {
				try {

					FileReader reader = new FileReader(p.toString() + "/rating.json");
					JSONArray contestsJson = (JSONArray) parser.parse(reader);
					reader.close();

					String handle = p.getFileName().toString();
					EnteredContest[] enteredContests = new EnteredContest[contestsJson.size()];

					JSONObject contestJson;
					for (int i = 0; i < contestsJson.size(); i++) {
						contestJson = (JSONObject) contestsJson.get(i);
						int contest_id = (int) ((long) contestJson.get("contestId"));
						int rank = (int) ((long) contestJson.get("rank"));
						enteredContests[i] = new EnteredContest(contest_id, rank);
					}

					tm.put(handle, enteredContests);

				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}

		serialize(tm, "./data/filtered/users_contests");
	}

	public static void seed() throws IOException {
//		Seed.seedContests();
//		Seed.seedUsersContests();
		Seed.seedUsersActivity();
	}

	public static void serialize(Object o, String fileName) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(fileName);
		ObjectOutputStream dout = new ObjectOutputStream(fileOut);
		dout.writeObject(o);
		dout.close();
		fileOut.close();
	}

	public static Object deserialize(String fileName) {
		Object obj = null;
		try {
			// Reading the object from a file
			FileInputStream file = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(file);

			// Method for deserialization of object
			obj = in.readObject();

			in.close();
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return obj;

	}
}
