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

public class Character_Select {
	String current = System.getProperty("user.dir");
	Font TitleFont;
	Font HoverFont;
	Font SmallFont;
	Font BiggerFont;
	JFrame frame = null;
	Player_Profile profile = null;
	public String[] Characters = {"Scientist", "Santa", "Mickey Mouse","Norris","Alien", "Pluto", "Smurf", "Donald Duck"};
	//165 x 165 image size
	int[] ImageY = {70, 320};
	int[] NameY = {240, 510};
	int[] PriceY = {270, 540};
	
	int[] ImageX = {80, 250, 450, 650};
	int[] NameX = {110, 290, 450, 700, 130, 300, 480, 650};
	int[] PriceX = {70, 230, 420, 640, 70, 230, 420, 620};;
	
	public int[] Characterprices = {0, 10000, 25000, 12000, 35000, 100000, 50000, 120000};
	public JLabel[] Characterimages = new JLabel[8];
	public JLabel[] Characternames = new JLabel[8];
	public JButton[] Characterprice = new JButton[8];
	public JLabel[] coins = new JLabel[8];
	JLabel Coins;
	
	JLabel glove;
	boolean usingleap = true;
	Thread glovethread;
	
	private Thread soundthread;
	Music_Handler musichandler;
	boolean threadstopper = false;

	public Character_Select(JFrame framein,Music_Handler m){
		frame = framein;
		musichandler = m;
		startmusichandler();
		
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
		frame.getContentPane().removeAll();
		frame.repaint();
		frame.setContentPane(new JLabel(new ImageIcon(current+"/ChalkBoard.jpg")));
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
	    	  Front_Menu front = new Front_Menu(frame,profile,new Controller(), musichandler);
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
		Coins = new JLabel("Coins: "+profile.getCoins());
		Coins.setFont(SmallFont);
		Coins.setForeground(Color.white);
		Coins.setBounds(740, 20, 500, 100);
		
		glove = new JLabel(new ImageIcon(current+"/Characters/GloveIcon.png"));
		glove.setVisible(true);
		glove.setBounds(100, 100, 100, 73);
		frame.add(glove);
		startglovetracking();
		
		generatecharacters();
		frame.add(Exit);
		frame.add(Coins);
		Exit.setBounds(640, -30, 400, 100);
		frame.setVisible(true);
	}
	public void generatecharacters() {
		int xcounter = 0;
		int ycounter = 0;
		String active = profile.getCharacter();
		for(int i = 0; i < 8; i++) {
			if (xcounter == 4) {
				xcounter = 0;
				ycounter = 1;
			}
			//Add name Label
			Characternames[i] = new JLabel(Characters[i]);
			Characternames[i].setFont(SmallFont);
			Characternames[i].setForeground(Color.white);
			frame.add(Characternames[i]);
			Characternames[i].setBounds(NameX[i], NameY[ycounter], 200, 50);
			Characternames[i].setVisible(true);
			
			//Add price Label
			
			if (!checkowned(i)) {
				Characterprice[i] = new JButton(Integer.toString(Characterprices[i]));
				Characterprice[i].setForeground(Color.white);
				coins[i] = new JLabel(new ImageIcon(current+"/Characters/Coins.png"));
				frame.add(coins[i]);
				coins[i].setBounds(PriceX[i]+20, PriceY[ycounter]+5, 40, 35);
			}
			else {
				if (Characters[i].equals(active)) {
					Characterprice[i] = new JButton("Active");
				}
				else {
					Characterprice[i] = new JButton("Owned");
				}
				Characterprice[i].setForeground(Color.green);
			}
			Characterprice[i].setBorderPainted(false); 
			Characterprice[i].setContentAreaFilled(false); 
			Characterprice[i].setFocusPainted(false); 
			Characterprice[i].setOpaque(false);
			Characterprice[i].setFont(BiggerFont);
			final int topassin = i;
			Characterprice[i].addActionListener(new ActionListener()
		    {
			  int count = topassin;
		      public void actionPerformed(ActionEvent e)
		      {
		    	  musichandler.playvoice(count);
		    	  checkmoney(Characterprices[count], Characters[count]);
		      }
		    });
			Characterprice[i].addMouseListener(new MouseAdapter() {
				int count = topassin;
		        public void mouseEntered(MouseEvent me) {
		        	Characterprice[count].setForeground(Color.green);
		        }
		        public void mouseExited(MouseEvent me) {
		        	boolean owned = checkowned(count);
		        	if (!owned) {
		        		Characterprice[count].setForeground(Color.white);
		        	}
		        }
		     });
			frame.add(Characterprice[i]);
			Characterprice[i].setBounds(PriceX[i], PriceY[ycounter], 200, 50);
			Characterprice[i].setVisible(true);
			
			//Add Image
			Characterimages[i] = new JLabel(new ImageIcon(current+"/Characters/"+Characters[i]+".png"));
			frame.add(Characterimages[i]);
			Characterimages[i].setBounds(ImageX[xcounter], ImageY[ycounter], 165, 165);
			Characterimages[i].setVisible(true);
			
			xcounter++;
		}
	}
	public boolean checkowned(int i) {
		int owned = profile.getunlocked(i);
		if (owned == 1) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean checkmoney(int amount, String charactername) {
		int coinsavaible = profile.getCoins();
		int found = 0;
		String current = profile.getCharacter();
		int currentfound = 0;
		
		for(int i = 0;i <8;i++) {
			 if (Characters[i].equals(charactername)) {
				 found = i;
			 }
			 if(current.equals(Characters[i])) {
				 currentfound = i;
			 }
		}
		if (checkowned(found)) {
			if (found != currentfound) {
				Characterprice[found].setText("Active");
				profile.setCharacter(charactername);
				Characterprice[currentfound].setText("Owned");
			}
			return true;
		}
		else {
		if (coinsavaible >= amount) {
			 profile.setCoins(coinsavaible-amount);
			 Coins.setText("Coins: "+profile.getCoins());
			 profile.setunlocked(found);
			 profile.setCharacter(charactername);
			 Characterprice[found].setText("Active");
			 Characterprice[currentfound].setText("Owned");
			 coins[found].setVisible(false);
			return true;
		}
		else {
			return false;
		}
		}
	}
	public Player_Profile getprofile() {
		return profile;
	}
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
