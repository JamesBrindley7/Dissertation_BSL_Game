import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author James Brindley
 *
 */
public class DynamicGesture implements Serializable{
	/**
	 * Class to store Dynamic Gestures
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Name of the dynamic sign
	 */
	private String name = ""; 
	/**
	 * ArrayList of the gestures stored in the dynamic sign
	 */
	private ArrayList<Gesture> Gestures = new ArrayList<>(); 
	/**
	 * Number of gestures within this dynamic sign
	 */
	private int numgestures = 0; 
	
	/**
	 * Adds a new Gesture to the dynamic set
	 * @param g
	 */
	public void addGesture(Gesture g) {
		Gestures.add(g);
		numgestures++;
	}
	/**
	 * Method to remove the first gesture and shuffle all other gestures down and add one to the end
	 * @param g
	 */
	public void replaceGesture(Gesture g) {
		try {
			ArrayList<Gesture> TempGestures = new ArrayList<>(); 
			TempGestures.add(Gestures.get(1));
			TempGestures.add(Gestures.get(2));
			TempGestures.add(Gestures.get(3));
			TempGestures.add(Gestures.get(4));
			TempGestures.add(g);
			Gestures.clear();
			Gestures = TempGestures;
		}
		catch(IndexOutOfBoundsException e) {
			
		}
	}
	/**
	 *  Gets the number of gestures stored in the dynamic sign
	 * @return Number of Gestures
	 */
	public int getnumberof() {
		return numgestures;
	}
	/**
	 * Gets all the gestures used in the dynamic sign
	 * @return
	 */
	public ArrayList<Gesture> getgestures(){
		return Gestures;
	}
	/**
	 * Gets a specific gesture from the dynamic sign
	 * @param i The position of the gesture
	 * @return The single gesture in that position
	 */
	public Gesture getgesture(int i){
		if (Gestures.size() >= i) {
			return Gestures.get(i);
		}
		return null;
	}
	/**
	 * Sets the name of the gesture
	 * @param name
	 */
	public void setname(String name) {
		this.name = name;
	}
	/**
	 * Gets the name of the gesture
	 * @return The name
	 */
	public String getname() {
		return name;
	}
}
