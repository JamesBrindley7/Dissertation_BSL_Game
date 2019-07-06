import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Vector;



public class vis {
	String current = System.getProperty("user.dir");
	Player_Profile profile = null;
	JFrame frame;
	Font TitleFont;
	Font HoverFont;
	Font SmallFont;
	Font BiggerFont;
	Font SmallerFont;
	Font MediumFont;
	JLabel glove;
	boolean usingleap = true;
	Thread glovethread;
	String[] alphabet = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	int donecounter = 0;
	int lettercounter = 0;
	JLabel lettershown;
	JLabel Display;
	JLabel lettersymbol;
	JLabel errormsg;
	JLabel timer;
	JButton skip;
	JButton start;
	JButton load;
	Thread recorderthread;
	Thread trainingthread;
	Thread handthread;
	Controller controller;
	public static Hashtable<String, Hashtable<Integer,Gesture>> GestureDatabase = new Hashtable<String, Hashtable<Integer,Gesture>>(); 
	public static Hashtable<String, Gesture> GestureBaseLine = new Hashtable<String, Gesture>(); 
	boolean displayinghand = true;
	
	JPanel center ;
	int handcounter;
	Thread handtrackingthread;
	Visualiser display;
	
	public Hashtable<Integer, Hashtable<Integer, Vector>> LeftHandHash = new Hashtable<Integer, Hashtable<Integer, Vector>>();
	public Hashtable<Integer, Hashtable<Integer, Vector>> RightHandHash = new Hashtable<Integer, Hashtable<Integer, Vector>>();
	Vector leftpalm;
	Vector rightpalm;
	Image img;
	

	Image jimage;
	
	
	boolean threadstopper = false;
	
	public vis(Controller c, double x, double y, Music_Handler m) {
		SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
		
  	JFrame visframe = new JFrame();
	  	visframe.setTitle("Visualiser");
	    visframe.setSize(590,400);
	    visframe.setLayout(new BorderLayout(1, 1));
	    visframe.setResizable(false);
	    visframe.setLocation((int)x + 1000, (int)y);
	    frame = visframe;
	    try {
		jimage = ImageIO.read( new File(current+"/HelpChalk.png"));
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	 frame.addWindowListener(new WindowAdapter() {
	     @Override
	     public void windowClosing(WindowEvent e) {
	    	threadstopper = true;
	     }
	 });
		controller = c;
		
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
		SmallerFont = font.deriveFont(15f);
		BiggerFont = font.deriveFont(25f);
		MediumFont = font.deriveFont(40f);
		
		framerepaint(m);
	        }});
	}
	public void framerepaint(Music_Handler m) {
		
		frame.getContentPane().removeAll();
        
        frame.setCursor(Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) ); 
        frame.setLayout(new BorderLayout(1, 1));
		
		 center = new JPanel();
         center.setLayout(new BorderLayout());
         center.setPreferredSize(new Dimension(590,400));
    
        
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
  	    	  threadstopper = true;
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
          
  		frame.add(center);
		frame.pack();
        frame.setVisible(true);
        start();
	     
	}
	public void start() {
		frame.remove(center);
     	center.setBounds(0, 0, 600, 400);
     	display = new Visualiser();
     	controller = new Controller();
     	
     	starthandtracking();
	}
	
	public void starthandtracking() {
		handtrackingthread = new Thread() {
 	        public void run() {
 	        	updatehands();
 	        }
		 };
		 handtrackingthread.start();
	}
    public void updatehands() {
    	
    	while(true) {
    		if(threadstopper == true) {
				break;
			}
    		display.capture(controller);
    		handcounter = display.gethandcount();
    		LeftHandHash = display.getlefthash();
    		RightHandHash = display.getrighthash();
    		leftpalm = display.getleftpalm();
    		rightpalm = display.getrightpalm();
    		center = new Drawfunction();
    		frame.add(center);
    		center.revalidate();
    		center.validate();
    		center.repaint();
    	}
    }
    public class Drawfunction extends JPanel {

    	public Drawfunction() {
       	 super();
       	 setPreferredSize(new Dimension(590,400));
       }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(590,400);
        }
        
        

        protected void paintComponent(Graphics g) 
		{
        	super.paintComponent(g);
        	try {
			int offset = 305;
			int yoffset = 200;
			
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.drawImage( jimage, 0, 0, this);        
			if(handcounter == 0) {
				return;
			}
			boolean hasusedleft = false;
			boolean hasusedleft2 = false;
			String[] order = display.getpos();
			if(handcounter > 2) {
				handcounter = 2;
			}
			for(int p = 0;p<handcounter;p++) {
				Hashtable<Integer, Hashtable<Integer, Vector>> handdata;
				if(p > 1) {
					return;
				}
				if(order[p] == "Left") {
					handdata  = LeftHandHash;
				}
				else {
					handdata  = RightHandHash;
				}
				for(int i = 1; i< 6; i++) {
					
					
					float palmxpos;
					float palmypos;
					if(order[p] == "Left" && hasusedleft == false) {
						palmxpos= leftpalm.getX();
						palmypos= leftpalm.getZ();
						hasusedleft = true;
					}
					else{
						palmxpos= rightpalm.getX();
						palmypos= rightpalm.getZ();
					}
					
					
					
					Vector todrawto = handdata.get(i).get(0);
					float ypostodraw = todrawto.getZ();
					int ypostodraw2 = (int) ypostodraw;
					
					float xpostodraw = todrawto.getX();
					float changexto = palmxpos - xpostodraw;
					int revxpostodraw = (int) (xpostodraw * -1);
					
					g2d.setColor(Color.white);
					if(i > 1) {
						Vector firstbonedraw = handdata.get(i-1).get(0);
						float ypos = firstbonedraw.getZ();
    					int revypos = (int) ypos;
    					
    					
						float xpos = firstbonedraw.getX();
						float changex = palmxpos - xpos;
						int revxpos = (int)xpos * -1;
						
						
						int prevx = revxpos;
						int prevy = revypos;
						
						
    					
						g2d.drawLine((int)prevx+offset, prevy+yoffset, (int)revxpostodraw+offset, ypostodraw2+yoffset);
					}
					
					Vector thumb = handdata.get(1).get(0);
					Vector little = handdata.get(5).get(0);
					
					g2d.drawLine((int)(little.getX()*-1)+offset, (int)thumb.getZ()+yoffset, (int)(little.getX()*-1)+offset, (int)little.getZ()+yoffset);
					
					g2d.drawLine((int)(thumb.getX()*-1)+offset, (int)thumb.getZ()+yoffset, (int)(little.getX()*-1)+offset, (int)thumb.getZ()+yoffset);
					
					switch(i) {
					case 1: g2d.setColor(Color.blue);
						break;
					case 2: g2d.setColor(Color.red);
						break;
					case 3: g2d.setColor(Color.green);
						break;
					case 4: g2d.setColor(Color.yellow);
						break;
					case 5: g2d.setColor(Color.orange);
						break;
					}
					
					for(int j = 0; j< 3; j++) {
						Vector firstbone = handdata.get(i).get(j);

						float ypos = firstbone.getZ();
						
						int revypos = (int) ypos;
						float xpos = firstbone.getX();
						float changex = palmxpos - xpos;
						int revxpos = (int) xpos * -1;
						
						Vector nextbone = handdata.get(i).get(j+1);
						float ypos2 = nextbone.getZ();
						
						int revypos2 = (int) ypos2;
						
						
						float xpos2 = nextbone.getX();
						float changex2 = palmxpos - xpos2;
						int revxpos2 = (int) xpos2 * -1;
						
						g2d.drawLine((int)revxpos+offset, revypos+yoffset, (int)revxpos2+offset, revypos2+yoffset);
					//	System.out.println("drawn line");
						g2d.drawOval((int)revxpos+offset, revypos+yoffset, 2, 2);
						//System.out.println("drawn oval 1");
						if (j == 3) {
							g2d.drawOval((int)revxpos2+offset, revypos2+yoffset, 2, 2);
							//System.out.println("drawn oval 2");
						}
					}
					if(!display.isleft() && hasusedleft2 == false) {
					   g2d.drawOval((int)(leftpalm.getX() * -1) +offset, (int)leftpalm.getZ()+yoffset, 10, 10);
					   hasusedleft2 = true;
					}
					else {
						g2d.drawOval((int)(rightpalm.getX() * -1 )+offset, (int)rightpalm.getZ()+yoffset, 10, 10);
					}
				}
				
			}
		}catch(NullPointerException e) {
		}	
		}

    }
}