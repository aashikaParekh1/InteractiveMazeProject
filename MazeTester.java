public class MazeTester
{
	private char[][] maze= {{'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
						    {'#','#',' ','#','#',' ','#','#',' ','#',' ','#','#','#','#','#','#','#','#',' ','#'},
							{'#',' ',' ',' ',' ',' ',' ',' ','E',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
							{'#','#','#',' ','#','#','#','#',' ','#','#','#','#','#','#','#','#','#','#','#','#'},
							{'#','#','#','#','#','#','#','#',' ','#','#','#','#','#','#','#','#','#','#','#','#'},
							{'#','#','#','#','#','#','#',' ',' ',' ','#','#','#','#','#','#','#','#','#','#','#'},
							{'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'}};


	int numCols = maze.length,numRows = maze[0].length;


	public MazeTester()
	{

		System.out.println(getLeftWall(2,16,1,5)); //Facing North
		//System.out.println(getLeftWall(5,5,1,5)); //Facing East
		//System.out.println(getLeftWall(5,5,2,5)); //Facing South
		//System.out.println(getLeftWall(5,5,3,5)); //Facing West

		//System.out.println(frontWallLocation(5,5,0,5)); //Facing North
		//System.out.println(frontWallLocation(5,5,1,5)); //Facing East
		//System.out.println(frontWallLocation(5,5,2,5)); //Facing South
		//System.out.println(frontWallLocation(5,5,3,5)); //Facing West
	}
				  //Explorer-> row,   col,   dir  , max distance to check ahead
	public String getLeftWall(int r,int c, int dir,int maxDist){

		String lw = "";

		if (dir == 0)
			for (int n = 0; n < maxDist; n++)
				if (r-n >= 0)
					lw+= maze[r-n][c-1];

		if (dir == 1)
					for (int n = 0; n < maxDist; n++)
						if (c+n >= 0)
							lw+= maze[r-1][c+n];

		if (dir == 2)
					for (int n = 0; n < maxDist; n++)
						if (r+n >= 0)
							lw+= maze[r+n][c+1];

		if (dir == 3)
					for (int n = 0; n < maxDist; n++)
						if (c-n >= 0)
							lw+= maze[r-1][c-n];



		//YOUR CODE HERE TO HANDLE OTHER DIRECTIONS
		return lw;
	}

						  //Explorer-> row,   col,   dir  , max number of steps ahead to check
	public Location frontWallLocation(int r,int c, int dir,int maxSteps){

		if (dir == 0) {

			//Write to return the location of the first wall North of explorer

		}

		return new Location(0,0);	// Default Return

	}




	public static void main(String[] args)
	{
		new MazeTester();
	}
}