package entities;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector3f;

import input.MousePosition;
import input.MouseScroll;

public class Camera {
	
	private static final String TAG="Camera Class";
	
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0 ;
	private static final float YOFFSET = 3;
	
	
	private Vector3f position =new Vector3f(0,0,0);
	private float pitch=10; //rotation in x,y,z
	private float yaw; //Camera aim left right
	private float roll; // roll 180 up side down
	
	private Player player;
	
	
	public Camera(Player player) {
		this.player = player;
		
		
	}
	
	
	public void move() {
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float varticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance,varticalDistance);
		this.yaw = 180 - (player.getRotY()+angleAroundPlayer);
		
		
	}
	
	
	
	public Vector3f getPosition() {
		return position;
	}
	public float getPitch() {
		return pitch;
	}
	
	public void invertPitch() {
		this.pitch = -pitch;
	}
	
	public float getYaw() {
		return yaw;
	}
	public float getRoll() {
		return roll;
	}

	

	private void calculateCameraPosition(float horizontalDistance,float varticalDistance) {
		float theta = player.getRotY() + angleAroundPlayer;
		
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		
		position.x = player.getPosition().x - offsetX;
		// minus offset because camera is in negative x,z direction
		position.z = player.getPosition().z - offsetZ;
		
		position.y = player.getPosition().y + varticalDistance + YOFFSET ;
		
		
	}
	
	
	private void calculateZoom() {
		float zoomLevel = input.MouseScroll.getWheelDY() * 1f;
		//System.out.println(TAG+"scroll dy "+MouseScroll.getWheelDY());
		distanceFromPlayer -= zoomLevel;
		
		
	}
	
	private void calculatePitch() {
		
		if(input.Mouse.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
			float pitchChange = (float)input.MousePosition.getMouseDY() * 0.1f;
			pitch -= pitchChange;
			
		}
	}
	
	
	private void calculateAngleAroundPlayer() {
		
		if(input.Mouse.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
			//MousePosition.wasFirst =  false;
			float angleChange = (float)input.MousePosition.getMouseDX() * 0.1f;
			angleAroundPlayer -= angleChange;
		//	System.out.println("angleAroundPlayer "+angleAroundPlayer);
			
		}else {	
			//MousePosition.wasFirst =false;
		}
		
	}
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}

	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	
	
	
	
	
	

}
