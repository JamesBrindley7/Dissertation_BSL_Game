import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.leapmotion.leap.Controller;

public class Bingo {
	/**
	 * The current directory
	 */
	private String current = System.getProperty("user.dir");
	/**
	 * Small font (20f)
	 */
	private Font SmallFont;
	/**
	 * Small font (15f)
	 */
	private Font SmallerFont;
	/**
	 * Bigger font (35f)
	 */
	private Font BiggerFont;
	/**
	 * The frame to be used to paint the GUI
	 */
	private JFrame frame = null;
	/**
	 * The player profile
	 */
	private Player_Profile profile;
	/**
	 * Array of alphabet buttons used in the setup stage to select the letters
	 */
	private JButton[] alphabetLabels = new JButton[26];
	/**
	 * Array of jlabels that contain the signs for each alphabet letter
	 */
	private JLabel[] Signs = new JLabel[26];
	/**
	 * Array of each letter in the alphabet
	 */
	private String[] alphabet = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	/**
	 * ArrayList of letters chosen by the user
	 */
	private ArrayList<Integer> chosen = new ArrayList<Integer>();
	/**
	 * ArrayList of the letters chosen by the computer
	 */
	private ArrayList<Integer> answers = new ArrayList<Integer>();
	/**
	 * ArrayList of the order the letters will be displayed on screen
	 */
	private ArrayList<Integer> numbers = new ArrayList<Integer>();
	/**
	 * ArrayList of letters already picked in bingo
	 */
	private ArrayList<String> letterspicked = new ArrayList<String>();
	/**
	 * Number of answers the computer got correct
	 */
	private int computercorrect = 0;
	/**
	 * Number of answers the user got correct
	 */
	private int usercorrect = 0;
	/**
	 * Label showing the users letters
	 */
	private JLabel yourletters;
	/**
	 * Labels showing all the computers letters
	 */
	private JLabel computerletters;
	/**
	 * Array of jlabels containing images of the loading bar icons
	 */
	private JLabel[] baricons = new JLabel[10];
	/**
	 * Loading bar outline label
	 */
	private JLabel baroutline;
	/**
	 * The time limit for each letter, defaults at 3
	 */
	private int timelimit = 3;
	/**
	 * The number that is picked
	 */
	private int numberpicked;
	/**
	 * Label showing all letters that have already been picked
	 */
	private JLabel lettersdone = new JLabel("Letters Picked: ");
	/**
	 * Exit Button
	 */
	private JButton Exit;
	/**
	 * Help Button
	 */
	private JButton Help;
	/**
	 * Glove image used by the glove tracker
	 */
	private JLabel glove;
	/**
	 * Boolean varaible to show if the leap is in use
	 */
	private boolean usingleap = true;
	/**
	 * Thread to control the glove movement
	 */
	private Thread glovethread= new Thread();
	/**
	 * Controller being used by the leap motion
	 */
	private Controller controller;
	/**
	 * The letter equivalent of the integer randomly picked
	 */
	private String lettertranslated;
	/**
	 * Thread to control the sign language recognition
	 */
	private Thread recorderthread= new Thread();
	/**
	 * Instance of the recognition algorithm
	 */
	private GestureCompare compare;
	/**
	 * String to store the letter found by the recognition algorithm
	 */
	private String letterfound;
	/**
	 * Int of the letter stored (Postion in the alphabet array)
	 */
	private int letterinteger;
	/**
	 * Boolean to stop the threads when the exit button is pressed
	 */
	private boolean threadstopper = false;
	private Thread soundthread;
	private Music_Handler musichandler;
	
	JRadioButton Easy;
	JRadioButton Medium;
	JRadioButton Hard;
	
	boolean needtochange = false;
	
	int computerprob = 75;
	
	/**
	 * Constructor for Bingo
	 * @param framein The frame to be used
	 * @param c The controller used by the leap motion
	 */
	public Bingo(JFrame framein, Controller c,Music_Handler m){
		controller = c;
		frame = framein;
		musichandler = m;
		
		
		
		File font_file = new File(current+"/chawp.otf");
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, font_file);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SmallFont = font.deriveFont(20f);
		SmallerFont = font.deriveFont(15f);
		BiggerFont = font.deriveFont(25f);
		for(int i = 0;i <26;i++) {
			numbers.add(i);	
		}
	}
	/**
	 * Method to create the GUI of the game
	 * Adds actionlisteners to buttons
	 * @param profilein
	 */
	public void loadselection(Player_Profile profilein) {
		profile = profilein;
		
		startmusichandler();

		frame.getContentPane().removeAll();
		frame.repaint();
		frame.setContentPane(new JLabel(new ImageIcon(current+"/ChalkBoard.jpg")));
		yourletters = new JLabel("Your Letters:");
		yourletters.setFont(SmallFont);
		yourletters.setForeground(Color.white);
		yourletters.setBounds(80, 30, 500, 100);
		yourletters.setVisible(true);
		frame.add(yourletters);
		
		computerletters = new JLabel("Computer Letters:");
		computerletters.setFont(SmallFont);
		computerletters.setForeground(Color.white);
		computerletters.setBounds(80, 70, 500, 100);
		computerletters.setVisible(true);
		frame.add(computerletters);
		
		JLabel timelabel = new JLabel("Time Limit (Seconds):");
		timelabel.setFont(SmallFont);
		timelabel.setForeground(Color.white);
		timelabel.setBounds(630, 70, 500, 50);
		timelabel.setVisible(true);
		frame.add(timelabel);
		
		glove = new JLabel(new ImageIcon(current+"/Characters/GloveIcon.png"));
		glove.setVisible(true);
		glove.setBounds(100, 100, 100, 73);
		frame.add(glove);
		startglovetracking();
		
		JTextField userbox = new JTextField();
		userbox.setFont(BiggerFont);
		frame.add(userbox);
		userbox.setBounds(700, 110, 100, 30);
		
		
		Easy = new JRadioButton("Easy");
		Easy.setBounds(770, 470, 100, 50);
		Easy.setBorderPainted(false); 
		Easy.setContentAreaFilled(false); 
		Easy.setFocusPainted(false); 
		Easy.setOpaque(false);
		Easy.setFont(SmallFont);
		Easy.setForeground(Color.white);
	    Medium = new JRadioButton("Medium");
	    Medium.setBounds(770, 505, 200, 50);
	    Medium.setBorderPainted(false); 
	    Medium.setContentAreaFilled(false); 
	    Medium.setFocusPainted(false); 
	    Medium.setOpaque(false);
	    Medium.setFont(SmallFont);
	    Medium.setForeground(Color.white);
	    Medium.setSelected(true);
	    Hard = new JRadioButton("Hard");
	    Hard.setBounds(770, 540, 100, 50);
	    Hard.setBorderPainted(false); 
	    Hard.setContentAreaFilled(false); 
	    Hard.setFocusPainted(false); 
	    Hard.setOpaque(false);
	    Hard.setFont(SmallFont);
	    Hard.setForeground(Color.white);
	    
	    ButtonGroup group = new ButtonGroup();

	    group.add(Easy);
	    group.add(Medium);
	    group.add(Hard);
	    
	    frame.add(Easy);
	    frame.add(Medium);
	    frame.add(Hard);
		
		int[] x = {100, 150, 200, 250, 300, 350};
		int[] y = {150, 250, 350, 450, 540};
		int ycounter = 0;
		int xcounter = 0;
		for (int i = 0; i<26; i++) {
			alphabetLabels[i] = new JButton(alphabet[i]);
			alphabetLabels[i].setBorderPainted(false); 
			alphabetLabels[i].setContentAreaFilled(false); 
			alphabetLabels[i].setFocusPainted(false); 
			alphabetLabels[i].setOpaque(false);
			alphabetLabels[i].setFont(SmallFont);
			alphabetLabels[i].setForeground(Color.white);
			 if (xcounter == 6) {
				 ycounter++;
				 xcounter = 0;
			 }
			 alphabetLabels[i].setBounds(x[xcounter], y[ycounter], 55, 55);
			 final int topassin = i;
			 alphabetLabels[i].addActionListener(new ActionListener()
			    {
				int count = topassin;
			      public void actionPerformed(ActionEvent e)
			      {
			    	  musichandler.playbuttonclick();
			    	  yourletters.setText(yourletters.getText()+" "+alphabet[count]+",");
			    	  chosen.add(count);
			    	  int computernum = generatenum();
			    	  computerletters.setText(computerletters.getText()+" "+alphabet[computernum]+",");
			    	  alphabetLabels[count].setEnabled(false);
			    	  if(chosen.size() == 10) {
			    		  try {
			    			  timelimit = Integer.valueOf(userbox.getText());
			    		  }catch(NumberFormatException ex) {
			    			  timelimit = 3;
			    		  }
			    		  loadgame();
			    	  }
			      }
			    });
				alphabetLabels[i].addMouseListener(new MouseAdapter() {
					int count = topassin;
			        public void mouseEntered(MouseEvent me) {
			        	alphabetLabels[count].setFont(BiggerFont);
			        }
			        public void mouseExited(MouseEvent me) {
			        	alphabetLabels[count].setFont(SmallFont);
			        }
			     });
			 xcounter++;
			 frame.add(alphabetLabels[i]);
			 Signs[i] = new JLabel(new ImageIcon(current+"/Signs/"+alphabet[i]+".png")); //Loads letter sign into label
			 Signs[i].setVisible(false);
			 frame.add(Signs[i]);
			 Signs[i].setBounds(400, 200, 118, 96);
		}
		Exit = new JButton("Exit");
		Exit.setBorderPainted(false); 
		Exit.setContentAreaFilled(false); 
		Exit.setFocusPainted(false); 
		Exit.setOpaque(false);
		Exit.setFont(SmallFont);
		Exit.setForeground(Color.white);
		Exit.setBounds(640, -30, 400, 100);
		Exit.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	 threadstopper = true;
	    	 if(needtochange) {
	    		 musichandler.stopallloops();
	    		 musichandler.playIntro();
	    	 }
	    	 musichandler.playbuttonclick();
	    	 Front_Menu front = new Front_Menu(frame,profile,new Controller(),musichandler);
	      }
	    });
		Exit.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	Exit.setFont(BiggerFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	Exit.setFont(SmallFont);
	        }
	     });
		frame.add(Exit);
		Help = new JButton("Help");
		Help.setBorderPainted(false); 
		Help.setContentAreaFilled(false); 
		Help.setFocusPainted(false); 
		Help.setOpaque(false);
		Help.setFont(SmallFont);
		Help.setForeground(Color.white);
		Help.setBounds(-80, -30, 400, 100);
		Help.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  musichandler.playbuttonclick();
	    	  Bingo_Help helpbingo = new Bingo_Help(musichandler);
	      }
	    });
		Help.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	Help.setFont(BiggerFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	Help.setFont(SmallFont);
	        }
	     });
		frame.add(Help);
		
		frame.setVisible(true);
	}
	/**
	 * Generates a random number
	 * Then checks to see if the answer has already been chosen, if not then chose it
	 * @return
	 */
	public int generatenum() {
		Random rand = new Random();
		boolean running = true;
		int num = 0;
		while(running) {
			num = rand.nextInt(26) + 0;
			if (!answers.contains(num)) {
				answers.add(num);
				running = false;
			}
		}
		return num;
	}
	/**
	 * Loads the second part of the bingo game including the count down, etc
	 */
	public void loadgame() {
		frame.getContentPane().removeAll();
		frame.repaint();
		frame.setContentPane(new JLabel(new ImageIcon(current+"/ChalkBoard.jpg")));
		frame.add(computerletters);
		frame.add(yourletters);
		for(int i = 0; i < 26;i++) {
			frame.add(Signs[i]);
		}
		needtochange = true;
		Easy.setVisible(false);
		Medium.setVisible(false);
		Hard.setVisible(false);
		if(Easy.isSelected()) {
			computerprob = 50;
		}
		else if(Medium.isSelected()) {
			computerprob = 75;
		}
		else if(Hard.isSelected()) {
			computerprob = 90;
		}
		
		compare = new GestureCompare();
		compare.loadcontroller(controller,profile);
		compare.load();
		startrecording();
		
		frame.add(glove);
		startglovetracking();
		
		frame.add(Exit);
		frame.add(Help);
		int iconx = 285;
		for(int i = 0; i < 10; i++) {
			baricons[i] = new JLabel(new ImageIcon(current+"/Characters/BarIcons.png"));
			baricons[i].setBounds(iconx, 410, 31,31);
			baricons[i].setVisible(true);
			iconx = iconx + 38;
			frame.add(baricons[i]);
		}
		try {
			musichandler.stopallloops();
			musichandler.playTension();
			loopcountdown();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	/**
	 * Method to set up the countdown and select a letter which is checked against the computers and users letters
	 * @throws InterruptedException
	 */
	public void loopcountdown() throws InterruptedException {
		if(threadstopper == true) {
			return;
		}
		if (numbers.size() == 0) {
			if(computercorrect > usercorrect) {
				computerletters.setText("Computer: Winner");
				yourletters.setText("You: Loser");
				
			}
			else if(usercorrect > computercorrect) {
				computerletters.setText("Computer: Loser");
				yourletters.setText("You: Winner");
			}
			else {
				computerletters.setText("Computer: Draw");
				yourletters.setText("You: Draw");
			}
			musichandler.stopallloops();
			musichandler.playIntro();
			return;
		}
		int num = generaterandomsign();
		numberpicked = num;
		letterspicked.add(alphabet[num]);
		baroutline = new JLabel(new ImageIcon(current+"/Characters/EmptyBar.png"));
		baroutline.setBounds(270, 400, 400, 52);
		baroutline.setVisible(true);
		frame.add(baroutline);
		
		lettersdone.setBounds(80, 500, 800, 50);
		lettersdone.setFont(SmallerFont);
		lettersdone.setForeground(Color.white);
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x<letterspicked.size(); x++) { 
		    sb.append(letterspicked.get(x)).append(", ");
		}
		sb.setLength(sb.length() - 1);
		String LetterDisplay = sb.toString();
		frame.add(lettersdone);
		
		for(int i = 0; i < 10; i++) {
			baricons[i].setVisible(true);
		}
		if (answers.contains(num)) {
			int randomnum = new Random().nextInt((100 - 0) + 1) + 0;
			if(randomnum < computerprob) {
				computercorrect++;
				answers.remove(Integer.valueOf(num));
				computerletters.setText("Computer Letters: "+generatenewstring());
				if (computercorrect == 10) {
					computerletters.setText("Computer: Winner");
					yourletters.setText("You: Loser");
					musichandler.stopallloops();
					musichandler.playIntro();
					needtochange = false;
					return;
				}
			}
		}
		if (chosen.contains(num)) {
			profile.setCoins(profile.getCoins()+500);
			if (usercorrect == 10) {
				yourletters.setText("You: Winner");
				computerletters.setText("Winner: Loser");
				musichandler.stopallloops();
				musichandler.playIntro();
				return;
			}
		}
		(new Thread() {
	        public void run() {
	        	try {
	        		Signs[numberpicked].setVisible(true);
	        		docountdown(num);
	        		lettersdone.setText("Letters Picked: "+ LetterDisplay);
	        		if(threadstopper == true) {
	    				return;
	    			}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	  }).start();
	}
	/**
	 * The countdown loop, called for each letter and checks if correct.
	 * @param num Letter being checked
	 * @throws InterruptedException
	 */
	public void docountdown(int num) throws InterruptedException {
		long timetosleep = timelimit*100;
		for(int i = 0; i < 10; i++) {
			if(threadstopper == true) {
				return;
			}
			if(letterfound != "" || letterfound != null) {
				//System.out.println("letter: "+letterfound );
				if(letterinteger == num) {
					musichandler.playCorrect();
					if (chosen.contains(num)) {
						profile.setCoins(profile.getCoins()+1000);
						usercorrect++;
						chosen.remove(Integer.valueOf(num));
						yourletters.setText("Your Letters: "+generatenewuserstring());
					}
					break;
				}
			}
			Thread.sleep(timetosleep);
			baricons[9-i].setVisible(false);
		}
		frame.remove(baroutline);
		Signs[numberpicked].setVisible(false);
		loopcountdown();
	}
	/**
	 * Generates a new string containing all the computers letters left
	 * @return
	 */
	public String generatenewstring() {
		String newstring = "";
		for(int i = 0; i < answers.size(); i++) {
			newstring = newstring + alphabet[answers.get(i)]+", ";
		}
		return newstring;
	}
	/**
	 * Generates a new string containing all the users letters left
	 * @return
	 */
	public String generatenewuserstring() {
		String newstring = "";
		for(int i = 0; i < chosen.size(); i++) {
			newstring = newstring + alphabet[chosen.get(i)]+", ";
		}
		return newstring;
	}
	/**
	 * Generates a random sign from the ArrayList
	 * @return
	 */
	public int generaterandomsign() {
		int num = 0;
		Collections.shuffle(numbers);
		num = numbers.get(0);
		numbers.remove(0);
		return num;
	}
	/**
	 * Updates the new glove position
	 */
	public void updateglove() {
		while(usingleap) {
			Point a = frame.getMousePosition();
			if(a != null) {
				int x = (int) a.getX() - 100;
				int y = (int) a.getY() - 30;
				glove.setBounds(x, y, 100, 73);
			}
			if(threadstopper == true) {
				break;
			}
		}
	}
	/**
	 * Starts the glove tracking thread
	 */
	public void startglovetracking() {
		glovethread = new Thread() {
 	        public void run() {
 	        	updateglove();
 	        }
		 };
		 glovethread.start();
	}
	/**
	 * Starts the gesture recognition thread
	 */
	public void startrecording() {
		recorderthread = new Thread() {
 	        public void run() {
 	        	loopthrough();
 	        }
		 };
		 recorderthread.start();
	}
	/**
	 * The loop for the gesture recognition, constantly gets the current gesture being performed
	 */
	public void loopthrough() {
		while(true){
			if(threadstopper == true) {
				break;
			}
			lettertranslated = compare.getframe();
			if(lettertranslated != null) {
				for(int i = 0; i < 26;i++) {
					if(lettertranslated.equals(alphabet[i])) {
						letterinteger = i;
					}
				}
				letterfound = lettertranslated;
			}
			else {
				letterfound = "";
				letterinteger = 27;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void startmusichandler() {
		soundthread = new Thread() {
		        public void run() {
		        	updatemusic();
		        }
		 };
		 soundthread.start();
	}
	public void updatemusic() {
		while(true) {
			if(threadstopper == true) {
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			musichandler.closeunused();
		}
	}
}
