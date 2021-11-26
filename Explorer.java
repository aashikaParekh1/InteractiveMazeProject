import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Polygon;

public class Explorer //hold data for explorer motion, location, and shape
{
	private Location loc;
	private int dir;
	private int size;
	private Color color;
	private boolean debug = true;
	private String direction = "";
	private int steps, turns= 0;

	//constructor
	public Explorer(Location loc, int dir, int size, Color color)
	{
		this.loc = loc;
		this.dir = dir;
		this.color = color;
		this.size = size;
	}

	//accessor
	public Color getColor()
	{
		return color;
	}

	public Location getLoc()
	{
		return loc;
	}

	public int getDir()
	{
		return dir;
	}

	public int getSize()
	{
		return size;
	}

	public int getSteps()
	{
		return steps;
	}

	public int getTurns()
	{
		return turns;
	}

	public boolean atLocation(int r, int c) //gives the row and col for a certain location
	{
		return (r == loc.getR() && c == loc.getC());
	}

	//moving through the maze
	public void move(int key, char[][] maze)
	{
		int r = getLoc().getR();
		int c = getLoc().getC();
		if(debug) System.out.println("Move called (" + r + "," + c + ") dir = " + dir+" key ="+key);

		//forward
		if(key==38)
		{
			//0=up 1=right 2=down 3=left

			//up
			if(dir==0){
				if(r > 0 && maze[r - 1][c] != '#') //if there is no wall
				{
					getLoc().setR(-1); //move one row up
					steps++;
					System.out.println("0");
				}
			}
			//right
			if(dir==1)
			{
				 System.out.println("dir == 1");
				if(c<maze[r].length-1 && maze[r][c+1] != '#')  //if there is no wall
				{
					getLoc().setC(+1); //move one col to the right
					steps++;
					System.out.println("1");
				}
			}
			// down
			if(dir == 2)
			{
				if(r<maze.length-1 && maze[r+1][c] != '#') //if there is no wall
				{
					getLoc().setR(+1); //move one row down
					steps++;
					System.out.println("2");
				}
			}
			//left
			if(dir == 3)
			{
				if(c > 0 && maze[r][c-1] != '#') //if there is no wall
				{
					getLoc().setC(-1); //move one col to the left
					steps++;
					System.out.println("3");
				}

			}
		}

		if(key==37) //rotate right
		{
			dir--;
			if(dir<0)
				dir=3;

			turns++;//add to turn count
		}

		if(key==39) //rotate left
		{
			dir++;
			if(dir>3)
				dir = 0;

			turns++;//add to turn count
		}

	}

	public Polygon getPoly() //the explorer is a triangle
	{
		int r=getLoc().getR();
		int c=getLoc().getC();
		Polygon arrowHead = new Polygon();

		//redraw when facing up
		if (dir == 0){
			arrowHead.addPoint( c*size+size,r*size+2*size);
			arrowHead.addPoint( (int)(c*size+size*1.5), (r*size+size));
			arrowHead.addPoint( c*size+size*2,r*size+size*2);
		}
		//redraw when facing right
		if (dir == 1 ){
			arrowHead.addPoint( c*size+size,r*size+size);
			arrowHead.addPoint( c*size+size*2, (int)(r*size+size*1.5));
			arrowHead.addPoint( c*size+size,r*size+size*2);
		}
		//redraw when facing down
		if (dir == 2 ){
			arrowHead.addPoint( c*size+size,r*size+size);
			arrowHead.addPoint( (int)(c*size+size*1.5), (r*size+size*2));
			arrowHead.addPoint( c*size+2*size,r*size+size);
		}
		//redraw when facing left
		if (dir == 3 ){
			arrowHead.addPoint( c*size+2*size,r*size+size);
			arrowHead.addPoint( c*size+size, (int)(r*size+size*1.5));
			arrowHead.addPoint( c*size+2*size,r*size+2*size);
		}

		return arrowHead;
	}


}