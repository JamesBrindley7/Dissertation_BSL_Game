import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.leapmotion.leap.Controller;

public class Hand_Man_Game {
	String current = System.getProperty("user.dir");
	Font TitleFont;
	Font HoverFont;
	Font SmallFont;
	Font BiggerFont;
	JFrame frame = null;
	JLabel gameover;
	String prevword = "";
	String[] alphabet = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	JButton[] alphabetLabels = new JButton[26];
	JLabel[] hangmanimages = new JLabel[26];
	JLabel[] Signs =  new JLabel[26];
	JLabel[] wordlines =  new JLabel[26];
	JLabel[] wordletters =  new JLabel[26];
	String[] guessedletters = new String[26];
	String[] hangwords = {"PASSWORD","COMPUTER","SCIENCE","BLANKET","APPLE","BANNANA", "LANGUAGE", "COOK", "IRON", "LAPTOP", "TEA", "TABLE","CHAIR"};
	int hangmanlives = 0; //max is 11 and then game over
	int leftX = 60;
	int RightX = 120;
	int Y = 5;
	int selected = 0;
	int signloaded = -1;
	int prevsign = -1;
	int lettersguessed = 0;
	boolean changed = true;
	Player_Profile profile = null;
	Controller controller;
	JLabel glove;
	boolean usingleap = true;
	Thread glovethread= new Thread();
	String letter = "";
	public static Hashtable<String, Hashtable<Integer,Gesture>> GestureBaseLine = new Hashtable<String, Hashtable<Integer,Gesture>>(); 
	
	int lettercounter = 0;
	String lettertranslated;
	Thread recorderthread= new Thread();
	GestureCompare compare;
	String letterfound;
	String[] allletters;
	int numletters;
	private Thread soundthread;
	Music_Handler musichandler;
	JButton restart;
	Thread gamethread;
	
	boolean loadedfirst = true;
	
	
	boolean threadstopper = false;
	boolean gamerestart = false;
	
	public Hand_Man_Game(JFrame framein,Music_Handler m){
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
		TitleFont = font.deriveFont(50f);
		HoverFont = font.deriveFont(70f);
		SmallFont = font.deriveFont(20f);
		BiggerFont = font.deriveFont(25f);
		
		
	}
	public void load(Player_Profile profilein,Controller c) {
		profile = profilein;
		controller = c;
		
		if(loadedfirst) {
			frame.getContentPane().removeAll();
			frame.repaint();
			frame.setContentPane(new JLabel(new ImageIcon(current+"/ChalkBoard.jpg")));
			startmusichandler();
			compare = new GestureCompare();
			compare.loadcontroller(controller,profile);
			compare.load();
			loadedfirst = false;
			reload();
			generatehangman();
		}
		else {
			gameover.setVisible(false);	
			for (int i = 0; i<26; i++) {
				alphabetLabels[i].setEnabled(true);
				alphabetLabels[i].setVisible(true);
				alphabetLabels[i].setForeground(Color.white);
			}
			 Signs[signloaded].setVisible(false);
			removelines(numletters);
			Y = 5;
	    	guessedletters = new String[26];
	    	selected = 0;
	    	signloaded = -1;
	    	prevsign = -1;
	    	lettersguessed = 0;
	    	changed = true;
	    	lettercounter = 0;
	    	lettertranslated = new String();
	    	letterfound  = new String();
	    	numletters = 0;
	    	hangmanlives = 0;
	    	updatehangman();
	    	startgamethread();
		}
		frame.setVisible(true);
	}
	public void reload() {
		JButton Exit = new JButton("Exit");
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
	    	 musichandler.playbuttonclick();
	    	 Front_Menu front = new Front_Menu(frame,profile, controller,musichandler);
	    	 
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
		
		restart = new JButton("New Game");
		restart.setBorderPainted(false); 
		restart.setContentAreaFilled(false); 
		restart.setFocusPainted(false); 
		restart.setOpaque(false);
		restart.setFont(SmallFont);
		restart.setForeground(Color.white);
		restart.setBounds(620, 570, 400, 100);
		restart.setVisible(false);
		restart.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	 gamerestart = true;
	    	 musichandler.playbuttonclick();
	    	 load(profile, controller);
	    	 restart.setVisible(false);
	      }
	    });
		restart.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	restart.setFont(BiggerFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	restart.setFont(SmallFont);
	        }
	     });
		frame.add(restart);
		
		glove = new JLabel(new ImageIcon(current+"/Characters/GloveIcon.png"));
		glove.setVisible(true);
		glove.setBounds(100, 100, 100, 73);
		frame.add(glove);
		startglovetracking();
		
		JButton Start = new JButton("Start Game");
		Start.setBorderPainted(false); 
		Start.setContentAreaFilled(false); 
		Start.setFocusPainted(false); 
		Start.setOpaque(false);
		Start.setFont(TitleFont);
		Start.setForeground(Color.white);
		Start.setBounds(230, 300, 500, 100);
		Start.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  Start.setVisible(false);
	    	  startrecording();
	    	  startgamethread();
	      }
	    });
		Start.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	Start.setFont(HoverFont);
	        	Start.setBounds(220, 300, 500, 100);
	        }
	        public void mouseExited(MouseEvent me) {
	        	Start.setFont(TitleFont);
	        	Start.setBounds(230, 300, 500, 100);
	        }
	     });
		JButton Help = new JButton("Help");
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
	    	  Hangman_Help helpwind = new Hangman_Help(musichandler);
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
		frame.add(Exit);
		frame.add(Start);
		gameover = new JLabel("Game Over");
		gameover.setFont(HoverFont);
		gameover.setForeground(Color.white);
		gameover.setVisible(false);
		gameover.setBounds(300, 300, 500, 100);
		frame.add(gameover);
		//Gets the alphabet buttons
		for (int i = 0; i<26; i++) {
			
			alphabetLabels[i] = new JButton(alphabet[i]);
			alphabetLabels[i].setBorderPainted(false); 
			alphabetLabels[i].setContentAreaFilled(false); 
			alphabetLabels[i].setFocusPainted(false); 
			alphabetLabels[i].setOpaque(false);
			alphabetLabels[i].setFont(SmallFont);
			alphabetLabels[i].setForeground(Color.white);
			 if (i % 2 == 0) {
				 Y = Y + 40;
				 alphabetLabels[i].setBounds(leftX, Y, 55, 55);
			 	
			 } 
			 else {
				 alphabetLabels[i].setBounds(RightX, Y, 55, 55);
		   }
			 alphabetLabels[i].setEnabled(false);
			 frame.add(alphabetLabels[i]);
			 Signs[i] = new JLabel(new ImageIcon(current+"/Signs/"+alphabet[i]+".png")); //Loads letter sign into label
			 Signs[i].setVisible(false);
			 frame.add(Signs[i]);
			 Signs[i].setBounds(750, 60, 100, 100);
			 final int topassin = i;
			 alphabetLabels[i].addActionListener(new ActionListener()
			    {
				int count = topassin;
			      public void actionPerformed(ActionEvent e)
			      {
			    	  musichandler.playbuttonclick();
			    	  if(signloaded != -1) {
			    		  Signs[signloaded].setVisible(false);
			    	  }
			    	  Signs[count].setVisible(true);
			    	  signloaded = count;
			    	  letter = alphabet[count];
			    	  if(!controller.isConnected()) {
			    		  guess(allletters,numletters);
			    		  checkworddone();
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
		}
	}
	public void generatehangman() {
		for(int counter = 1; counter<12;counter++) {
			hangmanimages[counter-1] = new JLabel(new ImageIcon(current+"/HangMan/HangMan"+counter+".png"));
			hangmanimages[counter-1].setBounds(300, 50, 400, 400);
			hangmanimages[counter-1].setVisible(false);
			frame.add(hangmanimages[counter-1]);
		}
	}
	public void updatehangman() {
		switch (hangmanlives) {
			case 0:
				for(int i = 0; i < 11 ;i++) {
					hangmanimages[i].setVisible(false);
				}
				break;
			case 1:
				hangmanimages[0].setVisible(true);
				break;
			case 2:
				hangmanimages[0].setVisible(false);
				hangmanimages[1].setVisible(true);
				break;
			case 3:
				hangmanimages[1].setVisible(false);
				hangmanimages[2].setVisible(true);
				break;
			case 4:
				hangmanimages[2].setVisible(false);
				hangmanimages[3].setVisible(true);
				break;
			case 5:
				hangmanimages[3].setVisible(false);
				hangmanimages[4].setVisible(true);
				break;
			case 6:
				hangmanimages[4].setVisible(false);
				hangmanimages[5].setVisible(true);
				break;
			case 7:
				hangmanimages[5].setVisible(false);
				hangmanimages[6].setVisible(true);
				break;
			case 8:
				hangmanimages[6].setVisible(false);
				hangmanimages[7].setVisible(true);
				break;
			case 9:
				hangmanimages[7].setVisible(false);
				hangmanimages[8].setVisible(true);
				break;
			case 10:
				hangmanimages[8].setVisible(false);
				hangmanimages[9].setVisible(true);
				break;
			case 11:
				hangmanimages[9].setVisible(false);
				hangmanimages[10].setVisible(true);
				break;
		}
		frame.repaint();
	}
	public String generateword() {
		Random rand = new Random();
		int wordselect = rand.nextInt(hangwords.length-1) + 1;
		String selectedword = hangwords[wordselect];
		return selectedword;
	}
	public int countletters(String word) {
		int length = word.length( );
		return length;
	}
	public void generatelines(int length) {
		int totalspan = 500;
		int sizeeach = (totalspan-length*10)/length;
		int difference = totalspan/length;
		for(int i = 0; i < length; i++) {
			wordlines[i] = new JLabel("-");
			wordlines[i].setFont(HoverFont);
			wordlines[i].setForeground(Color.white);
			frame.add(wordlines[i]);
			
			wordlines[i].setBounds(300+(difference*i), 550, sizeeach, 20);
			
			wordlines[i].setVisible(true);
		}
	}
	public void removelines(int length) {
		for(int i = 0; i < length; i++) {
			wordlines[i].setVisible(false);
			wordletters[i].setVisible(false);
		}
	}
	public void generateletters(String[] allletters) {
		int totalspan = 500;
		int length = allletters.length;
		int sizeeach = (totalspan-length*10)/length;
		int difference = totalspan/length;
		for(int i = 0;i<allletters.length;i++) {
			wordletters[i] = new JLabel(allletters[i]);
			wordletters[i].setFont(TitleFont);
			wordletters[i].setForeground(Color.white);
			frame.add(wordletters[i]);
			wordletters[i].setBounds(300+(difference*i), 510, sizeeach, 40);
			wordletters[i].setVisible(false);
		}
	}
	public boolean checkletters(String[] allletters, int length) {
		int selectedletter = signloaded;
		String letterloaded = alphabet[selectedletter];
		int[] foundletters = new int[length];
		boolean found = false;
		for(int i = 0;i<length;i++) {
			if(letterloaded.equals(allletters[i])) {
				musichandler.playCorrect();
				foundletters[i] = 1;
				wordletters[i].setVisible(true);
				found = true;
				lettersguessed++;
				
				guessedletters[lettersguessed] = letterloaded;
			}
		}
		if(!found) {
			musichandler.playIncorrect();
		}
		profile.setCoins(profile.getCoins()+(lettersguessed*500));
		alphabetLabels[selectedletter].setForeground(Color.gray);
		alphabetLabels[selectedletter].setEnabled(false);
		frame.repaint();
		return found;
	}
	public String[] convertintoarray(String word, int length) {
		
		String[] allletters = new String[length];
		for(int i = 0;i<length;i++) {
			allletters[i] = Character.toString(word.charAt(i));
		}
		return allletters;
	}
	public void checkchanged() {
		if (prevsign == signloaded) {
			changed = false;
		}
		else {
			changed = true;
		}
	}
	public void updatesignloaded() {
		prevsign = signloaded;
	}
	public void startgamethread() {
		gamethread = new Thread() {
 	        public void run() {
 	        	startgame();
 	        }
		 };
		gamethread.start();
	}
	public void startgame() {
		for(int p = 0; p < 26; p++) {
			alphabetLabels[p].setEnabled(true);
		}
		//compare = new GestureCompare();
		//compare.loadcontroller(controller,profile);
		//compare.load();
		boolean checking = true;
		String word = "";
		while(checking) {
			word = generateword();
			if(!prevword.equals(word)) {
				checking = false;
				prevword = word;
			}
		}
		numletters = countletters(word);
		generatelines(numletters);
		allletters = convertintoarray(word, numletters);
		generateletters(allletters);
		boolean gaming = true;
		gamerestart = false;
		while(gaming) {
			if(threadstopper == true || gamerestart == true) {
				gaming = false;
				break;
			}
			if(letterfound != null && signloaded != -1) {
				if (letterfound.equals(letter) && alphabetLabels[signloaded].isEnabled()) {
					boolean found = guess(allletters,numletters);
					
					if(found) {
						gaming = false;
						return;
					}

					alphabetLabels[signloaded].setEnabled(false);
					
					boolean check = checkworddone();
					if(check) {
						gaming = false;
						return;
					}
				}
				
			}	
			try{
				Thread.sleep(500);
		    }
		    catch(InterruptedException e){
		            System.out.println(e);
		    }
		}
	}
	public boolean checkworddone() {
		if (lettersguessed == numletters) {
			profile.setCoins(profile.getCoins()+5000);
			gameover.setText("Winner");
			gameover.setVisible(true);
			restart.setVisible(true);
			musichandler.playvoice(profile.getUsernameID());
			for(int i = 0; i < 26;i++) {
				alphabetLabels[i].setEnabled(false);
			}
			return true;
		}	
		return false;
	}
	public boolean guess(String[] allletters, int numletters) {
		boolean found = checkletters(allletters,numletters);
		if (!found) {
			hangmanlives++;
			updatehangman();
			if (hangmanlives == 11) {
				gameover.setVisible(true);
				restart.setVisible(true);
				return true;
			}
		}
		return false;
	}
	public Player_Profile getprofile() {
		return profile;
	}
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
	public void startglovetracking() {
		glovethread = new Thread() {
 	        public void run() {
 	        	updateglove();
 	        }
		 };
		 glovethread.start();
	}
	public void startrecording() {
		recorderthread = new Thread() {
 	        public void run() {
 	        	loopthrough();
 	        }
		 };
		 recorderthread.start();
	}
	public void loopthrough() {
		while(true){
			if(threadstopper == true) {
				break;
			}
			lettertranslated = compare.getframe();
			if(lettertranslated != null) {
				letterfound = lettertranslated;
			}
			else {
				letterfound = null;
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
