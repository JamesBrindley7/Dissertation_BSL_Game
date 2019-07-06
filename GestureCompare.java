import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Vector;

public class GestureCompare {
	String current = System.getProperty("user.dir");
	public static Hashtable<Integer, Gesture> GestureHash = new Hashtable<Integer, Gesture>(); 
	public static Hashtable<String, DynamicGesture> GestureDHash = new Hashtable<String, DynamicGesture>(); 
	public static Hashtable<Integer, Float> probhash = new Hashtable<Integer, Float>(); 
	public static Hashtable<String, Hashtable<Integer,Gesture>> GestureBaseLine = new Hashtable<String, Hashtable<Integer,Gesture>>(); 
	public static Gesture gesturetocheck;
	static String[] alphabet = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	static Controller controller;
	private Player_Profile profile;
	static float probperalpha;
	static DynamicGesture Past5 = new DynamicGesture();
	static int timesrun = 0;
	private static int LastH = 0;
	private static int LastJ = 0;
	
	public String getframe() {
		timesrun++;
		Frame frametouse = controller.frame();
		
		Gesture g = disectframe(frametouse);
		
		if(g != null) {
			
			updateorder(g);
		
			Gesture k = compareagainst(g);
		
			if(k == null) {
				return null;
			}
			return k.getname();
		}
		return null;
	}
	public void loadcontroller(Controller c,Player_Profile p) {
		controller = c;
		profile = p;
	}
	//Oldest is at 0 and newest at 4
	public void updateorder(Gesture n) {
		if(Past5.getnumberof() < 5) {
			Past5.addGesture(n);
		}
		else {
			Past5.replaceGesture(n);
		}
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
	         vec palm1 = new vec(palmposition.getX(), palmposition.getY(), palmposition.getZ());
	         
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
	            
	           // g.addhanddata(handType, handnormal, handdirection, palmposition, palvelocity, stabilizedpalmposition, palmtofingers, tippositions, fingerdata);
	            g.addhanddata(handType,palmtofingers,palm1);
	            
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
	
	public static Gesture compareagainst(Gesture g) {
		gesturetocheck = g;
		if(gesturetocheck == null) {
			return null;
		}
		probhash.clear();
		float highestprob = 0;
		int highest = 0;
		for(int i = 0; i<26; i++ ) {
			GestureHash = GestureBaseLine.get(alphabet[i]);
			if(i ==7 || i == 9) {
				if(timesrun > 5) {
					if(i == 7) {
						compareDynamicfeatures(Past5,GestureDHash.get(alphabet[i]), 1, alphabet[i]);	
					}
					else {
						compareDynamicfeatures(Past5,GestureDHash.get(alphabet[i]), 3, alphabet[i]);	
					}
					if(i ==7 || i == 9) {
						if((probperalpha/5)>highestprob) {
							highestprob = probperalpha/3;
							highest = i;
						}
					}
					//System.out.println("here"+probperalpha/5);
					probhash.put(i, probperalpha/5);
					probperalpha = 0;
				}
			}
			else {
				for(int j = 0; j <3; j++) {
					boolean check = comparefeatures(gesturetocheck,GestureHash.get(j), 1, alphabet[i]);
					//System.out.println(alphabet[i]+" - prob: " + probperalpha);
					//System.out.println(GestureHash.get(j).gethanddata(0).getpalmtofingers());
					
					if(!check) {
						probperalpha = 0;
						break;
					}
					else {
						if((probperalpha)>highestprob) {
							highestprob = probperalpha;
							highest = i;
						}
					}
					probperalpha = 0;
				}
			}
		}
		if(highest == 7) {
			LastH = 0;
		}
		else {
			LastH++;
		}
		if(highest == 9) {
			LastJ = 0;
		}
		else {
			LastJ++;
		}
		if(highestprob == 0) {
			return null;
		}
		
		if(highestprob > 0.915) {
			if(probhash.get(7) != null && probhash.get(9) != null) {
				if(probhash.get(7) > 0.915) {
					return GestureDHash.get(alphabet[7]).getgesture(0);
				}
				else if(probhash.get(9)> 0.915) {
					return GestureDHash.get(alphabet[9]).getgesture(0);
				}
				else {
					//System.out.println(highest);
					if(highest == 7 || highest == 9) {
						if(GestureDHash.get(alphabet[highest]).getgesture(0) == null) {
							return null;
						}
						else {
							return GestureDHash.get(alphabet[highest]).getgesture(0);
						}
					}
					if(GestureBaseLine.get(alphabet[highest]).get(0) == null) {
						return null;
					}
					else {
						return GestureBaseLine.get(alphabet[highest]).get(0);
					}
				}
			}
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public void load() {
		File f = new File((current+"/Data/"+profile.getUsername()+"/GestureDatabase.data"));
		if(f.isFile()) {
			try {
				FileInputStream fin = new FileInputStream((current+"/Data/"+profile.getUsername()+"/GestureDatabase.data"));
				ObjectInputStream ois = new ObjectInputStream(fin);
				GestureBaseLine = (Hashtable<String, Hashtable<Integer,Gesture>>) ois.readObject();
				ois.close();
				for(int i = 0; i<26; i++) {
					if(i != 7 && i !=9) {
						System.out.println(alphabet[i]+ " Loaded "+GestureBaseLine.get(alphabet[i]).size()+ " Gestures");
					}
				}
			
			} catch (IOException | ClassNotFoundException e) {
				System.out.println("Load Error");
			}
			loadDy();
			System.out.println("Base Line Loaded");
		}
	}
	@SuppressWarnings("unchecked")
	public void loadDy() {
		File f = new File((current+"/Data/"+profile.getUsername()+"/DynamicGestureDatabase.data"));
		if(f.isFile()) {
			try {
				FileInputStream fin = new FileInputStream((current+"/Data/"+profile.getUsername()+"/DynamicGestureDatabase.data"));
				ObjectInputStream ois = new ObjectInputStream(fin);
				GestureDHash = (Hashtable<String, DynamicGesture>) ois.readObject();
			
				System.out.println(alphabet[7]+ " Loaded Gesture");
		
				System.out.println(alphabet[9]+ " Loaded Gesture");
				ois.close();
			} catch (IOException | ClassNotFoundException e) {
				System.out.println("Load Error");
			}
			System.out.println("Dynamic Base Line Loaded");
		}
	}

	public static boolean comparefeatures(Gesture g, Gesture k, int type, String letter) {
		if(k != null) {
		float totalprob = 0;
		boolean correct = true;
		int errorrange = 0;
		if(type == 1) {
			errorrange = 20;	
		}
		else if( type == 2){
			errorrange = 25;
		}
		else {
			errorrange = 25;
		}
		if(g == null) {
			return false;
		}
		int numofhandsg = g.gethandnum();
		int numofhandsk = k.gethandnum();
		if(numofhandsk >= 2 && numofhandsg >= 2) {
			vec gpalm = g.gethanddata(0).getpalmposition();
			vec gpalm2 = g.gethanddata(1).getpalmposition();
			double palmdistance = calculate3D(gpalm.getX(),gpalm2.getX(),gpalm.getY(),gpalm2.getY(), gpalm.getZ(), gpalm2.getZ());
			
			vec kpalm = k.gethanddata(0).getpalmposition();
			vec kpalm2 = k.gethanddata(1).getpalmposition();
			double palmdistance2 = calculate3D(kpalm.getX(),kpalm2.getX(),kpalm.getY(),kpalm2.getY(), kpalm.getZ(), kpalm2.getZ());
			
			if(palmdistance < palmdistance2-errorrange || palmdistance > palmdistance2+errorrange) {
				probperalpha = 0;
				
				return false;
			}
		}
		
		if(numofhandsg == numofhandsk) {
			for(int i = 0; i < numofhandsk; i++) {
				
				HandData tempk = k.gethanddata(i);
				int handnum = g.gethandorder(k.gethanddata(i).gethandType());
				HandData tempg = g.gethanddata(handnum);
				//g is new k is stored
				for(int j = 0; j < tempg.getpalmtofingers().size(); j++) {
					if(numofhandsk > 1 && numofhandsg > 1) {
						ArrayList<Double> palmtootherhand = g.get(handnum);;
						ArrayList<Double> tocheck = k.get(i);
						
						//if(letter.equals("Y")) {
						//System.out.println("Baseline: "+tocheck);
						//System.out.println("Recorded: "+palmtootherhand);
						//System.out.println("");
						//}
						/**
						if(k.getname().equals("L")) {
							System.out.println(k.getname()+ handnum);
							System.out.println("Hand number: "+i);
							System.out.println("check"+tocheck+ " hand side: "+k.gethanddata(i).gethandType());
							System.out.println("mine"+palmtootherhand+ " hand side: "+g.gethanddata(handnum).gethandType());
						}
						*/
						double lengthg = palmtootherhand.get(j);
						double lengthk = tocheck.get(j);
						
						float probabilty = 0;
						if(lengthg >= lengthk) {
							probabilty = (float) (lengthk/lengthg);
						}
						else {
							probabilty = (float) (lengthg/lengthk);
						}
						if (lengthg < lengthk-(errorrange*2) || lengthg > lengthk+(errorrange*2)) {
							correct = false;
							
							return false;
						}
						totalprob = totalprob + probabilty;
					}
					
					double dubg = tempg.getpalmtofingers().get(j);
					double dubk = tempk.getpalmtofingers().get(j);
					
					float prob = 0;
					if(dubg >= dubk) {
						prob = (float) (dubk/dubg);
					}
					else {
						prob = (float) (dubg/dubk);
					}
					totalprob = totalprob + prob;
					if (dubg < dubk-errorrange || dubg > dubk+errorrange) {
						correct = false;
						
						return false;
					}
				}
				totalprob = totalprob/(numofhandsk*5);
				
			}
			
			probperalpha = probperalpha + totalprob;
			return correct;
		}
		probperalpha = 0;
		return false;
		}
		//System.out.println(letter+" is null");
		probperalpha = 0;
		return false;
	}
	public static void compareDynamicfeatures(DynamicGesture g, DynamicGesture k, int type, String letter) {
		for(int i = 0; i <5;i++) {
			Gesture g1 = g.getgesture(i);
			Gesture k1 = k.getgesture(i);
			if(g1 != null && k1 != null) {
				boolean check = comparefeatures(g1, k1, type, letter);
				if(!check) {
					break;
				}
			}
		}
	}
}
