import java.awt.Color;
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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.leapmotion.leap.Controller;

public class Translate {
	String current = System.getProperty("user.dir");
	Player_Profile profile = null;
	JLabel errorlabel;
	JTextField userbox;
	JFrame frame;
	int option = 1;
	JLabel glove;
	boolean usingleap = true;
	Thread glovethread= new Thread();
	Controller controller;
	JLabel lettersymbol;
	JLabel letter;
	String[] alphabet = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	int lettercounter = 0;
	String lettertranslated;
	String lastletter = "NONE";
	Thread recorderthread = new Thread();
	GestureCompare compare;
	boolean threadstopper = false;
	private Thread soundthread;
	Music_Handler musichandler;
	
	public Translate(JFrame framein,Player_Profile p, Controller controller,Music_Handler m) {
		profile = p;
		frame = framein;
		this.controller = controller;
		musichandler = m;
		
		frame.getContentPane().removeAll();
		frame.repaint();
		frame.setContentPane(new JLabel(new ImageIcon(current+"/ChalkBoard.jpg")));
		//frame.setCursor( frame.getToolkit().createCustomCursor(new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB ),new Point(),null ) );
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
		Font TitleFont = font.deriveFont(50f);
		Font SmallFont = font.deriveFont(20f);
		Font BiggerFont = font.deriveFont(25f);
		
		
		lettersymbol = new JLabel();
		lettersymbol.setBounds(400, 400, 150, 150);
		frame.add(lettersymbol);
		
		letter = new JLabel();
		letter.setBounds(450, 200, 150, 150);
		letter.setFont(TitleFont);
		letter.setForeground(Color.white);
		frame.add(letter);
		
		glove = new JLabel(new ImageIcon(current+"/Characters/GloveIcon.png"));
		glove.setVisible(true);
		glove.setBounds(100, 100, 100, 73);
		frame.add(glove);
		startglovetracking();
		
		startmusichandler();
		
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
		compare = new GestureCompare();
		compare.loadcontroller(controller,profile);
		compare.load();
		startrecording();
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
				if(!lastletter.equals(lettertranslated)) {
					musichandler.playConfirm();
					lettersymbol.setIcon(new ImageIcon(current+"/Signs/"+lettertranslated+".png"));
					letter.setText(lettertranslated);
					lastletter = lettertranslated;
					if(lastletter.equals("J") || lastletter.equals("H")) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
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
