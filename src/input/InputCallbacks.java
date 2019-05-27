package input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class InputCallbacks {
	
	
	
	public static void mousePositionCallback(long window,GLFWCursorPosCallback glfwCursorPosCallback) {
		GLFW.glfwSetCursorPosCallback(window, glfwCursorPosCallback);	
	}
	public static void mouseButtonCallback(long window,GLFWMouseButtonCallback mouseCallback) {
		GLFW.glfwSetMouseButtonCallback(window, mouseCallback);	
	}
	public static void mouseScrollCallback(long window,GLFWScrollCallback mouseScroll) {
		GLFW.glfwSetScrollCallback(window,mouseScroll);	
	}
	
	public static void keyCallBack(long window,GLFWKeyCallback keyCallBack) {
		GLFW.glfwSetKeyCallback(window, keyCallBack);
	}
	

}
