package main;

import java.io.IOException;

import utilities.Seed;
import utilities.Task2;

public class Main {

	private static void runTask2() {
		Task2.prepare();
		System.out.println("Done preparing...");
		System.out.println(Task2.evaluateContestPerformance("Noureldin_Khaled", true));
	}

	private static void seed() throws IOException {
		Seed.seed();
	}

	public static void main(String[] args) throws IOException {
//		runTask2();
//		seed();
	}
}
