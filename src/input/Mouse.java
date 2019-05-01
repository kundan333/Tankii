package input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class Mouse extends GLFWMouseButtonCallback{
	public static boolean[] button = new boolean[65536];

	@Override
	public void invoke(long window, int mouse_button, int button_action, int modkey) {
		// TODO Auto-generated method stub
		button[mouse_button]  =  button_action !=GLFW.GLFW_RELEASE;
		checkWasFirst(mouse_button,button_action);
		
		
	}
	public static boolean isButtonDown(int buttons) {
		return button[buttons];
	}
	
	private void checkWasFirst(int mouse_button,int button_action){
		if(mouse_button==GLFW.GLFW_MOUSE_BUTTON_LEFT &&button_action==GLFW.GLFW_RELEASE) {
			MousePosition.wasFirst = false;
		}
		if(mouse_button==GLFW.GLFW_MOUSE_BUTTON_LEFT &&button_action==GLFW.GLFW_PRESS) {
			MousePosition.wasFirst = true;
		}
		if(mouse_button==GLFW.GLFW_MOUSE_BUTTON_RIGHT &&button_action==GLFW.GLFW_RELEASE) {
			MousePosition.wasFirst = false;		
		}
		if(mouse_button==GLFW.GLFW_MOUSE_BUTTON_RIGHT &&button_action==GLFW.GLFW_PRESS) {
			MousePosition.wasFirst = true;
		}
	}
	

}
