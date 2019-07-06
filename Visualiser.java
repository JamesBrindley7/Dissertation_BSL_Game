
import java.util.Hashtable;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Vector;

public class Visualiser{
	public Hashtable<Integer, Vector> LFinger1 = new Hashtable<Integer, Vector>();
	public Hashtable<Integer, Vector> LFinger2 = new Hashtable<Integer, Vector>();
	public Hashtable<Integer, Vector> LFinger3 = new Hashtable<Integer, Vector>();
	public Hashtable<Integer, Vector> LFinger4 = new Hashtable<Integer, Vector>();
	public Hashtable<Integer, Vector> LFinger5 = new Hashtable<Integer, Vector>();
	public Hashtable<Integer, Hashtable<Integer, Vector>> LeftHandHash = new Hashtable<Integer, Hashtable<Integer, Vector>>();
	
	
	public Hashtable<Integer, Vector> RFinger1 = new Hashtable<Integer, Vector>();
	public Hashtable<Integer, Vector> RFinger2 = new Hashtable<Integer, Vector>();
	public Hashtable<Integer, Vector> RFinger3 = new Hashtable<Integer, Vector>();
	public Hashtable<Integer, Vector> RFinger4 = new Hashtable<Integer, Vector>();
	public Hashtable<Integer, Vector> RFinger5 = new Hashtable<Integer, Vector>();
	public Hashtable<Integer, Hashtable<Integer, Vector>> RightHandHash = new Hashtable<Integer, Hashtable<Integer, Vector>>();
	
	Vector leftpalm;
	Vector rightpalm;
	boolean isleft;
	String[] positions = new String[2];
	
	int handcounter = 0;
	public String[] getpos() {
		return positions;
	}
	public void capture(Controller controller) {
		Frame frame = controller.frame();
		HandList hands = frame.hands();
		int fingercount = 0;
		handcounter = hands.count();

		if(handcounter == 0 ) {
			return;
		}
		else {
		for(int i = 0; i < hands.count(); i++) {
			if(hands.count() > 2) {
				return;
			}
			if (hands.get(i).isLeft()) {
				leftpalm = hands.get(i).palmPosition();
				positions[i] = "Left";
			}
			else if (hands.get(i).isRight()) {
				rightpalm = hands.get(i).palmPosition();
				positions[i] = "Right";
				
			}
			for (Finger finger : hands.get(i).fingers()) {
				int bonecount = 0;
				for(Bone.Type boneType : Bone.Type.values()) {
					
					Bone bone = finger.bone(boneType);
					Vector vec = bone.nextJoint();
					if(bonecount == 1) {
							
					}
					if(hands.get(i).isLeft()) {
						switch(fingercount){
							case 0: LFinger1.put(bonecount, vec);
								break;
							case 1: LFinger2.put(bonecount, vec);
								
								break;
							case 2: LFinger3.put(bonecount, vec);
								break;
							case 3: LFinger4.put(bonecount, vec);
								break;
							case 4: LFinger5.put(bonecount, vec);
								break;
						}
					}
					else if (hands.get(i).isRight()) {
						switch(fingercount){
						case 0: RFinger1.put(bonecount, vec);
							break;
						case 1: RFinger2.put(bonecount, vec);
							break;
						case 2: RFinger3.put(bonecount, vec);
							break;
						case 3: RFinger4.put(bonecount, vec);
							break;
						case 4: RFinger5.put(bonecount, vec);
							break;
						}
					}
				bonecount++;
			}
				fingercount++;
			}
			if(hands.get(i).isLeft()) {
				if(i == 0) {
					isleft = true;
				}
				LeftHandHash.put(1, LFinger1);
				LeftHandHash.put(2, LFinger2);
				LeftHandHash.put(3, LFinger3);
				LeftHandHash.put(4, LFinger4);
				LeftHandHash.put(5, LFinger5);
				
			}
			else if (hands.get(i).isRight()) {
				RightHandHash.put(1, RFinger1);
				RightHandHash.put(2, RFinger2);
				RightHandHash.put(3, RFinger3);
				RightHandHash.put(4, RFinger4);
				RightHandHash.put(5, RFinger5);
				
			}
			fingercount = 0;
		}

		}
		}
	
	public Hashtable<Integer, Hashtable<Integer, Vector>> getlefthash() {
		return LeftHandHash;
	}
	
	public Hashtable<Integer, Hashtable<Integer, Vector>> getrighthash() {
		return RightHandHash;
	}
	public boolean isleft() {
		return isleft;
	}
	
	public int gethandcount() {
		return handcounter;
	}
	public Vector getleftpalm() {
		return leftpalm;
	}
	public Vector getrightpalm() {
		return rightpalm;
	}
	
}
