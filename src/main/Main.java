package main;

import java.io.FileNotFoundException;
import java.io.IOException;

import utilities.Seed;
import utilities.Task2;
import utilities.Task3;

public class Main {

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
	
	private static void seed() throws IOException {
		Seed.seed();
	}

	public static void main(String[] args) throws IOException {
//		seed();
		runTask3();
	}
}
