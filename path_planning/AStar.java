package path_planning;

import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ArrayList;
import java.io.IOException;

public class A_Star {

	//duplicateCheck
	// checks visited for c
	// if c is in visited, return -1
	// otherwise if search ends and c hasn't been found in visited, return 0
	public static int duplicateCheck(PriorityQueue<Node> leaves,Queue<Node> visited, int[] coordinates){

		PriorityQueue<Node> leafCopy = new PriorityQueue<Node>(leaves);
		Queue<Node> copy = new LinkedList<Node>(visited);

		while(!copy.isEmpty()){
			Node temp = new Node();
			temp = copy.poll();
			if(temp.getItem()[0] == coordinates[0] && temp.getItem()[1] == coordinates[1]){
				return -1;
			}			
		}

		while (!leafCopy.isEmpty()){
			Node temp2 = new Node();
			temp2 = leafCopy.poll();
			if(temp2.getItem()[0] == coordinates[0] && temp2.getItem()[1] == coordinates[1]){
				return -1;
			}
		}
		return 0;
	}

	public static class Sort implements Comparator<Node> {

		// Sorting by the Nodes fScore:
		//     Used by PriorityQueue open to sort all nodes such that the one at the front of the queue
		//	   is the one with the lowest f-score
		public int compare (Node x, Node y){
			int one = (int) x.getfScore();
			int two = (int) y.getfScore();

			return (one - two);
		}
	}

	// A* algorithm
	// Inputs:
	//	File_in f - object used for reading in text file and parsing all the data
	//			  - used to pull useful variables from input text
	public static int[][][] AStarAlgorithm(File_in f) throws IOException {
		
		Sort s = new Sort();	// used by PriorityQueue to sort nodes by f-score

		int depth = 1;			// initialize depth of child nodes
		int[][][] fullPaths = new int[f.getNum()][][];		// used to store all paths for nodes
		int[][] room = f.getMap();		// get room from file_in class
		int[] start;					// used to store root of each robot's tree
		int[] rendezvous = f.getRendezvous();	// get destination of all robots from file_in class
		
		int[][] path = null;			// used to store each robot's path
		
		Node current = new Node();

		//For each robot in the file, get the robots path to the goal node
		for (int i = 0; i < f.getNum(); i++){
			System.out.println("--------------------------------------------------------");
			ArrayList<int[]> coordinates = new ArrayList<int[]>();		// used to store path in coordinate form

			PriorityQueue<Node> open = new PriorityQueue<Node>(30, s);	// represents frontier at each iteration (contains nodes for expansion)
			Queue<Node> closed = new LinkedList<Node>();				// holds nodes that have already been traversed

			start = f.getRoboPos()[i];				// the robots starting position
			current.setItem(start);					// the initial position of the robot
			current.setGScore(Integer.MAX_VALUE);	// set root of tree to have large f-score to prevent looping

			open.offer(current);			// inserting the initial position of the robot into the priority queue


			// while frontier isn't empty and goal hasn't been reached
			while (!open.isEmpty()){
				if((current.getItem()[0] == rendezvous[0] && current.getItem()[1] == rendezvous[1])) {
					break;		// if rendezvous point reached, break
				}

				// temporary arrays for each direction the robot can take
				int[] tempCoordUp = new int[2];
				int[] tempCoordLeft = new int[2];
				int[] tempCoordRight = new int[2];
				int[] tempCoordDown = new int[2];

				int[] tempDown = new int[2];
				int[] tempUp = new int[2];
				int[] tempRight = new int[2];
				int[] tempLeft = new int[2];

				// get scenario (depends on where current is)				
				switch(CheckNode.check(room,  current.getItem())){

				case -1:
					current = open.poll();
					break;

					//Top Right corner (Get down and left)
				case 0: 
					// Set the temporary arrays for the down and right coordinates
					tempCoordDown[1] = current.getItem()[1];
					tempCoordDown[0] = current.getItem()[0];
					tempCoordRight[0] = current.getItem()[0];
					tempCoordRight[1] = current.getItem()[1];

					//down
					tempDown = tempCoordDown;				// populating Node properties
					tempDown[1]++;							// increment y of co-ordinate by 1

					if (!(CheckNode.check(room, tempDown) == -1)){				// don't create node if position is an obstacle
						if (duplicateCheck(open,closed, tempDown) == 0){		// if position exists in closed, do not create node
							Node down = new Node();			// create new node
							down.setItem(tempDown);			// set all properties for down node
							if(tempDown[0]==rendezvous[0] && tempDown[1] ==rendezvous[1]) {	// if rendezvous found, manipulate f-score of node to ensure it is put to the front of the frontier
								depth=0;
							}
							down.setGScore(depth);			// set all properties for node
							down.setHScore(rendezvous);
							down.setScenario(0);

							down.setParent(current);		// link down to current
							current.setDown(down);			// link current to down

							open.offer(down);				// add down to open
						}
					}
					//right
					tempRight = tempCoordRight;
					tempRight[0]++;					// increment x co-ordinate

					if (!(CheckNode.check(room, tempRight) == -1)){				// don't create node if position is an obstacle
						if (duplicateCheck(open,closed, tempRight) == 0){		// if position exists in closed, do not create node
							Node right = new Node();		// make right node if duplicate doesn't exist in closed
							right.setItem(tempRight);		// set properties for node
							if(tempRight[0]==rendezvous[0] && tempRight[1] ==rendezvous[1]) {	// if rendezvous found, manipulate f-score of node to ensure it is put to the front of the frontier
								depth=0;
							}
							right.setGScore(depth);			// set properties for node
							right.setHScore(rendezvous);
							right.setScenario(0);

							right.setParent(current);		// link right to current
							current.setRight(right);		// link current to right			

							open.offer(right);				// add right to open
						}
					}
					closed.add(current);					// add parent node to closed
					current = open.poll();					// get new node for expansion from frontier
					break;

					//Top Right corner (Get down and left)
				case 1: 
					tempCoordDown[1] = current.getItem()[1];
					tempCoordDown[0] = current.getItem()[0];
					tempCoordLeft[0] = current.getItem()[0];
					tempCoordLeft[1] = current.getItem()[1];

					//down
					tempDown = tempCoordDown;				// populating Node properties
					tempDown[1]++;						// increment y of co-ordinate by 1

					if (!(CheckNode.check(room, tempDown) == -1)){
						if (duplicateCheck(open,closed, tempDown) == 0){
							Node down = new Node();			// make down node if duplicate doesn't exist in closed
							down.setItem(tempDown);			// set all properties for down node
							if(tempDown[0]==rendezvous[0] && tempDown[1] ==rendezvous[1]) {
								depth=0;
							}
							down.setGScore(depth);
							down.setHScore(rendezvous);
							down.setScenario(1);

							down.setParent(current);		// link down to current
							current.setDown(down);			// link current to down

							open.offer(down);				// add down to open
							}
					}

					//left
					tempLeft = tempCoordLeft;
					tempLeft[0]--;

					if (!(CheckNode.check(room, tempLeft) == -1)){
						if (duplicateCheck(open,closed, tempLeft) == 0){
							Node left = new Node();		// make left node if duplicate doesn't exist in closed
							left.setItem(tempLeft);
							if(tempLeft[0]==rendezvous[0] && tempLeft[1] ==rendezvous[1]) {
								depth=0;
							}
							left.setGScore(depth);
							left.setHScore(rendezvous);
							left.setScenario(1);

							left.setParent(current);		// link left to current
							current.setLeft(left);		// link current to left			

							open.offer(left);				// add left to open
						}
					}
					closed.add(current);
					current = open.poll();
					break;

					//Bottom Left corner (Get up and right)
				case 2:
					tempCoordUp[0] = current.getItem()[0];
					tempCoordUp[1] = current.getItem()[1];
					tempCoordRight[0] = current.getItem()[0];
					tempCoordRight[1] = current.getItem()[1];


					//Up
					tempUp = tempCoordUp;			// populating Node properties
					tempUp[1]--;						// increment y of co-ordinate by 1

					if (!(CheckNode.check(room, tempUp) == -1)){
						if (duplicateCheck(open,closed, tempUp) == 0){
							Node up = new Node();			// make up node if duplicate doesn't exist in closed
							up.setItem(tempUp);			// set all properties for up node
							if(tempUp[0]==rendezvous[0] && tempUp[1] ==rendezvous[1]) {
								depth=0;
							}
							up.setGScore(depth);
							up.setHScore(rendezvous);
							up.setScenario(2);

							up.setParent(current);		// link up to current
							current.setUp(up);			// link current to up

							open.offer(up);				// add up to open
						}
					}
					//right
					tempRight = tempCoordRight;
					tempRight[0]++;

					if (!(CheckNode.check(room, tempRight) == -1)){
						if (duplicateCheck(open,closed, tempRight) == 0){
							Node right = new Node();		// make right node if duplicate doesn't exist in closed
							right.setItem(tempRight);
							if(tempRight[0]==rendezvous[0] && tempRight[1] ==rendezvous[1]) {
								depth=0;
							}
							right.setGScore(depth);
							right.setHScore(rendezvous);
							right.setScenario(2);

							right.setParent(current);		// link right to current
							current.setRight(right);		// link current to right			

							open.offer(right);				// add right to open
						}
					}
					closed.add(current);
					current = open.poll();
					break;


					//Bottom Right corner (Get up and left)		
				case 3: 
					tempCoordUp[0] = current.getItem()[0];
					tempCoordUp[1] = current.getItem()[1];
					tempCoordLeft[0] = current.getItem()[0];
					tempCoordLeft[1] = current.getItem()[1];

					//Up
					tempUp = tempCoordUp;			// populating Node properties
					tempUp[1]--;						// increment y of co-ordinate by 1

					if (!(CheckNode.check(room,  tempUp) == -1)){
						if (duplicateCheck(open,closed, tempUp) == 0){
							Node up = new Node();			// make up node if duplicate doesn't exist in closed
							up.setItem(tempUp);			// set all properties for up node
							if(tempUp[0]==rendezvous[0] && tempUp[1] ==rendezvous[1]) {
								depth=0;
							}
							up.setGScore(depth);
							up.setHScore(rendezvous);
							up.setScenario(3);

							up.setParent(current);		// link up to current
							current.setUp(up);			// link current to up

							open.offer(up);				// add up to open
						}
					}
					//left
					tempLeft = tempCoordLeft;
					tempLeft[0]--;
					if (!(CheckNode.check(room,  tempLeft) == -1)){
						if (duplicateCheck(open,closed, tempLeft) == 0){
							Node left = new Node();		// make left node if duplicate doesn't exist in closed
							left.setItem(tempLeft);
							if(tempLeft[0]==rendezvous[0] && tempLeft[1] ==rendezvous[1]) {
								depth=0;
							}
							left.setGScore(depth);
							left.setHScore(rendezvous);
							left.setScenario(3);

							left.setParent(current);		// link left to current
							current.setLeft(left);		// link current to left			

							open.offer(left);				// add left to open
						}
					}
					closed.add(current);
					current = open.poll();
					break;

				case 4: //Top Edge (Get left, right and down)
					tempCoordDown[1] = current.getItem()[1];
					tempCoordDown[0] = current.getItem()[0];
					tempCoordRight[0] = current.getItem()[0];
					tempCoordRight[1] = current.getItem()[1];
					tempCoordLeft[0] = current.getItem()[0];
					tempCoordLeft[1] = current.getItem()[1];

					//left
					tempLeft = tempCoordLeft;			// populating Node properties
					tempLeft[0]--;						// increment y of co-ordinate by 1
					if (!(CheckNode.check(room, tempLeft) == -1)){
						if (duplicateCheck(open,closed, tempLeft) == 0){
							Node left = new Node();			// make left node if duplicate doesn't exist in closed
							left.setItem(tempLeft);			// set all properties for left node
							if(tempLeft[0]==rendezvous[0] && tempLeft[1] ==rendezvous[1]) {
								depth=0;
							}
							left.setGScore(depth);
							left.setHScore(rendezvous);
							left.setScenario(4);

							left.setParent(current);		// link left to current
							current.setLeft(left);			// link current to left

							open.offer(left);				// add left to open
						}
					}

					//right
					tempRight = tempCoordRight;
					tempRight[0]++;
					if (!(CheckNode.check(room,  tempRight) == -1)){
						if (duplicateCheck(open,closed, tempRight) == 0){
							Node right = new Node();		// make right node if duplicate doesn't exist in closed
							right.setItem(tempRight);
							if(tempRight[0]==rendezvous[0] && tempRight[1] ==rendezvous[1]) {
								depth=0;
							}
							right.setGScore(depth);
							right.setHScore(rendezvous);
							right.setScenario(4);

							right.setParent(current);		// link right to current
							current.setRight(right);		// link current to right			

							open.offer(right);				// add right to open
						}
					}
					//down
					tempDown = tempCoordDown;
					tempDown[1]++;
					if (!(CheckNode.check(room,  tempDown) == -1)){
						if (duplicateCheck(open,closed, tempDown) == 0){
							Node down = new Node();		// make down node if duplicate doesn't exist in closed
							down.setItem(tempDown);
							if(tempDown[0]==rendezvous[0] && tempDown[1] ==rendezvous[1]) {
								depth=0;
							}
							down.setGScore(depth);
							down.setHScore(rendezvous);
							down.setScenario(4);

							down.setParent(current);		// link down to current
							current.setDown(down);		// link current to down			

							open.offer(down);				// add down to open
						}
					}
					closed.add(current);
					current = open.poll();
					break;

					//Bottom Edge (Get up, right, and left)
				case 5: 
					tempCoordUp[0] = current.getItem()[0];
					tempCoordUp[1] = current.getItem()[1];
					tempCoordRight[0] = current.getItem()[0];
					tempCoordRight[1] = current.getItem()[1];
					tempCoordLeft[0] = current.getItem()[0];
					tempCoordLeft[1] = current.getItem()[1];

					//left
					tempLeft = tempCoordLeft;			// populating Node properties
					tempLeft[0]--;						// increment y of co-ordinate by 1
					if (!(CheckNode.check(room,  tempLeft) == -1)){
						if (duplicateCheck(open,closed, tempLeft) == 0){
							Node left = new Node();			// make left node if duplicate doesn't exist in closed
							left.setItem(tempLeft);			// set all properties for left node
							if(tempLeft[0]==rendezvous[0] && tempLeft[1] ==rendezvous[1]) {
								depth=0;
							}
							left.setGScore(depth);
							left.setHScore(rendezvous);
							left.setScenario(5);

							left.setParent(current);		// link left to current
							current.setLeft(left);			// link current to left

							open.offer(left);				// add left to open
						}
					}
					//right
					tempRight = tempCoordRight;
					tempRight[0]++;
					if (!(CheckNode.check(room,  tempRight) == -1)){
						if (duplicateCheck(open,closed, tempRight) == 0){
							Node right = new Node();		// make right node if duplicate doesn't exist in closed
							right.setItem(tempRight);
							if(tempRight[0]==rendezvous[0] && tempRight[1] ==rendezvous[1]) {
								depth=0;
							}
							right.setGScore(depth);
							right.setHScore(rendezvous);
							right.setScenario(5);

							right.setParent(current);		// link right to current
							current.setRight(right);		// link current to right			

							open.offer(right);				// add right to open
						}
					}

					//Up
					tempUp = tempCoordUp;
					tempUp[1]--;
					if (!(CheckNode.check(room,  tempUp) == -1)){
						if (duplicateCheck(open,closed, tempUp) == 0){
							Node up = new Node();		// make up node if duplicate doesn't exist in closed
							up.setItem(tempUp);
							if(tempUp[0]==rendezvous[0] && tempUp[1] ==rendezvous[1]) {
								depth=0;
							}
							up.setGScore(depth);
							up.setHScore(rendezvous);
							up.setScenario(5);

							up.setParent(current);		// link up to current
							current.setUp(up);		// link current to up			

							open.offer(up);				// add up to open
						}
					}
					closed.add(current);
					current = open.poll();
					break;

					//Left Edge (Get up, down, and right)
				case 6: 
					tempCoordDown[1] = current.getItem()[1];
					tempCoordDown[0] = current.getItem()[0];
					tempCoordUp[0] = current.getItem()[0];
					tempCoordUp[1] = current.getItem()[1];
					tempCoordRight[0] = current.getItem()[0];
					tempCoordRight[1] = current.getItem()[1];

					//down
					tempDown = tempCoordDown;			// populating Node properties
					tempDown[1] = current.getItem()[1]+1;						// increment y of co-ordinate by 1

					if (!(CheckNode.check(room, tempDown) == -1)){
						if (duplicateCheck(open,closed, tempCoordDown) == 0){
							Node down = new Node();			// make left node if duplicate doesn't exist in closed
							down.setItem(tempCoordDown);			// set all properties for left node
							if(tempDown[0]==rendezvous[0] && tempDown[1] ==rendezvous[1]) {
								depth=0;
							}
							down.setGScore(depth);
							down.setHScore(rendezvous);
							down.setScenario(6);

							down.setParent(current);		// link down to current
							current.setDown(down);			// link current to down

							open.offer(down);				// add down to open
						}
					}

					//right
					tempRight = tempCoordRight;
					tempRight[0]++;
					if (!(CheckNode.check(room, tempRight) == -1)){
						if (duplicateCheck(open,closed, tempRight) == 0){
							Node right = new Node();		// make right node if duplicate doesn't exist in closed
							right.setItem(tempRight);
							if(tempRight[0]==rendezvous[0] && tempRight[1] ==rendezvous[1]) {
								depth=0;
							}
							right.setGScore(depth);
							right.setHScore(rendezvous);
							right.setScenario(6);

							right.setParent(current);		// link right to current
							current.setRight(right);		// link current to right		

							open.offer(right);				// add right to open
						}
					}


					//Up
					tempUp = tempCoordUp;
					tempUp[1]--;

					if (!(CheckNode.check(room, tempUp) == -1)){
						if (duplicateCheck(open,closed, tempUp) == 0){
							Node up = new Node();		// make up node if duplicate doesn't exist in closed
							up.setItem(tempUp);
							if(tempUp[0]==rendezvous[0] && tempUp[1] ==rendezvous[1]) {
								depth=0;
							}
							up.setGScore(depth);
							up.setHScore(rendezvous);
							up.setScenario(6);

							up.setParent(current);		// link up to current
							current.setUp(up);		// link current to up		

							open.offer(up);				// add up to open
						}
					}

					closed.add(current);
					current = open.poll();
					break;

					//Right Edge (Get up, down, and left)
				case 7: 
					tempCoordDown[1] = current.getItem()[1];
					tempCoordDown[0] = current.getItem()[0];
					tempCoordUp[0] = current.getItem()[0];
					tempCoordUp[1] = current.getItem()[1];
					tempCoordLeft[0] = current.getItem()[0];
					tempCoordLeft[1] = current.getItem()[1];

					//left
					tempLeft = tempCoordLeft;			// populating Node properties
					tempLeft[0]--;						// increment y of co-ordinate by 1

					if (!(CheckNode.check(room, tempLeft) == -1)){
						if (duplicateCheck(open,closed, tempLeft) == 0){
							Node left = new Node();			// make left node if duplicate doesn't exist in closed
							left.setItem(tempLeft);			// set all properties for left node
							if(tempLeft[0]==rendezvous[0] && tempLeft[1] ==rendezvous[1]) {
								depth=0;
							}
							left.setGScore(depth);
							left.setHScore(rendezvous);
							left.setScenario(7);

							left.setParent(current);		// link left to current
							current.setLeft(left);			// link current to left

							open.offer(left);				// add left to open
						}
					}

					//down
					tempDown = tempCoordDown;
					tempDown[1]++;

					if (!(CheckNode.check(room, tempDown) == -1)){
						if (duplicateCheck(open,closed, tempDown) == 0){
							Node down = new Node();		// make right node if duplicate doesn't exist in closed
							down.setItem(tempDown);
							if(tempDown[0]==rendezvous[0] && tempDown[1] ==rendezvous[1]) {
								depth=0;
							}
							down.setGScore(depth);
							down.setHScore(rendezvous);
							down.setScenario(7);

							down.setParent(current);		// link down to current
							current.setDown(down);		// link current to down			

							open.offer(down);				// add down to open
						}
					}


					//Up
					tempUp = tempCoordUp;
					tempUp[1]--;

					if (!(CheckNode.check(room, tempUp) == -1)){
						if (duplicateCheck(open,closed, tempUp) == 0){
							Node up = new Node();		// make up node if duplicate doesn't exist in closed
							up.setItem(tempUp);
							if(tempUp[0]==rendezvous[0] && tempUp[1] ==rendezvous[1]) {
								depth=0;
							}
							up.setGScore(depth);
							up.setHScore(rendezvous);
							up.setScenario(7);

							up.setParent(current);		// link up to current
							current.setUp(up);		// link current to up			

							open.offer(up);				// add up to open
						}
					}

					closed.add(current);
					current = open.poll();
					break;

					//Inner pixels (Get up, down, left, and right
				case 8: 
					tempCoordDown[1] = current.getItem()[1];
					tempCoordDown[0] = current.getItem()[0];
					tempCoordUp[0] = current.getItem()[0];
					tempCoordUp[1] = current.getItem()[1];
					tempCoordRight[0] = current.getItem()[0];
					tempCoordRight[1] = current.getItem()[1];
					tempCoordLeft[0] = current.getItem()[0];
					tempCoordLeft[1] = current.getItem()[1];

					//left
					tempLeft = tempCoordLeft;			// populating Node properties
					tempLeft[0]--;						// increment y of co-ordinate by 1

					if (!(CheckNode.check(room, tempLeft) == -1)){
						if (duplicateCheck(open,closed, tempLeft) == 0){
							Node left = new Node();			// make left node if duplicate doesn't exist in closed
							left.setItem(tempLeft);			// set all properties for left node
							if(tempLeft[0]==rendezvous[0] && tempLeft[1] ==rendezvous[1]) {
								depth=0;
							}
							left.setGScore(depth);
							left.setHScore(rendezvous);
							left.setScenario(8);

							left.setParent(current);		// link left to current
							current.setLeft(left);			// link current to left

							open.offer(left);				// add left to open
						}
					}


					//right
					tempRight = tempCoordRight;
					tempRight[0]++;
					if (!(CheckNode.check(room, tempRight) == -1)){
						if (duplicateCheck(open,closed, tempRight) == 0){
							Node right = new Node();		// make right node if duplicate doesn't exist in closed
							right.setItem(tempRight);
							if(tempRight[0]==rendezvous[0] && tempRight[1] ==rendezvous[1]) {
								depth=0;
							}
							right.setGScore(depth);
							right.setHScore(rendezvous);
							right.setScenario(8);

							right.setParent(current);		// link right to current
							current.setRight(right);		// link current to right			

							open.offer(right);				// add right to open
						}
					}


					//up
					tempUp = tempCoordUp;
					tempUp[1]--;
					if (!(CheckNode.check(room, tempUp) == -1)){
						if (duplicateCheck(open,closed, tempUp) == 0){
							Node up = new Node();		// make up node if duplicate doesn't exist in closed
							up.setItem(tempUp);
							if(tempUp[0]==rendezvous[0] && tempUp[1] ==rendezvous[1]) {
								depth = 0;
							}
							up.setGScore(depth);
							up.setHScore(rendezvous);
							up.setScenario(8);

							up.setParent(current);		// link up to current
							current.setUp(up);		// link current to up			

							open.offer(up);				// add up to open
						}
					}


					//down
					tempDown = tempCoordDown;			// populating Node properties
					tempDown[1]++;						// increment y of co-ordinate by 1
					if (!(CheckNode.check(room, tempDown) == -1)){
						if (duplicateCheck(open,closed, tempDown) == 0){
							Node down = new Node();			// make left node if duplicate doesn't exist in closed
							down.setItem(tempDown);			// set all properties for left node
							if(tempDown[0]==rendezvous[0] && tempDown[1] ==rendezvous[1]) {
								depth = 0;
							}
							down.setGScore(depth);
							down.setHScore(rendezvous);
							down.setScenario(8);

							down.setParent(current);		// link down to current
							current.setDown(down);			// link current to down

							open.offer(down);				// add down to open
						}
					}

					closed.add(current);
					current = open.poll();
					break;

				}

				depth++;  								// increase depth
			}	// End of While loop
			closed.add(current);
			while (!closed.isEmpty()){
				System.out.println("Visited: "+closed.peek().getItem()[0]+ ", "+closed.peek().getItem()[1]);	// print visited positions on map while visited isn't empty
				coordinates.add(closed.poll().getItem());		// get positions from nodes in closed to get path
			}
			int pathCount = 0;
			if (open.isEmpty()){		// if frontier is empty, solution hasn't been found
				path = new int[0][0];	// robot's path is empty
				pathCount = 0;
				System.out.println("no solution");
			}else{						// otherwise solution has been found
				path = new int[coordinates.size()][2];	// path will hold co-ordinates of robot's path to rendezvous
				pathCount = 0;
				
				System.out.printf("\nPath\n");
				for (int[] n : coordinates) {
					path[pathCount] = n;
	//				System.out.printf("%d,%d\n", path[pathCount][0],path[pathCount][1]);
					pathCount++;
				}
			}
			fullPaths[i] = new int[pathCount][2];	// store robot's path into full array
			fullPaths[i] = path;
			closed = new LinkedList<Node>();		// clear out closed for next robot
		} 		// End of for loop

		return fullPaths;		// return array of each robot's path
	}
}