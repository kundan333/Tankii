package input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MousePosition extends GLFWCursorPosCallback{

	
	    private double mouseX;
	    private double mouseY;
	    private static double mouseDX;
	    private static double mouseDY;
	    public  static boolean wasFirst;
	    
	    
	    public static void init() {
	    	wasFirst=false;
	    }
	
		
		@Override
		public void invoke(long window, double xpos, double ypos) {
			// TODO Auto-generated method stub
			
			if (!wasFirst) {
	            mouseDX=0;
	            mouseDY=0;		
	        } else {
	            mouseDX += xpos - mouseX;
	            mouseDY += ypos - mouseY;
	        }
	        mouseX = xpos;
	        mouseY = ypos;
			
			
		}
		
		public static double getMouseDX() {
	        double result = mouseDX;
	        mouseDX = 0;
	        return result;
	    }
	 
	    public static double getMouseDY() {
	        double result = mouseDY;
	        mouseDY = 0;
	        return result;
	    }


		public double getMouseX() {
			return mouseX;
		}


		public double getMouseY() {
			return mouseY;
		}
	    
	    

}
