package blatt8;

/* Programm von Michael Mardaus
 * DSEA Übung 1 
 * Gruppe 6
 * Blatt 8 Aufgabe 3
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

class Star implements Comparable<Star> {

	static int PRIME = 2053;
	int x, y, z;

	public Star(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public int compareTo(Star o) {
		int v;
		v = x - o.x;
		if (v != 0)
			return v;
		v = y - o.y;
		if (v != 0)
			return v;
		v = z - o.z;
		if (v != 0)
			return v;
		return 0;
	}

	// hashed die adresse des sterns in jeder dimension auf 0,..,9
	// von jeder dimension wird nur der hunderter genutzt und verkettet
	// zb 123,456,789 ==> 147
	@Override
	public int hashCode() {
		int hx,hy,hz,hash;
		hx = this.x / 100;
		hy = this.y / 100;
		hz = this.z / 100;
		
		hash = hx*100 + hy*10 + hz;

		return hash;
	}
}

public class StarMap {

	static String FILENAME = "bin/blatt8/stars.txt";
	static int NUM_STARS = 1000;

	// a collection containing all stars
	Collection<Star> stars = new ArrayList<>(NUM_STARS);
	Star centerStar;
	int radius;
	//1000er-array of arraylists fuer die hashtable
	@SuppressWarnings("unchecked")
	ArrayList<Star>[] hashTable = new ArrayList[1000];

	public static void main(String[] args) throws FileNotFoundException {

		// load stars and create hashtable
		StarMap starMap = new StarMap();
		starMap.loadStars();
		System.out.println("# Stars: " + starMap.stars.size());
		System.out.println("# Star: " + starMap.centerStar.x + ", "
				+ starMap.centerStar.y + ", " + starMap.centerStar.z);
		System.out.println("# Radius: " + starMap.radius);
		starMap.calcHashTable();

		// get nearest Stars
		Collection<Star> nearestStars = starMap.getNearest(starMap.centerStar,
				starMap.radius);

		// print nearest Stars
		SortedSet<Star> nearestStarsSorted = new TreeSet<>(nearestStars);
		System.out.println("nearest stars: " + nearestStarsSorted.size());
		for (Star star : nearestStarsSorted)
			System.out.format("%d %d %d\n", star.x, star.y, star.z);
	}

	void loadStars() throws FileNotFoundException {
		Scanner s = new Scanner(new File(FILENAME));
		for (int i = 0; i < NUM_STARS; ++i) {
			int x = s.nextInt();
			int y = s.nextInt();
			int z = s.nextInt();
			stars.add(new Star(x, y, z));
		}

		int x = s.nextInt();
		int y = s.nextInt();
		int z = s.nextInt();
		centerStar = new Star(x, y, z);
		radius = s.nextInt();
		s.close();
	}

	void calcHashTable() {
		//initialisieren der 1000 arraylists
	    for (int i = 0; i<1000; i++){
	    	ArrayList<Star> starList = new ArrayList<Star>(); 
	    	hashTable[i] = starList;
	    }
	    
	    //alle stars werden hier gehashed und in der hashtable gespeichert
	    for (Star star : this.stars){
	    	hashTable[star.hashCode()].add(star);
	    }
	    return;
	}

	//Funktion die alle Sterne zu einem gegebenen Stern und einem radius ermittelt und als Star-Collection zurueck gibt
	Collection<Star> getNearest(Star star, int radius) {
		Collection<Star> starCollect = new ArrayList<Star>();
		//pro x,y,z die minimal und maximal moegliche wuerfel koordinate ermitteln
		for ( int x = hashedMinMax(star.x, radius)/10; x <= hashedMinMax(star.x, radius)%10; x++){
			for ( int y = hashedMinMax(star.y, radius)/10; y <= hashedMinMax(star.y, radius)%10; y++){
				for ( int z = hashedMinMax(star.z, radius)/10; z <= hashedMinMax(star.z, radius)%10; z++){
					// solange in der hashtable an der koordinate noch ein stern ist haben wir einen kandidaten
					while (!hashTable[x*100+y*10+z].isEmpty()){
						Star candidate = hashTable[x*100+y*10+z].get(0);
						//checken ob die norm des sternes passt
						if (norm(star,candidate) < 140){
							starCollect.add(candidate); //dann in die ausgabe aufnehmen
						}
						// der stern kann aus der hashtable raus, damit die while schleife weiterlaueft.
						hashTable[x*100+y*10+z].remove(0);
					}
				}
			}
		}
		return starCollect;
	}
	
	//Maximumsnorm von zwei Sternen
	int norm (Star star, Star candidate){
		return Math.max( Math.max(Math.abs(star.x-candidate.x),Math.abs(star.y-candidate.y)) ,Math.abs(star.z-candidate.z));
	}
	
	//funktion die min und max hash koordinate zu einer koordinate und radius zurueck gibt
	//zwei rueckgabeparameter werden in einer variable zurueckgegeben
	int hashedMinMax (int x, int radius){
		int min,max;
		//koordinate um radius nach links und rechts schieben und hashen
		min = (x - radius) / 100;
		max = (x + radius) / 100;
		//min und max wird bei 0 bzw. 9 gecappt
		if (min < 0) min = 0;
		if (max > 9) max = 9;
		return min*10 + max;
	}
}
