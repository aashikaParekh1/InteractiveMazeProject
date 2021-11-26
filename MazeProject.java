import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import javax.swing.JFrame;
import javax.swing.JPanel;

//add ons: gradient, 3 songs based on events, count down timer

public class MazeProject extends JPanel implements KeyListener, ActionListener, Runnable
{
	private JFrame frame;
	private int col=0, row=0,dir = 0; //Location and direction of explorer
	private int size = 40;  		  //size of grid square in pixels
	private boolean debug = true;     //Set to true to print debug info
	private boolean won = false;	//set to true if expolorer is on the green square
	private boolean is3D = false; 	//switch from 2D to 3D
	private static boolean movable = true; //pause and plays the explorer
	private Location endLoc;		//Finish line of maze
	private Explorer explorer;		//explorer class
	private int numRows = 15, numCols=20; // set based on uploaded data
	private char[][] maze=new char[numRows][numCols]; //maze
	private String direction = " "; //shows the direction of the explorer
	private int steps, turns = 0; //shows how many steps are taken and the turn count
	private UpDownBot udb; //up down bot **(not used as an addon)**
	private String lw = ""; //left wall variable
	private String rw = ""; //right wall variable
	private int bwCount = -1; //back wall moving variable
	private int colorCount; //color counter for switch statement
	private Clip clip1; //lofi background beats
	private Clip clip2; //Congratulations song
	private Clip clip3; //you lost song
	private long countdown = 15; //timer count down time
	private static long startTime = System.currentTimeMillis(); //current time in milliseconds
	private boolean clock = true; //set to run if the timer needs to start or stop
	Thread thread = null; //thread for updatable jframe
	Graphics2D g2; //graphics



	public MazeProject()
	{
		setBoard();  //write this method to read board from file
		frame=new JFrame("MAZE PROJECT");
		frame.setSize(1000,1000);
		frame.add(this);
		frame.addKeyListener(this);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		music(); //start lofi music
		musicCELEB(); //start congratulations music
		clip2.stop(); //stop congratulations music so only lofi is heard
		lose();  //start loser music
		clip3.stop(); //stop loser music so only lofi is heard
		System.out.println("---------ACCESSOR COMPLETE--------"); //checker print statement

	}

	public void actionPerformed(ActionEvent e) {
		
	}

	public void paintComponent(Graphics g)
	{

		//Make Background Blank
		super.paintComponent(g);
		g2=(Graphics2D)g;
		g2.setColor(Color.BLACK);
		g2.fillRect(0,0,frame.getWidth(),frame.getHeight());


		if(is3D) //if its displaying the 3D maze
		{
			g2.setColor(Color.YELLOW); //text color is set to yellow
			g2.drawString("3D Maze!", 100, 30); //sizing of maze


			int shrink = 50;

			//left wall creation
			colorCount = 0;
			for(int s = 0; s <5; s++)
			{
				int next = shrink*s;
				int [] xLocsL = {100+next, 150+next, 150+next, 100+next}; //sizing for X
				int [] yLocsL = {100+next, 150+next, 650-next, 700-next}; //sizing for Y
				Polygon leftWall = new Polygon(xLocsL, yLocsL, xLocsL.length); //create the trapazoidal polygon for walls
				g2.setColor(getColorCount(colorCount)); //setting color based on case statement
				g2.fill(leftWall); //fill the color
				g2.setColor(Color.BLACK); //background color
				g2.draw(leftWall); //draws the 5 polygons
				colorCount++; //adding 1 to the color count (next case statement)
			}

			//right wall creation ~ similar to left wall
			colorCount = 0; //resets case statement
			for(int s = 0; s <5; s++)
			{
				int next = shrink*s;
				int [] xLocsR = {700-next, 650-next, 650-next, 700-next}; //different sizing 
				int [] yLocsR = {100+next, 150+next, 650-next, 700-next};
				Polygon rightWall = new Polygon(xLocsR, yLocsR, xLocsR.length);
				g2.setColor(getColorCount(colorCount));
				g2.fill(rightWall);
				g2.setColor(Color.BLACK);
				g2.draw(rightWall);
				colorCount++;

			}

			//ceiling creation ~ similar to left wall
			colorCount = 0;
			for(int s = 0; s <5; s++)
			{
				int next = shrink*s;
				int [] xLocsT = {100+next, 700-next, 650-next, 150+next}; //different sizing
				int [] yLocsT = {100+next, 100+next, 150+next, 150+next};
				Polygon topWall = new Polygon(xLocsT, yLocsT, xLocsT.length);
				g2.setColor(getColorCount(colorCount));
				g2.fill(topWall);
				g2.setColor(Color.BLACK);
				g2.draw(topWall);
				colorCount++;

			}

			//ground creation ~ similar to left wall
			colorCount = 0;
			for(int s = 0; s <5; s++)
			{
				//ground
				int next = shrink*s;
				int [] xLocsG = {100+next, 700-next, 650-next, 150+next}; //different sizing
				int [] yLocsG = {700-next, 700-next, 650-next, 650-next};
				Polygon ground = new Polygon(xLocsG, yLocsG, xLocsG.length);
				g2.setColor(getColorCount(colorCount));
				g2.fill(ground);
				g2.setColor(Color.BLACK);
				g2.draw(ground);
				colorCount++;

			}

			//getLeftWall ~ acessing different (hallways shown in gray)
			String lw = getLeftWall(explorer.getLoc().getR(), explorer.getLoc().getC(), explorer.getDir(), 5); //using the location to figure out where the left turns need to be
			System.out.println("Row: " + explorer.getLoc().getR()+ " Col: " + explorer.getLoc().getC() + " Dir: " + explorer.getDir()); //print to see the row, coloumn, direction
			System.out.println("lw: " + lw);
			int moveShrink = 50;
			//lw ="# #  ";
			for(int s = 0; s < lw.length(); s++)
			{

				if(lw.charAt(s) == (' ')) //if its empty 
				{
					g2.setColor(Color.GRAY); //color gray
					g2.fillRect(50+moveShrink*(s+1),100+moveShrink*(s+1),50,600-moveShrink*(s+1)*2); //rectangle size on the trapazoid polygon left side

				}
			}

			//getRightWall ~ acessing different (hallways shown in gray)
			String rw = getRightWall(explorer.getLoc().getR(), explorer.getLoc().getC(), explorer.getDir(), 5); //using the location to figure out where the right turns need to be
			System.out.println("Row: " + explorer.getLoc().getR()+ " Col: " + explorer.getLoc().getC() + " Dir: " + explorer.getDir()); //print to see the row, coloumn, direction
			System.out.println("rw: " + rw);
			int moveShrink2 = 50;
			//rw ="# #  ";
			for(int s = 0; s < rw.length(); s++)
			{

				if(rw.charAt(s) == (' ')) //if its empty 
				{
					g2.setColor(Color.GRAY); //color gray
					g2.fillRect(700-moveShrink2*(s+1),100+moveShrink2*(s+1),50,600-moveShrink2*(s+1)*2); //rectangle size on the trapazoid polygon right side

				}
			}

			//backwall counter based on the wall amount
			int bwCount = getBackWall(explorer.getLoc().getR(), explorer.getLoc().getC(), explorer.getDir(), 5); //same as left and right
			System.out.println("CHECK: " + bwCount); //printing back wall count

			int variable = 50 * bwCount;
			int variable2 = 100 * bwCount;
			g2.setColor(Color.BLACK); //set to black
			//colorCount++;
			g2.fillRect(151 + variable, 151 + variable, 499 - variable2, 499 - variable2); //repainting the backwall on the exsisting walls when it gets closer




		}
		else{
			// DRAW MAZE
			g2.setColor(Color.GRAY);
			for(int c=0;c<maze[0].length;c++)
			for(int r=0;r<maze.length;r++){
				if (explorer.atLocation(r, c)){
					//EXPLORER
					g2.fillRect(c*size+size,r*size+size,size,size);
					g2.setColor(explorer.getColor());
					g2.fill(explorer.getPoly());
					g2.setColor(Color.GRAY);
				}
				else if (udb != null && udb.atLocation(r,c)){// NEW CODE FOR UpDownBot
					//EXPLORER
					g2.setColor(Color.MAGENTA);
					g2.fillRect(c*size+size,r*size+size,size,size);
					g2.setColor(Color.GRAY);
				}
				//FINAL TILE
				else if (maze[r][c] == 'F'){
					g2.setColor(Color.GREEN);
					g2.fillRect(c*size+size,r*size+size,size,size);
					g2.setColor(Color.GRAY);
				}
				else if(maze[r][c]==' ') // OPEN SQUARE GRAY
					g2.fillRect(c*size+size,r*size+size,size,size);
				else // BLOCKED SQUARE GRAY OUTLINE
					g2.drawRect(c*size+size,r*size+size,size,size);
			}
		}
		if (debug){ // PRINT EXTRA INFO TO HELP DEBUG
			g2.setColor(Color.CYAN);
			g2.drawString("WELCOME TO THE MAZE GAME!",40, numRows*size+3*size); //welcome message
			g2.drawString("It’s your job to get to the green square switching between 2D and 3D view (press the space bar). There's 30 seconds on the clock, good luck!",40, numRows*size+4*size);
			g2.setColor(Color.WHITE);
			g2.drawString("Direction: " + explorer.getTurns(),40, numRows*size+5*size); //shows direction the explorer is facing
			g2.drawString("Moves: " + explorer.getSteps(),40, numRows*size+6*size); //the amount of moves the explorer took


			if(clock) //clock counting down is enabled
			{
	        	if(countdown > 0) //if the time is not 0
				{
					countdown = (14000 -(System.currentTimeMillis()-startTime))/1000; //keep the timer running
					System.out.println(countdown);
				}
				else
				{
					movable = false; //freeze explorer
					clip3.start();
				}	
					
			}

			g2.drawString("Time Remaining: " + countdown , 40, numRows*size+7*size); //show the time remaining
		}
		if(won){
			//setting win statement to CYAN
			g2.setColor(Color.CYAN);
			g2.drawString("WIN!!!",40, numRows*size+8*size);
			g2.drawString("Wanna beat it again? Click \"R\" to restart!",40, numRows*size+9*size);
		}
		if(!movable){ //when the explorer can't move
			//statements to show the player
			g2.setColor(Color.CYAN);
			g2.drawString("TIME IS UP!",40, numRows*size+8*size);
			g2.drawString("Hit the \"R\" key to try again!",40, numRows*size+9*size);
			clip1.stop(); //stop the lofi music
		}

	}
	public void keyPressed(KeyEvent e)
	{
		System.out.println(e.getKeyCode()); //prints the keys

		if(e.getKeyCode() == 32) //if spacebar is pressed switch views
			is3D = !is3D;

		if(movable) //when the explorer moves
		{
			explorer.move(e.getKeyCode(), maze);
			if(endLoc.equals(explorer.getLoc())) //if the explorer hits the end tile
			{
					won = true; //turn won on
					clip1.stop(); //stop lofi music
					clip2.start(); //turn on congratulations song
					clock = false; //stop the clock
					clip3.stop(); //stop the loser song in case it plays

					//movable = false;

			}
			else
			{
				clip1.start(); //start lofi music
				clip2.stop(); //stop the congratulations song
				clip3.stop(); //stop the loser song
				clock = true; //turn clock on

			}

		}

		if(e.getKeyCode() == 82) //if the R button is clicked to restart
		{
			clip1.start(); //start lofi music
			clip2.stop(); //stop the congratulations song
			setBoard();  //reset the maze
			repaint();   //repaint
			won = false; //turn off won
			movable = true; //let the explorer move
			countdown = 15; //restart the timer
			startTime = System.currentTimeMillis(); //reseting the current time
		}


		repaint();


	}
	public void keyReleased(KeyEvent e) //Required for interface, leave empty
	{
	}
	public void keyTyped(KeyEvent e) // Required for interface, leave open
	{
	}

	public void setBoard()
	{
		File name = new File("maze1.txt");
		try
		{
			BufferedReader input = new BufferedReader(new FileReader(name));
			String text,output="";
			int r = 0;
			while((text=input.readLine())!= null)
			{
				if(debug) System.out.println("len->" + text.length()); //debug statement for sizing of maze
				for(int c = 0; c < text.length(); c++)
				{
					maze[r][c] = text.charAt(c);
					if(text.charAt(c) == 'E') //sets explorer at E from text file
					{
						explorer = new Explorer(new Location(r,c), 0, size, Color.RED);
						maze[r][c] = ' ';
					}
					if(text.charAt(c) == 'F') //sets final tile to green from text file
					{
						endLoc = new Location(r, c); //end location for explorer
					}

				}
				r++;
			}
		}
		catch (IOException io)
		{
			System.out.println("File does not exist");
		}

	}

	public String getLeftWall(int r,int c, int dir,int maxDist){

			lw = "";

			if (dir == 0) //if explorer is facing up
				for (int n = 0; n < maxDist; n++)
					if (r-n >= 0)
						lw+= maze[r-n][c-1]; //row up + column one to the left

			if (dir == 1) //if explorer is facing right
						for (int n = 0; n < maxDist; n++)
							if (c+n < numCols)
								lw+= maze[r-1][c+n]; //row one down + column right

			if (dir == 2) //if explorer is facing down
						for (int n = 0; n < maxDist; n++)
							if (r+n < numRows)
								lw+= maze[r+n][c+1];  //row down + colum one to the right

			if (dir == 3) //if explorer is facing left
						for (int n = 0; n < maxDist; n++)
							if (c-n >= 0)
								lw+= maze[r+1][c-n]; //row one up + column left

			return lw;
	}

	//similar to getLeftWall
	public String getRightWall(int r,int c, int dir,int maxDist){

			rw = "";

			if (dir == 0) //facing up
				for (int n = 0; n < maxDist; n++)
					if (r-n >= 0)
						rw+= maze[r-n][c+1]; //row up + column one to the right

			if (dir == 1) //facing right
						for (int n = 0; n < maxDist; n++)
							if (c+n < numCols)
								rw+= maze[r+1][c+n]; //row one down + column right

			if (dir == 2) //facing down
						for (int n = 0; n < maxDist; n++)
							if (r+n < numRows)
								rw+= maze[r+n][c-1]; //row down + column one to the left

			if (dir == 3) //facing left
						for (int n = 0; n < maxDist; n++)
							if (c-n >= 0)
								rw+= maze[r-1][c-n]; //row one up + column left

			return rw;
	}
	public int getBackWall(int r, int c, int dir, int maxDist){
		bwCount = 0;

		if (dir == 1) //facing right
				for (int n = 1; n <= maxDist; n++)
					if (maze[r][c+n] == ' ') //checking to see if its empty only then will it add it to the count
						bwCount++;
					else 
						break; 

		if (dir == 2) //facing down
				for (int n = 1; n <= maxDist; n++)
					if (maze[r+n][c] == ' ') //in the same column
						bwCount++;
					else
						break;

		if (dir == 3) //facing left
				for (int n = 1; n <= maxDist; n++)
					if (maze[r][c-n] == ' ') //in the same row
						bwCount++;
					else
						break;

		if (dir == 0) //facing up
				for (int n = 1; n <= maxDist; n++)
					if(maze[r-n][c] == ' ')
						bwCount++; //adds it to the count
					else
						break;

		return bwCount; 
	}

	public void music(){ //lofi music

		try{
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(MazeProject.class.getResource("/sound/goldcoast.wav")); //music file
			clip1 = AudioSystem.getClip();
			clip1.open(audioIn);
			clip1.start(); //start song
			System.out.println("---------SOUND IS PLAYING--------");
		}catch(Exception ex){
			System.out.println("Error with playing sound");
			ex.printStackTrace();
		}

	}

	public void musicCELEB(){ //congratulation music

		try{
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(MazeProject.class.getResource("/sound/congrats.wav"));
			clip2 = AudioSystem.getClip();
			clip2.open(audioIn);
			clip2.start(); //start song
			System.out.println("---------CELEB SOUND IS PLAYING--------");
		}catch(Exception ex){
			System.out.println("Error with playing sound");
			ex.printStackTrace();
		}

	}

	public void lose(){ //loser music

		try{
			AudioInputStream audioIn3 = AudioSystem.getAudioInputStream(MazeProject.class.getResource("/sound/lose.wav"));
			clip3 = AudioSystem.getClip();
			clip3.open(audioIn3);
			clip3.start(); //start song
			System.out.println("---------LOSE SOUND IS PLAYING--------");
		}catch(Exception ex){
			System.out.println("Error with playing sound");
			ex.printStackTrace();
		}

	}


	public Color getColorCount(int colorCount) //gradient color chaner
	{
		switch(colorCount){ //color counter from above will chose the color here

			case 0: //outermost square (1)
				Color lightestBlue = new Color(207, 228, 232); 
				return lightestBlue;

			case 1: // second square
				Color lighterBlue = new Color(169, 205, 212);
				return lighterBlue;

			case 2: //third square
				 Color realBlue = new Color(134, 180, 189);
				return realBlue;


			case 3: //fourth square
				Color darkerBlue = new Color(104, 156, 166);
				return darkerBlue;


			case 4: //fifth square
				Color darkestBlue = new Color(53, 100, 110);
				return darkestBlue;

		}

		Color pink = new Color(207, 228, 232); //it will become pink if there's a mistake
		return pink;
	}

	  //thread code to keep updating the jframe
	  public void start() { //starting the thread when it's null
	    if (thread == null) {
	      thread = new Thread(this);
	      thread.start(); 
	    }
	  }

	  public void stop() { //stopping the thread and setting it to 0
	    thread = null;
	  }

	  public void run() {
	    while (thread != null) { //when running
	      try {
	        Thread.sleep(1000); //every second it will update
	      } catch (InterruptedException e) {
	      }
	      repaint(); //repaint the frame to show the clock
	    }
	    thread = null;
	  }

	  public void update(Graphics g) {
	    paint(g2); //paint the clock to show every second
	  }	

	public static void main(String[] args) {

	    MazeProject maze = new MazeProject();
	    maze.start(); //start the thread

	}
}