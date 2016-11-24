import java.io.*;
import java.util.Scanner;

public class File_in{
	
	private int width;
	private int height;
	private int num;
	private int[][] roboPos;
	private int[] rendezvous;
	private int[][] map;
	
	public static File_in readFile() throws IOException {
		//String filename = "roomInstance.txt";
		String line = null;
		String[] tempstr;
		
		//retrieves what the user inputs.
		Scanner user_in = new Scanner(System.in);
		
		//String unformatted_input;			
		int dimWidth = 0;
		int dimHeight = 0;
		int numRobots = 0;
		int i, j = 0;
		int posRobots[][];
		int position[];
		int rvous[] = new int[2];
		int roomInstance[][]; 
		String fileInput = new String();
		
		//output to the user to ask them to input the file path
		System.out.println("Please enter the full path to the input text file");
		System.out.println("Ex: X:/folder/file.txt");
		System.out.println("Input file: ");
		
		//Assign what the user inputted to the variable fileInput
		fileInput += user_in.nextLine();
		
		
		try {
			// FileReader reads text files in the default encoding.
			FileReader fr = new FileReader(fileInput);
	
			// Always wrap FileReader in BufferedReader.
			BufferedReader br = new BufferedReader(fr);
	
			// read first line of text file
			line = br.readLine();		
			tempstr = line.split(" ");					//Split at the space
			dimWidth = Integer.parseInt(tempstr[0]);	//first value is the room width 
			dimHeight = Integer.parseInt(tempstr[1]);	//second value is the room height
	
			// reads the second line of the text file
			line = br.readLine();
			numRobots = Integer.parseInt(line);		// get number of robots
			posRobots = new int[numRobots][];		// initialize the size of posRobots to the nu
	
			// get the information for each robot, one at a time.
			for (i = 0; i < numRobots; i++) {
				position = new int[2];
				line = br.readLine();				// Reads the next line of the text file
	
				tempstr = line.split(" ");			// splits the line at the space
	
				position[0] = Integer.parseInt(tempstr[0]); // x position of the robots starting position
				position[1] = Integer.parseInt(tempstr[1]); // y position of the robots starting position
	
				posRobots[i] = new int[2];
				posRobots[i] = position;			// adds the starting position of each robot
			}
	
			line = br.readLine();					// get rendezvous point
			tempstr = line.split(" ");				// split at the space
	
			rvous[0] = Integer.parseInt(tempstr[0]);	// x position of the rendezvous location
			rvous[1] = Integer.parseInt(tempstr[1]);	// y position of the rendezvous location
	
			// Initialize roomInstance size
			roomInstance = new int[dimWidth][dimHeight];
	
			// reads in the room line by line and saves it to the roomInstance variable
			for (j = 0; j < dimHeight; j++) {		// loops through the file height amount of times
				line = br.readLine();				// goes to next line
				tempstr = line.split("");			// get individual characters
				for (i = 0; i < dimWidth; i++) {	// Loops through each line width amount of times	
					roomInstance[i][j] = Integer.parseInt(tempstr[i]);		// save individual character to roomInstance array
				}
			}
			
			// creates a new fileInstance variable with the width, height, number of robots, their starting positions, the rendezvous location and the room.
			File_in fileInstance = new File_in(dimWidth, dimHeight, numRobots, posRobots, rvous, roomInstance);
	
			br.close();         // close file
			return fileInstance;
		}
		
		// throws an exception if the file is not found 
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileInput + "'");                
		}
		
		// throws an exception is the file cannot be read
		catch(IOException ex) {
			System.out.println("Error reading file '"+ fileInput + "'");                  
		}
		user_in.close();
		
		return null;
	}
	
	// sets the variables
	public File_in(int dimWidth, int dimHeight, int roboNum, int[][] posRobots, int[] rvous, int[][] world){
		this.width = dimWidth;
		this.height = dimHeight;
		this.num = roboNum;
		
		this.roboPos = posRobots;
		this.rendezvous = rvous;
		this.map = world;
	}
	
	// returns the width of the room
	public int getWidth() {
		return this.width;
	}
	
	// returns the height of the room
	public int getHeight() {
		return this.height;
	}
	
	// returns the number of robots in the room
	public int getNum() {
		return this.num;
	}
	
	// returns the robots positions in the room
	public int[][] getRoboPos() {
		return this.roboPos;
	}
	
	// returns the rendezous position in the room
	public int[] getRendezvous() {
		return this.rendezvous;
	}
	
	// returns the map of the room
	public int[][] getMap() {
		return this.map;
	}
}