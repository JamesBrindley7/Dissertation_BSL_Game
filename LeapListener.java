import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.leapmotion.leap.*;
public class LeapListener extends Listener {
	public void onConnect(Controller controller) {
        System.out.println("Leap Motion Gesture Connected");
    }
	public void saveframe(Frame frame) {
		byte[] serializedFrame = frame.serialize();
		Path filepath = Paths.get("signs/frame.data");
		try {
			Files.write(filepath, serializedFrame);
			System.out.println("Saved to a file");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
