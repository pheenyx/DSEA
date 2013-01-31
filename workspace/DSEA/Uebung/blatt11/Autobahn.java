package blatt11;

/* Programm von Michael Mardaus
 * DSEA Übung 1 
 * Gruppe 6
 * Blatt 11 Aufgabe 1
 */

/*
 * Aufgabenteil a) 
 * Es ist hier sinnvoll mit einer Adj.Liste anstatt einer Matrix zu arbeiten, da die Matrix sehr duenn besetzt waere.
 * Es gibt wesentlich mehr Woerter die nicht aehnlich sind, als welche die sich bis auf einen Buchstaben gleichen.
 * Daher ist eine Liste speichereffizienter.
 */

import java.io.*;
import java.util.ArrayList;


//repraesentiert eine eingelesene Verbindung zwischen 2 autobahn abfahren und deren distanz
class Connection {
	private int startPoint;
	private int endPoint;
	private double distance;

	// constructor
	public Connection(int startPoint, int endPoint, double distance) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.distance = distance;
	}

	// getter, setter nicht noetig
	public int getStartPoint() {
		return this.startPoint;
	}

	public int getEndPoint() {
		return this.endPoint;
	}

	public double getDistance() {
		return this.distance;
	}
}

class Abfahrt{
	private String name;
	private int index;
	private ArrayList<Connection> Connections;

	// constructor
	public Abfahrt(String name, int index) {
		this.name = name;
		this.index = index;
		this.Connections = new ArrayList<Connection>();
	}

	// getter und setter
	public String getName() {
		return this.name;
	}

	public int getIndex() {
		return this.index;
	}

	public void addConnection(Connection con) {
		this.Connections.add(con);
	}

	public ArrayList<Connection> getConnections() {
		return this.Connections;
	}
}

class Autobahn {
	ArrayList<Connection> T;               //kruskals spannbaum knoten
	String fileName;                       //datei mit autobahnknoten
	ArrayList<Abfahrt> Abfahrten;          //adj liste originalgraph
	ArrayList<Abfahrt> AbfahrtenKruskal;   //adj liste kruskals graph
	ArrayList<Connection> ConnectionList;  //sortierte knoten liste fuer kruskalalgo
	static int[] next;
//	int[] rep;
//	int[] size;

	// constructor
	public Autobahn(String file) {
		this.T = new ArrayList<Connection>();
		this.fileName = file;
		this.Abfahrten = new ArrayList<Abfahrt>();
		this.AbfahrtenKruskal = new ArrayList<Abfahrt>();
		this.ConnectionList = new ArrayList<Connection>();
		Autobahn.next = null;
//		this.rep = null;
//		this.size = null;
	}

	// readFile liest die Datei ein 
	public boolean readFile() {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(this.fileName));
			// erste zeile lesen um die beiden zeilenanzahlen zu wissen
			String line = in.readLine();
			int numberOfAbfahrten = Integer.parseInt(line.split(" ")[0]);
			int numberOfConnections = Integer.parseInt(line.split(" ")[1]);
			
			//jetzt wo ich weiss wieviele abfahrten = knoten wir haben kann ich die union
			// find struktur initalisieren
			next = new int[numberOfAbfahrten];
//			rep = new int[numberOfAbfahrten];
//			size = new int[numberOfAbfahrten];
			
			//dann abfahrten einlesen und union find initialisieren
			for (int i = 0; i < numberOfAbfahrten; i++){
				line = in.readLine();
				Abfahrt abfahrt = new Abfahrt(line,i);
				Abfahrt abfahrtKruskal = new Abfahrt(line,i);
				Abfahrten.add(abfahrt);
				AbfahrtenKruskal.add(abfahrtKruskal);
				
				//union-find aus VL ist zu umstaendlich
//				rep[i] = i;
//				next[i] = -1;
//				size[i] = 1;
				
				//einfachere union-find-stuktur, kommt mit einem zeiger auf next aus
				//dieser wird initial auf sie selbst zeigen, dieses feld ist gleichzeitig der repraesentant
				next[i] = i;

			}
			
			//und verbindungen einlesen, dabei adj liste aufbauen
			for (int i = 0; i < numberOfConnections; i++){
				line = in.readLine();
				int startIndex = Integer.parseInt(line.split(" ")[0]);
				int endIndex = Integer.parseInt(line.split(" ")[1]);
				double dist = Double.parseDouble(line.split(" ")[2]);
				//zwei umgekehrte connection obj. bauen, als zwei gerichtete kanten.
				//fuer jede richtung in der adjListe
				Connection con1 = new Connection(startIndex,endIndex,dist);
				Connection con2 = new Connection(endIndex,startIndex,dist);
				
				//con in die sortierte Kanten Liste einfuegen
				//TODO das geht auch besser mit binarySearch oder so
				int j=0;
				while (j <= ConnectionList.size()){
					if (j == ConnectionList.size()){
						ConnectionList.add(con1);
						break;
					} else if (con1.getDistance() > ConnectionList.get(j).getDistance()){
						j++;
					} else {
						ConnectionList.add(j, con1);
						break;
					}
				}
				
				Abfahrten.get(startIndex).addConnection(con1);
				Abfahrten.get(endIndex).addConnection(con2);
				
			}
			return true; // dann erfolg rueckmelden
			// bei fehlern abbrechen
		} catch (IOException e) {
			System.out.println("read file not successful");
			return false;
			// aufraeumen falls eine datei geoeffnet wurde
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					System.out.println("file read not successful");
				}
			}
		}
	}
	
	//beim suchen wird solange dem next gefolgt bis man den knoten mit zeiger auf sich selbst gefunden hat
	public int find(int abfahrt){
		int iter = abfahrt;
		while (next[iter] != iter){
			iter = next[iter];
		}
		return iter;

//		return rep[abfahrt];
	}
	
	
	//union funktion die auf einfacherer union-find-struktur arbeitet.
	//beim vereinigen wird einfach der zeiger auf das zweite element gelegt.
	public void union(int u, int v){
		next[v] = u;

//		int smallerOne, largerOne;
//		if (size[u] < size[v]) {
//			smallerOne = u;
//			largerOne = v;
//		} else {
//			smallerOne = v;
//			largerOne = u;
//		}
//		
//		int iter = smallerOne;
//		while (iter != -1){
//			rep[iter] = rep[largerOne];
//			size[iter] = -1;
//			if (next[iter] == -1){
//				next[iter] = next[largerOne];
//				next[largerOne]= smallerOne;
//			}
//			iter = next[iter];
//		}
	}
	

	
	//sucht fuer jede abfahrt i und jeder der connections j, ob es zu dessem endpunkt eine umgekehrte connection k gibt
	//die die gleiche distanz hat. (falls es zwei kanten geben sollte (67,1968) wird kein fehler gezaehlt.) 
	public boolean isUndirectedGraph(ArrayList<Abfahrt> Abfahrten){
		int mismatchcounter = 0;
		for (int i = 0; i < Abfahrten.size(); i++){
			for (int j = 0; j < Abfahrten.get(i).getConnections().size(); j++){
				int currentStartAbfahrt = Abfahrten.get(i).getConnections().get(j).getStartPoint();
				int currentZielAbfahrt = Abfahrten.get(i).getConnections().get(j).getEndPoint();
				double currentDistance = Abfahrten.get(i).getConnections().get(j).getDistance();
				boolean found = false;
				for (int k = 0; k < Abfahrten.get(currentZielAbfahrt).getConnections().size(); k++){
					if (Abfahrten.get(currentZielAbfahrt).getConnections().get(k).getEndPoint() == currentStartAbfahrt){
						found = (Abfahrten.get(currentZielAbfahrt).getConnections().get(k).getDistance() == currentDistance);
						if (found) break;  // ohne diese zeile werden faelle wie 67,1968 als fehler gewertet, weil es 2 kanten mit verschiedenem gewicht gibt.
					}
				} //for k
				if (!found){
					mismatchcounter++;
				}
			} //for j
		} //for i
		if (mismatchcounter > 0) {
			return false;
		} else {
			return true;
		}
	}
	

	//Kruskal algorithmus nach VL
	public void kruskal(){
		for (Connection current : ConnectionList){
			if(find(current.getStartPoint()) != find(current.getEndPoint())){
				T.add(current);
				union(find(current.getStartPoint()) , find(current.getEndPoint()));
			}
		}
	}
	
	//Graph nochmal aufbauen mit dem Spannbaum den Kruskal übriggelassen hat.
	public void graphNachKruskal(){
		for (int i = 0; i < T.size(); i++){
			
			Connection con1 = new Connection(T.get(i).getStartPoint(),T.get(i).getEndPoint(),T.get(i).getDistance());
			Connection con2 = new Connection(T.get(i).getEndPoint(),T.get(i).getStartPoint(),T.get(i).getDistance());
						
			AbfahrtenKruskal.get(T.get(i).getStartPoint()).addConnection(con1);
			AbfahrtenKruskal.get(T.get(i).getEndPoint()).addConnection(con2);
		}
	}
	
	//Kanten zaehlen im Graph
	public int countEdges(ArrayList<Abfahrt> abfList){
		int counter = 0;
		for (Abfahrt abf : abfList){
			counter+=abf.getConnections().size();
		}
		return counter;
	}

	public static void main(String[] args) {
		Autobahn ab = new Autobahn("bin/blatt11/autobahn.txt");

		// einlesen der datei
		if (!ab.readFile()) {
			System.out.println("Einlesen fehlgeschlagen");
		}
		//erst der original graph
		//Problem: 
		//z.B. Abfahrt 67 und 1968 haben unterschiedliche Distanzen in der Datei. einmal 0,8 und einmal 0,9
		//also gibt es zum Päarchen 67,1968 je zwei ungleiche Kanten.
		System.out.println("Anzahl an gerichteten Kanten vor Kruskal: " + ab.countEdges(ab.Abfahrten));
		System.out.println("Anzahl an ungerichteten Kanten nach Kruskal: " + ab.ConnectionList.size());
		System.out.println("Anzahl an Knoten vor Kruskal: " + ab.Abfahrten.size());
		System.out.println("org. Graph is undirected: " + ab.isUndirectedGraph(ab.Abfahrten));
		
		//dann kruskal anwenden und nochmal auswerten
		ab.kruskal();
		ab.graphNachKruskal();
		System.out.println("Anzahl an gerichteten Kanten nach Kruskal: " + ab.countEdges(ab.AbfahrtenKruskal));
		System.out.println("Anzahl an ungerichteten Kanten nach Kruskal: " + ab.T.size());
		System.out.println("Anzahl an Knoten nach Kruskal: " + ab.AbfahrtenKruskal.size());
		System.out.println("Kruskal Graph is undirected: " + ab.isUndirectedGraph(ab.AbfahrtenKruskal));
		
	}
}