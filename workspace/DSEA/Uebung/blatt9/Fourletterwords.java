package blatt9;

/* Programm von Michael Mardaus
 * DSEA Übung 1 
 * Gruppe 6
 * Blatt 9 Aufgabe 2
 */

/*
 * Aufgabenteil a) 
 * Es ist hier sinnvoll mit einer Adj.Liste anstatt einer Matrix zu arbeiten, da die Matrix sehr duenn besetzt waere.
 * Es gibt wesentlich mehr Woerter die nicht aehnlich sind, als welche die sich bis auf einen Buchstaben gleichen.
 * Daher ist eine Liste speichereffizienter.
 */

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

//Die word Klasse ist der Knoten einer linkedList.
//Jedes word hat einen Wert, einen Liste von aehnlichen Woertern, einen Vorgaenger und ein flag 
//das anzeigt ob die Breitensuche schon da war.
class Word {

	private String value;
	private ArrayList<Word> similar;
	private boolean visited;
	private Word predecessor;

	//constructor
	public Word(String v) {
		this.value = v;
		this.similar = new ArrayList<Word>();
		this.visited = false;
		this.predecessor = null;
	}

	//getter und setter
	public void setValue(String v) {
		this.value=v;
	}
	public String getValue(){
		return this.value;
	}

	public void setSimilar(ArrayList<Word> n){
		this.similar=n;
	}
	public ArrayList<Word> getSimilar(){
		return this.similar;
	}

	public boolean getVisited(){
		return this.visited;
	}
	public void setVisited(boolean v){
		this.visited=v;
	}

	public Word getPredecessor(){
		return this.predecessor;
	}
	public void setPredecessor(Word p){
		this.predecessor=p;
	}
}

//Die Klasse Fourletterwords nimmt den Inhalt der Datei auf und enthaelt die Funktionen um die Adjaszenzliste
//zu erstellen und die Breitensuche auszufuehren.
class Fourletterwords {

	ArrayList<Word> words;
	String fileName;

	//constructor
	public Fourletterwords(String file) {
		this.words = new ArrayList<Word>();
		this.fileName = file;
	}


	//readFile liest die Datei ein und speichert jedes Wort daraus in der Liste words
	public boolean readFile() {
		BufferedReader in = null;
		try { // zeilenweise einlesen bis nichts mehr kommt
			in = new BufferedReader(new FileReader(this.fileName));
			for (String line; (line = in.readLine()) != null;) {
				Word e = new Word(line);
				this.words.add(e);
			}
			return true; // dann erfolg rueckmelden
			//bei fehlern abbrechen 
		} catch (IOException e) { 
			System.out.println("read file not successful");
			return false;
			//aufraeumen falls eine datei geoeffnet wurde
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

	//buildAdjacencyList erstellt die Adj.Liste der aehnlichen Woerter auf der Arraylist words
	public void buildAdjacencyList() {
		for (int i = 0; i < this.words.size(); i++) {            //Einmal von vorne nach hinten durch die Woerter laufen
			for (int j = i+1; j < this.words.size(); j++) {      //und nochmal fuer alle Woerter die nach dem i-ten kommen
				int diffs = 0;                                   //counter fuer unterschiede
				int letterIdx = 0;                               //buchstaben Index im Wort
				
				//solang wir noch nicht 2 unterschiede haben oder am ende eines wortes sind
				//vergleichen wir letter fuer letter ob sie bei i und j gleich sind 
				while ((diffs < 2) && (letterIdx < this.words.get(j).getValue().length())) {
					if (this.words.get(i).getValue().charAt(letterIdx) != this.words.get(j).getValue().charAt(letterIdx)) {
						diffs++;
					} // end if
					letterIdx++;
				} // end while
				//falls es nur einen unterschied gab, kommt das wort i in die adj.liste von j und umgekehrt
				if (diffs == 1) {
					this.words.get(i).getSimilar().add(this.words.get(j));
					this.words.get(j).getSimilar().add(this.words.get(i));
				}
			}
		}
	}

	//breadthSearch macht die Breitensuche auf einer Queue beginnend bei startWord, und endet sobald sie endWord gefunden hat
	private Word breadthSearch(Word startWord, Word endWord) {
		Queue<Word> q = new LinkedList<Word>();                            //die queue
		boolean found = false;                                             //flag zum abbrechen

		int wordIdx = 0;                                                   //der index des startworts in der woerterliste wird gesucht  
		for (wordIdx = 0; wordIdx < this.words.size(); wordIdx++){
			if (this.words.get(wordIdx).getValue().equals(startWord.getValue())) {
				found = true;
				break;
			}
		}
		
		if (found == true) {
			//startwort in die queue speichern
			q.offer(this.words.get(wordIdx));                                  //startword hinten an die queue anhaengen
			while (!q.isEmpty()) {                                             //solange die queue nicht leer ist 
				Word node = q.poll();
				if (node.getValue().equals(endWord.getValue())) {              //schauen ob der kopf der queue das gesuchte endwort ist
					return node;                                               //dann sind wir fertig und geben es aus
				} else {                                                       //falls nicht
					for (int j = 0; j < node.getSimilar().size(); j++) {
						if (node.getSimilar().get(j).getVisited() == false) {  //haengen wir alle seine aehnlichen woerter die wir noch nicht besucht haben
							q.offer(node.getSimilar().get(j));                 //hinten an die queue an 
							node.getSimilar().get(j).setPredecessor(node);     //und markieren den weg mittels des vorgaengerattributs
							node.getSimilar().get(j).setVisited(true);         //und markieren das word als "besucht"
						}
					}
				}
			}
		} else {    //das startwort wurde nicht gefunden
			System.out.println("Das startWort exisitiert nicht in der Liste");
		}
		return null;
	}

	//ausgeben des gefundenen weges von start nach ende
	public void printPath(Word startWord, Word endWord){
		Word end = this.breadthSearch(startWord,endWord);              //hier wird die suche ausgefuehrt
		if(end != null){                                               //falls ein weg gefunden wurde
			ArrayList<Word> path = new ArrayList<Word>();              //alle woerter auf dem weg in path speichern (rueckwaerts)
			path.add(end);
			
			Word currentWord = end;
			while(!currentWord.getPredecessor().getValue().equals(startWord.getValue())){  //falls einer der vorgaenger das startwort ist sind wir fertig
				path.add(currentWord.getPredecessor());                                    //solange gehen wir die kette von hinten nach vorne zum naechsten vorgaenger
				currentWord = currentWord.getPredecessor();
			}
			
			path.add(startWord);                                       // hier noch das startwort in die path liste uebernehmen

			for(int k=path.size()-1; k>=0; k--){                       //path liste dann in umgekehrter reihenfolge ausgeben
				System.out.println(path.get(k).getValue());
			}
		} else {
			System.out.println("Kein Weg zwischen den Woertern gefunden");
		}
	}

	public static void main(String[] args) {
		Fourletterwords vier = new Fourletterwords("bin/blatt9/4LetterWords.txt");
		
		//einlesen der datei
		if (!vier.readFile()) {
			System.out.println("Einlesen fehlgeschlagen");
		}
		//liste bauen
		vier.buildAdjacencyList();

		//suchen und ausgeben
		Word startWord = new Word("abys");
		Word endWord = new Word("axle");
		vier.printPath(startWord, endWord);
	}
}
