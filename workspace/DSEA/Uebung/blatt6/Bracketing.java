package blatt6;

/* Programm von Michael Mardaus
 * DSEA Übung 1 
 * Gruppe 6
 * Blatt 6 Aufgabe 3
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bracketing {

	public static void main(String[] args) {
		// query n, d_i
		Scanner s = new Scanner(System.in);
		System.out.println("# n?");
		int n = s.nextInt();
		List<Integer> dims = new ArrayList<>();
		for (int i = 0; i <= n; ++i) {
			System.out.println("# d_" + (i + 1) + "?");
			dims.add(s.nextInt());
		}
		s.close();

		int cost = bestBracketingCost(dims);
		System.out.println(cost);
	}

	public static int bestBracketingCost(List<Integer> dims) {
		int n = dims.size()-1;
		// memoization passiert in diesem feld, das die minima speichert
		int[][] cost = new int[n][n];    //optimale kostenmatrix
		int[][] pathway = new int[n][n];     //optimaler weg matrix
		
		//die arrays initialisieren mit 0 auf der diagonalen - keine kosten fuer matrix mit sich selbst
		for (int i = 0; i < n; i++) {
			cost[i] = new int[n];
			cost[i][i] = 0;
			pathway[i] = new int[n];
		}
		for (int ii = 1; ii < n; ii++) {            //schleifendurchlauf sodass k zwischen i und j liegt
			for (int i = 0; i < n - ii; i++) {
				int j = i + ii;
				cost[i][j] = Integer.MAX_VALUE;    //erst auf "unendlich" setzen
				for (int k = i; k < j; k++) {
					//System.out.println("cost"+i+","+k+" + cost"+(k+1)+","+j+" + dim"+i+" * dim"+k+" * dim"+j);
					int minimum = cost[i][k] + cost[k + 1][j] + dims.get(i) * dims.get(k+1) * dims.get(j+1);      //formel aus VL
					if (minimum < cost[i][j]) {    //minimum merken
						cost[i][j] = minimum;
						pathway[i][j] = k;         //genutzes k der aufteilung merken
					}
				}
			}
		}
		return cost[0][n-1];   //die loesung ist der beste weg von 0 bis n-1
	}
}
