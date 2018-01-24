package tree;

import util.Helper;
import util.TreeNode;

public class Traversals {

	public static void main(String []args){
		TreeNode tree = Helper.getSampleTree();
		System.out.println("In order:");
		inOrder(tree);
		System.out.println("\nPre order:");
		preOrder(tree);
		System.out.println("\nPost order:");
		postOrder(tree);
	}
	
	public static void preOrder(TreeNode tree){
		if(tree == null)
			return;
		System.out.print(tree.data);
		preOrder(tree.left);
		preOrder(tree.right);
	}
	public static void inOrder(TreeNode tree){
		if(tree == null)
			return;
		inOrder(tree.left);
		System.out.print(tree.data);
		inOrder(tree.right);
	}
	public static void postOrder(TreeNode tree){
		if(tree == null)
			return;
		postOrder(tree.left);
		postOrder(tree.right);
		System.out.print(tree.data);
	}
	
}
