import java.awt.BorderLayout;
import java.awt.Color;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Vector;


public class Calibration {
	String current = System.getProperty("user.dir");
	Player_Profile profile = null;
	JLabel errorlabel;
	JTextField userbox;
	JFrame frame;
	int option = 1;
	JLabel glove;
	boolean usingleap = true;
	Thread glovethread;
	Controller controller;
	int handcounter;
	Thread handtrackingthread;
	Visualiser display;
	Vector leftpalm;
	Vector rightpalm;
	Image img;
	JPanel center ;
	public Hashtable<Integer, Hashtable<Integer, Vector>> LeftHandHash = new Hashtable<Integer, Hashtable<Integer, Vector>>();
	public Hashtable<Integer, Hashtable<Integer, Vector>> RightHandHash = new Hashtable<Integer, Hashtable<Integer, Vector>>();
	int level = 0;
	boolean threadstopper = false;
	
	int[] thumbBoxX = {350,450};
	int[] thumbBoxY = {360,450};
	
	int[] indexBoxX = {400,450};
	int[] indexBoxY = {400,485};
	
	int[] middleBoxX = {410,460};
	int[] middleBoxY = {400,485};
	
	int[] ringBoxX = {440,490};
	int[] ringBoxY = {400,485};
	
	int[] pinkyBoxX = {460,515};
	int[] pinkyBoxY = {400,485};
	
	Image jimage;
	
	
	public Calibration() {
		SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
		controller = new Controller();
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
		Font HoverFont = font.deriveFont(70f);
		Font SmallFont = font.deriveFont(20f);
		Font BiggerFont = font.deriveFont(25f);
		Font SliteFont = font.deriveFont(35f);
		
		
		 JFrame visframe = new JFrame();
   	  	visframe.setTitle("Visualiser");
   	    visframe.setSize(960,678);
   	    visframe.setLayout(new BorderLayout(1, 1));
   	    visframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   	    visframe.setResizable(false);
   	    try {
			jimage = ImageIO.read( new File(current+"/Cali/Cali-palm.png") );
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
   	    frame = visframe;
    	 frame.addWindowListener(new WindowAdapter() {
   	     @Override
   	     public void windowClosing(WindowEvent e) {
   	    	threadstopper = true;
   	     }
    	 });
    	 
    	 
    	 center = new JPanel();
   	     center.setLayout(new BorderLayout());
   	     center.setPreferredSize(new Dimension(960,678));
   	     frame.add(center);
		 frame.pack();
         frame.setVisible(true);
         start();
	        }});
	}
	public void start() {
		frame.remove(center);
     	center.setBounds(0, 0, 800, 600);
     	display = new Visualiser();
     	
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
    	boolean called = false;
    	if (!controller.isConnected()) {
			startgame();
   	     }
    	center.setVisible(true);
    	boolean lefty = false;
    	while(true) {
    		if(threadstopper == true) {
				return;
			}
    		lefty = false;
    		display.capture(controller);
    		handcounter = display.gethandcount();
    		center = new Drawfunction();
    		frame.add(center);
    		center.revalidate();
    		center.validate();
    		center.repaint();
    		if(handcounter >=1) {
    			
        		LeftHandHash = display.getlefthash();
        		RightHandHash = display.getrighthash();
        		leftpalm = display.getleftpalm();
        		rightpalm = display.getrightpalm();
        		
        		center = new Drawfunction();
        		if(display.getpos()[0].equals("Left") && handcounter >=1) {
        			if(display.getpos()[1] == null) {
        				lefty = true;
        			}
        		}
        		if(handcounter >=1 && called == false && lefty == false) {
        			boolean check = check(RightHandHash, rightpalm);
        			if(check) {
        				try {
    						Thread.sleep(1000);
    					} catch (InterruptedException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
        				called = true;
        				startgame();
        			}
        		}
        	}
    	}
    }
    public void startgame() {
    	threadstopper = true;
		JFrame frame = new JFrame();
		frame.setTitle("The School of British Sign Language");
		frame.setSize(960,678);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setResizable(false);
		Controller controller = new Controller();
		//LeapListener listener = new LeapListener();
		LeapMouse mouselistener = new LeapMouse();
		
		//controller.addListener(listener);
		Point xandy = frame.getLocationOnScreen();
		mouselistener.updatescreenlocation((int)xandy.getX(), (int)xandy.getX());
		
		Account_Select front = new Account_Select(frame, controller, mouselistener);
		this.frame.dispose();
    }
    public boolean check(Hashtable<Integer, Hashtable<Integer, Vector>> handhash, Vector palm) {
    	boolean check = performcheck(handhash.get(1), thumbBoxX, thumbBoxY);
    	if(!check) {
    		return false;
    	}
    	check = performcheck(handhash.get(2), indexBoxX, indexBoxY);
    	if(!check) {
    		return false;
    	}
    	check = performcheck(handhash.get(3), middleBoxX, middleBoxY);
    	if(!check) {
    		return false;
    	}
    	check = performcheck(handhash.get(4), ringBoxX, ringBoxY);
    	if(!check) {
    		return false;
    	}
    	check = performcheck(handhash.get(5), pinkyBoxX, pinkyBoxY);
    	if(!check) {
    		return false;
    	}
		return true;
    }
    public boolean performcheck(Hashtable<Integer, Vector> handhash, int[] masterx, int[] mastery) {
    	try {
    	int offset = 440;
		int yoffset = 0;
    	for(int i = 0;i<3;i++) {
    		float x = handhash.get(i).getX();
    		x = x * -1;
    		float y = handhash.get(i).getY();
    		/**
    		float combinedx = offset + x;
			float combinedy = yoffset + y;
			
			System.out.println("Round: "+i);
			System.out.println("My X: "+combinedx);
			System.out.println(masterx[0]+ " x "+masterx[1]);
			System.out.println("My Y: "+combinedy);
			System.out.println(mastery[0]+ " y "+mastery[1]);
			*/
    		if(x+offset < masterx[0] || x+offset > masterx[1] || y+yoffset < mastery[0] || y+yoffset > mastery[1]) {
    			return false;
    		}
    		else {
    		}
    	}
    	}
    	catch(NullPointerException e) {
    		System.out.println("No hand error");
    		return false;
    	}
    	
    	return true;
    }
    public void next() {
    	frame.setContentPane(new JLabel(new ImageIcon(current+"/Cali/Cali-fist.png")));
    }
	public class Drawfunction extends JPanel {

        public Drawfunction() {
        	 super();
        	 setPreferredSize(new Dimension(960,678));
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(960,678);
        }
        

        protected void paintComponent(Graphics g) 
		{
        	super.paintComponent(g);
        	try {
			int offset = 465;
			int yoffset = 280;
			
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.drawImage( jimage, 0, 0, this);        
			if(handcounter == 0) {
				return;
			}
			boolean hasusedleft = false;
			boolean hasusedleft2 = false;
			String[] order = display.getpos();
			g2d.drawOval((int)thumbBoxX[0], thumbBoxY[0], 5, 5);
			
			
			
			for(int p = 0;p<handcounter;p++) {
				if(handcounter > 2) {
					break;
				}
				Hashtable<Integer, Hashtable<Integer, Vector>> handdata;
				float palmxpos;
				float palmypos;
				if(order[p].equals("Left") && hasusedleft == false) {
					handdata  = LeftHandHash;
					palmxpos= leftpalm.getX();
					palmypos= leftpalm.getZ();
					 g2d.drawOval((int)(leftpalm.getX() * -1) +offset, (int)leftpalm.getZ()+yoffset, 10, 10);
					   hasusedleft2 = true;
				}
				else {
					handdata  = RightHandHash;
					palmxpos= rightpalm.getX();
					palmypos= rightpalm.getZ();
					g2d.drawOval((int)(rightpalm.getX() * -1 )+offset, (int)rightpalm.getZ()+yoffset, 10, 10);
				}
				
				for(int i = 1; i< 6; i++) {
					
					
					
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
				}
				if(hasusedleft2) {
					hasusedleft = true;
				}
			}
		}catch(NullPointerException e) {
		}	
		}

    }
}
