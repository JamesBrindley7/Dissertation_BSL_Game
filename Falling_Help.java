
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

	public class Falling_Help {
		String current = System.getProperty("user.dir");
		Font TitleFont;
		Font HoverFont;
		Font SmallFont;
		Font BiggerFont;
		Font MediumFont;
		JFrame frame;
		public Falling_Help(Music_Handler m) {
			
			frame = new JFrame();
			frame.setTitle("Help");
			frame.setSize(600,424);
			frame.setResizable(false);
			frame.setLayout(null);
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
			SmallFont = font.deriveFont(25f);
			BiggerFont = font.deriveFont(30f);
			MediumFont = font.deriveFont(40f);
			frame.setContentPane(new JLabel(new ImageIcon(current+"/HelpChalk.png")));
			
			JLabel rules = new JLabel("Instructions");
			rules.setFont(BiggerFont);
			rules.setForeground(Color.white);
			rules.setBounds(175, 30, 250, 100);
			
			JLabel rule1 = new JLabel("1) In this game letters will");
			rule1.setFont(SmallFont);
			rule1.setForeground(Color.white);
			rule1.setBounds(50, 80, 500, 100);
			JLabel rule11 = new JLabel("fall from the top of the screen");
			rule11.setFont(SmallFont);
			rule11.setForeground(Color.white);
			rule11.setBounds(50, 110, 500, 100);
			JLabel rule12 = new JLabel("you must peform the sign for each");
			rule12.setFont(SmallFont);
			rule12.setForeground(Color.white);
			rule12.setBounds(50, 140, 500, 100);
			
			JLabel rule122 = new JLabel("letter to make them dissapear");
			rule122.setFont(SmallFont);
			rule122.setForeground(Color.white);
			rule122.setBounds(50, 170, 500, 100);
			
			JLabel rule2 = new JLabel("2) Each letter that falls past the");
			rule2.setFont(SmallFont);
			rule2.setForeground(Color.white);
			rule2.setBounds(50, 240, 500, 100);
			
			JLabel rule22 = new JLabel("line will make you lose one life");
			rule22.setFont(SmallFont);
			rule22.setForeground(Color.white);
			rule22.setBounds(50, 270, 500, 100);
			
			JButton next = new JButton("Next Page");
			next.setBorderPainted(false); 
			next.setContentAreaFilled(false); 
			next.setFocusPainted(false); 
			next.setOpaque(false);
			next.setFont(SmallFont);
			next.setForeground(Color.white);
			next.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	  m.playbuttonclick();
		    	  loadsecond(m);
		      }
		    });
			next.addMouseListener(new MouseAdapter() {
		        public void mouseEntered(MouseEvent me) {
		        	next.setFont(BiggerFont);
		        }
		        public void mouseExited(MouseEvent me) {
		        	next.setFont(SmallFont);
		        }
		     });
			next.setBounds(-30, -35, 300, 100);
			frame.add(next);
			
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
		public void loadsecond(Music_Handler m) {
			frame.getContentPane().removeAll();
			frame.repaint();
			frame.setContentPane(new JLabel(new ImageIcon(current+"/HelpChalk.png")));
			
			JLabel rules = new JLabel("Instructions 2");
			rules.setFont(BiggerFont);
			rules.setForeground(Color.white);
			rules.setBounds(175, 30, 250, 100);
			
			JLabel rule1 = new JLabel("3) When your lives hit 0 it will");
			rule1.setFont(SmallFont);
			rule1.setForeground(Color.white);
			rule1.setBounds(50, 80, 500, 100);
			JLabel rule11 = new JLabel("be game over");
			rule11.setFont(SmallFont);
			rule11.setForeground(Color.white);
			rule11.setBounds(50, 110, 500, 100);
			JLabel rule12 = new JLabel("4) You are awarded 500 gold");
			rule12.setFont(SmallFont);
			rule12.setForeground(Color.white);
			rule12.setBounds(50, 180, 500, 100);
			
			JLabel rule122 = new JLabel("coins for each letter correctly");
			rule122.setFont(SmallFont);
			rule122.setForeground(Color.white);
			rule122.setBounds(50, 210, 500, 100);
			
			JLabel rule2 = new JLabel("signed and 5000 for each level");
			rule2.setFont(SmallFont);
			rule2.setForeground(Color.white);
			rule2.setBounds(50, 240, 500, 100);
			
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
			frame.setVisible(true);
		}
	}

