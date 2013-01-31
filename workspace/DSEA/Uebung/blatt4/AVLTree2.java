package blatt4;
/* Programm von Michael Mardaus
 * DSEA Übung 1 
 * Gruppe 6
 * Blatt 4 Aufgabe 3
 */

public class AVLTree2 {

	class Node {
		Node left, right;					//Childnodes
		int balance, key;					//balance der Teilbaeume von hier -1=linkslastig, 1=rechtslastig, 0=ausgeglichen

		Node(int x) {						//constructor
			key = x;						//traegerwert des baumes
			left = right = null;
			balance = 0;
		}
	}

	public Node root;						//zum eroeffnen der wurzel

	public AVLTree2() {						//zum eroefnnen des baumes
		root = null;
	}
	
    //ueberladene funktion fuer tiefe an der wurzel
	public int depth() {
		return depth(root);
	}

	//zum ermitteln der tiefe rekursiv
	public int depth(Node node) {
		if (node == null)
			return 0;
		return 1 + Math.max(depth(node.left), depth(node.right));
	}

	//rechtsrotieren an knoten v
	public Node rotateRight(Node v) {
		Node u = v.left;			//das linke kind von v nach oben ziehen und dabei
		v.left = u.right;			//den rechten teilbaum des "linken kinds von v" abstreifen an v (TB links an v anhaengen)
		u.right = v;				//dann "linkes kind von v" mit v verbinden, v ist danach rechtes kind.

		v.balance = 0;				//balancen sind danach ausgeglichen
		u.balance = 0;

		return u;					//u aka "linkes kind von v" ist dann die neue "wurzel"
	}

	//linksrotieren an knoten v
	public Node rotateLeft(Node v) {
		Node u = v.right;			//das rechte kind von v nach oben ziehen und dabei
		v.right = u.left;			//den linken teilbaum des "rechten kinds von v" abstreifen an v (TB rechts an v anhaengen)
		u.left = v;					//dann "rechtes kind von v" mit v verbinden, v ist danach linkes kind.

		v.balance = 0;				//balancen sind danach ausgeglichen
		u.balance = 0;

		return u;					//u aka "rechtes kind von v" ist dann die neue "wurzel"
	}

	//erst linksrotieren (an w.left) dann rechtsrotieren an w
	public Node rotateLeftRight(Node w) {
		Node u = w.left;			//im prinzip wie oben, man streift nach einander erst 
		Node v = u.right;
		u.right = v.left;			//den linken teilbaum von w.left.right ab an w.left ab
		v.left = u;
		w.left = v.right;			//und dann den rechten teilbaum von w.left.right
		v.right = w;				//an w selbst. 

		w.balance = (v.balance == 1) ? -1 : 0;  //w wird linkslastig wenn v rechtslastig war, sonst ausgeglichen
		u.balance = (v.balance == -1) ? 1 : 0;	//u wird rechtslastig wenn v linkslastig war, sonst ausgeglichen
		v.balance = 0;							//v ist immer ausgeglichen danach

		return v;					//v ist neue "wurzel"
	}

	//erst rechtsrotieren (an w.right) dann rechtsrotieren an w
	public Node rotateRightLeft(Node w) {
		Node u = w.right;			//im prinzip wie oben, man streift nach einander erst 
		Node v = u.left;
		u.left = v.right;			//den rechten teilbaum von w.right.left ab an w.right ab
		v.right = u;
		w.right = v.left;			//und dann den linken teilbaum von w.right.left
		v.left = w;					//an w selbst. 

		w.balance = (v.balance == -1) ? 1 : 0;	//w wird rechtslastig wenn v linkslastig war, sonst ausgeglichen
		u.balance = (v.balance == 1) ? -1 : 0;	//u wird linkslastig wenn v rechtslastig war, sonst ausgeglichen
		v.balance = 0;							//v ist immer ausgeglichen danach

		return v;					//v ist neue "wurzel"
	}

	//ueberladenes insert, das erkennt ob an der wurzel eingefuegt wird
	public void insert(int x) {
		if (root == null)
			root = new Node(x);
		else
			root = insert(root, x);
	}

	//globales flag um das rebalancing auszuloesen
	boolean rebalance;

	public Node insert(Node node, int x) {
		if (x > node.key) {						//einfuegen in den rechten teilbaum
			if (node.right == null) {			//wenn noch nichts rechts
				node.right = new Node(x);		//neuen knoten machen
				node.balance++;					//balance rechtsverschieben
				rebalance = (node.balance > 0);	//wenn jetzt rechtslastig muss ggf rebalanced werden
				return node;
			}

			node.right = insert(node.right, x);	//wenn rechts schon was da war rekursiv neu einfuegen dort
			if (!rebalance)						//wenn das flag noch false ist fertig
				return node;

			if (node.balance < 0) {				//flag steht auf rebalance, also schauen was zutun ist
				node.balance = 0;				//wenn es linkslastig war, dann ist jetzt alles ausgeglichen
				rebalance = false;
				return node;
			}
			if (node.balance == 0) {			//wenn er ausgeglichen war ist er jetzt rechtslastig
				node.balance = 1;
				return node;
			}
			if (node.balance > 0) {				//wenn er schon rechtslastig war muss rotiert werden
				rebalance = false;				//flag schon mal abhaengen, da jetzt ausgeglichen wird
				if (node.right.balance == 1)	//wenn am rechten kind immernoch rechtslast ist 
					return rotateLeft(node);	//wird linksrotiert
				else
					return rotateRightLeft(node);	//sonst wird erst rechts, dann links rotiert
			}
		} else { // if (x < node.key) {			//einfuegen in den linken teilbaum
			if (node.left == null) {			//wenn links noch nichts ist
				node.left = new Node(x);		//neuen knoten erstellen
				node.balance--;					//balance linksverschieben
				rebalance = (node.balance < 0);	//wenn jetzt linkslastig ist muss rebalanced werden
				return node;					
			}

			node.left = insert(node.left, x);	//wenn links schon was da war rekursiv neu einfuegen dort
			if (!rebalance)						//wenn das flag noch false ist fertig
				return node;

			if (node.balance > 0) {				//flag steht auf rebalance, also schauen was zutun ist
				node.balance = 0;				//wenn es rechtslastig war, dann ist jetzt alles ausgeglichen
				rebalance = false;
				return node;
			}
			if (node.balance == 0) {			//wenn er ausgeglichen war ist er jetzt rechtslastig
				node.balance = -1;
				return node;
			}
			if (node.balance < 0) {				//wenn er schon linkslastig war muss rotiert werden
				rebalance = false;				//flag schon mal abhaengen, da jetzt ausgeglichen wird
				if (node.left.balance == -1)	//wenn am links kind immernoch linkslast ist 
					return rotateRight(node);	//wird rechtsrotiert
				else
					return rotateLeftRight(node);	//sonst wird erst links, dann recht rotiert
			}
		}
		return null;

	}

	static public void main(String[] args) {
		int n = 10000000;

		AVLTree2 T = new AVLTree2();
		for (int i = n - 1; i >= 0; i--)
			T.insert(i);

		System.out.println(T.depth());
	}

}