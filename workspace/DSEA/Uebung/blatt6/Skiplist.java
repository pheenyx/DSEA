package blatt6;

/* Programm von Michael Mardaus
 * DSEA Übung 1 
 * Gruppe 6
 * Blatt 6 Aufgabe 1
 */

import java.util.Random;

//Knoten Klasse mit Pointer auf next und down und dem key und seiner Hoehe in der Liste
class Node {
	Node next, down;
	// unterster Level = 0, alle weiteren groesser
	int key, level;

	// konstruktor mit key und level
	public Node(int key, int level) {
		this.key = key;
		this.level = level;
	}
}

// Skiplistklasse mit Wahrscheinlichkeit p
class Skiplist {
	// head ist der der startknoten der skiplist oben links, p die
	// Wahrscheinlichkeit
	// einen Knoten nach oben zu heben
	Node head;
	private double p;
	int maxHeight;

	public Skiplist(double p) {
		this.head = new Node(Integer.MIN_VALUE, 0);
		this.p = p;
		this.maxHeight = 0;
	}

	public void insert(int key) {
		Node currentNode;

		// levelcounter wird mit der gewuerfelten anzahl levels initialisiert in
		// der das neue element sein soll
		int levelCounter = rollDice();

		Node currentRowHead = this.head;
		Node upperNode = null;

		// Der key-Knoten soll nur eingefügt werden wenn er nicht schon
		// existiert
		if (!search(key)) {

			// wenn beim wuerfeln rauskam dass level hinzugefuegt werden, dann
			// hiermit den neuen head ueber dem alten anbringen und hoehe anpassen
			while (this.head.level < levelCounter) {
				this.head = new Node(Integer.MIN_VALUE,currentRowHead.level + 1);
				this.head.down = currentRowHead;
				currentRowHead = this.head;
				this.maxHeight++;
			}

			// wenn wir hoeher sind als das neue element gewuerfelt wurde nach
			// unten gehen bis der level erreicht ist
			while (currentRowHead.level > levelCounter) {
				currentRowHead = currentRowHead.down;
			}

			// richtiger level gefunden, dann einfuegen
			while ((levelCounter >= 0) && (currentRowHead.level == levelCounter)) {
				currentNode = currentRowHead;
				// nach rechts gehen bis ich am ende bin oder der naechste key zu gross waere
				while ((currentNode.next != null) && (currentNode.next.key < key)) {
					currentNode = currentNode.next;
				}
				// einfügen
				Node newNode = new Node(key, currentNode.level);
				newNode.next = currentNode.next;
				currentNode.next = newNode;
				
				//wenn das element schon im darueberliegenden level eingefuegt wurde wird hier der 
				//down pointer auf das neue element dieses levels gesetzt
				if (upperNode != null){
					upperNode.down = currentNode.next;
				}
				upperNode = newNode;

				levelCounter--;
				currentRowHead = currentRowHead.down;
			}
		}
	}

	//funktion zur berechnung wie oft der key-Knoten eingefügt werden soll
	public int rollDice() {
		int counter = 0;
		Random rand = new Random();
		while (rand.nextDouble() <= p) {
			counter++;
		}
		//limitieren auf die letzte hoehe +1 um turmbau zu verhindern
		if (counter >= this.maxHeight+1) counter = this.maxHeight+1;
		return counter;
	}

	//ausgabe der liste
	public void print() {
		Node currentNode;
		Node currentRowHead = this.head;
		//ausgabe zeilenweise von oben nach unten durch die listen gehen
		while (currentRowHead.level >= 0) {
			currentNode = currentRowHead;
			System.out.print(currentNode.key);
			while (currentNode.next != null) {
				currentNode = currentNode.next;
				System.out.print(" " + currentNode.key);
			}
			//wenn es noch runter geht am kopf runtergehen
			if (currentRowHead.down != null){
				currentRowHead = currentRowHead.down;
				System.out.println();
			} else {
				System.out.println();
				break; //abbruchbedingung, wenn ich in der untersten reihe angekommen bin
			}
		}
	}

	//element in der liste suchen, von head links oben nach rechts bis 
	//das naechste zu gross ist, dann runter bis ich unten bin
	public boolean search(int key) {
		Node currentNode = this.head;
		do {
			while (currentNode.next != null && currentNode.next.key < key) {
				currentNode = currentNode.next;
			}
			if (currentNode.next != null && currentNode.next.key == key) return true;
			currentNode = currentNode.down;
		} while (currentNode != null);
		return false;
	}
	
	//element in der liste suchen, von head links oben nach rechts bis 
	//das naechste zu gross ist, dann runter bis ich unten bin
	public boolean remove(int key) {
		boolean ret = false;
		Node currentNode = this.head;
		do {
			while (currentNode.next != null && currentNode.next.key < key) {
				currentNode = currentNode.next;
			}
			if (currentNode.next != null && currentNode.next.key == key) {
				currentNode.next = currentNode.next.next;
				trimList();
				ret = true;
			}
			currentNode = currentNode.down;
		} while (currentNode != null);
		return ret;
	}
	
	public void trimList(){
		Node currentRowHead = this.head;
		while(currentRowHead.next == null && currentRowHead.down != null){
			this.head = currentRowHead.down;
			currentRowHead = currentRowHead.down;
		}
	}

	public static void main(String[] args) {
		Skiplist skip = new Skiplist(0.3);
		
		//Aufgabenteil a
		int[] insertList = { 9, 37, 40, 43, 28, 45, 49, 21, 8, 37, 13, 41, 14, 4, 33, 47, 49, 44, 23, 32, 12, 15, 23, 41 };
		for (int item : insertList) {
			skip.insert(item);
		}
		skip.print();
		
		System.out.println("Jetzt wird gelöscht");
		//Aufgabenteil b
		int[] removeList = { 9, 43, 49, 37, 14, 47, 23, 15, 41 };
		for (int item : removeList) {
			skip.remove(item);
		}
		skip.print();
	}
}
