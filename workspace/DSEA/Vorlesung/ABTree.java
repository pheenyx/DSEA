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

	ABTreeRootNode root = new ABTreeRootNode();

	void insert(int value) {
		if (root.children.size() < 2) {
			root.children.add(new ABTreeLeaf(root, value));
			Collections.sort(root.children);
		} else {
			root.insert(value);
		}
	}

	void remove(int value) {
		root.remove(value);
	}

	void print() {
		root.print();
		System.out.println();
	}
}

abstract class ABTreeNode implements Comparable<ABTreeNode> {

	ABTreeInnerNode parent;

	abstract void insert(int value);

	abstract void remove(int value);

	abstract int getLowestKey();

	abstract void print();

	@Override
	public int compareTo(ABTreeNode o) {
		int result = getLowestKey() - o.getLowestKey();
		return (int) Math.signum(result);
	}
}

class ABTreeLeaf extends ABTreeNode {

	int value;

	ABTreeLeaf(ABTreeInnerNode parent, int value) {
		this.value = value;
		this.parent = parent;
	}

	@Override
	int getLowestKey() {
		return value;
	}

	@Override
	void insert(int value) {
		ABTreeLeaf leaf = new ABTreeLeaf(parent, value);
		parent.children.add(leaf);
		Collections.sort(parent.children);
	}

	@Override
	void remove(int value) {
		if (this.value != value)
			return;
		parent.children.remove(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != ABTreeLeaf.class)
			return false;
		ABTreeLeaf rhs = (ABTreeLeaf) obj;
		return value == rhs.value;
	}

	void print() {
		System.out.print("(" + value + ")");
	}
}

class ABTreeInnerNode extends ABTreeNode {

	static int a = 2;
	static int b = 5;
	ArrayList<ABTreeNode> children = new ArrayList<>();

	ABTreeInnerNode(ABTreeInnerNode parent) {
		this.parent = parent;
	}

	@Override
	int getLowestKey() {
		return children.get(0).getLowestKey();
	}

	ABTreeNode getTargetNode(int value) {
		int i;
		for (i = 1; i < children.size(); ++i) {
			ABTreeNode child = children.get(i);
			int childLowestKey = child.getLowestKey();
			if (value < childLowestKey)
				break;
		}
		ABTreeNode child = children.get(i - 1);
		return child;
	}

	@Override
	void insert(int value) {
		ABTreeNode child = getTargetNode(value);
		child.insert(value);
		checkSize();
	}

	@Override
	void remove(int value) {
		ABTreeNode child = getTargetNode(value);
		child.remove(value);
		checkSize();
	}

	void checkSize() {
		if (children.size() < a)
			fusion();
		if (children.size() > b)
			split();
	}

	void split() {
		// TODO Aufgabe 5.1
	}

	void fusion() {
		// TODO Aufgabe 5.1
	}

	void print() {
		System.out.print("(");
		for (int i = 0; i < children.size(); ++i)
			children.get(i).print();
		System.out.print(")");
	}
}

class ABTreeRootNode extends ABTreeInnerNode {

	ABTreeInnerNode rootParent;

	ABTreeRootNode() {
		super(new ABTreeInnerNode(null));
		rootParent = parent;
		parent.children.add(this);
	}

	@Override
	void insert(int value) {
		super.insert(value);

		// TODO Aufgabe 5.1
	}

	@Override
	void remove(int value) {
		super.remove(value);

		// TODO Aufgabe 5.1
	}

	@Override
	void checkSize() {
		if (children.size() > b)
			split();
	}
}
