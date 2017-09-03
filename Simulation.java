//-----------------------------------------------------------------------------
// Rhea Rupani
// rrupani
// 12B PA4
// 11/20/16
// Simulation.java
//-----------------------------------------------------------------------------

import java.io.*;
import java.util.Scanner;

public class Simulation {
	public static void main(String[] args) throws IOException {

		/* Variables*/
		Scanner sc = null;
		PrintWriter report = null;
		PrintWriter trace = null;
		Queue StorageC = new Queue();
		Queue Storage = new Queue();
		Queue finished = new Queue();
		Queue[] processorQ = null;
		int m = 0;
		Job j = null;
		int time = 0;

		if (args.length < 3) {
			System.out.println("Usage: Simultation infile");
			System.exit(1);
		}

		sc = new Scanner(new File(args[0]));
		report = new PrintWriter(new FileWriter(args[0] + ".rpt"));
		trace = new PrintWriter(new FileWriter(args[0] + ".trc"));
		

		m = numOfJobs(sc); /*Get num of jobs*/

		while(sc.hasNextLine()) {
			j = getJob(sc);
			StorageC.enqueue(j);
		}

		/* Trace and Report*/
		trace.println("Trace file: " + (args[0] + ".trc"));
		trace.println(m + " Jobs:");
		trace.println(StorageC.toString());
		trace.println();

		report.println("Report file: " + (args[0] + ".rpt"));
		report.println(m + " Jobs:");
		report.println(StorageC.toString());
		report.println();
		report.println("***********************************************************");

		/* Main simulation loop from (1,m-1)*/
		for(int n = 1; n < m; n++) { /*Creates one less proccess than jobs*/
			int totalWait = 0;
			int maxWait = 0;
			double avgWait = 0.0;

			for(int i = 1; i < StorageC.length()+1; i++) {
				j = (Job)StorageC.dequeue();
				j.resetFinishTime();
				Storage.enqueue(j);
				StorageC.enqueue(j);
			}

			int processors = n;
			processorQ = new Queue[n+2];
			processorQ[0] = Storage;
			processorQ[n+1] = finished;
			for(int i = 1; i < n+1; i++) {
				processorQ[i] = new Queue();
			}

			trace.println("*****************************");
			if(processors == 1)
				trace.println(processors + " processor:");
			else
				trace.println(processors + " processors:");
			trace.println("*****************************");

			trace.println("time=" + time);
			trace.println("0: " + Storage.toString());
			for(int i = 1; i < processors+1; i++) {
				trace.println(i + ": " + processorQ[i]);
			}

			/* As long as still jobs pending*/
			while(finished.length() != m) {
				int compFinal = Integer.MAX_VALUE;
				int finalIndex = 1;
				int comp = -1;
				int length = -1;
				int finalLength = -1;
				Job compare = null;

				/* Checks storage arrival time*/
				if (!Storage.isEmpty()) {
					compare = (Job)Storage.peek();
					compFinal = compare.getArrival();
					finalIndex = 0;
				}

				for(int i = 1; i < processors+1; i++) {
					if (processorQ[i].length() != 0) {
						compare = (Job)processorQ[i].peek();
						comp = compare.getFinish();
					}
					if (comp == -1) {
						/* Do Nothing?*/
					} else if (comp < compFinal) {
						compFinal = comp;
						finalIndex = i;
					}
					time = compFinal;
				}

				if (finalIndex == 0) {
					int tempIndex = 1;
					finalLength = processorQ[tempIndex].length();
					for (int i = 1; i < processors+1; i++) {
						length = processorQ[i].length();
						if (length < finalLength) {
							finalLength = length;
							tempIndex = i;
						}
					}

					compare = (Job)Storage.dequeue();
					processorQ[tempIndex].enqueue(compare);
					if (processorQ[tempIndex].length() == 1) {
						compare = (Job)processorQ[tempIndex].peek();
						compare.computeFinishTime(time);
					}
				} else {
					compare = (Job)processorQ[finalIndex].dequeue();
					finished.enqueue(compare);
					int tempWait = compare.getWaitTime();
					if (tempWait > maxWait)
						maxWait = tempWait;
					totalWait += tempWait;

					if (processorQ[finalIndex].length() >= 1) {
						compare = (Job)processorQ[finalIndex].peek();
						compare.computeFinishTime(time);
					}
				}

				trace.println();
				trace.println("time=" + time);
				trace.println("0: " + Storage.toString());
				for(int i = 1; i < processors+1; i++)
					trace.println(i + ": " + processorQ[i]);
			}

			avgWait = ((double)totalWait/m);
			avgWait = (double)Math.round(avgWait*100)/100;
			trace.println();
			if (processors == 1)
				report.println(processors + " processor: totalWait=" + totalWait + ", maxWait=" + maxWait + ", averageWait=" + avgWait);
			else
				report.println(processors + " processors: totalWait=" + totalWait + ", maxWait=" + maxWait + ", averageWait=" + avgWait);

			time = 0;
			finished.dequeueAll();
		}

		sc.close();
		report.close();
		trace.close();

	}

	
	public static int numOfJobs(Scanner sc) { 
		String s = sc.nextLine();
		int x = Integer.parseInt(s);
		return x;
	}

	public static Job getJob(Scanner in) { // Given in SimulationStub
		String[] s = in.nextLine().split(" ");
		int a = Integer.parseInt(s[0]);
		int d = Integer.parseInt(s[1]);
		return new Job(a, d);
	}
}