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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.leapmotion.leap.Controller;

public class Account_Select {
	
	/**
	 * The current directory
	 */
	private String current = System.getProperty("user.dir");
	/**
	 * The players profile
	 */
	private Player_Profile profile = null;
	/**
	 * The Error label to display an error if occured
	 */
	private JLabel errorlabel;
	/**
	 * Text box for the user to enter their username
	 */
	private JTextField userbox;
	/**
	 * The frame to display the GUI on
	 */
	private JFrame frame;
	/**
	 * Option for mouse movement (Leap or Traditional)
	 */
	private int option = 1;
	/**
	 * Image of the glove used as a mouse
	 */
	private JLabel glove;
	/**
	 * Boolean value to stop the leap motion mouse listener
	 */
	private boolean usingleap = true;
	/**
	 * The glove thread to move the image
	 */
	private Thread glovethread;
	/**
	 * Leap Motion Controller
	 */
	private Controller controller;
	private boolean threadstopper = false;
	private Thread soundthread;
	private Music_Handler musichandler;
	/**
	 * Constructor for the Account Selection
	 * Sets the GUI and places objects in the correct position on the screen
	 * @param framein
	 * @param controller
	 * @param mouselistener
	 */
	public Account_Select(JFrame framein, Controller controller, LeapMouse mouselistener) {
		frame = framein;
		this.controller = controller;
		musichandler = new Music_Handler();
		startmusichandler();
		musichandler.playIntro();
		frame.setContentPane(new JLabel(new ImageIcon(current+"/ChalkBoard.jpg")));
		frame.setCursor( frame.getToolkit().createCustomCursor(new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB ),new Point(),null ) );
		File font_file = new File(current+"/chawp.otf");
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, font_file);
		} catch (FontFormatException e) {
			
		} catch (IOException e) {
			
		}
		Font TitleFont = font.deriveFont(50f);
		Font HoverFont = font.deriveFont(70f);
		Font SmallFont = font.deriveFont(20f);
		Font BiggerFont = font.deriveFont(25f);
		Font SliteFont = font.deriveFont(35f);
			
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
		
		errorlabel = new JLabel();
		errorlabel.setFont(TitleFont);
		errorlabel.setForeground(Color.white);
		
		
		JLabel connectlabel = new JLabel();
		connectlabel.setFont(BiggerFont);
		connectlabel.setBounds(655, 545, 200, 50);
		if (controller.isConnected()) {
			connectlabel.setText("Connected");
			connectlabel.setForeground(Color.green);
		}
		else {
			connectlabel.setText("Not Connected");
			connectlabel.setForeground(Color.red);
		}
		frame.add(connectlabel);
		
		
		userbox = new JTextField();
		userbox.setFont(BiggerFont);
		userbox.addActionListener(new AbstractAction()
		{
		
		    public void actionPerformed(ActionEvent e)
		    {
		    	try {
		    		musichandler.playbuttonclick();
					login();
				} catch (ClassNotFoundException e1) {
					
				} catch (IOException e1) {
					
				}
		    }
		});
		userbox.setHorizontalAlignment(JTextField.CENTER);
		
		JButton login = new JButton("Login");
		login.setBorderPainted(false); 
		login.setContentAreaFilled(false); 
		login.setFocusPainted(false); 
		login.setOpaque(false);
		login.setFont(TitleFont);
		login.setForeground(Color.white);
		login.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  try {
	    		  musichandler.playbuttonclick();
				login();
			} catch (ClassNotFoundException e1) {
			} catch (IOException e1) {
			}
	      }
	    });
		login.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	login.setFont(HoverFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	login.setFont(TitleFont);
	        }
	     });
		
		JButton togglemouse = new JButton("Traditional");
		togglemouse.setBorderPainted(false); 
		togglemouse.setContentAreaFilled(false); 
		togglemouse.setFocusPainted(false); 
		togglemouse.setOpaque(false);
		togglemouse.setFont(BiggerFont);
		togglemouse.setForeground(Color.white);
		togglemouse.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  musichandler.playbuttonclick();
	    	  if(option == 0) {
	    		  option = 1;
	    		  togglemouse.setText("Traditional");
	    		  controller.removeListener(mouselistener);
	    		  
	    	  }
	    	  else {
	    		  option = 0;
	    		  togglemouse.setText("Using Leap");
	    		  controller.addListener(mouselistener);
	    		  usingleap = true;
	    		  startglovetracking();
	    	  }
	      }
	    });
		togglemouse.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	togglemouse.setFont(SliteFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	togglemouse.setFont(BiggerFont);
	        }
	     });
		togglemouse.setBounds(350, 450, 280, 200);
		frame.add(togglemouse);
		
		JLabel Toggle = new JLabel("Click to toggle mouse input");
		Toggle.setFont(SmallFont);
		Toggle.setForeground(Color.white);
		Toggle.setBounds(330, 400, 400, 200);
		frame.add(Toggle);
		
		labeltitle1.setBounds(270, 50, 500, 100);
		labeltitle2.setBounds(150, 100, 700, 100);
		userbox.setBounds(350, 250, 280, 30);
		login.setBounds(350, 300, 280, 100);
		errorlabel.setBounds(300, 600, 280, 100);
		
		frame.add(userbox);
		frame.add(labeltitle1);
		frame.add(labeltitle2);
		frame.add(errorlabel);
		frame.add(login);
		frame.setVisible(true);
	}
	/**
	 * Login method to either create the user profile or load the user profile into the Player Profile instance
	 * This is done by checking if the user name typed has a file in the directory or not. If nothing is typed then log in as guest
	 * Loads the next menu (GameTrain)
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void login() throws IOException, ClassNotFoundException {
		threadstopper = true;
		boolean cheatcode = false;
		String username = userbox.getText();
		if(userbox.getText().equals("")) {
			username = "Guest";
		}
		if(userbox.getText().equals("unlock"))
		{
			username = "Guest";
			cheatcode = true;
		}
		File f = new File(current+"/UserData/"+username+".SignProf");
		if(!f.isFile()) {
			new File(current+"/Data/"+username+"/").mkdirs();
			profile = new Player_Profile(username);
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(profile);
			oos.close();
		}
		else {
			FileInputStream fin = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fin);
			profile = (Player_Profile) ois.readObject();
			ois.close();
		}
		if(cheatcode) {
			profile.setCoins(1000000);
		}
		GameTrain front = new GameTrain(frame,profile, controller,musichandler);
	}
	/**
	 * Method to update the glove image postion on the screen by getting the mouse position
	 */
	public void updateglove() {
		while(usingleap) {
			if(threadstopper == true) {
				return;
			}
			Point a = frame.getMousePosition();
			if(a != null) {
				int x = (int) a.getX() - 100;
				int y = (int) a.getY() - 30;
				glove.setBounds(x, y, 100, 73);
			}
		}
	}
	/**
	 * Starts the glove tracking thread
	 */
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
}