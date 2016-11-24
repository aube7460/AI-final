package path_planning;

public class CheckNode{
	
	//Determines what scenario the inputted coordinate is.  Returns the scenario
	
	public static int check(int[][] map, int pos[]){
		int x = pos[0];
		int y = pos[1];
		int roomWidth = map.length;
		int roomLength = map[0].length;
		// OBSTACLE OR NAH
		if (x >= roomWidth || y >= roomLength){
			return -1;
		}
		else if (map[x][y] == 1) {
			return -1;
		}
		else{
			// CASE 0 - TOP LEFT CORNER
			if (x == 0 && y == 0){
				return 0;			
			// CASE 1 - TOP RIGHT CORNER
			}else if (x == roomWidth && y == 0){
				return 1;
			// CASE 2 - BOTTOM LEFT
			}else if (x == 0 && y == roomLength){
				return 2;
			// CASE 3 - BOTTOM RIGHT
			}else if (x == roomWidth && y == roomLength){
				return 3;
			// CASE 4 - TOP SIDE
			}else if (y == 0){
				return 4;
			// CASE 5 - BOTTOM SIDE
			}else if (y == roomLength){
				return 5;
			// CASE 6 - LEFT
			}else if (x == 0){
				return 6;
			// CASE 7 - RIGHT
			}else if (x == roomWidth){
				return 7;
			// CASE 8 - EVERYTHING ELSE
			}else{
				return 8;
			}
		}
	}
	