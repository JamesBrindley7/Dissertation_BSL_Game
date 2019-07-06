import java.io.Serializable;
import java.util.ArrayList;

import com.leapmotion.leap.Vector;

public class HandData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String handType = "";
	private Vector handnormal = null;
	private Vector handdirection = null;
	private vec palmposition = null;
	private Vector palvelocity = null;
	private Vector stabilizedpalmposition = null;
	private ArrayList<Double> palmtofingers = new ArrayList<>(); //Distances between palm and fingers in 3D
	private ArrayList<Vector> tippositions = new ArrayList<>(); //Finger tip positions
	private ArrayList<ArrayList<Vector>> fingerdata = new ArrayList<>(); //1 Thumb etc
    
    public HandData(String handType,ArrayList<Double> palm4 ,vec palm1) {
    	sethandType(handType);
    	//sethandnormal(hand1);
    	//sethanddirection(hand2);
    	setpalmposition(palm1);
    	//setpalvelocity(palm2);
    	//setstabilizedpalmposition(palm3);
    	setpalmtofingers(palm4);
    	//settippositions(tip1);
    	//setfingerdata(fingerdata);
    }
    public void sethandType(String i) {
    	this.handType = i;
    }
    public String gethandType() {
    	return handType;
    }
    
    public void sethandnormal(Vector i) {
    	this.handnormal = i;
    }
    public Vector gethandnormal() {
    	return handnormal;
    }
    public void sethanddirection(Vector i) {
    	this.handdirection = i;
    }
    public Vector gethanddirection() {
    	return handdirection;
    }
    public void setpalmposition(vec i) {
    	this.palmposition = i;
    }
    public vec getpalmposition() {
    	return palmposition;
    }
    public void setpalvelocity(Vector i) {
    	this.palvelocity = i;
    }
    public Vector getpalvelocity() {
    	return palvelocity;
    }
    public void setstabilizedpalmposition(Vector i) {
    	this.stabilizedpalmposition = i;
    }
    public Vector getstabilizedpalmposition() {
    	return stabilizedpalmposition;
    }
    public void setpalmtofingers(ArrayList<Double> i) {
    	this.palmtofingers = i;
    }
    public ArrayList<Double> getpalmtofingers() {
    	return palmtofingers;
    }
    public void settippositions(ArrayList<Vector> i) {
    	this.tippositions = i;
    }
    public ArrayList<Vector> gettippositions() {
    	return tippositions;
    }
    public void setfingerdata(ArrayList<ArrayList<Vector>> i) {
    	this.fingerdata = i;
    }
    public ArrayList<ArrayList<Vector>> getfingerdata() {
    	return fingerdata;
    }
}
