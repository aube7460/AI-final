package path_planning;

// Calculates the straight-line distance to from current position to the rendezvous point.

public class Heuristic {
	public static float heuristicSLD(int meet[],int currentPos[]) {
		int X =0,Y=0;
		int x1=0,y1=0;
		int x2=0,y2=0;
		
		// x and y values for the current position and the meeting point.
		x1 = currentPos[0];
		y1 = currentPos[1];
		x2 = meet[0];
		y2 = meet[1];
		
		X = (x2-x1);			// X value for computing the length of the line
		Y = (y2-y1);			// Y value for computing the length of the line
			
		return (float) Math.sqrt((X*X)+(Y*Y));		// Calculate the straight line distance from the robot to the meeting point
	}	
}