///////////////////////////////////////
// External Sorter by
// Copyright (c) 2012 Gregory R. Everitt
// This software is distributed under the MIT License.
// See LICENSE.txt for more details.
////////////////////////////////////////

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;


public class Sorter {
	public final static int MEM_LIMIT = 10;
	public final static int NUM_RUNS = 10;
	public final static int TOTAL_N = 100;
	public final static int MAXIMUM = Integer.MAX_VALUE;
	/**
	 * @param args Name of the file you want to sort.
	 * @throws FileNotFoundException throws in case data to be sorted is not found
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String externalDataName = args[0];
		int runNumber = 0; // this goes into the file name of runs
		int[] runlist = new int[MEM_LIMIT];
		Scanner fileScanner = new Scanner(new File(externalDataName));
		
		while (fileScanner.hasNext()) {
			// loads next 10 values into the runlist array
			for(int i=0; i<MEM_LIMIT; i++){
				runlist[i]= fileScanner.nextInt();
			}

			int[] tempBuff = new int[MEM_LIMIT];
			int[] sortedRunlist = BottomUpSort(MEM_LIMIT, runlist, tempBuff);

			// time to output sorted runlist
			PrintStream output = null;
			try {
				output = new PrintStream("tempRun"+runNumber);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			PrintStream outRun = new PrintStream(output);

			for (int i : sortedRunlist) { // iterate through sorted runlist and print to file
				outRun.println(i);
			}

			outRun.close();
			runNumber++;
		}
		
		// time to collect the first numbers of each run
		int[] firstNums = new int[NUM_RUNS];
		Scanner[] everyRunScanner = new Scanner[NUM_RUNS]; // a Scanner for each run
		runNumber = 0; // just so we can iterate through the files for loading purposes
		for (int i = 0; i<NUM_RUNS; i++) { // we get the first values of each file
			everyRunScanner[i] = new Scanner(new File("tempRun"+runNumber));
			if (everyRunScanner[i].hasNextInt())
				firstNums[i] = everyRunScanner[i].nextInt();
			else
				firstNums[i] = MAXIMUM;
			runNumber++;
		}
		
		// create PrintStream to print merged runs to
		PrintStream output = null;
		try {
			output = new PrintStream("sortedData");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		PrintStream outSorted = new PrintStream(output);
		
		for (int i = 0; i<TOTAL_N; i++) {
			int currentMin = firstNums[0];
			int minIndex = 0;
			for (int j = 0; j<NUM_RUNS; j++) {
				if (currentMin > firstNums[j]){
					currentMin = firstNums[j];
					minIndex = j;
				}
				
			}
			outSorted.println(currentMin); // minimum value is printed, now to next line
			if (everyRunScanner[minIndex].hasNextInt())
				firstNums[minIndex] = everyRunScanner[minIndex].nextInt();
			else
				firstNums[minIndex] = MAXIMUM;
		}
		outSorted.close();
		
	}
	public static int[] BottomUpSort(int n, int[] record, int[] tempBuff) {
		int width;

		for(width =1; width < n; width = 2 * width) {
			int i;

			for(i= 0; i < n; i = i + 2 * width) {
				BottomUpMerge(record, i, Math.min(i+width, n), Math.min(i+2*width, n), tempBuff);
			}
			record = Arrays.copyOf(tempBuff, tempBuff.length);
		}
		return record;
	}
	public static void BottomUpMerge(int[] A, int left, int right, int end, int[] B)
	{
		int i0 = left;
		int i1 = right;
		int j;

		/* while there are elements in the left or right lists */
		for (j = left; j < end; j++)
		{
			/* if left list head exists and is <= existing right list head */
			if (i0 < right && (i1 >= end || A[i0] <= A[i1]))
			{
				B[j] = A[i0];
				i0++;
			}
			else
			{
				B[j] = A[i1];
				i1++;
			}
		}
	}
}

