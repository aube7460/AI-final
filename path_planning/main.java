package path_planning;

import java.io.*;

// Runs the main program

public class Main {

	public static void main(String[] args) throws IOException {
		int[][][] path;
		
		// reads the file in
		File_in f = File_in.readFile();
		
		// gets the path using the A* algorithm 
		path = A_Star.AStarAlgorithm(f);
		
		// outputs the path to a file
		File_out.outputRoomsPaths(path);
	}
}
