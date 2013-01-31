package blatt2;
/* Programm von Michael Mardaus
 * DSEA Übung 1 
 * Gruppe 6
 * Blatt 2 Aufgabe 3a
 */

import java.util.Scanner;

public class Aufgabe3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		double dInput; //variablendeklaration
		
		dInput = s.nextDouble(); // usereingabe auf dInput
		s.close();  // Scanner wird nicht mehr benötigt
		
		System.out.println(f(dInput));   // Ausgabe

	}
	
	public static double f(double x){
		if (x < 1) return 1.0;  // Abbruchbedingung
		else{
			return 20.0/9.0*f(Math.floor(x/2.0)) + f(Math.floor(2.0/3.0*x)) + x; // Funktion vom Blatt
		}
	}
}

