
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.*;

import com.leapmotion.leap.Controller;

public class Front_Menu {

	String current = System.getProperty("user.dir");
	Player_Profile profile = null;
	JFrame frame;
	Font TitleFont;
	Font HoverFont;
	Font SmallFont;
	Font BiggerFont;
	Font MediumFont;
	JLabel glove;
	boolean usingleap = true;
	Thread glovethread;
	Controller controller;
	
	boolean threadstopper = false;
	
	private Thread soundthread;
	Music_Handler musichandler;
	
	public Front_Menu(JFrame framein, Player_Profile profilein, Controller c,Music_Handler m) {
		profile = profilein;
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
		MediumFont = font.deriveFont(40f);
		
		framerepaint();
		
	
	}
	public void SaveGame() throws IOException {
		String username = profile.getUsername();
		File f = new File(current+"/UserData/"+username+".SignProf");
		FileOutputStream fos = new FileOutputStream(f);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(profile);
		oos.close();
	}
	public void framerepaint() {
		frame.getContentPane().removeAll();
		frame.repaint();
		frame.setContentPane(new JLabel(new ImageIcon(current+"/ChalkBoard.jpg")));
		profile.setCoins(profile.getCoins()+500);
		JLabel labeltitle1 = new JLabel("The School of");
		labeltitle1.setFont(TitleFont);
		labeltitle1.setForeground(Color.white);
		
		JLabel labeltitle2 = new JLabel("British Sign Language");
		labeltitle2.setFont(TitleFont);
		labeltitle2.setForeground(Color.white);
		
		glove = new JLabel(new ImageIcon(current+"/Characters/GloveIcon.png"));
		glove.setVisible(true);
		glove.setBounds(100, 100, 100, 73);
		frame.add(glove);
		
		startglovetracking();
		startmusichandler();
		
		JButton Falling = new JButton("Falling");
		Falling.setBorderPainted(false); 
		Falling.setContentAreaFilled(false); 
		Falling.setFocusPainted(false); 
		Falling.setOpaque(false);
		Falling.setFont(MediumFont);
		Falling.setForeground(Color.white);
		Falling.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	musichandler.playbuttonclick();
	    	Letters_Falling_Game fallinggame = new Letters_Falling_Game(frame,controller,musichandler);
	    	fallinggame.load(profile);
	      }
	    });
		Falling.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	Falling.setFont(TitleFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	Falling.setFont(MediumFont);
	        }
	     });
		JButton QuizButton = new JButton("Quiz");
		QuizButton.setBorderPainted(false); 
		QuizButton.setContentAreaFilled(false); 
		QuizButton.setFocusPainted(false); 
		QuizButton.setOpaque(false);
		QuizButton.setFont(MediumFont);
		QuizButton.setForeground(Color.white);
		QuizButton.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	musichandler.playbuttonclick();
	    	Quiz_Game quiz = new Quiz_Game(frame,controller,musichandler);
	    	quiz.load(profile);
	      }
	    });
		QuizButton.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	QuizButton.setFont(TitleFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	QuizButton.setFont(MediumFont);
	        }
	     });
		
		JButton CharacterButton = new JButton("Character Selection");
		CharacterButton.setBorderPainted(false); 
		CharacterButton.setContentAreaFilled(false); 
		CharacterButton.setFocusPainted(false); 
		CharacterButton.setOpaque(false);
		CharacterButton.setFont(SmallFont);
		CharacterButton.setForeground(Color.white);
		CharacterButton.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  musichandler.playbuttonclick();
	    	  Character_Select Characterselect = new Character_Select(frame,musichandler);
	    	  Characterselect.load(profile);
	      }
	    });
		CharacterButton.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	CharacterButton.setFont(BiggerFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	CharacterButton.setFont(SmallFont);
	        }
	     });
		
		JButton Save = new JButton("Save");
		Save.setBorderPainted(false); 
		Save.setContentAreaFilled(false); 
		Save.setFocusPainted(false); 
		Save.setOpaque(false);
		Save.setFont(SmallFont);
		Save.setForeground(Color.white);
		Save.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  try {
	    		musichandler.playSave();
				SaveGame();
				Save.setForeground(Color.green);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	      }
	    });
		Save.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	Save.setFont(BiggerFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	Save.setFont(SmallFont);
	        }
	     });
		
		
		JButton Hangman = new JButton("Hangman");
		Hangman.setBorderPainted(false); 
		Hangman.setContentAreaFilled(false); 
		Hangman.setFocusPainted(false); 
		Hangman.setOpaque(false);
		Hangman.setFont(MediumFont);
		Hangman.setForeground(Color.white);
		Hangman.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  musichandler.playbuttonclick();
	    	  Hand_Man_Game hangmangame = new Hand_Man_Game(frame,musichandler);
	    	  hangmangame.load(profile,controller);
	    	  
	      }
	    });
		Hangman.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	Hangman.setFont(TitleFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	Hangman.setFont(MediumFont);
	        }
	     });

		JButton Bingo = new JButton("Bingo");
		Bingo.setBorderPainted(false); 
		Bingo.setContentAreaFilled(false); 
		Bingo.setFocusPainted(false); 
		Bingo.setOpaque(false);
		Bingo.setFont(MediumFont);
		Bingo.setForeground(Color.white);
		Bingo.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  musichandler.playbuttonclick();
	    	  Bingo Bingogame = new Bingo(frame,controller,musichandler);
	    	  Bingogame.loadselection(profile);
	    	  
	      }
	    });
		Bingo.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	Bingo.setFont(TitleFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	Bingo.setFont(MediumFont);
	        }
	     });
		
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
	    	  musichandler.playbuttonclick();
	    	  try {
				SaveGame();
	    	  } catch (IOException e1) {
	    		  // TODO Auto-generated catch block
	    		  e1.printStackTrace();
	    	  }
	    	  GameTrain front = new GameTrain(frame,profile,controller,musichandler);
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
		Exit.setBounds(640, -30, 400, 100);
		
		JButton visual = new JButton("Visualiser");
		visual.setBorderPainted(false); 
		visual.setContentAreaFilled(false); 
		visual.setFocusPainted(false); 
		visual.setOpaque(false);
		visual.setFont(SmallFont);
		visual.setForeground(Color.white);
		visual.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  musichandler.playbuttonclick();
	    	  vis v = new vis(controller, frame.getLocationOnScreen().getX(), frame.getLocationOnScreen().getY(),musichandler);
	    
	      }
	    });
		visual.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	visual.setFont(BiggerFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	visual.setFont(SmallFont);
	        }
	     });
		frame.add(visual);
		visual.setBounds(610, 40, 350, 100);
		
		frame.add(Bingo);
		frame.add(labeltitle1);
		frame.add(labeltitle2);
		frame.add(Falling);
		frame.add(Hangman);
		frame.add(CharacterButton);
		frame.add(QuizButton);
		frame.add(Save);
		
		labeltitle1.setBounds(270, 50, 500, 100);
		labeltitle2.setBounds(150, 100, 700, 100);
		Falling.setBounds(100, 300, 280, 100);
		Hangman.setBounds(570, 300, 280, 100);
		CharacterButton.setBounds(550, 520, 350, 100);
		Save.setBounds(55, 520, 100, 100);
		QuizButton.setBounds(300, 400, 350, 100);
		Bingo.setBounds(300, 500, 350, 100);
		
		if(!controller.isConnected()) {
			Bingo.setEnabled(false);
			Falling.setEnabled(false);
		}
		frame.setVisible(true);
	}
	public void updateglove() {
		while(usingleap) {
			if(threadstopper == true) {
				break;
			}
			Point a = frame.getMousePosition();
			if(a != null) {
				int x = (int) a.getX() - 100;
				int y = (int) a.getY() - 30;
				glove.setBounds(x, y, 100, 73);
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
