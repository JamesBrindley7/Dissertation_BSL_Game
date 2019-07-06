import java.io.Serializable;
import java.util.ArrayList;

import com.leapmotion.leap.Vector;

public class Gesture implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name = "";
	private int handsused = 0;
	private ArrayList<HandData> Hands = new ArrayList<>();
	ArrayList<Double> palmtofirst = new ArrayList<>();
	ArrayList<Double> palmtosecond = new ArrayList<>();
	String firsthand;
    
	public void addhanddata(String handType, ArrayList<Double> palm4, vec palm1) {
		HandData h = new HandData(handType,palm4,palm1);
		Hands.add(h);
		handsused++;
	}
	public int gethandnum() {
		return handsused;
	}
	public String getfirst() {
		return firsthand;
	}
	public HandData gethanddata(int i){
		return Hands.get(i);
	}
	public void addother(ArrayList<Double> p1, ArrayList<Double> p2) {
		handsused = 2;
		palmtofirst = p1;
		palmtosecond = p2;
	}
	public ArrayList<Double> get1 () {
		return palmtofirst;
	}
	public ArrayList<Double> get2 () {
		return palmtosecond;
	}
	public ArrayList<Double> get (int i) {
		if(i == 0) {
			return palmtofirst;
		}
		else {
			return palmtosecond;
		}
	}
	public HandData gethanddata(String i){

		
		for(int j = 0; j < Hands.size(); j++) {
			if (Hands.get(j).gethandType().equals(i)) {
				return Hands.get(j);
			}
		}
		return null;
	}
	public int gethandorder(String i) {
		for(int j = 0; j < Hands.size(); j++) {
			if (Hands.get(j).gethandType().equals(i)) {
				return j;
			}
		}
		return 0;
	}
	public ArrayList<ArrayList<Double>> getpamtofinger() {
		ArrayList<ArrayList<Double>> dataset = new ArrayList<ArrayList<Double>>();
		for(int j = 0; j < Hands.size(); j++) {
			dataset.add(gethanddata(j).getpalmtofingers());
		}
		return dataset;
	}
	public void setname(String name) {
		this.name = name;
	}
	public String getname() {
		return name;
	}
}
