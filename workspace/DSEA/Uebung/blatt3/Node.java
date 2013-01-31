package blatt3;
/* Programm von Michael Mardaus
 * DSEA Übung 1 
 * Gruppe 6
 * Blatt 3 Aufgabe 1
 */

import java.util.Random;
import java.util.Scanner;

public class Node {
	// die Werte des Knotens
	private Node leftChild;
	private Node rightChild;
	private Integer value;
	
	// 3 verschiedene Konstruktoren
	public Node(){
		this.value = null;
		this.leftChild = null;
		this.rightChild = null;
	}
	public Node(int value){
		this.value = value;
		this.leftChild = null;
		this.rightChild = null;
	}
	public Node(int value, Node left, Node right){
		this.value = value;
		this.leftChild = left;
		this.rightChild = right;
	}
	
	// zugriffsfunktionen
	public Node getLeftChild(){
		return this.leftChild;
	}
	public void setLeftChild(Node leftChild){
		this.leftChild = leftChild;
	}
	public Node getRightChild(){
		return this.rightChild;
	}
	public void setRightChild(Node rightChild){
		this.rightChild = rightChild;
	}
	public int getValue(){
		return this.value;
	}
	public void setValue(int value){
		this.value = value;
	}
	
	// neuen Knoten einfuegen		
	public Node insert(int k) {							
		if (this.value == null ){						// beim ersten insert gibt es noch keine wurzel, und es wurde constructed mit null wert auf dem Integer value
			this.value = k;
			return this;
		} else {
			if (k < this.getValue()){					// wenn k kleiner als der aktuelle knoten, nach links suchen
				if (this.getLeftChild() == null) {		// wenn noch kein knoten da ist einen neuen anlegen
					Node l = new Node(k);
					this.setLeftChild(l);				//neuen knoten als linken setzen
//					System.out.println(l.getValue());
					return l;
				} else {
					this.getLeftChild().insert(k);		// sonst mit dem linken knoten rekursiv selbst aufrufen
				}				
			} else { 									//  dann ist k groesser - gleich gibt es ja nicht wegen (fussnote1) - also rechts weiter suchen
				if (this.getRightChild() == null) {  	//wenn rechts nichts ist neuen knoten anlegen
					Node r = new Node(k);
					this.setRightChild(r);				// und den neuen als rechten setzen
//					System.out.println(r.getValue());
					return r;
				} else {
					this.getRightChild().insert(k);		// sonst wieder mit dem rechten rekursiv selbst aufrufen
				}
			}
		}
		return null; 								// sollte nie erreicht werden		
	}	
	
	// Tiefe ermitteln
	public int maxDepth() {
		int countLeft = 0, countRight = 0;			// zwei zaehler fuer den linken und den rechte teilbaum
		if (this.getLeftChild() != null) {			// wenn es links weiter geht 
			countLeft += this.getLeftChild().maxDepth();  // suche rekursiv weiter und addiere die rueckgabe zum zaehler 
		}
		if (this.getRightChild() != null) {			// ebenso fuer rechts
			countRight += this.getRightChild().maxDepth();
		}
		return Math.max(countLeft,countRight) + 1;	// gebe die groessere der beiden tiefen zurueck +1 fuer sich selbst
	}	
	
	//hilfsfunktion zum ausgeben des baumes  - nicht gefordert
	private void drawTree(int level) {
		if (getRightChild() != null){
			getRightChild().drawTree(level + 1);
		}
		for (int i = 0; i < level ; i++) {
			System.out.print("    ");
		}
		System.out.println("+"+getValue());
		if (getLeftChild() != null) {
			getLeftChild().drawTree(level + 1);
		}
	}
	
	// hilfsfunktion zum sortierten ausgeben
	public int inOrderOutput(){  
		if(this.getLeftChild() != null) this.getLeftChild().inOrderOutput();		// erst schauen ob es links runter geht, dann rekursiv dort aufrufen
		System.out.println(this.getValue());										// falls es nicht weiter nach links geht sich selbst ausgeben
		if(this.getRightChild() != null) this.getRightChild().inOrderOutput();		// dann nach rechts schauen und rekursiv aufrufen
		return this.getValue();														// und jeweils den wert für den rekursiven rueckweg weitergeben
	}
	
	// hilfsfunktion swap 												// aus der vorlesung
	static void swap(int a[],int i,int j) {
		int tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int n,m;
		Random random = new Random(0);
				
		Scanner s = new Scanner(System.in);
		n = s.nextInt();												// n und m einlesen
		m = s.nextInt();
		s.close();
		
		int a[] = new int[n];											// feld inistialisieren
		double tiefen = 0.0;
		
		for (int k = 0; k < m; k++){
			
			Node node = new Node();
			for (int i=0;i<n;i++)										// aufsteigend befuellen
				a[i] = i;
	
			// Erzeuge eine zufällige Permutation des Eingabefeldes.
			// Jede Permutation hat die gleiche Wahrscheinlichkeit.
			for(int i=n-1;i>0;i--) {
	//			int j = (int) Math.floor((i+1)*Math.random());			// feld gut schuetteln
				int j = (int) Math.floor((i+1)*random.nextDouble());	// feld gut schuetteln
				swap(a,j,i);
			}
			
			for (int i = 0; i < n; i++){
				node.insert(a[i]);										// das erzeugte gemischte array in den baum inserten
			}
			
			if (m == 1) {												// aufgabenteil e
				node.inOrderOutput();
			}
			
//			for (int i=0;i<n;i++)										// array ausgeben
//				System.out.print(a[i] +" ");
//			System.out.println();
//			node.drawTree(0);											// baum ausgeben
			
			tiefen += node.maxDepth();									// tiefen aufaddieren
		}
		
		System.out.println(tiefen / (double)m);							// durchschnittstiefe ausgeben

/*		Node n = new Node(5);
		n.insert(2); n.insert(7); n.insert (4); n.insert (8);  n.insert(1); n.insert(6);
		System.out.println("MaxDepth: " + n.maxDepth());
		n.drawTree(0);
		n.inOrderOutput();
*/
	}

}
