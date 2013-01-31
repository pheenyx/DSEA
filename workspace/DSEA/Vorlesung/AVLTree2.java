public class AVLTree2 {

	class Node {
		Node left, right;
		int balance, key;

		Node(int x) { key = x; left = right = null; balance = 0; }
	}

	public Node root;

	public AVLTree2() {
		root = null;
	}

	public int depth() {
		return depth(root);
	}

	public int depth(Node node) {
		if (node == null) return 0;
		return 1+Math.max(depth(node.left), depth(node.right));
	}

	public Node rotateRight(Node v) {
		// ...
		return null;
	}

	public Node rotateLeft(Node v) {
		Node u = v.right;
		v.right = u.left;
		u.left = v;

		v.balance = 0;
		u.balance = 0;

		return u;
	}

	public Node rotateLeftRight(Node w) {
		// ...
		return null;
	}

	public Node rotateRightLeft(Node w) {
		Node u = w.right;
		Node v = u.left;
		u.left = v.right;
		v.right = u;
		w.right = v.left;
		v.left = w;

		w.balance = (v.balance == -1)? 1 : 0;
		u.balance = (v.balance ==  1)? -1 : 0;
		v.balance = 0;

		return v;
	}

	public void insert(int x) {
		if (root == null) root = new Node(x);
		else root = insert(root,x);
	}

	boolean rebalance;

	public Node insert(Node node, int x) {
		if (x > node.key) {
			if (node.right == null) {
				node.right = new Node(x);
				node.balance++;
				rebalance = (node.balance > 0);
				return node;
			}
			
			node.right = insert(node.right,x);
			if (!rebalance) return node;
			
			if (node.balance < 0) {
				node.balance = 0;
				rebalance = false;
				return node;
			}
			if (node.balance == 0) {
				node.balance = 1;
				return node;
			}
			if (node.balance > 0) {
				rebalance = false;
				if (node.right.balance == 1) return rotateLeft(node);
				else return rotateRightLeft(node);
			}
		} 
		// ...
		return null;

	}

	static public void main(String[] args) {
		int n = 10000000;
		
		AVLTree2 T = new AVLTree2();
		for(int i=n-1;i>=0;i--)
			T.insert(i);

		System.out.println(T.depth());
	}

}
