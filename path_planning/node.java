/* Node.java
 * 		Contains the necessary attributes and function for a functional Node class:
 * 		private Node left 			- adjacent left node relative to current node in the room
 *		private Node right 			- adjacent right node relative to current node in the room
 *		private Node parent			- adjacent parent node relative to current node in the room
 *		private Node down 			- adjacent downwards node relative to current node in the room
 *		private Node up 			- adjacent upwards node relative to current node in the room
 *		private int gScore			- score for path cost	
 *		private float fScore		- sum of path cost and heuristic cost
 *		private float hScore		- heuristic score for current node
 *		private int[] item 			- coordinates of current node
 *											item[0] : x coordinate of node
 *											item[1] : y coordinate of node	
 *		private int scenario		- scenario for current node:
 *											-1: obstacle encountered/repeated coordinate 
 *											0: top left corner
 * 											1: top right corner 
 * 											2: bottom left corner 
 * 											3: bottom right corner
 * 											4: top side 
 * 											5: bottom side 
 * 											6: left side 
 * 											7: right side 
 * 											8: anywhere else
 */

package path_planning;

import java.util.ArrayList;

public class Node {
	private Node left = null;
	private Node right = null;
	private Node parent = null;
	private Node down = null;
	private Node up = null;
	private int gScore; //ADD SET/GET METHODS
	private float fScore; //ADD SET/GET METHODS
	private float hScore; //ADD SET/GET METHODS
	private int scenario = -1;
	private int[] item = null;

	public Node() {

	}

	public Node(int[] item) {
		this.setItem(item);
	}

	public int[] getItem() {
		return this.item;
	}

	public void setItem(int[] item) {
		int temp[] = new int[2];
		this.item = temp;
		this.item = item;
	}

	public Node getLeft() {
		return this.left;
	}

	public void setLeft(Node left) {
		this.left = new Node();
		this.left = left;
	}

	public Node getDown() {
		return this.down;
	}

	public void setDown(Node down) {
		this.down = new Node();
		this.down = down;
	}

	public Node getRight() {
		return this.right;
	}

	public void setRight(Node right) {
		this.right = new Node();
		this.right = right;
	}

	public Node getParent() {
		return this.parent;
	}

	public void setParent(Node parent) {
		this.parent = new Node();
		this.parent = parent;
	}
	public Node getUp() {
		return this.up;
	}

	public void setUp(Node up) {
		this.up = new Node();
		this.up = up;
	}

	public void setScenario(int scenario) {
		this.scenario = scenario;
	}

	public int getScenario() {
		return this.scenario;
	}

	public float getHeuristic(){
		return this.hScore;
	}

	public void setHScore(int[] rendezvous){
		this.hScore = Heuristic.heuristicSLD(rendezvous, this.item);
	}
	public void setGScore(int depth){
		this.gScore = depth/5;
	}
	public float getGScore(){

		return this.gScore;
	}
	public float getfScore(){
		this.fScore = this.getHeuristic() + this.getGScore();
		//System.out.println("Node: "+ this.getItem()[0]+","+this.getItem()[1] +": f score: "+this.fScore);
		return this.fScore;
	}
}
