package utilities;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import models.Problem;
import models.Solution;

public class Task4 {
	static TreeMap<String, TreeMap<String, Solution>> users_problems;
	static ArrayList<Integer> adjlist[];
	static ArrayList<Integer> dag[];
	static int V;
	static int dfsCounter, SCC, dfs_num[], dfs_low[], SCCIndex[], importance[];
	static ArrayList<PriorityQueue<Integer>> SCCGroup;
	static Stack<Integer> stack;
	static ArrayList<Problem> problems;

	public static void prepare() throws FileNotFoundException {
		users_problems = (TreeMap<String, TreeMap<String, Solution>>) Seed.deserialize("./data/filtered/users_solutions");
	}

	public static ArrayList<ArrayList<String>> selectProblems(String[] handles, String tag, int minSolved,
			int maxSolved, int p, int cnt) {
		problems = getProblems(handles, tag, minSolved, maxSolved);
		
		constructGraph(handles, p);
		
		tarjanSCC();
		
		dag = new ArrayList[SCC];
		for (int i = 0; i < SCC; i++)
			dag[i] = new ArrayList<>();
		
		for (int u = 0; u < V; u++)
			for (int v : adjlist[u])
				if (SCCIndex[u] != SCCIndex[v]) 
					dag[SCCIndex[u]].add(SCCIndex[v]);
		
		importance = new int[SCC];
		for (int u = 0; u < V; u++)
			importance[SCCIndex[u]] += problems.get(u).points;
		
		ArrayList<Integer> SCCOrder = Kahn();
		if (SCCOrder == null) return new ArrayList<>();
		
		ArrayList<ArrayList<String>> res = new ArrayList<>();
		for (int component : SCCOrder) {
			ArrayList<String> deadlockSet = new ArrayList<>();
			while (!SCCGroup.get(component).isEmpty()) {
				int u = SCCGroup.get(component).remove();
				deadlockSet.add(problems.get(u).problemCode);
			}
			
			res.add(deadlockSet);
		}
		
		return res;
	}
	
	private static ArrayList<Integer> Kahn() {
		int N = dag.length;

		int[] inDegree = new int[N];
		ArrayList<Integer> sortedArray = new ArrayList<Integer>(N);
		for(int i = 0; i < N; ++i)
			for(int v: dag[i])
				++inDegree[v];

		PriorityQueue<Integer> roots = new PriorityQueue<>(new Comparator<Integer>() {

			@Override
			public int compare(Integer a, Integer b) {
				if (importance[a] != importance[b])
					return importance[b] - importance[a];
				
				Problem AFirstProblem = problems.get(SCCGroup.get(a).peek());
				Problem BFirstProblem = problems.get(SCCGroup.get(b).peek());
				
				if (AFirstProblem.solvedCount != BFirstProblem.solvedCount)
					return BFirstProblem.solvedCount - AFirstProblem.solvedCount;
				return AFirstProblem.problemCode.compareTo(BFirstProblem.problemCode);
			}
		});

		for(int i = 0; i < N; ++i)
			if(inDegree[i] == 0)
				roots.add(i);

		while(!roots.isEmpty()) {
			int u = roots.remove();
			sortedArray.add(u);
			for(int v: dag[u])
				if(--inDegree[v] == 0)
					roots.add(v);
		}

		boolean valid = true;
		for (int i = 0; i < N && valid; i++) 
			if (inDegree[i] != 0)
				valid = false;
		
		if (!valid) return null;
		return sortedArray;
	}

	private static ArrayList<Problem> getProblems(String[] handles, String tag, int minSolved, int maxSolved) {
		TreeSet<Problem> ts = new TreeSet<>();

		for (String handle : handles) {
			TreeMap<String, Solution> userSolutions = users_problems.get(handle);
			if (userSolutions == null)
				continue;

			for (Entry<String, Solution> entry : userSolutions.entrySet()) {
				Problem problem = entry.getValue().problem;
				if (problem.solvedCount >= minSolved && problem.solvedCount <= maxSolved && (tag == null
						|| problem.tags.contains(tag)))
					ts.add(problem);
			}
		}

		ArrayList<Problem> res = new ArrayList<>();
		for (Problem p : ts)
			res.add(p);

		return res;
	}

	private static boolean isBefore(Problem x, Problem y, String[] handles, int p) {
		int c = 0;

		for (String handle : handles) {
			TreeMap<String, Solution> userSolutions = users_problems.get(handle);
			if (userSolutions == null)
				continue;

			Solution s1 = userSolutions.get(x.problemCode);
			if (s1 == null)
				continue;

			Solution s2 = userSolutions.get(y.problemCode);
			if (s2 == null || s1.solvedTime < s2.solvedTime)
				c++;
		}

		double percentage = 1.0 * c / handles.length;
		percentage *= 100.0;
		return percentage >= p;
	}

	private static void constructGraph(String[] handles, int p) {
		V = problems.size();
		adjlist = new ArrayList[V];
		for (int i = 0; i < V; i++)
			adjlist[i] = new ArrayList<>();

		for (int i = 0; i < V; i++)
			for (int j = 0; j < V; j++) {
				if (i == j)
					continue;

				if (isBefore(problems.get(i), problems.get(j), handles, p))
					adjlist[i].add(j);
			}
	}

	private static void tarjanSCC() { // O(V + E)
		dfsCounter = 0;
		SCC = 0;
		dfs_num = new int[V];
		dfs_low = new int[V];
		SCCIndex = new int[V];
		Arrays.fill(SCCIndex, -1);
		stack = new Stack<>();
		SCCGroup = new ArrayList<>();

		for (int i = 0; i < V; ++i)
			if (dfs_num[i] == 0)
				tarjanSCC(i);
	}

	private static void tarjanSCC(int u) {
		dfs_num[u] = dfs_low[u] = ++dfsCounter;
		stack.push(u);

		for (int v : adjlist[u]) {
			if (dfs_num[v] == 0)
				tarjanSCC(v);
			if (SCCIndex[v] == -1)
				dfs_low[u] = Math.min(dfs_low[u], dfs_low[v]);
		}
		if (dfs_num[u] == dfs_low[u]) {
			// SCC found
			PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {

				@Override
				public int compare(Integer a, Integer b) {
					if (problems.get(a).solvedCount != problems.get(b).solvedCount)
						return problems.get(b).solvedCount - problems.get(a).solvedCount;
					return problems.get(a).problemCode.compareTo(problems.get(b).problemCode);
				}
			});
			while (true) {
				int v = stack.pop();
				SCCIndex[v] = SCC;
				pq.add(v);
				if (v == u)
					break;
			}
			
			SCCGroup.add(pq);
			SCC++;
		}

	}
}
