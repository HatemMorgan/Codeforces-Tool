package utilities;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import models.Submission;
import models.User;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

public class Seed {
	public static void seedUsers() throws IOException {
		Path path = Paths.get("data/users");

		// holds all users to be written to disk
		ArrayList<User> users = new ArrayList<User>();
		Path outDirPath = Paths.get("seed/users");

		Files.createDirectories(outDirPath);

		// iterate over users directory and transform each user to a simplified
		// version
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
					User user = new User();
					ArrayList<Submission> userSubmissions = new ArrayList<Submission>();

					JSONObject submissionJson;
					Submission submission;

					for (Object o : submissions) {
						submissionJson = (JSONObject) o;

						if (!submissionJson.get("verdict").equals("OK"))
							continue;

						submission = new Submission();
						submission.setCreationTime((long) submissionJson.get("creationTimeSeconds"));

						userSubmissions.add(submission);
					}

					String handle = p.getFileName().toString();
					long rating = 1500;
					if (!contests.isEmpty())
						rating = (long) ((JSONObject) contests.get(contests.size() - 1)).get("newRating");

					user.setHandle(handle);
					user.setRating((int) rating);
					user.setSubmissions(userSubmissions);

					users.add(user);

					// create new file
					Path newFilePath = Paths.get(outDirPath + "/" + handle + ".json");
					Files.createFile(newFilePath);
					Files.write(newFilePath, new Gson().toJson(user).getBytes());
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			// sort in ascending order according to rating
			Collections.sort(users, (x, y) -> {
				return x.getRating() - y.getRating();
			});
			
			Path newFilePath = Paths.get("seed/allUsers.json");
			Files.createFile(newFilePath);
			Files.write(newFilePath, new Gson().toJson(users).getBytes());
//			serialize(users, "allUsers.json");
		}
	}

	private static void serialize(Object o, String fileName) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(fileName);
		ObjectOutputStream dout = new ObjectOutputStream(fileOut);
		dout.writeObject(o);
		dout.close();
		fileOut.close();
	}

	public static void main(String[] args) throws IOException {
		seedUsers();
	}
}
