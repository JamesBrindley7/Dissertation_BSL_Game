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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.leapmotion.leap.Controller;

public class Quiz_Game {
	String current = System.getProperty("user.dir");
	Font TitleFont;
	Font HoverFont;
	Font SmallFont;
	Font BiggerFont;
	String[] alphabet = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	JLabel[] Signs =  new JLabel[26];
	Player_Profile profile = null;
	JFrame frame = null;
	JLabel Question = null;
	JLabel[] answersigns = new JLabel[4];
	JLabel[] afterfade = new JLabel[2];
	JLabel notification;
	ArrayList<Integer> answers = new ArrayList<Integer>();
	int positionbox = 0;
	int currentletter = 0;
	int[] answerx = {450, 715};
	int[] answery = {200, 380};
	int[] xposition = {406,662};
	int[] yposition = {205,377};
	JLabel template;
	boolean exitback = false;
	JLabel glove;
	boolean usingleap = true;
	Thread glovethread;
	boolean gaming = false;
	int letterinteger;
	Thread gamethread;
	
	String lettertranslated;
	Thread recorderthread;
	GestureCompare compare;
	String letterfound;
	boolean threadstopper = false;
	Controller controller;
	
	private Thread soundthread;
	Music_Handler musichandler;
	private boolean accepted;
	
	public Quiz_Game(JFrame framein,Controller c,Music_Handler m){
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
	public void load(Player_Profile profilein){
		profile = profilein;
		exitback = false;
		
		startmusichandler();
		
		frame.getContentPane().removeAll();
		frame.repaint();
		frame.setContentPane(new JLabel(new ImageIcon(current+"/Characters/Classroom.png")));
		
		JButton Exit = new JButton("Exit");
		Exit.setBorderPainted(false); 
		Exit.setContentAreaFilled(false); 
		Exit.setFocusPainted(false); 
		Exit.setOpaque(false);
		Exit.setFont(SmallFont);
		Exit.setForeground(Color.black);
		Exit.setBounds(640, -30, 400, 100);
		Exit.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  
	    	  threadstopper = true;
	    	  musichandler.playbuttonclick();
	    	  Front_Menu front = new Front_Menu(frame,profile, new Controller(),musichandler);
	    	  frame.remove(answersigns[0]);
	  		  frame.remove(answersigns[1]);
	  		  frame.remove(answersigns[2]);
	  		  frame.remove(answersigns[3]);
	  		  frame.remove(template);
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
		
		JButton Help = new JButton("Help");
		Help.setBorderPainted(false); 
		Help.setContentAreaFilled(false); 
		Help.setFocusPainted(false); 
		Help.setOpaque(false);
		Help.setFont(SmallFont);
		Help.setForeground(Color.black);
		Help.setBounds(-50, -30, 400, 100);
		Help.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  musichandler.playbuttonclick();
	    	  Quiz_Help helpwind = new Quiz_Help(musichandler);
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
		
		
		glove = new JLabel(new ImageIcon(current+"/Characters/GloveIcon.png"));
		glove.setVisible(true);
		glove.setBounds(100, 100, 100, 73);
		frame.add(glove);
		startglovetracking();
		
		
		JButton guess1 = new JButton("1");
		guess1.setBorderPainted(false); 
		guess1.setContentAreaFilled(false); 
		guess1.setFocusPainted(false); 
		guess1.setOpaque(false);
		guess1.setFont(SmallFont);
		guess1.setForeground(Color.black);
		guess1.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  if(accepted) {
				(new Thread() {
	    	        public void run() {
	    	        	try {
	    	        		
							checkanswer(answers.get(0));
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    	        }
	    	  }).start();
	    	  }
	      }
	    });
		guess1.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	guess1.setFont(BiggerFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	guess1.setFont(SmallFont);
	        }
	     });
		frame.add(guess1);
		guess1.setBounds(xposition[0], yposition[0], 50, 50);
		guess1.setVisible(true);
		
		JButton guess2 = new JButton("2");
		guess2.setBorderPainted(false); 
		guess2.setContentAreaFilled(false); 
		guess2.setFocusPainted(false); 
		guess2.setOpaque(false);
		guess2.setFont(SmallFont);
		guess2.setForeground(Color.black);
		guess2.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  if(accepted) {
				(new Thread() {
	    	        public void run() {
	    	        	try {
							checkanswer(answers.get(1));
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    	        }
	    	  }).start();
	    	  }
	   
	      }
	    });
		guess2.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	guess2.setFont(BiggerFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	guess2.setFont(SmallFont);
	        }
	     });
		frame.add(guess2);
		guess2.setBounds(xposition[1], yposition[0], 50, 50);
		guess2.setVisible(true);
		
		JButton guess3 = new JButton("3");
		guess3.setBorderPainted(false); 
		guess3.setContentAreaFilled(false); 
		guess3.setFocusPainted(false); 
		guess3.setOpaque(false);
		guess3.setFont(SmallFont);
		guess3.setForeground(Color.black);
		guess3.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  if(accepted) {
				(new Thread() {
	    	        public void run() {
	    	        	try {
							checkanswer(answers.get(2));
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    	        }
	    	  }).start();
	    	  }
	      }
	    });
		guess3.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	guess3.setFont(BiggerFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	guess3.setFont(SmallFont);
	        }
	     });
		frame.add(guess3);
		guess3.setBounds(xposition[0], yposition[1], 50, 50);
		guess3.setVisible(true);
		
		JButton guess4 = new JButton("4");
		guess4.setBorderPainted(false); 
		guess4.setContentAreaFilled(false); 
		guess4.setFocusPainted(false); 
		guess4.setOpaque(false);
		guess4.setFont(SmallFont);
		guess4.setForeground(Color.black);
		guess4.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  if(accepted) {
				(new Thread() {
	    	        public void run() {
	    	        	try {
							checkanswer(answers.get(3));
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    	        }
	    	  }).start();
	    	  }
	      }
	    });
		guess4.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	guess4.setFont(BiggerFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	guess4.setFont(SmallFont);
	        }
	     });
		frame.add(guess4);
		guess4.setBounds(xposition[1], yposition[1], 50, 50);
		guess4.setVisible(true);
		
		JLabel podium = new JLabel(new ImageIcon(current+"/Characters/Podium.png"));
		frame.add(podium);
		podium.setBounds(50, 300, 250, 477);
		podium.setVisible(true);
		
		String character = profile.getCharacter();
		JLabel Player = new JLabel(new ImageIcon(current+"/Characters/"+character+".png"));
		frame.add(Player);
		Player.setBounds(90, 250, 150, 150);
		Player.setVisible(true);
		frame.setVisible(true);
		
		JLabel QuestionTitle = new JLabel("Question");
		QuestionTitle.setFont(BiggerFont);
		QuestionTitle.setForeground(Color.black);
		QuestionTitle.setBounds(420, 40, 200, 100);
		QuestionTitle.setVisible(true);
		frame.add(QuestionTitle);
		
		Question = new JLabel("");
		Question.setFont(SmallFont);
		Question.setForeground(Color.black);
		Question.setBounds(420, 90, 500, 100);
		Question.setVisible(true);
		frame.add(Question);
		
		notification = new JLabel("+1000"); 
		notification.setVisible(false);
		notification.setFont(BiggerFont);
		notification.setForeground(Color.black);
		notification.setBounds(700, 80, 100, 50);
		frame.add(notification);
		
		afterfade[0] = new JLabel(new ImageIcon(current+"/Characters/Correct.png"));
		frame.add(afterfade[0]);
		afterfade[0].setVisible(false);
		afterfade[1] = new JLabel(new ImageIcon(current+"/Characters/Wrong.png"));
		frame.add(afterfade[1]);
		afterfade[1].setVisible(false);
		
		for(int i = 0; i < 26; i++) {
			Signs[i] = new JLabel(new ImageIcon(current+"/Signs/"+alphabet[i]+".png")); //Loads letter sign into label
			Signs[i].setVisible(false);
			frame.add(Signs[i]);
		}
		accepted = true;
		generateQuestion();
		generatetemplate();
		startgame();
	}
	public void generateQuestion() {
		 musichandler.playConfirm();
		Random rand = new Random();
		currentletter = rand.nextInt(26) + 0;
		Question.setText("What is the Sign for the letter "+alphabet[currentletter]+"?");
		answers.add(currentletter);
		generateanswers();
	}
	public void generateanswers() {
		letterfound = "";
		letterinteger = -1;
		Random rand = new Random();
		for(int i = 0; i < 3; i++) {
			int num = rand.nextInt(26) + 0;
			if (!answers.contains(num)) {
				answers.add(num);
			}
			else {
				i--;
			}
		}
		randomiseanswers();
		int x = 0;
		int y = 0;
		for(int i = 0; i < 4; i++) {
			if (x == 2) {
				x = 0;
				y = 1;
			}
			if (answers.get(i) == currentletter) {
				positionbox = i;
			}
			answersigns[i] = Signs[answers.get(i)];
			answersigns[i].setBounds(answerx[x], answery[y], 150, 150);
			answersigns[i].setVisible(true);
			frame.add(answersigns[i]);
			x++;
		}
	}
	public void randomiseanswers() {
		Collections.shuffle(answers);
	}
	public void generatetemplate() {
		template = new JLabel(new ImageIcon(current+"/Characters/Template.png"));
		frame.add(template);
		template.setBounds(400, 50, 500, 500);
		template.setVisible(true);
	}
	public void checkanswer(int letterguessed) throws InterruptedException {
		accepted = false;
		if (currentletter == letterguessed) {
			musichandler.playCorrect();
			musichandler.playvoice(profile.getUsernameID());
			profile.setCoins(profile.getCoins()+1000);
			int x = -200;
			int y = -200;
			switch(positionbox) {
			case 0:
				x = xposition[0];
				y = yposition[0];
				break;
			case 1:
				x = xposition[1];
				y = yposition[0];
				break;
			case 2:
				x = xposition[0];
				y = yposition[1];
				break;
			case 3:
				x = xposition[1];
				y = yposition[1];
				break;
			}
			afterfade[0].setBounds(x, y, 232, 160);
			afterfade[0].setVisible(true);
			notification.setVisible(true);
			Thread.sleep(2000);
			afterfade[0].setVisible(false);
			notification.setVisible(false);
		}
		else {
			for(int i = 0; i < answers.size();i++) {
				if(letterguessed == answers.get(i)) {
					musichandler.playIncorrect();
					int x = -200;
					int y = -200;
					switch(i) {
					case 0:
						x = xposition[0];
						y = yposition[0];
						break;
					case 1:
						x = xposition[1];
						y = yposition[0];
						break;
					case 2:
						x = xposition[0];
						y = yposition[1];
						break;
					case 3:
						x = xposition[1];
						y = yposition[1];
						break;
					}
					afterfade[1].setBounds(x, y, 232, 160);
					afterfade[1].setVisible(true);
					Thread.sleep(2000);
					afterfade[1].setVisible(false);
				}
			}
		}
		answers.clear();
		frame.remove(answersigns[0]);
		frame.remove(answersigns[1]);
		frame.remove(answersigns[2]);
		frame.remove(answersigns[3]);
		frame.remove(template);
		if(threadstopper == true) {
			return;
		}
		accepted = true;
		generateQuestion();
		generatetemplate();
	}
	public Player_Profile getprofile() {
		return profile;
	}
	public void loadgame() {
		compare = new GestureCompare();
		compare.loadcontroller(controller,profile);
		compare.load();
		startrecording();
		gaming = true;
		while(gaming) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(letterfound != "" || letterfound != null) {
				//System.out.println(letterfound +" "+ letterinteger);
				//System.out.println(answers);
				if (answers.contains(letterinteger)){
					try {
						checkanswer(letterinteger);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
			if(threadstopper == true) {
				break;
			}
		}
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
	public void startgame() {
		gamethread = new Thread() {
 	        public void run() {
 	        	loadgame();
 	        }
		 };
		 gamethread.start();
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
			//System.out.println(lettertranslated);
			if(lettertranslated != null) {
				letterfound = lettertranslated;
				System.out.println(letterfound);
				for(int i = 0; i < 26;i++) {
					if(lettertranslated.equals(alphabet[i])) {
						letterinteger = i;
					}
				}
			}
			/**
			else {
				letterfound = "";
				letterinteger = -1;
			}
			*/
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
