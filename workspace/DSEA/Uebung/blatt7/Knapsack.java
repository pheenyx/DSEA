package blatt7;

/* Programm von Michael Mardaus
 * DSEA Übung 1 
 * Gruppe 6
 * Blatt 7 Aufgabe 1
 */


import java.util.TreeSet;

// simple item with weight and value attributes
class Item implements Comparable<Item> {
	public int weight;
	public int value;

	// constructor
	public Item(int weight, int value) {
		this.weight = weight;
		this.value = value;
	}

	@Override
	public int compareTo(Item o) {
		return weight - o.weight;
	}
}

public class Knapsack {

	// max weight allowed in this knapsack
	public int maxWeight;

	// all available items
	public Item[] items;

	// maximum value achieved by the solution
	public int maxValue;

	// used items for the solution
	// implemented as a TreeSet to get an ordered output
	public TreeSet<Item> usedItems = new TreeSet<>();

	public static void main(String[] args) {

		// Create knapsack with maxWeigth and add items
		Knapsack knapsack = new Knapsack(49);
		knapsack.items = new Item[] { new Item(1, 1), new Item(2, 1), new Item(3, 3),
				new Item(4, 7), new Item(5, 3), new Item(6, 8), new Item(7, 7), new Item(8, 7),
				new Item(9, 9), new Item(10, 5), new Item(15, 20), new Item(20, 21),
				new Item(25, 15) };

		knapsack.solveProblem();

		// print contents of the knapsack and the total value
		System.out.println("calculated maximum value: " + knapsack.maxValue);
		int totalValue = 0;
		int totalWeight = 0;
		for (Item item : knapsack.usedItems) {
			System.out.println("(" + item.weight + "," + item.value + ")");
			totalValue += item.value;
			totalWeight += item.weight;
		}
		System.out.println("knapsack value: " + totalValue + " (should be " + knapsack.maxValue
				+ ")");
		System.out.println("knapsack weight: " + totalWeight + " of " + knapsack.maxWeight);
	}

	// constructor
	Knapsack(int maxWeight) {
		this.maxWeight = maxWeight;
	}

	/*
	 * Implement a solution of task 7.1 here.
	 * 
	 * Use the variables "maxWeight" and "items" to get all needed information.
	 * 
	 * Store the calculated maximum value in the field "maxValue" and add all
	 * items which are used in the solution to the set "usedItems".
	 */
	private void solveProblem() {

//		A similar dynamic programming solution for the 0-1 knapsack problem also runs in pseudo-polynomial time.
//		As above, assume w_1,w_2,..,w_n, W are strictly positive integers. 
//		Define m[i,w] to be the maximum value that can be attained with weight less than or equal to w using items up to i.
//		We can define m[i,w] recursively as follows:
//		 * m[i,w] = m[i-1,w] if w_i > w (the new item is more than the current weight limit)
//		 * m[i,w] = max( m[i-1,w], m[i-1],w-w_i] + v_i) if w_i <= w.
//		The solution can then be found by calculating m[n,W]. To do this efficiently we can use a table to store previous computations.
//		The following is pseudo code for the dynamic program:
//		Input:
//		Values (stored in items[i].value)
//		Weights (stored in items[i].weight)
//		Number of distinct items (n)
//		Knapsack capacity (W)
		
		//kurze Bezeichner fuer die array dimensionen
		int n = this.items.length;
		int W = this.maxWeight;
		
		//2d array memo enthaelt die jeweils optimalen valuesummen fuer die ersten i items
		//mit maximalgewicht j in memo[i][j]
		//2d array boolmemo enthaelt jeweils ein true wenn ein item im schritt benutzt wurde
		int[][] memo = new int[n+1][W+1];
		boolean[][] boolmemo = new boolean[n+1][W+1];
		
		//initialisieren der ersten zeile, ohne item kein value
		for (int w = 0; w < W; w++){
			memo[0][w] = 0;
			boolmemo[0][w] = false;
		}

		//dann wir je ein item mehr dazugenommen und damit von maximal gewicht 0 bis W durchprobiert
		for (int i = 1; i <= n; i++){
			for (int j = 0; j <= W; j++){
				if (j >= this.items[i-1].weight){
					//memo[i][j] = Math.max(memo[i-1][j], memo[i-1][j-this.items[i-1].weight] + this.items[i-1].value);
					//max ersetzt durch if abfrage
					//wenn das gewicht noch passt und der wert sich erhoeht hat wird der neue wert gespeichert
					//und in boolmemo ein true vermerkt
					if (memo[i-1][j-this.items[i-1].weight] + this.items[i-1].value >= memo[i-1][j]){
						memo[i][j] = memo[i-1][j-this.items[i-1].weight] + this.items[i-1].value;
						boolmemo[i][j] = true;
					} else {  //sonst bleibt der wert bei dem ohne dieses item
						memo[i][j] = memo[i-1][j];
					}

				} else { // wenn das gewicht nicht mehr passt bleibt der wert beim alten
					memo[i][j] = memo[i-1][j];
				}
			}
		}
		
		//hier wird herausgefunden welche items die loesung geben
		//dazu gehe ich rueckwaerts durch mein flagarray boolmemo und suche jeweils den 
		//letzten optimalen gegenstand fuer den verbleibenden restplatz im rucksack
		int free = W;   //restliches verbleibendes gewicht
		for (int k = n; k>0; k--){
			if (boolmemo[k][free]){
				usedItems.add(this.items[k-1]); //dieses item ist dabei
				free = free - this.items[k-1].weight;  //itemgewicht vom restlichen abziehen
			} else {  //sonst muss es wieder raus
				usedItems.remove(this.items[k-1]);
			}
		}
		maxValue = memo[n][W];  //optimale loesung fuer alle items und bis maxWeight
		return;
	}
}
