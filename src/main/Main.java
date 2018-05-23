package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import utilities.Seed;
import utilities.Task2;
import utilities.Task3;
import utilities.Task4;

public class Main {
	static ArrayList<Integer>[] adjList;
	
	private static void runTask2() {
		Task2.prepare();
		System.out.println("Done preparing...");
		System.out.println(Task2.evaluateContestPerformance("Noureldin_Khaled", true));
	}

	private static void runTask3() throws FileNotFoundException {
		Task3.prepare();
		System.out.println("Done preparing...");
		System.out.println(Task3.getActiveUsers(0, Integer.MAX_VALUE, 3200, 3500, 5));
	}
	
	private static void runTask4() throws FileNotFoundException {
		Task4.prepare();
		System.out.println("Done preparing...");
		String[] handles = {"Noureldin_Khaled", "KEMDAK"};
		
		System.out.println(Task4.selectProblems(handles, null, 1000, 10000, 50, 5));
	}
	
	private static void seed() throws IOException {
		Seed.seed();
	}

	public static void main(String[] args) throws IOException {
//		runTask2();
//		runTask3();
		runTask4();
//		seed();
//		TreeMap<String, TreeMap<String, Solution>> tm = (TreeMap<String, TreeMap<String, Solution>>) Seed.deserialize("./data/filtered/users_solutions");
//		System.out.println(tm.get("Noureldin_Khaled"));
	}
	
}
