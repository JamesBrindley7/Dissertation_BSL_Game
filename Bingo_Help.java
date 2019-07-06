import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
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

public class Bingo_Help {
	/**
	 * Current directory
	 */
	private String current = System.getProperty("user.dir");
	/**
	 * The small font (25f)
	 */
	private Font SmallFont;
	/**
	 * The bigger font (30f)
	 */
	private Font BiggerFont;
	/**
	 * Constructor for Bingo Help button, creates the GUI and sets out the instructions
	 */
	public Bingo_Help(Music_Handler m) {
		
		JFrame frame = new JFrame();
		frame.setTitle("Help");
		frame.setSize(600,424);
		frame.setLayout(null);
		frame.setResizable(false);
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
		SmallFont = font.deriveFont(25f);
		BiggerFont = font.deriveFont(30f);
		frame.setContentPane(new JLabel(new ImageIcon(current+"/HelpChalk.png")));
		
		JLabel rules = new JLabel("Instructions");
		rules.setFont(BiggerFont);
		rules.setForeground(Color.white);
		rules.setBounds(175, 30, 250, 100);
		
		JLabel rule1 = new JLabel("1) In this game you must pick");
		rule1.setFont(SmallFont);
		rule1.setForeground(Color.white);
		rule1.setBounds(50, 80, 500, 100);
		JLabel rule11 = new JLabel("10 different letters to use as");
		rule11.setFont(SmallFont);
		rule11.setForeground(Color.white);
		rule11.setBounds(50, 110, 500, 100);
		JLabel rule12 = new JLabel("your bingo letters, when one is");
		rule12.setFont(SmallFont);
		rule12.setForeground(Color.white);
		rule12.setBounds(50, 140, 500, 100);
		
		JLabel rule122 = new JLabel("displayed then you must perform");
		rule122.setFont(SmallFont);
		rule122.setForeground(Color.white);
		rule122.setBounds(50, 170, 500, 100);
		
		JLabel rule2 = new JLabel("it within the timelimit");
		rule2.setFont(SmallFont);
		rule2.setForeground(Color.white);
		rule2.setBounds(50, 200, 500, 100);
		
		JLabel rule22 = new JLabel("2) Each answer is worth 500 Coins");
		rule22.setFont(SmallFont);
		rule22.setForeground(Color.white);
		rule22.setBounds(50, 270, 500, 100);
		
		JButton Exit = new JButton("Exit");
		Exit.setBorderPainted(false); 
		Exit.setContentAreaFilled(false); 
		Exit.setFocusPainted(false); 
		Exit.setOpaque(false);
		Exit.setFont(SmallFont);
		Exit.setForeground(Color.white);
		Exit.setBounds(445, -35, 150, 100);
		Exit.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  m.playbuttonclick();
	    	  frame.dispose();
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
		frame.add(rules);
		frame.add(rule1);
		frame.add(rule11);
		frame.add(rule12);
		frame.add(rule122);
		frame.add(rule2);
		frame.add(rule22);
		frame.setVisible(true);
		
	}
}
