package blue.made.angleserver.util.quadtree;

/**
 * Created by Sam Sartor on 3/8/16.
 */
public class BoolQuadTree {
	abstract class TreeNode {
		public int w;

		public TreeNode(int w) {
			this.w = w;
		}

		abstract boolean get(int i, int j);

		abstract TreeNode set(int i, int j, boolean to);

		abstract int constant();
	}

	class ParentTreeNode extends TreeNode {
		TreeNode[] nodes = new TreeNode[16];

		public ParentTreeNode(int w) {
			super(w);
		}

		@Override
		public boolean get(int i, int j) {
			int hw = w / 4;
			return nodes[j * 4 + i].get(i % hw, j % hw);
		}

		@Override
		public TreeNode set(int i, int j, boolean to) {
			return null;
		}

		@Override
		int constant() {
			return 0;
		}
	}

	class EndTreeNode extends TreeNode {
		short data = 0;

		public EndTreeNode(int w, boolean fill) {
			super(w);
			if (fill)
				data = -1;
		}

		@Override
		public boolean get(int i, int j) {
			int k = j * 4 + i;
			return ((data >>> k) & 1) != 0;
		}

		@Override
		public TreeNode set(int i, int j, boolean to) {
			if ((to && data == -1) || (!to && data == 0))
				return this;
			int k = j * 4 + i;
			if (w == 4) {
				if (to)
					data |= 1 << k;
				else
					data &= ~(1 << k);
				return this;
			} else {
				int dw = w / 4;
				ParentTreeNode node = new ParentTreeNode(w);
				for (int ind = 0; ind < 16; ind++) {
					EndTreeNode newend = new EndTreeNode(dw, ((data >>> k) & 1) != 0);
					node.nodes[ind] = newend;
					if (ind == k)
						node.nodes[ind] = newend.set(i % dw, j % dw, to);
				}
				return node;
			}
		}

		@Override
		int constant() {
			if (data == 0)
				return -1; // All false (0b000000...)
			if (data == -1)
				return 1; // All true (0b111111...)
			return 0; // Mixed (0b010110...)
		}
	}

	private int width;
	private TreeNode root;

	public BoolQuadTree(int depth, boolean init) {
		width = 1 << (depth * 2); // 1 << (n * 2) = 2^(n * 2) = 4^n
		root = new EndTreeNode(width, init);
	}

	//TODO get and set
	//TODO de-subdivide when possible
}
