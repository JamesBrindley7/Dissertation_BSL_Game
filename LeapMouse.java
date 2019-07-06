import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import com.leapmotion.leap.*;

public class LeapMouse extends Listener {
	Robot robot = null;
	int mouseX = 0;
	int mouseY = 0;
	int prevX = 0;
	int prevY = 0;
	int screenxloc = 0;
	int screenyloc = 0;
	int tryx;
	int tryy;
	public void onConnect(Controller controller) {
        System.out.println("Leap Motion Mouse Loaded");
        try {
			robot = new Robot();
			controller.enableGesture(com.leapmotion.leap.Gesture.Type.TYPE_SCREEN_TAP);
			controller.enableGesture(com.leapmotion.leap.Gesture.Type.TYPE_KEY_TAP);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void onFrame(Controller controller) {
		Frame frame = controller.frame();	
		HandList Hands = frame.hands();
		Hand hand2 = Hands.rightmost();
		Vector tipposition = null;
		for (Finger finger : hand2.fingers()) {
			Finger.Type fingerType = finger.type();
			if(fingerType.name() == "TYPE_INDEX") {
			     tipposition = finger.tipPosition();
			}
		}
		
        float xpos = tipposition.getX();
        float ypos = tipposition.getZ();
        
        if (xpos > prevX+7 || xpos < prevX-7) {
        	int revxpos = (int) -xpos;
        	tryx = screenxloc+(960/2)+( (int) revxpos*2);
        	prevX = (int) xpos;
        }
        if (ypos > prevY+7 || ypos < prevY-7) {
        	int revypos = (int) -ypos;
            tryy = screenyloc+50+((int)ypos*2);	
            prevY = (int) ypos;
        }
        
        robot.mouseMove(tryx, tryy);
        
        for(com.leapmotion.leap.Gesture g : frame.gestures()) {
        	if(g.type() == com.leapmotion.leap.Gesture.Type.TYPE_SCREEN_TAP || g.type() == com.leapmotion.leap.Gesture.Type.TYPE_KEY_TAP) {
        		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        		System.out.println("Clicked");
        	}
        }		
	}
	
	public void updatescreenlocation(int x, int y) {
		screenxloc = x;
		screenyloc = y;
	}
}
