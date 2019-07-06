import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music_Handler {
	private String current = System.getProperty("user.dir");
	ArrayList<Clip> LoopClipStorage = new ArrayList<Clip>();
	ArrayList<Clip> ClipStorage = new ArrayList<Clip>();
	File ButtonClick;
	File IntroMusic;
	File TensionMusic;
	File CalmMusic;
	
	File Correct;
	File Incorrect;
	File Confirm;
	File Save;
	
	File Alien;
	File Donald;
	File Mickey;
	File Norris;
	File Pluto;
	File Santa;
	File Scientist;
	File Smurf;
	
	File Explosion;
	
	public Music_Handler() {
		ButtonClick = new File(current+"/Sounds/Button_Click.wav");
		IntroMusic = new File(current+"/Sounds/Intro_Music.wav");
		CalmMusic = new File(current+"/Sounds/Calm_Music.wav");
		TensionMusic = new File(current+"/Sounds/Tension.wav");

		Explosion = new File(current+"/Sounds/Explosion_Sound.wav");
		
		Correct = new File(current+"/Sounds/Correct.wav");
		Incorrect = new File(current+"/Sounds/Incorrect.wav");
		Confirm = new File(current+"/Sounds/Confirmation.wav");
		Save = new File(current+"/Sounds/Save.wav");
		
		Alien = new File(current+"/Sounds/Voice_Alien.wav");
		Donald = new File(current+"/Sounds/Voice_Donald.wav");
		Mickey = new File(current+"/Sounds/Voice_Mickey.wav");
		Norris = new File(current+"/Sounds/Voice_Norris.wav");
		Pluto = new File(current+"/Sounds/Voice_Pluto.wav");
		Santa = new File(current+"/Sounds/Voice_Santa.wav");
		Scientist = new File(current+"/Sounds/Voice_Scientist.wav");
		Smurf = new File(current+"/Sounds/Voice_Smurf.wav");
	}
	public void playbuttonclick() {
		playsound(ButtonClick, false);
	}
	public void playIntro() {
		stopallloops();
		playsound(IntroMusic, true);
	}
	public void playCalm() {
		stopallloops();
		playsound(CalmMusic, true);
	}
	public void playTension() {
		stopallloops();
		playsound(TensionMusic, true);
	}
	
	public void playexplosion() {
		playsound(Explosion, false);
	}
	
	public void playCorrect() {
		playsound(Correct, false);
	}
	public void playIncorrect() {
		playsound(Incorrect, false);
	}
	public void playConfirm() {
		playsound(Confirm, false);
	}
	public void playSave() {
		playsound(Save, false);
	}
	
	public void playAlien() {
		playsound(Alien, false);
	}
	public void playDonald() {
		playsound(Donald, false);
	}
	public void playSanta() {
		playsound(Santa, false);
	}
	public void playNorris() {
		playsound(Norris, false);
	}
	public void playMickey() {
		playsound(Mickey, false);
	}
	public void playScientist() {
		playsound(Scientist, false);
	}
	public void playPluto() {
		playsound(Pluto, false);
	}
	public void playSmurf() {
		playsound(Smurf, false);
	}
	public void playvoice(int i) {
		switch (i){	
			case 0:
				playScientist();
				break;
			case 1:
				playSanta();
				break;
			case 2:
				playMickey();
				break;
			case 3:
				playNorris();
				break;
			case 4:
				playAlien();
				break;
			case 5:
				playPluto();
				break;
			case 6:
				playSmurf();
				break;
			case 7:
				playDonald();
				break;
		}
	}
	
	public void playsound(File sound, boolean loopconst) {	
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(sound));
			clip.start();
			if(loopconst) {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				LoopClipStorage.add(clip);
			}
			else {
				ClipStorage.add(clip);
			}
		}catch(Exception e) {
			System.out.println("Failed to load sound");
		}
	}
	
	public void stopallloops() {
		if(LoopClipStorage.size() == 0) {
			return;
		}
		for(int i = 0; i < LoopClipStorage.size();i++) {
			LoopClipStorage.get(i).stop();
			LoopClipStorage.get(i).close();
		}
	}
	
	public void closeunused() {
		if(ClipStorage.size() == 0) {
			return;
		}
		for(int i = 0; i < ClipStorage.size();i++) {
			if(!ClipStorage.get(i).isRunning()) {
				ClipStorage.get(i).stop();
				ClipStorage.get(i).close();
			}
		}
	}
}
