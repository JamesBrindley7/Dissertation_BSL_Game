import java.io.Serializable;

public class Player_Profile implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String Username = "";
	public int Coins = 1000;
	public String Character = "Scientist";
	public int[] CharactersUnlocked = {1,0,0,0,0,0,0,0};
	public String[] Characters = {"Scientist", "Santa", "Mickey Mouse","Norris","Alien", "Pluto", "Smurf", "Donald Duck"};

	public Player_Profile(String name) {
		setUsername(name);
	}
	public void setUsername(String Data) {
		Username = Data; 
	}
	public void setCoins(int Data) {
		Coins = Data;
	}
	public void setCharacter(String Data) {
		Character = Data;
	}
	public void setunlocked(int position) {
		CharactersUnlocked[position] = 1;
	}
	
	public String getUsername() {
		return Username; 
	}
	public int getUsernameID() {
		int counter = 0;
		for(int i = 0;i< 8; i++) {
			counter = i;
			if(Character.equals(Characters[i])) {
				break;
			}
		}
		return counter; 
	}
	public int getCoins() {
		return Coins;
	}
	public String getCharacter() {
		return Character;
	}
	public int getunlocked(int position) {
		return CharactersUnlocked[position];
	}
	
}
