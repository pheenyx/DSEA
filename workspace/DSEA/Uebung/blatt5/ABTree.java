package blatt5;
/* Programm von Michael Mardaus
 * DSEA Übung 1 
 * Gruppe 6
 * Blatt 5 Aufgabe 1d
 */

import java.util.ArrayList;
import java.util.Collections;

public class ABTree {

    public static void main(String[] args) {
        ABTree tree = new ABTree();
        for (int i = 1; i < 19; ++i) {
        tree.insert(i);
        tree.print();
        }
        for (int i = 5; i < 15; ++i) {
        tree.remove(i);
        tree.print();
        }
        for (int i = 9; i < 12; ++i) {
        tree.insert(i);
        tree.print();
        }


    }
 
    ABTreeRootNode root = new ABTreeRootNode();                  //Wurzelknoten erstellen

 
    //fuegt neuen knoten in den baum ein
    void insert(int value) {
        if (root.children.size() < 2) {                         //die minimal anzahl an kindern ist noch nicht erreicht
            root.children.add(new ABTreeLeaf(root, value));     //knoten wird einfach als kind angehängt
            Collections.sort(root.children);                    //dann neusortiert
        } else {
            root.insert(value);//minimale kinderzahl ist erreicht, dann wird die insert methode der parent klasse gerufen
                               //und geprueft ob die maximal anzahl kinder erreicht ist, dann split. sonst einfügen
        }
    }

    //entfernt knoten aus dem baum, ruft die parent klasse auf
    //diese checkt ob die minimalanzahl kinder erreicht wird, dann wird fusion gerufen. sonst wird entfernt.
    void remove(int value) {
        root.remove(value);
    }

    //gibt den baum aus
    void print() {
        root.print();
        System.out.println();
    }
}

//vaterklasse aus der abstrahiert wird
abstract class ABTreeNode implements Comparable<ABTreeNode> {

    ABTreeInnerNode parent;

    abstract void insert(int value);

    abstract void remove(int value);

    abstract int getLowestKey();

    abstract void print();

    //vergleicht zwei knoten
    //-1 wenn o rechts steht, 1 wenn o links steht
    @Override
    public int compareTo(ABTreeNode o) {
        int result = getLowestKey() - o.getLowestKey();
        return (int) Math.signum(result);
    }
}

//blattklasse
class ABTreeLeaf extends ABTreeNode {

    int value;

    //constructor mit parent und wert setzen
    ABTreeLeaf(ABTreeInnerNode parent, int value) {
        this.value = value;
        this.parent = parent;
    }

    //gibt den wert des blatts aus
    @Override
    int getLowestKey() {
        return value;
    }

    //geschwisterknoten unter dem aktuellen vaterknoten einfügen, dann sortieren
    @Override
    void insert(int value) {
        ABTreeLeaf leaf = new ABTreeLeaf(parent, value);
        parent.children.add(leaf);
        Collections.sort(parent.children);
    }

    //loeschen des knotens mit wert value, mit extra check auf eigenen wert.
    @Override
    void remove(int value) {
        if (this.value != value) {
            return;
        }
        parent.children.remove(this);
    }

    //check ob uebergebenes objekt ein leaf ist mit gleichem wert
    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != ABTreeLeaf.class) {
            return false;
        }
        ABTreeLeaf rhs = (ABTreeLeaf) obj;
        return value == rhs.value;
    }

    //aktuellen knoten ausgeben
    @Override
    void print() {
        System.out.print("(" + value + ")");
    }
}

//innerer knoten-klasse
class ABTreeInnerNode extends ABTreeNode {

    static int a = 2;
    static int b = 5;
    ArrayList<ABTreeNode> children = new ArrayList<ABTreeNode>();

    //constructor mit parent
    ABTreeInnerNode(ABTreeInnerNode parent) {
        this.parent = parent;
    }

    //kleinsten wert der eigenen kinder ausgeben
    @Override
    int getLowestKey() {
        return this.children.get(0).getLowestKey();
    }

    //sucht den korrekten knoten zum einfuegen des wertes value
    ABTreeNode getTargetNode(int value) {
        int i;
        for (i = 1; i < this.children.size(); ++i) {          //alle kinder durchsuchen
            ABTreeNode child = this.children.get(i);
            int childLowestKey = child.getLowestKey();        //nach ihrem kleinsten wert
            if (value < childLowestKey) {                     //fertig wenn value < als der kleinste wert des kinds
                break;                                        //(man ist jetzt ein i zu weit gegangen)
            }
        }
        ABTreeNode child = children.get(i - 1);               //dann das letzte kind davor zurückgeben
        return child;
    }

    //neuen knoten einfuegen, dazu suchen wo dann einfuegen, dann groesse pruefen (ggf.split).
    @Override
    void insert(int value) {
        ABTreeNode child = getTargetNode(value);
        child.insert(value);
        checkSize();
    }

    //loeschen, dazu suchen wo dann loeschen, dann groesse pruefen (ggf.fusion)
    @Override
    void remove(int value) {
        ABTreeNode child = getTargetNode(value);
        child.remove(value);
        checkSize();
    }

    //wenn die groessen nicht stimmen fusionieren oder splitten
    void checkSize() {
        if (children.size() < a) {
            fusion();
        }
        if (children.size() > b) {
            split();
        }
    }

    //split operation
    void split() {
        ABTreeInnerNode newNode = new ABTreeInnerNode(this.parent);    //neuen knoten erstellen und an den vater haengen
        this.parent.children.add(newNode);                             //knoten den kindern des vater zuweisen
        newNode.parent = this.parent;                                  //vater setzen
        int j = (this.children.size()) / 2;                            //kinder uebertragen
        while (j < this.children.size()) {
        newNode.children.add(this.children.get(j));
        this.children.remove(j);
        }
/*        for (int i = this.children.size()/2 ; i<this.children.size() ; i++){
            newNode.children.add(this.children.get(i));
            this.children.remove(i);
        }*/
    }
    
    //fusion operation
    void fusion() {
       
    }

    //alle kinder ausgeben
    void print() {
        System.out.print("(");
        for (int i = 0; i < children.size(); ++i) {
            children.get(i).print();
        }
        System.out.print(")");
    }
}

//wurzelklasse
class ABTreeRootNode extends ABTreeInnerNode {

    ABTreeInnerNode rootParent;

    //neuen wurzelknoten erschaffen
    ABTreeRootNode() {
        super(new ABTreeInnerNode(null));
        rootParent = parent;
        parent.children.add(this);
    }

    //einfuegen unter wurzelknoten
    @Override
    void insert(int value) {
        if (this.children.size() + 1 <= b) {                      //wenn noch keine b kinder da sind
            super.insert(value);                                  //einfach einfuegen
        } else {                                                  //sonst muss gesplittet werden
            super.insert(value);
            ABTreeInnerNode newNode = new ABTreeInnerNode(this);  //neuen knoten erstellen
            newNode.children.addAll(this.children);               //alle kinder zum neuen knoten schieben
            this.children.clear();                                //alle eigenen kinder loeschen
            this.children.add(newNode);                           //neuen knoten als kind anhaengen
            newNode.parent = this;
            int j = 1;
            while (j < this.parent.children.size()) {             //geschwister als eigene kinder anhaengen
                this.children.add(this.parent.children.get(j));
                j++;
            }
        }
    }

    //loeschen unter der wurzel
    @Override
    void remove(int value) {
        super.remove(value);
    }

    //wenn mehr als b kinder vorliegen, splitten
    @Override
    void checkSize() {
        if (children.size() > b) {
            split();
        }
    }
}
