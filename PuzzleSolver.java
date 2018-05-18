/** Puzzle Solver
 * @author Shannon Stork
 */

import java.io.IOException;
import java.util.Random;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class PuzzleSolver {
	// An inner class to represent a node in the search tree
		public class PuzzleNode {
			
			// The fields
			private PuzzleNode parent;
			
			private PuzzleNode child1;
			
			private PuzzleNode child2;
			
			private PuzzleNode child3;
			
			private PuzzleNode child4;
			
			private int[][] nodeState;
			
			private int level;
			
			private PuzzleNode next;
			
			private String move;
			
			// The constructor
			public PuzzleNode(int[][] nodeState, PuzzleNode parent) throws IOException {
				this.nodeState = nodeState;
				this.parent = parent;
				setLevel();
			}

			
			// Getter-setter methods
			public PuzzleNode getChild1() {
				return child1;
			}

			public void setChild1(PuzzleNode child1) {
				this.child1 = child1;
			}

			public PuzzleNode getChild2() {
				return child2;
			}

			public void setChild2(PuzzleNode child2) {
				this.child2 = child2;
			}

			public PuzzleNode getChild3() {
				return child3;
			}

			public void setChild3(PuzzleNode child3) {
				this.child3 = child3;
			}

			public PuzzleNode getChild4() {
				return child4;
			}

			public void setChild4(PuzzleNode child4) {
				this.child4 = child4;
			}
			
			public PuzzleNode getParent() {
				return parent;
			}

			public void setParent(PuzzleNode parent) {
				this.parent = parent;
			}


			public int[][] getNodeState() {
				return nodeState;
			}

			public void setNodeState(int[][] nodeState) {
				this.nodeState = nodeState;
			}

			public PuzzleNode getNext() {
				return next;
			}

			public void setNext(PuzzleNode next) {
				this.next = next;
			}
			
			public String getMove() {
				return move;
			}
			
			public void setMove(String move) {
				this.move = move;
			}

			public int getLevel() {
				return level;
			}
			
			// To improve efficiency, this method keeps track of the depth of the current node
			public void setLevel() {
				if (parent == null)
					level = 0;
				else
					level = parent.getLevel() + 1;
			}
			
			/** Returns whether or not a node has a next node in a queue
			 * @return boolean has next
			 */
			public boolean hasNext() {
				if (getNext() != null)
					return true;
				return false;
			}
			
			/** Expands a node by initializing the children
			 * 
			 * @throws IOException
			 */
			public void expand() throws IOException {
				int I = -1;
				int J = -1;
				for (int i = 0; i < 3; i++)
					for (int j = 0; j < 3; j++)
						if (nodeState[i][j] == 0) {
							I = i;
							J = j;
							break;
						}
				String state = printState(getNodeState());
				if (I != 0) {
					PuzzleSolver solver1 = new PuzzleSolver();
					solver1.setState(state);
					solver1.move("up");
					setChild1(new PuzzleNode(solver1.getState(), this));
					child1.setMove("up");
				}
				if (I != 2) {
					PuzzleSolver solver2 = new PuzzleSolver();
					solver2.setState(state);
					solver2.move("down");
					setChild2(new PuzzleNode(solver2.getState(), this));
					child2.setMove("down");
				}
				if (J != 0) {
					PuzzleSolver solver3 = new PuzzleSolver();
					solver3.setState(state);
					solver3.move("left");
					setChild3(new PuzzleNode(solver3.getState(), this));
					child3.setMove("left");
				}
				if (J != 2) {
					PuzzleSolver solver4 = new PuzzleSolver();
					solver4.setState(state);
					solver4.move("right");
					setChild4(new PuzzleNode(solver4.getState(), this));
					child4.setMove("right");
				}
			}
			
			/** Traces the path from the goal node to the root
			 * @return PuzzleQueue storing the path
			 */
			public void tracePath() {
				PuzzleNode[] arr = new PuzzleNode[31];
				PuzzleNode node = this;
				int index = 0;
				while (node != null) {
					arr[index] = node;
					node = node.getParent();
					index++;
				}
				while (index > 0) {
					node = arr[index - 1];
					if (node.getMove() != null)
						System.out.println(node.getMove());
					index--;
					
				}
			}
			
		}
		
		public class PuzzleQueue {
			
			//the end
			private PuzzleNode end;

			//the root
			private PuzzleNode root;
			
			//the number of elements in the queue
			private int size;
			
			public PuzzleQueue(PuzzleNode root) {
				this.root = root;
				end = root;
				size = 1;
			}
			
			public PuzzleQueue() {
			}

			public PuzzleNode getEnd() {
				return end;
			}

			public void setEnd(PuzzleNode end) {
				this.end = end;
			}


			public PuzzleNode getRoot() {
				return root;
			}

			public void setRoot(PuzzleNode root) {
				this.root = root;
			}
			
			public int getSize() {
				return size;
			}
			
			/** Adds the given node to the end of the queue
			 * @param node
			 */
			public void addToQueue(PuzzleNode node) {
				if(isEmpty()) {
					setRoot(node);
					setEnd(node);
				}
				else {
					end.setNext(node);
					setEnd(node);
				}
				size++;
			}
			
			/** Removes the first element (the lowest cost element) from the queue
			 * @return node that was in front of queue
			 */
			public PuzzleNode removeFront() {
				PuzzleNode oldRoot = getRoot();
				if (!oldRoot.hasNext()) {
					setRoot(null);
					size--;
					return oldRoot;
				}
				PuzzleNode newRoot = getRoot().getNext();
				getRoot().setNext(null);
				setRoot(newRoot);
				size--;
				return oldRoot;
			}
			
			/** Indicates whether the queue is empty
			 * @return boolean is empty
			 */
			public boolean isEmpty() {
				if (size == 0)
					return true;
				return false;
			}

		}
		
		// Stores the puzzle
		private int[][] puzzle;
		
		// Stores the node limit
		private int maxNodes;
		
		// Stores the number of nodes traversed
		private int numNodes;
		
		//Stores the root of the puzzle
		private PuzzleNode root;
		
		// Indicates whether the goal has been reached
		private boolean solnFound;
		
		/** Constructor
		 */
		public PuzzleSolver() {
			 puzzle = new int[3][3];
		}


		/**
		 * @param puzzle the puzzle to set
		 */
		public void setPuzzle(int[][] puzzle) {
			this.puzzle = puzzle;
		}


		/**
		 * @return the maxNodes
		 */
		public int getMaxNodes() {
			return maxNodes;
		}


		/**
		 * @param maxNodes the maxNodes to set
		 */
		public void setMaxNodes(int maxNodes) {
			this.maxNodes = maxNodes;
		}
		
		/**
		 * @return the maxNodes
		 */
		public int getNumNodes() {
			return numNodes;
		}


		/**
		 * @param maxNodes the maxNodes to set
		 */
		public void setNumNodes(int maxNodes) {
			this.numNodes = numNodes;
		}

		/** Returns the address of the puzzle (primarily for testing purposes)
		 * @return int[][] puzzle
		 */
		public int[][] getState() {
			return puzzle;
		}
		
		/** Sets the state of the puzzle to the input string
		 * @param String state; string representation of current state in the form "b12 345 678"
		 */
		public void setState(String state) {
			int index = 0;
			//loops through the array
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					//represents the blank tile as 0
					if (state.charAt(index) == 'b')
						getState()[i][j] = 0;
					//sets the other tiles to their proper int values
					else if (state.charAt(index) != ' ')
						getState()[i][j] = Character.getNumericValue(state.charAt(index));
					//skips over spaces in the input string
					else if (state.charAt(index) == ' ')
						index++;
					//System.out.println(puzzle[i][j]);
					index++;
				}
				index++;
			}		
		}
		/** Checks if a node is the goal state
		 * 
		 */
		public boolean goal(PuzzleNode node) {
			int[][] state = node.getNodeState();
			for (int i = 0; i < 3; i++)
				for (int j = 0; j < 3; j++)
					if (state[i][j] != 3 * i + j)
						return false;
			return true;
		}
		
		/** Determines h1 of the given state (number of misplaced tiles)
		 * @param int[][] state
		 * @return int heuristic
		 */
		private int h1(int[][] state) {
			int heuristic = 0;
			//loops through the array
			for (int i = 0; i < state.length; i++)
				for (int j = 0; j < state[0].length; j++)
					//adds one if a misplaced tile is found
					if (state[i][j] != j + 3*i)
						heuristic++;
			return heuristic;
		}
		
		/** Determines h2 of the given state (sum of distances of tiles from goal)
		 * @param int[][] state
		 * @return int heuristic
		 */
		private int h2(int[][] state) {
			int heuristic = 0;
			//loops through the array
			for (int i = 0; i < state.length; i++)
				for (int j = 0; j < state[0].length; j++)
					//if a tile is misplaced, adds the absolute value between actual and expected value to h2
					if (state[i][j] != j + 3*i)
						heuristic = heuristic + java.lang.Math.abs(state[i][j] - j - 3*i);
			return heuristic;
		}
		
		/** Makes n random moves from the goal state
		 * @param int n number of moves
		 */
		public void randomizeState (int n) throws IOException {
			setState("b12 345 678");
			Random random = new Random();
			random.setSeed(20);
			//Randomizes the state n times. If the randomizer picks an invalid move, it tries again.
			for (int i = 0; i < n; i++) {
				int randomizer = (int)(4 * random.nextDouble());
				try {
				if (randomizer == 0)
				    move("up");
			    else if (randomizer == 1)
				    move ("down");
		    	else if (randomizer == 2)
				    move ("left");
			    else
				    move("right");
				} catch (IOException e) {
					i--;
				}
		    }
		}
		
		/** Prints the current state
		 * @param int[][] puzzle state
		 * @return String current state
		 */
		public String printState(int[][] puzzle) {
			StringBuilder state = new StringBuilder();
			//loops through the array
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					//converts 0 back to 'b'
					if (puzzle[i][j] == 0)
						state.append('b');
					//handles all other integers
					else 
						state.append(puzzle[i][j]);
					//puts in proper spaces
					if (j == 2 && i != 2)
						state.append(' ');
				}
			}
			return state.toString();
		}
		
		/**Moves the blank tile up, down, left, or right
		 * @param String direction
		 */
		public void move(String direction) throws IOException {
			//finds the blank tile
			int I = 0;
			int J = 0;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (puzzle[i][j] == 0) {
						I = i;
						J = j;
						break;
					}
				}
			}
			//deals with bad moves
			if (!direction.equals("right") && !direction.equals("left") && !direction.equals("up") && !direction.equals("down"))
				throw new IOException("Invalid direction input");
			else if (I == 0 && direction.equals("up"))
				throw new IOException("Invalid move given board state");
			else if (I == 2 && direction.equals("down"))
				throw new IOException("Invalid move given board state");
			else if (J == 0 && direction.equals("left"))
					throw new IOException("Invalid move given board state");
			else if (J == 2 && direction.equals("right"))
						throw new IOException("Invalid move given board state");
			//moves the tile
			if (direction.equals("up")) {
				puzzle[I][J] = puzzle[I - 1][J];
				puzzle[I - 1][J] = 0;
			}
			else if (direction.equals("down")) {
				puzzle[I][J] = puzzle[I + 1][J];
				puzzle[I + 1][J] = 0;
			}
			else if (direction.equals("left")) {
				puzzle[I][J] = puzzle[I][J - 1];
				puzzle[I][J - 1] = 0;
			}
			else if (direction.equals("right")) {
				puzzle[I][J] = puzzle[I][J + 1];
				puzzle[I][J + 1] = 0;
			}
		}
		
		/** Prints out the final path for A* search
		 * @param PuzzleQueue the queue to print
		 */
		public void printQueue(PuzzleQueue queue) {
			PuzzleNode node = queue.getRoot();
			while (node != null) {
				if (node.getMove() != null)
					System.out.println(node.getMove());
				node = node.getNext();
			}
		}
		
		/** Generates f(n) for a given state for A* search
		 * @param int[][] state; current state of the puzzle
		 * @param String heuristic; "h1" or "h2"
		 */
		public int aStarFn(String heuristic, PuzzleNode node) throws IOException{
			int f = 0;
			int g = 0;
			int h = 0;
			//sets the heuristic
			if (heuristic.equals("h1"))
				h = h1(node.getNodeState());
			else if (heuristic.equals("h2"))
				h = h2(node.getNodeState());
			else
				throw new IOException("Invalid heuristic");
			//sets the distance to reach the node
			g = node.getLevel();
			//computes and returns f
			f = h + g;
			return f;
		}
		
		/** Orders the children of a puzzle node by heuristic and returns an ordered array
		 * @param PuzzleNode node
		 * @param String heuristic
		 * @return array in order of lowest to highest heuristic
		 */
		public PuzzleNode[] orderChildren(PuzzleNode node, String heuristic) throws IOException {
			PuzzleNode[] nodes = new PuzzleNode[4];
			int[] heuristics = new int[4];
			int index = 0;
			if (node.getChild1() != null) {
				nodes[index] = node.getChild1();
				heuristics[index] = aStarFn(heuristic, nodes[index]);
				index++;
			}
			if (node.getChild2() != null) {
				nodes[index] = node.getChild2();
				heuristics[index] = aStarFn(heuristic, nodes[index]);
				index++;
			}
			if (node.getChild3() != null) {
				nodes[index] = node.getChild3();
				heuristics[index] = aStarFn(heuristic, nodes[index]);
				index++;
			}
			if (node.getChild4() != null) {
				nodes[index] = node.getChild4();
				heuristics[index] = aStarFn(heuristic, nodes[index]);
				index++;
			}
			PuzzleNode[] order = order(nodes, heuristics);
			return order;
		}
		
		/** Helper method; orders nodes in terms of heuristic
		 * 
		 */
		public PuzzleNode[] order(PuzzleNode[] nodes, int[] heuristics) {
			int end = nodes.length;
			for (int i = 0; i < end; i++)
				for (int j = i + 1; j < end; j++) {
					if (nodes[j] == null)
						end = j;
					else if ((heuristics[i] > heuristics[j] && heuristics[j] != -1 )|| heuristics[i] == -1) {
						PuzzleNode n = nodes[i];
						int h = heuristics[i]; 
						nodes[i] = nodes[j];
						nodes[j] = n;
						heuristics[i] = heuristics[j];
						heuristics[j] = h;
					}
				}
			return nodes;
		}
		/** A helper method to compare two puzzle states
		 * @param int[][] state1
		 * @param int[][] state2
		 * @return boolean states are equal
		 */
		public boolean statesAreEqual(int[][] state1, int[][]state2) {
			for (int i = 0; i < state1.length; i++)
				for (int j = 0; j < state1.length; j++)
					if (state1[i][j] != state2[i][j])
						return false;
			return true;
		}
		
		/** A helper method to test if a given state is already represented in a queue AND has a lower heuristic
		 * @param PuzzleNode node
		 * @param PuzzleQueue queue
		 * @return boolean is in queue
		 */
		public boolean isInQueue(PuzzleNode node, PuzzleQueue queue, String heuristic) throws IOException {
			PuzzleNode queueNode = queue.getRoot();
			if (queueNode != null && statesAreEqual(node.getNodeState(), queueNode.getNodeState()))
				return true;
			while (queueNode != null && queueNode.hasNext()) {
				if (statesAreEqual(node.getNodeState(), queueNode.getNodeState()) && aStarFn(heuristic, node) > aStarFn(heuristic, queueNode))
					return true;
				queueNode = queueNode.getNext();
			}
			return false;
		}
		
		/** A helper method to test if a given state is already represented in an array
		 * @param PuzzleNode node
		 * @param PuzzleNode[] array
		 * @return boolean is in queue
		 */
		public boolean isInArray(PuzzleNode node, PuzzleNode[] array) throws IOException {
			int index = 0;
			if (array[0] != null && statesAreEqual(node.getNodeState(), array[0].getNodeState()))
				return true;
			while (index < array.length && array[index] != null) {
				if (statesAreEqual(node.getNodeState(), array[index].getNodeState()))
					return true;
				index++;
			}
			return false;
		}
		
		/** Solves the puzzle using A* search
		 * @param String heuristic- h1 or h2
		 */
		public void solveAStar (String heuristic) throws IOException {
			//initializes the tree to hold the root node representing the current state
			root = new PuzzleNode(getState(), null);
			solnFound = false;
			if (goal(root)) {
				solnFound = true;
				System.out.println("Solution found!");
				System.out.println("Number of steps: 0");
			}
			PuzzleQueue open = new PuzzleQueue(root);
			PuzzleQueue closed = new PuzzleQueue();
			numNodes = 0;
			while (!open.isEmpty() && solnFound == false) {
				// remove first node from open and generate its successors
				PuzzleNode current = open.removeFront();
				current.expand();
				numNodes++;
				if (getMaxNodes() != 0 && numNodes > getMaxNodes())
					throw new IOException("Max nodes exceeded before solution was found");
				// order successors by heuristic
				PuzzleNode[] order = orderChildren(current, heuristic);
				for(int index = 0; index < order.length; index++) {
					if (order[index] == null)
						break;
					if (goal(order[index])) {
						System.out.println("Solution found!");
						System.out.println("Number of steps: " + order[index].getLevel());
						order[index].tracePath();
						closed.addToQueue(order[index]);
						solnFound = true;
						break;
					}
					else if (isInQueue(order[index], open, heuristic))
						;
					else if (isInQueue(order[index], closed, heuristic))
						;
					else
						open.addToQueue(order[index]);
				}
				closed.addToQueue(current);
			}
			if (solnFound == false)
				throw new IOException("No solution found");
		}
		
		/** Solves the puzzle using beam search with a maximum of k states held in memory
		 * @param int k states
		 */
		public void solveBeam(int k) throws IOException {
			solnFound = false;
			root = new PuzzleNode(getState(), null);
			if (goal(root)) {
				solnFound = true;
				System.out.println("Solution found!");
				System.out.println("Number of steps: 0");
			}
			PuzzleNode[] bestk = new PuzzleNode[k];
			bestk[0] = new PuzzleNode(getState(), null);
			numNodes = 0;
			//continues iterating as long as the solution has not been found
			while (solnFound == false) {
				PuzzleNode[] successors = new PuzzleNode[k * 4];
				int successorIndex = 0;
				//generate successors of best k states, stopping if goal is found
				for (int index = 0; index < k; index++) {
					if (bestk[index] == null)
						break;
					bestk[index].expand();
					if (bestk[index].getChild1() != null && !isInArray(bestk[index].getChild1(), bestk) && !isInArray(bestk[index].getChild1(), successors)) {
						successors[successorIndex] = bestk[index].getChild1();
						if (goal(successors[successorIndex])) {
							solnFound = true;
							System.out.println("Solution found!");
							System.out.println("Number of steps: " + successors[successorIndex].getLevel());
							successors[successorIndex].tracePath();
							break;
						}
						numNodes++;
						successorIndex++;
						if (getMaxNodes() != 0 && numNodes > getMaxNodes())
							throw new IOException("Max nodes exceeded before solution was found");
					}
					if (bestk[index].getChild2() != null && !isInArray(bestk[index].getChild2(), bestk) && !isInArray(bestk[index].getChild2(), successors)) {
						successors[successorIndex] = bestk[index].getChild2();
						if (goal(successors[successorIndex])) {
							solnFound = true;
							System.out.println("Solution found!");
							System.out.println("Number of steps: " + successors[successorIndex].getLevel());
							successors[successorIndex].tracePath();
							break;
						}
						numNodes++;
						successorIndex++;
						if (getMaxNodes() != 0 && numNodes > getMaxNodes())
							throw new IOException("Max nodes exceeded before solution was found");
					}
					if (bestk[index].getChild3() != null && !isInArray(bestk[index].getChild3(), bestk) && !isInArray(bestk[index].getChild3(), successors)) {
						successors[successorIndex] = bestk[index].getChild3();
						if (goal(successors[successorIndex])) {
							solnFound = true;
							System.out.println("Solution found!");
							System.out.println("Number of steps: " + successors[successorIndex].getLevel());
							successors[successorIndex].tracePath();
							break;
						}
						numNodes++;
						successorIndex++;
						if (getMaxNodes() != 0 && numNodes > getMaxNodes())
							throw new IOException("Max nodes exceeded before solution was found");
					}
					if (bestk[index].getChild4() != null && !isInArray(bestk[index].getChild4(), bestk) && !isInArray(bestk[index].getChild4(), successors)) {
						successors[successorIndex] = bestk[index].getChild4();
						if (goal(successors[successorIndex])) {
							solnFound = true;
							System.out.println("Solution found!");
							System.out.println("Number of steps: " + successors[successorIndex].getLevel());
							successors[successorIndex].tracePath();
							break;
						}
						numNodes++;
						successorIndex++;
						if (getMaxNodes() != 0 && numNodes > getMaxNodes())
							throw new IOException("Max nodes exceeded before solution was found");
					}
					numNodes++;
					if (getMaxNodes() != 0 && numNodes > getMaxNodes())
						throw new IOException("Max nodes exceeded before solution was found");
				}
				//choose best k of them based on evaluation function
				if (solnFound == false) {
					int[] heuristics = new int[4 * k];
					for (int index = 0; index < 4 * k; index++) {
						if (successors[index] == null)
							heuristics[index] = -1;
						else
							heuristics[index] = h2(successors[index].getNodeState());
					}
					PuzzleNode[] order = order(successors, heuristics);
					for (int index = 0; index < k; index++) {
						if (order[index] == null)
							break;
						bestk[index] = order[index];
					}
				}
			}
			
		}
		
		public static void testing() throws IOException {
			PuzzleSolver baseCase = new PuzzleSolver();
			baseCase.setState("b12 345 678");
			PuzzleSolver a1 = new PuzzleSolver();
			a1.setState("1b2 345 678");
			PuzzleSolver b1 = new PuzzleSolver();
			b1.setState("312 b45 678");
			PuzzleSolver a2 = new PuzzleSolver();
			a2.setState("312 645 b78");
			PuzzleSolver b2 = new PuzzleSolver();
			b2.setState("12b 345 678");
			PuzzleSolver a3 = new PuzzleSolver();
			a3.setState("312 645 7b8");
			PuzzleSolver b3 = new PuzzleSolver();
			b3.setState("125 34b 678");
			PuzzleSolver a4 = new PuzzleSolver();
			a4.setState("312 645 78b");
			PuzzleSolver b4 = new PuzzleSolver();
			b4.setState("125 348 67b");
			PuzzleSolver a6 = new PuzzleSolver();
			a6.setState("312 645 78b");
			PuzzleSolver b6 = new PuzzleSolver();
			b6.setState("142 7b5 368");
			PuzzleSolver a8 = new PuzzleSolver();
			a8.setState("142 765 b38");
			PuzzleSolver b8 = new PuzzleSolver();
			b8.setState("b42 165 738");
			PuzzleSolver[] puzzles = new PuzzleSolver[13];
			puzzles[0] = baseCase;
			puzzles[1] = a1;
			puzzles[2] = b1;
			puzzles[3] = a2;
			puzzles[4] = b2;
			puzzles[5] = a3;
			puzzles[6] = b3;
			puzzles[7] = a4;
			puzzles[8] = b4;
			puzzles[9] = a6;
			puzzles[10] = b6;
			puzzles[11] = a8;
			puzzles[12] = b8;
			int[] aStarh1cost = new int[13];
			int[] aStarh2cost = new int[13];
			int[] beamcost = new int[13];
			for (int index = 0; index < 13; index++) {
				puzzles[index].setMaxNodes(10);
				puzzles[index].solveAStar("h1");
				aStarh1cost[index] = puzzles[index].getNumNodes();
				System.out.println(index + " h1 cost: " + aStarh1cost[index]);
				puzzles[index].solveAStar("h2");
				aStarh2cost[index] = puzzles[index].getNumNodes();
				System.out.println(index + " h2 cost: " + aStarh2cost[index]);
				puzzles[index].solveBeam(10);
				beamcost[index] = puzzles[index].getNumNodes();
				System.out.println(index + " beam cost: " + beamcost[index]);
			}
		}
		
		public static void testing2() throws IOException{
			System.out.println("Remember to unseed the random number generator!");
			PuzzleSolver[] random = new PuzzleSolver[100];
			for (int index = 0; index < 100; index++) {
				random[index] = new PuzzleSolver();
				random[index].randomizeState(10);
				random[index].setMaxNodes(200);
				try {
					random[index].solveAStar("h1");
				} catch (IOException e) {
					System.out.println("1 h1 failure");
				}
				try {
					random[index].solveAStar("h2");
				} catch (IOException e) {
					System.out.println("1 h2 failure");
				}try {
					random[index].solveBeam(10);
				} catch (IOException e) {
					System.out.println("1 beam failure");
				}
			}
		}
		
		public static void main(String[] args) throws IOException, FileNotFoundException {
			PuzzleSolver puzzle = new PuzzleSolver();
			boolean fromFile = false;
			if(args.length != 0 && args[0].indexOf(".txt") != -1) {
				FileReader reader = new FileReader("D:/PuzzleSolver.txt");
				StringBuilder builder = new StringBuilder();
				int wordCount = 0;
				try {
					int c;
					while ((c = reader.read()) != -1) {
						builder.append((char)c);
						if ((char)c == ' ' || (char)c == '\n')
							wordCount++;
					}
					reader.close();
				} catch (FileNotFoundException e) {
					System.err.println("File not found");
				}
				String file = builder.toString();
				String[] fileArgs = new String[wordCount + 1];
				int word = 0;
				for (int i = 0; i < file.length(); i++) {
					if (file.charAt(i) != ' ' && file.charAt(i) != '\n') {
						if (fileArgs[word] == null)
							fileArgs[word] = Character.toString(file.charAt(i));
						else
							fileArgs[word] = new String(fileArgs[word] + file.charAt(i));
					}
					else
						word++;
				}
				args = fileArgs;
				fromFile = true;
			}
			int index = 0;
			while (index < args.length) {
				if (args[index].equals("setState")) {
					puzzle.setState(args[index+1] + " " + args[index+2] + " " + args[index+3]);
					index++;
				}
				if (args[index].equals("randomizeState")) {
					puzzle.randomizeState(Integer.parseInt(args[index+1]));
					index++;
				}
				if (args[index].equals("printState")) {
					System.out.println(puzzle.printState(puzzle.getState()));
					index++;
				}
				if (args[index].equals("move")) {
					puzzle.move(args[index+1]);
					index++;
				}
				if (args[index].equals("maxNodes")) {
					puzzle.setMaxNodes(Integer.parseInt(args[index+1]));
					index++;
				}
				if (args[index].equals("solve") && args[index+1].equals("A-star")) {
					index++;
					puzzle.solveAStar(args[index+1]);
					index++;
				}
				if (args[index].equals("solve") && args[index+1].equals("beam")) {
					index++;
					puzzle.solveBeam(Integer.parseInt(args[index+1]));
					index++;
				}
				index++;
			}
		}
}
