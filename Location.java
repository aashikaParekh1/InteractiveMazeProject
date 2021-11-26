public class Location //hold data for location of an object in the maze
{
	private int r;
	private int c;

	//constructor
	public Location(int r, int c){
		this.r = r;
		this.c = c;

	}

	//accessor
	public int getR()
	{
		return r;
	}
	public int getC()
	{
		return c;
	}

	public String toString()
	{
		return "(" + getR() + "," + getC() + ")"; //output: (row,col)
	}

	//checks if the row and col is same as the other location's object row and col
	public boolean equals(Location other)
	{
		return (this.r == other.getR() && this.c == other.getC());
	}

	//row and col equal to paramerter row and col
	public boolean equals(int r, int c)
	{
		return this.r == r && this.c == c;
	}

	//increases row by parameter value
	public void setR(int a)
	{
		r += a;
	}

	//increases column by parameter value
	public void setC(int b)
	{
		c += b;
	}
}