package display;


import java.nio.IntBuffer;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Display {
	private long window;
	private static final int WIDTH =1280;
	private static final int HEIGHT =720;
	private static long lastFrameTime ;
	private static float deltaTime;
	
	

	/**
	 * 
	 *
	 */
	public Display() {
		
	}


	public void createDisplay() {
		// Setup an error callback. The default implementation
					// will print the error message in System.err.
					GLFWErrorCallback.createPrint(System.err).set();

					// Initialize GLFW. Most GLFW functions will not work before doing this.
					if ( !glfwInit() )
						throw new IllegalStateException("Unable to initialize GLFW");

					
					// Configure GLFW
					glfwDefaultWindowHints(); // optional, the current window hints are already the default
					glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
					glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 0);
					glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
					glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

					// Create the window
					window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
					
					if ( window == NULL )
						throw new RuntimeException("Failed to create the GLFW window");

					// Setup a key callback. It will be called every time a key is pressed, repeated or released.
					glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
						if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
							glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
					});

					// Get the thread stack and push a new frame
					try ( MemoryStack stack = stackPush() ) {
						IntBuffer pWidth = stack.mallocInt(1); // int*
						IntBuffer pHeight = stack.mallocInt(1); // int*

						// Get the window size passed to glfwCreateWindow
						glfwGetWindowSize(window, pWidth, pHeight);

						// Get the resolution of the primary monitor
						GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

						// Center the window
						glfwSetWindowPos(
							window,
							(vidmode.width() - pWidth.get(0)) / 2,
							(vidmode.height() - pHeight.get(0)) / 2
						);
					} // the stack frame is popped automatically

					// Make the OpenGL context current
					glfwMakeContextCurrent(window);
					// Enable v-sync
					glfwSwapInterval(1);

					// Make the window visible
					glfwShowWindow(window);
					GL.createCapabilities();
					lastFrameTime=getTime();
		
	}
	
	
	public void destroyDisplay() {
	
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
		
	}
	
	public boolean isWindowClosed() {
		return glfwWindowShouldClose(window);
	}
	
	public void swapWindowBuffers() {
		glfwSwapBuffers(window); // swap the color buffers
		
	}
	
	public void pollEvents() {
		
		glfwPollEvents();
	}
	public void setWindowTitle(String title) {
		glfwSetWindowTitle(window,title);
		
	}


	public long getWindow() {
		return window;
	}


	public static int getWidth() {
		return WIDTH;
	}
 

	public static int getHeight() {
		return HEIGHT;
	}
	
	public  void updateFrameTime() {
		long currentFrameTime = getTime();
		deltaTime = (currentFrameTime - lastFrameTime)/1000f;
		lastFrameTime = currentFrameTime;
	}
	
	public static long getTime() {
	    return System.nanoTime() / 1000000; //millis
	}


	public static float getDeltaTime() {
		return deltaTime;
	}
	
	
	
	
	
	
	
	
}
