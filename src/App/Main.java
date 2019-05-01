package App;

import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import components.Position;
import display.Display;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import input.InputCallbacks;
import input.Mouse;
import input.MousePosition;
import input.MouseScroll;
import loader.Loader;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import render.MasterRenderer;
import textures.ModelTexture;

public class Main {
	private Display display;
	private List<Entity> entities;
	private Camera camera ;
	private MasterRenderer renderer;
	private List<Light> lights; 
	public void run() {
		
		init();
		loop();
		display.destroyDisplay();
		
	}
	
	private void init() {
		display = new Display();
		display.createDisplay();
		InputCallbacks.mouseButtonCallback(display.getWindow(), new Mouse());
		InputCallbacks.mousePositionCallback(display.getWindow(), new MousePosition());
		InputCallbacks.mouseScrollCallback(display.getWindow(),new MouseScroll());
		//glfwSetKeyCallback(window, keyCallback = new KeyboardHandler());
		
		entities = new ArrayList<Entity>();
		
		//---player--//
		Loader  loader = new Loader();
		ModelData playerData = OBJFileLoader.loadOBJ("lowpolychar");
		RawModel rawPlayer = loader.loadToVAO(playerData.getVertices(), playerData.getTextureCoords(), playerData.getNormals(), playerData.getIndices());	
		TexturedModel playerModel = new TexturedModel(rawPlayer,new ModelTexture(loader.loadTexture("lowpolycharuv")));
		Player player = new Player(playerModel,new Vector3f(0,0,0),0,0,0,2);
		
		//----//
		
		camera = new Camera(player);	
		renderer = new MasterRenderer(loader,camera);
		
		//---light---//
		lights = new ArrayList<Light>();
		Light light = new Light(new Vector3f(102500,150000,-102500),new Vector3f(1f,1f,1f));
		lights.add(light);
		//----------//
		
		entities.add(player);
		
		
	}
	
	private void loop() {
		GL.createCapabilities();
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
		
		while(!display.isWindowClosed()) {
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			//OpenGL stuff
			
			/*if(Mouse.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
				System.out.println("mouse test success");
			}*/
			//glfwSetWindowTitle(display.getWindow(),"DeltaTime "+Display.getDeltaTime());
			camera.move();
			
			renderer.renderAll(lights, camera, entities);
			
			display.updateFrameTime();
			display.swapWindowBuffers();
			display.pollEvents();
		
		}
		
		
	}
	
	
	
	public static void main(String args[]) {
		new Main().run();
		
	}
	
	
	

}
