package input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class Mouse extends GLFWMouseButtonCallback{
	public static boolean[] button = new boolean[65536];

	@Override
	public void invoke(long window, int mouse_button, int button_action, int modkey) {
		// TODO Auto-generated method stub
		button[mouse_button]  =  button_action !=GLFW.GLFW_RELEASE;
		
	}
	public static boolean isButtonDown(int buttons) {
		return button[buttons];
	}

}
