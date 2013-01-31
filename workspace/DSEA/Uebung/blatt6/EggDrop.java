package blatt6;
/* Programm von Michael Mardaus
 * DSEA Übung 1 
 * Gruppe 6
 * Blatt 6 Aufgabe 4
 */

public class EggDrop {
	//Funktion die das Minimum an noetigen Wuerfen ermittelt k Eier 
	//aus n Stockwerken zu werfen um die Bruchfestigkeit zu testen.
	public static int eggDrop(int n, int k){
		//memoization passiert in diesem feld, das die minima speichert
		int[][] floorEggArray = new int[n+1][k+1];
		int erg;
		int i, j, x;

		// Wir brauchen einen Versuch fuer einen Stock und 0 für 0
		for (i = 1; i <= k; i++){
			floorEggArray[1][i] = 1;
			floorEggArray[0][i] = 0;
		}

		// Wenn wir nur noch ein Ei haben muessen wir alle Stockwerke durchprobieren
		for (j = 1; j <= n; j++)
			floorEggArray[j][1] = j;

		// Das restliche Array wird beim Aufrufen gefuellt.
		for (i = 2; i <= k; i++)
		{
			for (j = 2; j <= n; j++)
			{
				floorEggArray[j][i] = 2^16; // indikator fuer quasi unendlich
				for (x = 1; x <= j; x++)
				{
					erg = 1 + Math.max(floorEggArray[x-1][i-1], floorEggArray[j-x][i]);    //formel aus der VL
					if (erg < floorEggArray[j][i])         //pruefen auf neues minimum
						floorEggArray[j][i] = erg;         //dann setzen
				}
			}
		}

		/*for (i = 0 ; i <=k; i++){
	    	System.out.print(i+"#\t");
	    	for (j=0 ; j<=n; j++){
	    		System.out.print(floorEggArray[j][i]+"\t");
	    	}
	    	System.out.println();
	    }*/

		// floorEggArray[n][k] enthaelt das ergebnis
		return floorEggArray[n][k];
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int n = 100, k = 4;
		System.out.println("Beste Strategie mit "+k+" Eiern und "+n+" Stockwerken sind "+ eggDrop(n, k)+" Wuerfe");
	}

}
