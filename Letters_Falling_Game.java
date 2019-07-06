import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import com.leapmotion.leap.Controller;

public class Letters_Falling_Game {
	String current = System.getProperty("user.dir");
	Font TitleFont;
	Font HoverFont;
	Font SmallFont;
	Font BiggerFont;
	JFrame frame = null;
	JLabel Letter = null;
	String[] alphabet = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	String[] alphabet2 = {"B","B","B","B","B","B","B","B","B","B","B","B","B","B","B","B","B","B","B","B","B","B","B","B","B","B"};
	boolean[] rocketlaunched = new boolean[30];
	JLabel[] Letters =  new JLabel[30];
	JLabel Lives;
	JLabel gameover;
	int CurrentX = 400;
	int CurrentY = 10;
	int SpeedX = 2;
	int SpeedY = 2;
	int live = 3;
	Player_Profile profile = null;
	int lettercounter = 0;
	String lettertranslated;
	Thread recorderthread= new Thread();
	GestureCompare compare;
	String letterfound;
	int score = 0;
	JLabel scorelabel;
	ArrayList<Integer> togotolist = new ArrayList<Integer>();
	JLabel glove;
	boolean usingleap = true;
	Thread glovethread = new Thread();
	Controller controller;
	Thread gamethread= new Thread();
	
	boolean threadstopper = false;
	
	boolean isgameover = false;
	
	JRadioButton Easy;
	JRadioButton Medium;
	JRadioButton Hard;
	
	private Thread soundthread;
	Music_Handler musichandler;
	JButton restart;
	
	Thread rocketthread;
	int xloc;
	int yloc;
	int togoto;
	
	Thread lightthread;
	boolean loadedfirst = true;
	
	boolean gamerestart = false;
	
	boolean needtochangemusic = false;
	
	public Letters_Falling_Game(JFrame framein, Controller c,Music_Handler m){
		frame = framein;
		controller = c;
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
	public void load(Player_Profile profilein) {
		profile = profilein;
		
		if(loadedfirst) {
			startmusichandler();
			frame.getContentPane().removeAll();
			frame.repaint();
			frame.setContentPane(new JLabel(new ImageIcon(current+"/ChalkBoard.jpg")));
			compare = new GestureCompare();
			compare.loadcontroller(controller,profile);
			compare.load();
			loadedfirst = false;
			loadbuttons();
		}
		else {
			for(int i = 0;i<30;i++) {
				if(Letters[i] != null) {
					Letters[i].setVisible(false);
					Letters[i] = null;
				}
			}
			live = 3;
			score = 0;
			CurrentX = 400;
			CurrentY = 10;
			SpeedX = 2;
			SpeedY = 2;
			isgameover = false;
			lettercounter = 0;
			lettertranslated = new String();
			letterfound = new String();
			score = 0;
			rocketlaunched = new boolean[30];
			togotolist.clear();
			gameover.setVisible(false);
			Lives.setText("Lives: "+live);
			scorelabel.setText("Score: 0");
			startgamethread();
		}
		frame.setVisible(true);
		
	}
	public void loadbuttons() {
		
		glove = new JLabel(new ImageIcon(current+"/Characters/GloveIcon.png"));
		glove.setVisible(true);
		glove.setBounds(100, 100, 100, 73);
		frame.add(glove);
		startglovetracking();
		
		JButton Exit = new JButton("Exit");
		Exit.setBorderPainted(false); 
		Exit.setContentAreaFilled(false); 
		Exit.setFocusPainted(false); 
		Exit.setOpaque(false);
		Exit.setFont(SmallFont);
		Exit.setForeground(Color.white);
		Exit.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  threadstopper = true;
	    	  if(needtochangemusic) {
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
		Easy = new JRadioButton("Easy");
		Easy.setBounds(75, 50, 100, 50);
		Easy.setBorderPainted(false); 
		Easy.setContentAreaFilled(false); 
		Easy.setFocusPainted(false); 
		Easy.setOpaque(false);
		Easy.setFont(SmallFont);
		Easy.setForeground(Color.white);
	    Medium = new JRadioButton("Medium");
	    Medium.setBounds(75, 85, 200, 50);
	    Medium.setBorderPainted(false); 
	    Medium.setContentAreaFilled(false); 
	    Medium.setFocusPainted(false); 
	    Medium.setOpaque(false);
	    Medium.setFont(SmallFont);
	    Medium.setForeground(Color.white);
	    Medium.setSelected(true);
	    Hard = new JRadioButton("Hard");
	    Hard.setBounds(75, 120, 100, 50);
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
	    	  Falling_Help helpwind = new Falling_Help(musichandler);
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
	    	 load(profile);
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
		
		
		
		JButton Start = new JButton("Start Game");
		Start.setBorderPainted(false); 
		Start.setContentAreaFilled(false); 
		Start.setFocusPainted(false); 
		Start.setOpaque(false);
		Start.setFont(TitleFont);
		Start.setForeground(Color.white);
		Start.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  musichandler.playbuttonclick();
	    	  Start.setVisible(false);
	    	  startgamethread();
	      }
	    });
		Start.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	Start.setFont(HoverFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	Start.setFont(TitleFont);
	        }
	     });
		
		scorelabel = new JLabel("Score: "+score);
		scorelabel.setFont(BiggerFont);
		scorelabel.setForeground(Color.white);
		
		JLabel StopLine = new JLabel("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
		StopLine.setFont(BiggerFont);
		StopLine.setForeground(Color.white);
		
		Lives = new JLabel("Lives: "+live);
		Lives.setFont(BiggerFont);
		Lives.setForeground(Color.white);
		
		gameover = new JLabel("Game Over");
		gameover.setFont(HoverFont);
		gameover.setForeground(Color.white);
		gameover.setVisible(false);
		
	
		frame.add(scorelabel);
		frame.add(Exit);
		frame.add(Start);
		frame.add(StopLine);
		frame.add(Lives);
		frame.add(gameover);
	
		
		scorelabel.setBounds(710, 510, 500, 100);
		Lives.setBounds(90, 510, 500, 100);
		Exit.setBounds(640, -30, 400, 100);
		Start.setBounds(250, 300, 500, 100);
		gameover.setBounds(300, 300, 500, 100);
		StopLine.setBounds(80, 450, 1000, 50);
		
	}
	public void generateletters() {
		Random rand = new Random();
		String[] Letterstodrop = new String[50];
		for(int i = 0;i<50;i++) {
			int n = rand.nextInt(25) + 1;
			Letterstodrop[i] = alphabet[n];
		}
		int x = 100;
		int[] xgrid = {100, 400, 750};
		int xcounter = 0;
		int[] ygrid = {0, -300, -600, -900, -1200, -1500, -1800, -2100, -2400, -2700, -3000};
		int ycounter = 0;
		for(int i = 0;i<30;i++) {
			int yoffset = rand.nextInt(150) + 1;
			int xoffset = rand.nextInt(90) + 1;
			Letters[i] = new JLabel(Letterstodrop[i]);
			Letters[i].setFont(TitleFont);
			Letters[i].setForeground(Color.white);
			frame.add(Letters[i]);
			if(xcounter > 2) {
				xcounter = 0;
				ycounter++;
			}
			Letters[i].setBounds(xgrid[xcounter] + xoffset, ygrid[ycounter] - yoffset, 50, 50);
			Letters[i].setVisible(true);
			xcounter++;
		}
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
		Easy.setVisible(false);
		Medium.setVisible(false);
		Hard.setVisible(false);
		generateletters();
		if(Easy.isSelected()) {
			SpeedX = 1;
			SpeedY = 1;
		}
		else if(Medium.isSelected()) {
			SpeedX = 2;
			SpeedY = 2;
		}
		else if(Hard.isSelected()) {
			SpeedX = 3;
			SpeedY = 3;
		}
		boolean gaming = true;
		int completed = 0;
		gamerestart = false;
		startrecording();
		String lowestletter = "None";
		int prevlowest = 0;
		int position = 0;
		while(gaming) {
			if(threadstopper == true || gamerestart == true) {
				gaming = false;
				break;
			}
			CurrentX += SpeedX;
			CurrentY += SpeedY;
			prevlowest = 0;
			for(int i = 0;i<30;i++) {
				if (Letters[i] != null) {
					if(Letters[i].getY() > prevlowest && Letters[i].getY() > 50 && rocketlaunched[i] == false) {
						prevlowest = Letters[i].getY();
						lowestletter = Letters[i].getText(); 
						position = i;
					}
					if(Letters[i].getY() < 450) {
						Letters[i].setLocation(Letters[i].getX(), Letters[i].getY()+SpeedY);
					}
					if (Letters[i] != null && !togotolist.contains(i)) {
						if(Letters[i].getY()+SpeedY >=450) {
						startlightning(i);
						togotolist.add(i);
						musichandler.playIncorrect();
						live--;
						completed++;
						Lives.setText("Lives: "+live);
						if (live == 2) {
							
							musichandler.stopallloops();
							musichandler.playCalm();
							needtochangemusic = true;
						}
						if (live == 1) {
							
							musichandler.stopallloops();
							musichandler.playTension();
						}
						if (live == 0) {
							
							musichandler.stopallloops();
							musichandler.playIntro();
							gameover.setVisible(true);
							needtochangemusic = false;
							restart.setVisible(true);
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return;
						}
						}
					}
				}
			}
			
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			if(lowestletter != "None" && letterfound !="" && letterfound != null && Letters[position] != null && rocketlaunched[position] == false && !togotolist.contains(position)) {
				if(letterfound.equals(lowestletter)) {
					Letters[position].setForeground(Color.red);
					musichandler.playCorrect();
					rocketlaunched[position] = true;
					//Letters[position].setVisible(false);
					yloc = (int) Letters[position].getLocation().getY();
					xloc = (int) Letters[position].getLocation().getX();
					togoto = position;
					togotolist.add(position);
					startrocketthread();
					lowestletter = "None";
					completed++;
					score = score + 1000;
					profile.setCoins(profile.getCoins()+(1000*SpeedX));
					scorelabel.setText("Score: "+score);
				}
			}
			if(completed == 30) {
				isgameover = true;
				musichandler.playConfirm();
				gameover.setText("Winner");
				gameover.setVisible(true);
				recorderthread.interrupt();
				score = score + 10000;
				profile.setCoins(profile.getCoins()+10000);
				restart.setVisible(true);
				return;
			}
			frame.repaint();
		}
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
				//System.out.println(letterfound);
			}
			else {
				letterfound = "";
			}
			try {
				if(threadstopper == true) {
					break;
				}
				else {
					Thread.sleep(100);
				}
			} catch(InterruptedException e) {
				System.out.println("Something went wrong, please exit the gamemode");
			}
		}
	}
	public void startlightning(int i) {
		lightthread = new Thread() {
 	        public void run() {
 	        	lightningstrike(i);
 	        }
		 };
		 lightthread.start();
	}
	public void lightningstrike(int i) {
		JLabel light = new JLabel(new ImageIcon(current+"/Characters/Lightning_Forever.gif"));
		frame.add(light);
		light.setBounds( (int) Letters[i].getLocation().getX()-100, (int) Letters[i].getLocation().getY()-160, 200, 200);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Letters[i].setVisible(false);
		Letters[i]=null;
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame.remove(light);
		light = null;
		
	}
	public void startrocketthread() {
		rocketthread = new Thread() {
 	        public void run() {
 	        	rocketloop();
 	        }
		 };
		 rocketthread.start();
	}
	public void rocketloop() {
		int ylocation = yloc;
		int rockety = 550;
		int xlocation = xloc-60;
		int target = togoto;
		JLabel explosion = new JLabel(new ImageIcon(current+"/Characters/Explosion.gif"));
		explosion.setVisible(false);
		frame.add(explosion);
		JLabel rocket = new JLabel(new ImageIcon(current+"/Characters/RocketShip.png"));
		frame.add(rocket);
		rocket.setVisible(true);
		rocket.setBounds(xlocation, rockety, 150, 150);
		boolean running = true;
		while(running){
			if(threadstopper == true) {
				break;	
			}
			rocket.setLocation(xlocation, rockety);
			if(rockety > ylocation -3 && rockety < ylocation+3) {
				rocket.setVisible(false);
				explosion.setBounds(xlocation-20, rockety-60, 221, 150);
				explosion.setVisible(true);
				musichandler.playexplosion();
				
				Letters[target].setVisible(false);
				Letters[target] = null;
				
				try {
					Thread.sleep(600);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				explosion.setVisible(false);
				running = false;
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rockety = rockety - SpeedY;
			if(ylocation < 450 && !isgameover) {
				ylocation = ylocation + SpeedY;
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
