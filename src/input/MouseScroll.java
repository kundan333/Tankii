package input;

import org.lwjgl.glfw.GLFWScrollCallback;

public class MouseScroll extends GLFWScrollCallback{

	private static float wheelDY;
	private static float wheelDX;
	
	@Override
	public void invoke(long window, double xoffset, double yoffset) {
		// TODO Auto-generated method stub
	
		wheelDX=(float)xoffset;
		wheelDY=(float)yoffset;
		
	}

	public static float getWheelDY() {
		float result =wheelDY;
		wheelDY=0;
		return result;
	}

	public static float getWheelDX() {
		
		float result =wheelDX;
		wheelDX=0;
		return result;
	}

	
	
	
	
	

}
