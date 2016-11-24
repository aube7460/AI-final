/* File_out.java
 * 
 * outputRoomsPaths(int[][][] product)
 * 		outputs a file into the working directory of the program.
 * 		if the file (path_planning_output.txt) already exists, it is deleted and recreated with the newest instance of the program
 * 		the path of each robot is outputting into a text file, separated by "---"
 */

package path_planning;

import java.io.File;
import java.io.FileWriter;

public class File_out {

	public static void outputRoomsPaths(int[][][] product) {
		//Initialize required variables
		File f = null;
		String output = new String(System.getProperty("user.dir")); //get current directory
		System.out.println(output);									//print directory where file will be placed
		try{
			// for each string in string array 
			// create new file
			f= new File(output,"path_find_output.txt");							
			if(f.exists() && !f.isDirectory()) { 					//if path_find_output.txt already exists in current directory, it will be deleted
				f.delete();
				System.out.println("path_find_output.txt cleared");
			}

			f.createNewFile();										//create new path_find_output.txt
			FileWriter writer = new FileWriter(f); 					//write each coordinate into the created .txt file
			for (int[][] d : product) {
				String start_coordinates = new String();
				writer.write("------------------------------\n");
				start_coordinates += "Robot starting at " + d[0][0] + ","+d[0][1]+"\n";		
				writer.write(start_coordinates); //Write the starting coordinates of the robot into the .txt file
				for (int[] n: d) {
					String temp = new String();
					temp+= n[0] + "," + n[1] + "\n"; //write each coordinate on the path on each line of the .txt file
					writer.write(temp);
				}
			}
			writer.close(); //close writer
		}catch(Exception e){
			// if any I/O error occurs
			e.printStackTrace();
		}
	}
}
