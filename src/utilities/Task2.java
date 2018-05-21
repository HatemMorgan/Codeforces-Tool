package utilities;

import java.awt.Color;
import java.awt.Dimension;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import models.Contest;
import models.Contest.Row;
import models.Contest.Submission;
import models.EnteredContest;

public class Task2 {
	static TreeMap<String, EnteredContest[]> users;
	static TreeMap<Integer, Contest> contests;

	public static void prepare() {
		users = (TreeMap<String, EnteredContest[]>) Seed.deserialize("./data/filtered/users_contests");
		contests = (TreeMap<Integer, Contest>) Seed.deserialize("./data/filtered/contests");
	}

	public static HashMap<Integer, Integer> evaluateContestPerformance(String handle, boolean plot) {
		HashMap<Integer, Integer> res = new HashMap<>();

		EnteredContest[] enteredContests = users.get(handle);

		if (enteredContests == null)
			return null;

		for (EnteredContest enteredContest : enteredContests) {
			int contestID = enteredContest.getContest_id();
			int oldRank = enteredContest.getRank();

			Contest contest = contests.get(contestID);

			int index = -1;
			for (int i = oldRank - 1; i < contest.getRows().length && index == -1; i++) {
				Row current = contest.getRows()[i];
				if (current.handle.equalsIgnoreCase(handle))
					index = i;
			}

			if (index == -1)
				continue;

			Row userRow = contest.getRows()[index];

			Row key;
			if (contest.getType() == CONTEST_TYPE.ICPC) {
				int newPenalty = userRow.penalty;
				for (int i = 0; i < userRow.submissions.length; i++) {
					Submission submission = userRow.submissions[i];
					if (submission.points > 0)
						newPenalty -= Math.max(0, (submission.rejectedCount * 20));
				}

				key = new Row(userRow.points, newPenalty, 0, userRow.handle, null);
			} else {
				int newPoints = userRow.points;
				for (int i = 0; i < userRow.submissions.length; i++) {
					Submission submission = userRow.submissions[i];
					if (submission.points > 0)
						newPoints += (submission.rejectedCount * 50);
				}

				key = new Row(newPoints, userRow.penalty, 0, userRow.handle, null);
			}

			int idx = Arrays.binarySearch(contest.getRows(), key, new Comparator<Row>() {

				@Override
				public int compare(Row r1, Row r2) {
					if (r1.points != r2.points)
						return r2.points - r1.points;
					return r1.penalty - r2.penalty;
				}
			});

			int newRank = contest.getRows()[Math.abs(idx + 1)].rank;
			if (idx >= 0)
				newRank = contest.getRows()[idx].rank;

			System.out.println("Contest: " + contestID + "\n oldRow: " + userRow.toString() + ", OldRank: " + oldRank
					+ "\n newRow: " + key + ", newRank: " + newRank);
			res.put(contestID, oldRank - newRank);
		}

		if (plot) {
			System.out.println("Starting Plot...");
			plot(res, handle);
		}
		return res;
	}

	private static void plot(HashMap<Integer, Integer> hm, String handle) {
		// Create dataset
		XYDataset dataset = createDataset(hm);

		// Create chart
		JFreeChart chart = ChartFactory.createScatterPlot("Difference in Rank vs Contest ID", "Contest ID",
				"Difference in Rank", dataset);

		// Changes background color
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(new Color(255, 228, 196));

		// Create Panel
		ChartPanel panel = new ChartPanel(chart);
		panel.setSize(new Dimension(ChartPanel.DEFAULT_WIDTH, ChartPanel.DEFAULT_HEIGHT));

		write(chart, panel, "./data/plots/" + handle + ".png");
	}

	private static XYDataset createDataset(HashMap<Integer, Integer> hm) {
		XYSeriesCollection dataset = new XYSeriesCollection();

		XYSeries series = new XYSeries("Rank Change");
		for (Entry<Integer, Integer> entry : hm.entrySet())
			series.add(entry.getKey(), entry.getValue());

		dataset.addSeries(series);

		return dataset;
	}

	private static void write(JFreeChart chart, ChartPanel panel, String chartName) {
		try {
			OutputStream out = new FileOutputStream(chartName);
			ChartUtils.writeChartAsPNG(out, chart, panel.getWidth(), panel.getHeight());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
