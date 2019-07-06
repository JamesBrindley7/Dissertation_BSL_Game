
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;

import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Vector;



public class Train {
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
	int[] dynamicnum = {7, 9};
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
	JButton Custom;
	Thread recorderthread;
	Thread trainingthread;
	Thread handthread;
	Controller controller;
	public static Hashtable<String, Hashtable<Integer,Gesture>> GestureDatabase = new Hashtable<String, Hashtable<Integer,Gesture>>(); 
	public static Hashtable<String, Gesture> GestureBaseLine = new Hashtable<String, Gesture>(); 
	public static Hashtable<String, DynamicGesture> GestureBaseLineD = new Hashtable<String, DynamicGesture>(); 
	public static Hashtable<String, DynamicGesture> GestureDatabaseD = new Hashtable<String, DynamicGesture>(); 
	public static Hashtable<Integer, Gesture> GestureHashbackup = new Hashtable<Integer, Gesture>(); 
	
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
	boolean admincontroll = false;
	
	
	boolean threadstopper = false;
	private Thread soundthread;
	Music_Handler musichandler;
	
	public Train(JFrame framein, Player_Profile profilein, Controller c,Music_Handler m) {
		
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
		SmallerFont = font.deriveFont(15f);
		BiggerFont = font.deriveFont(25f);
		MediumFont = font.deriveFont(40f);
		
		framerepaint();
	    
	}
	public void framerepaint() {
		frame.getContentPane().removeAll();
		frame.repaint();
		frame.setContentPane(new JLabel(new ImageIcon(current+"/ChalkBoard.jpg")));
        
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
	    	  vis v = new vis(controller, frame.getLocationOnScreen().getX(), frame.getLocationOnScreen().getY(), musichandler);
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
		visual.setBounds(310, -30, 350, 100);
		
		lettershown = new JLabel("A");
		lettershown.setFont(HoverFont);
		lettershown.setForeground(Color.white);
		lettershown.setBounds(90, 75, 100, 100);
		frame.add(lettershown);
		
		timer = new JLabel("5");
		timer.setFont(HoverFont);
		timer.setForeground(Color.white);
		timer.setBounds(800, 50, 100, 100);
		timer.setVisible(false);
		frame.add(timer);
		
		lettersymbol = new JLabel(new ImageIcon(current+"/Signs/"+alphabet[lettercounter]+".png"));
		lettersymbol.setBounds(150, 55, 150, 150);
		frame.add(lettersymbol);
		
		errormsg = new JLabel("Inputted Gesture doesnt fall within acceptable baseline");
		errormsg.setFont(SmallerFont);
		errormsg.setBounds(75, 475, 600, 150);
		errormsg.setForeground(Color.white);
		errormsg.setVisible(false);
		frame.add(errormsg);
		
		Display = new JLabel(new ImageIcon(current+"/Characters/Train/"+donecounter+".png"));
		Display.setBounds(685, 380, 200, 200);
		frame.add(Display);
		
		
		skip = new JButton("Admin Start");
		skip.setBorderPainted(false); 
		skip.setContentAreaFilled(false); 
		skip.setFocusPainted(false); 
		skip.setOpaque(false);
		skip.setFont(SmallFont);
		skip.setForeground(Color.white);
		skip.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	   
	    	  displayinghand = false;
	    	  load.setVisible(false);
	    	  
	    	  if(lettercounter == 26) {
	    		  musichandler.playSave();
	    		  saveAdmin();
	    		  skip.setEnabled(false);
	    		  skip.setText("Saved");
	    		  System.out.println("Data Saved");
	    		  load.setVisible(true);
	    		  timer.setVisible(false);
	    		  if(profile.getUsername().equals("Guest")) {
	    			//Duplicate dup = new Duplicate(GestureBaseLine, GestureBaseLineD);
	    		  }
	    	  }
	    	  else {
	    		  musichandler.playbuttonclick();
	    		  	if(lettercounter == 7 || lettercounter == 9) {
	    		  		startDrecording();
	    		  		skip.setEnabled(false);
	    		  	}
	    		  	else {
	    		  		startrecording();
		    	  		skip.setEnabled(false);
	    		  	}
	    	  }
	    	  
	      }
	    });
		skip.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	skip.setFont(BiggerFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	skip.setFont(SmallFont);
	        }
	     });
		frame.add(skip);
		skip.setBounds(650, 310, 250, 100);
		if(profile.getUsername().equals("Guest")) {
			skip.setVisible(true);
		}
		else {
			skip.setVisible(false);
		}
		
		start = new JButton("Start");
		start.setBorderPainted(false); 
		start.setContentAreaFilled(false); 
		start.setFocusPainted(false); 
		start.setOpaque(false);
		start.setFont(SmallFont);
		start.setForeground(Color.white);
		start.addActionListener(new ActionListener()
	    {
			boolean startedbeefore = false;
	      public void actionPerformed(ActionEvent e)
	      {
	    	  if(!startedbeefore) {
	    		  load();
	    		  startedbeefore = true;
	    	  }
	    	  load.setVisible(false);
	    	  if(lettercounter == 26) {
	    		  musichandler.playSave();
	    		  save();
	    		  start.setEnabled(false);
	    		  start.setText("Saved");
	    		  System.out.println("Data Saved");
	    		  timer.setVisible(false);
	    	  }
	    	  else {
	    		  musichandler.playbuttonclick();
	    		  if(lettercounter == 7 || lettercounter == 9) {
	    			  	startDtraining();
	    			  	start.setEnabled(false);
	    		  	}
	    		  	else {
	    		  		starttraining();
		    		  	start.setEnabled(false);
	    		  	}
	    	  }
	      }
	    });
		start.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	start.setFont(BiggerFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	start.setFont(SmallFont);
	        }
	     });
		frame.add(start);
		start.setBounds(740, 110, 150, 100);
		//start.setBounds(740, 170, 150, 100);
		if(profile.Username.equals("Guest")) {
			start.setEnabled(false);
		}
		
		Custom = new JButton("Skip Letter");
		Custom.setBorderPainted(false); 
		Custom.setContentAreaFilled(false); 
		Custom.setFocusPainted(false); 
		Custom.setOpaque(false);
		Custom.setFont(SmallFont);
		Custom.setForeground(Color.white);
		Custom.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  GestureHashbackup.clear();
	    	  if(admincontroll == false) {
	    		  musichandler.playbuttonclick();
	    		  if(profile.getUsername().equals("Guest")) {
	    			  load();
    			  }
    			  else {
    				  loaduser();
    			  }
	    	  }
	    	  else {
	    
	    		  if(lettercounter == 26) {
	    			  musichandler.playSave();
	    			  if(profile.getUsername().equals("Guest")) {
	    				  saveAdmin();
	    			  }
	    			  else {
	    				  save();
	    			  }
	    			  Custom.setEnabled(false);
	    			  Custom.setText("Saved");
	    			  System.out.println("Data Saved");
	    			  load.setVisible(true);
	    			  timer.setVisible(false);
	    			  
	    		  }
	    		  else {
	    			  musichandler.playbuttonclick();
	    			  lettercounter++;
	    			  donecounter = 0;
	    			  if (lettercounter < 26) {
	    				  lettersymbol.setIcon(new ImageIcon(current+"/Signs/"+alphabet[lettercounter]+".png"));
	    				  lettershown.setText(alphabet[lettercounter]);
	    			  }
	    			  else {
	    				  lettersymbol.setVisible(false);
	    				  lettershown.setVisible(false);
	    				  Custom.setText("Save");
	    			  }
	    		  }
	    	  }
	    	  admincontroll = true;
	      }
	    });
		Custom.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	Custom.setFont(BiggerFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	Custom.setFont(SmallFont);
	        }
	     });
		frame.add(Custom);
		Custom.setBounds(-80, 570, 400, 100);
		
		load = new JButton("Load");
		load.setBorderPainted(false); 
		load.setContentAreaFilled(false); 
		load.setFocusPainted(false); 
		load.setOpaque(false);
		load.setFont(SmallFont);
		load.setForeground(Color.white);
		load.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  load.setVisible(false);
	    	  musichandler.playSave();
	    	  load();
	      } 	
	    });
		load.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	load.setFont(BiggerFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	load.setFont(SmallFont);
	        }
	     });
		frame.add(load);
		load.setBounds(740, 50, 150, 100);
		
		JButton Help = new JButton("Help");
		Help.setBorderPainted(false); 
		Help.setContentAreaFilled(false); 
		Help.setFocusPainted(false); 
		Help.setOpaque(false);
		Help.setFont(SmallFont);
		Help.setForeground(Color.white);
		Help.setBounds(-80, -30, 400, 100);
		Help.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  musichandler.playbuttonclick();
	    	  TrainHelp helpwind = new TrainHelp(musichandler);
	      }
	    });
		Help.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent me) {
	        	Help.setFont(BiggerFont);
	        }
	        public void mouseExited(MouseEvent me) {
	        	Help.setFont(SmallFont);
	        }
	     });
		 frame.add(Help);
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
	public void startrecording() {
		recorderthread = new Thread() {
 	        public void run() {
 	        	recordset();
 	        }
		 };
		 recorderthread.start();
	}
	
	public void startDrecording() {
		recorderthread = new Thread() {
 	        public void run() {
 	        	recorddynamicset();
 	        }
		 };
		 recorderthread.start();
	}
	
	public void starttraining() {
		trainingthread = new Thread() {
 	        public void run() {
 	        	trainset();
 	        }
		 };
		 trainingthread.start();
	}
	
	public void startDtraining() {
		trainingthread = new Thread() {
 	        public void run() {
 	        	trainsetDy();
 	        }
		 };
		 trainingthread.start();
	}
	
	public void saveAdmin() {
		 FileOutputStream fileOut;
	     ObjectOutputStream out;
		try {
			fileOut = new FileOutputStream(current+"/Data/GestureBaseLine.data"); 
			out = new ObjectOutputStream(fileOut);
			out.writeObject(GestureBaseLine);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		saveAdminDy();
		System.out.println("Base Line Saved");
	}
	public void saveAdminDy() {
		 FileOutputStream fileOut;
	     ObjectOutputStream out;
		try {
			fileOut = new FileOutputStream(current+"/Data/DynamicGestureBaseLine.data"); 
			out = new ObjectOutputStream(fileOut);
			out.writeObject(GestureBaseLineD);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		System.out.println("Dynamic Base Line Saved");
	}
	public void save() {
		 FileOutputStream fileOut;
	     ObjectOutputStream out;
		try {
			File directory = new File(current+"/Data/"+profile.getUsername());
			if (!directory.exists()){
				directory.mkdir();
				System.out.println("Making");
			}
			fileOut = new FileOutputStream(current+"/Data/"+profile.getUsername()+"/GestureDatabase.data"); 
			out = new ObjectOutputStream(fileOut);
			out.writeObject(GestureDatabase);
			out.close();
			/**
			System.out.println(current+"/Data/"+profile.getUsername()+"/GestureDatabase.data");
			System.out.println(GestureDatabase.get("B").get(0).getname());
			for(int i = 0; i < 26; i++) {
				System.out.println(GestureDatabase.get(alphabet[i]).size());
				System.out.println(GestureDatabase.get(alphabet[i]).get(0).getname());
				System.out.println(alphabet[i]+ " " +GestureDatabase.get(alphabet[i]).get(0).gethanddata(0).getpalmtofingers());
			}
			*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		saveDy();
		System.out.println("DataBase Saved");
	}
	public void saveDy() {
		 FileOutputStream fileOut;
	     ObjectOutputStream out;
		try {
			File directory = new File(current+"/Data/"+profile.getUsername());
			if (! directory.exists()){
				directory.mkdir();
			}
			fileOut = new FileOutputStream(current+"/Data/"+profile.getUsername()+"/DynamicGestureDatabase.data"); 
			out = new ObjectOutputStream(fileOut);
			out.writeObject(GestureDatabaseD);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Dynamic DataBase Saved");
	}
	@SuppressWarnings("unchecked")
	public void load() {
		File f = new File(current+"/Data/GestureBaseLine.data");
		if(f.isFile()) {
			try {
				FileInputStream fin = new FileInputStream(current+"/Data/GestureBaseLine.data");
				ObjectInputStream ois = new ObjectInputStream(fin);
				GestureBaseLine = (Hashtable<String, Gesture>) ois.readObject();
				ois.close();
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block	
				e.printStackTrace();
			}
			loadDy();
			System.out.println("Base Line Loaded");
		}
	}
	@SuppressWarnings("unchecked")
	public void loadDy() {
		File f = new File(current+"/Data/DynamicGestureBaseLine.data");
		if(f.isFile()) {
			try {
				FileInputStream fin = new FileInputStream(current+"/Data/DynamicGestureBaseLine.data");
				ObjectInputStream ois = new ObjectInputStream(fin);
				GestureBaseLineD = (Hashtable<String,DynamicGesture>) ois.readObject();
				ois.close();
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block	
				e.printStackTrace();
			}
			System.out.println("Dynamic Base Line Loaded");
		}
	}
	@SuppressWarnings("unchecked")
	public void loaduser() {
		File f = new File((current+"/Data/"+profile.getUsername()+"/GestureDatabase.data"));
		if(f.isFile()) {
			try {
				FileInputStream fin = new FileInputStream((current+"/Data/"+profile.getUsername()+"/GestureDatabase.data"));
				ObjectInputStream ois = new ObjectInputStream(fin);
				GestureDatabase = (Hashtable<String, Hashtable<Integer,Gesture>>) ois.readObject();
				ois.close();
			
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			loadDyuser();
			System.out.println("Base Line Loaded");
		}
	}
	@SuppressWarnings("unchecked")
	public void loadDyuser() {
		File f = new File((current+"/Data/"+profile.getUsername()+"/DynamicGestureDatabase.data"));
		if(f.isFile()) {
			try {
				FileInputStream fin = new FileInputStream((current+"/Data/"+profile.getUsername()+"/DynamicGestureDatabase.data"));
				ObjectInputStream ois = new ObjectInputStream(fin);
				GestureDatabaseD = (Hashtable<String, DynamicGesture>) ois.readObject();
				
				ois.close();
				
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Dynamic Base Line Loaded");
		}
	}
	public void recorddynamicset() {
		try {
			timer.setVisible(true);
			timer.setText("5");
			for(int j = 0; j < 5; j++) {
				Thread.sleep(1000);
				timer.setText(String.valueOf(Integer.parseInt(timer.getText()) - 1));
				if(threadstopper == true) {
					return;
				}
			}
			DynamicGesture dg = new DynamicGesture();
			
			dg.addGesture(recordgesture());
			Thread.sleep(100);
			dg.addGesture(recordgesture());
			Thread.sleep(100);
			dg.addGesture(recordgesture());
			Thread.sleep(100);
			dg.addGesture(recordgesture());
			Thread.sleep(100);
			dg.addGesture(recordgesture());
			
				if(GestureBaseLineD.containsKey(alphabet[lettercounter])) {
					GestureBaseLineD.replace(alphabet[lettercounter], dg);
				}
				else {
					GestureBaseLineD.put(alphabet[lettercounter], dg);
				}
				
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		musichandler.playCorrect();
		lettercounter++;
		if (lettercounter < 26) {
			lettersymbol.setIcon(new ImageIcon(current+"/Signs/"+alphabet[lettercounter]+".png"));
			lettershown.setText(alphabet[lettercounter]);
		}
  	  	else {
  	  		lettersymbol.setVisible(false);
  	  		lettershown.setVisible(false);
  	  		skip.setText("Save");
  	  	}
		donecounter = 0;
  	  	skip.setEnabled(true);
		
	}
	public void recordset() {
			try {
				timer.setVisible(true);
				timer.setText("5");
				for(int j = 0; j < 5; j++) {
					Thread.sleep(1000);
					timer.setText(String.valueOf(Integer.parseInt(timer.getText()) - 1));
					if(threadstopper == true) {
						return;
					}
				}
				if(GestureBaseLine.containsKey(alphabet[lettercounter])) {
					GestureBaseLine.replace(alphabet[lettercounter], recordgesture());
				}
				else {
					GestureBaseLine.put(alphabet[lettercounter], recordgesture());
				}
				//System.out.println(GestureBaseLine.get(alphabet[lettercounter]).gethanddata("Right hand").gethandType());
				//System.out.println(GestureBaseLine.get(alphabet[lettercounter]).getfirst());
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			musichandler.playCorrect();
		lettercounter++;
		if (lettercounter < 26) {
			lettersymbol.setIcon(new ImageIcon(current+"/Signs/"+alphabet[lettercounter]+".png"));
			lettershown.setText(alphabet[lettercounter]);
		}
  	  	else {
  	  		lettersymbol.setVisible(false);
  	  		lettershown.setVisible(false);
  	  		skip.setText("Save");
  	  	}
		donecounter = 0;
  	  	skip.setEnabled(true);
	}
	public void trainsetDy() {
		DynamicGesture dg = new DynamicGesture();
		errormsg.setVisible(false);
		System.out.println("--------"+alphabet[lettercounter]+"--------");
		try {
			timer.setVisible(true);
			timer.setText("5");
			for(int j = 0; j < 5; j++) {
				Thread.sleep(1000);
				timer.setText(String.valueOf(Integer.parseInt(timer.getText()) - 1));
				if(threadstopper == true) {
					return;
				}
			}
			for(int i = 0;i < 5;i++) {
				Gesture recordedgesture = traingesture(i, 2);
				if (recordedgesture == null) {
					musichandler.playIncorrect();
					donecounter = 0;
					errormsg.setVisible(true);
					start.setEnabled(true);
					return;
				}
				musichandler.playCorrect();
				recordedgesture.setname(alphabet[lettercounter]);
				dg.addGesture(recordedgesture);
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(GestureDatabaseD.containsKey(alphabet[lettercounter])) {
			GestureDatabaseD.replace(alphabet[lettercounter], dg);
		}
		else {
			GestureDatabaseD.put(alphabet[lettercounter], dg);
		}
		lettercounter++;
		if (lettercounter < 26) {
			lettersymbol.setIcon(new ImageIcon(current+"/Signs/"+alphabet[lettercounter]+".png"));
			lettershown.setText(alphabet[lettercounter]);
		}
  	  	else {
  	  		lettersymbol.setVisible(false);
  	  		lettershown.setVisible(false);
  	  		start.setText("Save");
  	  	}
		donecounter = 0;
  	  	start.setEnabled(true);
	}
	public void trainset() {
		Hashtable<Integer, Gesture> GestureHash = new Hashtable<Integer, Gesture>(); 
		if(GestureHashbackup.size() >= 1) {
			for(int j = 0; j < GestureHashbackup.size();j++) {
				GestureHash.put(j, GestureHashbackup.get(j));
			}
		}
		errormsg.setVisible(false);
		Custom.setEnabled(false);
		if(donecounter == 0) {
			System.out.println("--------"+alphabet[lettercounter]+"--------");
		}
		for(int i = donecounter ; i< 3; i++) {
			try {
				timer.setVisible(true);
				timer.setText("5");
				for(int j = 0; j < 5; j++) {
					Thread.sleep(1000);
					timer.setText(String.valueOf(Integer.parseInt(timer.getText()) - 1));
					if(threadstopper == true) {
						return;
					}
				}
				Gesture recordedgesture = traingesture(1,1);
				if (recordedgesture == null) {
					musichandler.playIncorrect();
					errormsg.setVisible(true);
					start.setEnabled(true);
					Custom.setEnabled(true);
					GestureHashbackup = GestureHash;
					return;
				}
				System.out.println("Gesture add using key: "+donecounter);
				musichandler.playCorrect();
				recordedgesture.setname(alphabet[lettercounter]);
				GestureHash.put(donecounter, recordedgesture);
				donecounter++;
				Display.setIcon(new ImageIcon(current+"/Characters/Train/"+donecounter+".png"));
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(GestureDatabase.containsKey(alphabet[lettercounter])) {
			System.out.println("Replacing current gesture set");
			GestureDatabase.replace(alphabet[lettercounter], GestureHash);
		}
		else {
			System.out.println("Adding new gesture set");
			GestureDatabase.put(alphabet[lettercounter], GestureHash);
		}
		GestureHashbackup.clear();
		lettercounter++;
		if (lettercounter < 26) {
			lettersymbol.setIcon(new ImageIcon(current+"/Signs/"+alphabet[lettercounter]+".png"));
			lettershown.setText(alphabet[lettercounter]);
		}
  	  	else {
  	  		lettersymbol.setVisible(false);
  	  		lettershown.setVisible(false);
  	  	  	Custom.setEnabled(false);
  	  		start.setText("Save");
  	  	}
		donecounter = 0;
		Custom.setEnabled(true);
		Display.setIcon(new ImageIcon(current+"/Characters/Train/"+donecounter+".png"));
  	  	start.setEnabled(true);
	}
	
	public Gesture recordgesture() {
		Frame frame = controller.frame();
		Gesture recordedgesture = disectframe(frame);
		recordedgesture.setname(alphabet[lettercounter]);
		donecounter = 0;
		Display.setIcon(new ImageIcon(current+"/Characters/Train/"+donecounter+".png"));
		ArrayList<ArrayList<Double>> dataset = recordedgesture.getpamtofinger();
		for(int i = 0; i < dataset.size(); i++) {
			System.out.println("Palm to Finger (Hand "+i+"): "+dataset.get(i));
		}
		return recordedgesture;
	}
	public Gesture traingesture(int v, int type) {
		try {
		String[] hands = {"Left hand","Right hand"};
		Frame frame = controller.frame();
		Gesture recordedgesture = disectframe(frame);
		Gesture compare;
		if(type == 1) {
			compare = GestureBaseLine.get(alphabet[lettercounter]);
		}
		else {
			compare = GestureBaseLineD.get(alphabet[lettercounter]).getgesture(v);
		}
		boolean iscorrect = false;
		boolean bothhands = false;
		int basesize = compare.gethandnum();
		
		int mysize = recordedgesture.gethandnum();
		if(basesize == 0|| mysize == 0) {
			System.out.println("Hand Number Invalid");
			return null;
		}
		int errorrange = 40*type;
		
		if(basesize >= 2 && mysize >= 2) {
			vec gpalm = recordedgesture.gethanddata(0).getpalmposition();
			vec gpalm2 = recordedgesture.gethanddata(1).getpalmposition();
			double palmdistance = calculate3D(gpalm.getX(),gpalm2.getX(),gpalm.getY(),gpalm2.getY(), gpalm.getZ(), gpalm2.getZ());
			vec kpalm = compare.gethanddata(0).getpalmposition();;
			vec kpalm2 = compare.gethanddata(1).getpalmposition();
			
			double palmdistance2 = calculate3D(kpalm.getX(),kpalm2.getX(),kpalm.getY(),kpalm2.getY(), kpalm.getZ(), kpalm2.getZ());
			
			if(palmdistance < palmdistance2-(errorrange+30) || palmdistance > palmdistance2+(errorrange+30)) {
				System.out.println("Palm Distance Invalid: "+palmdistance+"!="+palmdistance2);
				return null;
			}
		} 
		
		if (basesize == mysize) {
			for(int y = 0;y<basesize;y++) {
				int counter = y;	
				String handtouse;
				if(basesize == 1) {
					handtouse = hands[1];
				}
				else {
					handtouse = hands[y];
				}
				
				String handside = compare.gethanddata(handtouse).gethandType();
			
				String recordhandside = recordedgesture.gethanddata(handtouse).gethandType();
				
				
				if(!recordhandside.equals(handside) && basesize > 1) {
					if(recordhandside.equals("Right hand")) {
						counter = y+1;
					}
					else{
						counter = y-1;
					}
				}
				else if(basesize == 1){
					counter = 1;
				}
				
				ArrayList<Double> palmtofingers = compare.gethanddata(hands[counter]).getpalmtofingers();
				
				System.out.println("Baseline: "+palmtofingers);
				System.out.println("Recorded: "+recordedgesture.gethanddata(handtouse).getpalmtofingers());
				System.out.println("");
				int correct = 0;
				for(int k = 0; k<5;k++) {
					Double length = palmtofingers.get(k);
					Double yourlength = recordedgesture.gethanddata(handtouse).getpalmtofingers().get(k);
					if(yourlength > length-errorrange && yourlength < length+errorrange) {
						correct++;
					}
					if (correct == 5) {
						if(iscorrect) {
							bothhands = true;
						}
						if(basesize == 1) {
							bothhands = true;
						}
						iscorrect = true;
					}
				}
			}
		}
		/**
		ArrayList<ArrayList<Double>> dataset = recordedgesture.getpamtofinger();
		ArrayList<ArrayList<Double>> dataset2 = compare.getpamtofinger();
		for(int i = 0; i < dataset.size(); i++) {
			System.out.println("Palm to Finger (Hand "+i+"): "+dataset.get(i));
			System.out.println("Palm to Finger (Hand "+i+"): "+dataset2.get(i));
		}
		*/
		if(bothhands) {
			return recordedgesture;
		}
		else {
			System.out.println("Hands do not fall within acceptable parameters");
			return null;
		}
		}
		catch(NullPointerException e) {
			System.out.println("Somethings gone wrong");
		}
		return null;
	}
	public static Gesture disectframe(Frame frame) {
		Gesture g = new Gesture();
		int handcount = frame.hands().count();
		ArrayList<Double> palmtootherfingers = new ArrayList<>();
		ArrayList<Double> palmtootherfingers2 = new ArrayList<>();
		ArrayList<Vector> firsthandpos = new ArrayList<>();
		Vector firstpalm = null;
		boolean firsthanddone = false;
		 for(Hand hand : frame.hands()) { //For each hand
			 String handType = hand.isLeft() ? "Left hand" : "Right hand";
			 
	         Vector palmposition = hand.palmPosition();
	         if(firsthanddone == false) {
	        	 firstpalm = palmposition;
	         }
	         vec palm1 = new vec(palmposition.getX(),palmposition.getY(),palmposition.getZ());
	        
	         
	         ArrayList<Double> palmtofingers = new ArrayList<>(); //Distances between palm and fingers in 3D
	         ArrayList<Vector> tippositions = new ArrayList<>(); //Finger tip positions
	         ArrayList<ArrayList<Vector>> fingerdata = new ArrayList<>();
	            for (Finger finger : hand.fingers()) { //For each finger
	            	
	            	ArrayList<Vector> fdata = new ArrayList<>();
	            	fdata.add(finger.direction());
	            	fdata.add(finger.tipVelocity());
	            	fingerdata.add(fdata);
	         
	                Bone bone = finger.bone(Bone.Type.TYPE_DISTAL);
	                Vector fingertip = bone.nextJoint();
	                tippositions.add(fingertip);
	                if(firsthanddone == false && handcount != 1) {
	                	firsthandpos.add(fingertip);
	            	}
	                //gets the distance between each finger tip and the palm
	                double distance = calculate3D(fingertip.getX(), palmposition.getX(), fingertip.getY(), palmposition.getY(), fingertip.getZ(), palmposition.getZ());
	                palmtofingers.add(distance);
	                
	                if(firsthanddone == true && handcount == 2) {
	                	double newdinstace = calculate3D(fingertip.getX(), firstpalm.getX(), fingertip.getY(), firstpalm.getY(), fingertip.getZ(), firstpalm.getZ());
	                	palmtootherfingers.add(newdinstace);
	            	} 
	            }
	            if(firsthanddone == true && handcount == 2) {
	      
	            	for(int p = 0;p< firsthandpos.size();p++) {
            			double newdinstace2 = calculate3D(firsthandpos.get(p).getX(), palmposition.getX(), firsthandpos.get(p).getY(), palmposition.getY(), firsthandpos.get(p).getZ(), palmposition.getZ());
            			palmtootherfingers2.add(newdinstace2);
            		}
	            }
	            //g.addhanddata(handType, handnormal, handdirection, palmposition, palvelocity, stabilizedpalmposition, palmtofingers, tippositions, fingerdata);
	            g.addhanddata(handType, palmtofingers,palm1);
	   
	            firsthanddone = true;
		    }
		 if (handcount == 2) {
         	g.addother(palmtootherfingers,palmtootherfingers2);
         }
		 return g;
	}
	
	
	public static double calculate3D(double x1, double x2, double y1, double y2, double z1, double z2) {
		double tippalmx = x1 - x2;
        double tippalmy = y1 - y2;
        double tippalmz = z1 - z2;
        tippalmx = tippalmx * tippalmx;
        tippalmy = tippalmy * tippalmy;
        tippalmz = tippalmz * tippalmz;
        double tippalmtotal = tippalmx + tippalmy + tippalmz;
        double distance = Math.sqrt(tippalmtotal);
        return distance;
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