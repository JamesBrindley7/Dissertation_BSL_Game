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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.leapmotion.leap.Controller;

public class Rules {
	String current = System.getProperty("user.dir");
	Player_Profile profile = null;
	JFrame frame;
	Font TitleFont;
	Font HoverFont;
	Font SmallFont;
	Font BiggerFont;
	Font MediumFont;
	
	boolean threadstopper = false;
	JLabel glove;
	Thread glovethread;
	
	public Rules(JFrame framein, Player_Profile profilein, Controller controller,Music_Handler m) {
		frame = framein;
		frame.getContentPane().removeAll();
		frame.repaint();
		
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
		SmallFont = font.deriveFont(15f);
		BiggerFont = font.deriveFont(20f);
		MediumFont = font.deriveFont(25f);
		
		
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
	
	JButton Exit = new JButton("Exit");
	Exit.setBorderPainted(false); 
	Exit.setContentAreaFilled(false); 
	Exit.setFocusPainted(false); 
	Exit.setOpaque(false);
	Exit.setFont(BiggerFont);
	Exit.setForeground(Color.white);
	Exit.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
    	  threadstopper = true;
    	  m.playbuttonclick();
    	  GameTrain menu = new GameTrain(frame, profilein, controller, m);
      }
    });
	Exit.addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent me) {
        	Exit.setFont(MediumFont);
        }
        public void mouseExited(MouseEvent me) {
        	Exit.setFont(BiggerFont);
        }
     });
	frame.add(Exit);
	Exit.setBounds(640, -30, 400, 100);
	
	JLabel label1 = new JLabel("1) Only Quiz and Hang Man can be played without using the Leap Motion Controller");
	label1.setFont(SmallFont);
	label1.setForeground(Color.white);
	label1.setBounds(80, 150, 900, 100);
	frame.add(label1);
	
	JLabel label2 = new JLabel("2) To save your progress press the save button on the Game screen");
	label2.setFont(SmallFont);
	label2.setForeground(Color.white);
	label2.setBounds(80, 200, 900, 100);
	frame.add(label2);
	
	JLabel label3 = new JLabel("3) Each letter should be trained 3 times by yourself using the Training screen");
	label3.setFont(SmallFont);
	label3.setForeground(Color.white);
	label3.setBounds(80, 250, 900, 100);
	frame.add(label3);
	
	JLabel label4 = new JLabel("4) To test your knowledge or the training perform the sign in the Translate screen");
	label4.setFont(SmallFont);
	label4.setForeground(Color.white);
	label4.setBounds(80, 300, 900, 100);
	frame.add(label4);
	
	JLabel label5 = new JLabel("5) If a sign doesnt work use the skip letter button to only record that letter");
	label5.setFont(SmallFont);
	label5.setForeground(Color.white);
	label5.setBounds(80, 350, 900, 100);
	frame.add(label5);
	
	JLabel label6 = new JLabel("6) If a sign is NOT recognised during a game show both palms to the sensor and try again");
	label6.setFont(SmallFont);
	label6.setForeground(Color.white);
	label6.setBounds(80, 400, 900, 100);
	frame.add(label6);
	
	JLabel label7 = new JLabel("7) Each screen has a help button which gives you information about that specific screen");
	label7.setFont(SmallFont);
	label7.setForeground(Color.white);
	label7.setBounds(80, 450, 900, 100);
	frame.add(label7);
	
	JLabel label8 = new JLabel("8) The visuliser can help you see what the Leap Motion is seeing");
	label8.setFont(SmallFont);
	label8.setForeground(Color.white);
	label8.setBounds(80, 500, 900, 100);
	frame.add(label8);
	
	frame.setVisible(true);
	
	
	}
	public void updateglove() {
		while(true) {
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
}
