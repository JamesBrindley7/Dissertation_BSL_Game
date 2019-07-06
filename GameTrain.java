import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.leapmotion.leap.Controller;

public class GameTrain {
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
	boolean threadstopper;
	private Thread soundthread;
	Music_Handler musichandler;
	
	public GameTrain(JFrame framein, Player_Profile profilein, Controller controller,Music_Handler m) {
		profile = profilein;
		frame = framein;
		this.controller = controller;
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
	public void framerepaint() {
		frame.getContentPane().removeAll();
		frame.repaint();
	frame.setContentPane(new JLabel(new ImageIcon(current+"/ChalkBoard.jpg")));
	JLabel labeltitle1 = new JLabel("The School of");
	labeltitle1.setFont(TitleFont);
	labeltitle1.setForeground(Color.white);
	
	JLabel labeltitle2 = new JLabel("British Sign Language");
	labeltitle2.setFont(TitleFont);
	labeltitle2.setForeground(Color.white);
	
	labeltitle1.setBounds(270, 50, 500, 100);
	labeltitle2.setBounds(150, 100, 700, 100);
	
	frame.add(labeltitle1);
	frame.add(labeltitle2);
	
	glove = new JLabel(new ImageIcon(current+"/Characters/GloveIcon.png"));
	glove.setVisible(true);
	glove.setBounds(100, 100, 100, 73);
	frame.add(glove);
	
	startglovetracking();
	
	startmusichandler();
	
	JButton Train = new JButton("Train");
	Train.setBorderPainted(false); 
	Train.setContentAreaFilled(false); 
	Train.setFocusPainted(false); 
	Train.setOpaque(false);
	Train.setFont(TitleFont);
	Train.setForeground(Color.white);
	Train.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
    	  threadstopper = true;
    	  musichandler.playbuttonclick();
    	  Train training = new Train(frame, profile,controller,musichandler);
      }
    });
	Train.addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent me) {
        	Train.setFont(HoverFont);
        }
        public void mouseExited(MouseEvent me) {
        	Train.setFont(TitleFont);
        }
     });
	Train.setBounds(150, 250, 280, 200);
	frame.add(Train);
	
	JButton Translate = new JButton("Translate");
	Translate.setBorderPainted(false); 
	Translate.setContentAreaFilled(false); 
	Translate.setFocusPainted(false); 
	Translate.setOpaque(false);
	Translate.setFont(TitleFont);
	Translate.setForeground(Color.white);
	Translate.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
    	  threadstopper = true;
    	  musichandler.playbuttonclick();
    	  Translate translater = new Translate(frame,profile,controller,musichandler);
      }
    });
	Translate.addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent me) {
        	Translate.setFont(HoverFont);
        }
        public void mouseExited(MouseEvent me) {
        	Translate.setFont(TitleFont);
        }
     });
	Translate.setBounds(250, 380, 480, 200);
	frame.add(Translate);
	File filecheck = new File(current+"/Data/"+profile.getUsername()+"/GestureDatabase.data");
	if (!filecheck.exists()){
		Translate.setEnabled(false);
	}
	
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
    	  musichandler.playSave();
    	  System.exit(0);
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
	
	JButton Game = new JButton("Game");
	Game.setBorderPainted(false); 
	Game.setContentAreaFilled(false); 
	Game.setFocusPainted(false); 
	Game.setOpaque(false);
	Game.setFont(TitleFont);
	Game.setForeground(Color.white);
	Game.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
    	  threadstopper = true;
    	  musichandler.playbuttonclick();
    	  Front_Menu front = new Front_Menu(frame,profile,controller,musichandler);
      }
    });
	Game.addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent me) {
        	Game.setFont(HoverFont);
        }
        public void mouseExited(MouseEvent me) {
        	Game.setFont(TitleFont);
        }
     });
	Game.setBounds(500, 250, 280, 200);
	
	JButton Rulebook = new JButton("Rule Book");
	Rulebook.setBorderPainted(false); 
	Rulebook.setContentAreaFilled(false); 
	Rulebook.setFocusPainted(false); 
	Rulebook.setOpaque(false);
	Rulebook.setFont(SmallFont);
	Rulebook.setForeground(Color.white);
	Rulebook.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
    	  threadstopper = true;
    	  musichandler.playbuttonclick();
    	  Rules r = new Rules(frame,profile,controller,musichandler);
      }
    });
	Rulebook.addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent me) {
        	Rulebook.setFont(BiggerFont);
        }
        public void mouseExited(MouseEvent me) {
        	Rulebook.setFont(SmallFont);
        }
     });
	Rulebook.setBounds(670, 470, 280, 200);
	frame.add(Rulebook);
	
	if(!controller.isConnected()) {
		Train.setEnabled(false);
		Translate.setEnabled(false);
	}
	
	frame.add(Game);
	frame.setVisible(true);
	}
	public void updateglove() {
		while(usingleap) {
			Point a = frame.getMousePosition();
			if(a != null) {
				int x = (int) a.getX() - 100;
				int y = (int) a.getY() - 30;
				glove.setBounds(x, y, 100, 73);
			}
			if( threadstopper == true) {
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
