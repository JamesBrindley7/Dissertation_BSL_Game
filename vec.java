import java.io.Serializable;

public class vec implements Serializable{
	float xpos;
	float ypos;
	float zpos;
	
	public vec(float x, float y, float z) {
		setY(y);
		setX(x);
		setZ(z);
	}
	public float getY() {
		return ypos;
	}
	public float getX() {
		return xpos;
	}
	public float getZ() {
		return zpos;
	}
	
	public void setY(float y) {
		ypos = y;
	}
	public void setX(float x) {
		xpos = x;
	}
	public void setZ(float z) {
		zpos = z;
	}
}
