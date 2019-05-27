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
import entities.Tree;
import input.InputCallbacks;
import input.KeyboardHandler;
import input.Mouse;
import input.MousePosition;
import input.MouseScroll;
import loader.Loader;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import render.MasterRenderer;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class Main {
	private Display display;
	private List<Entity> entities;
	private Camera camera ;
	private MasterRenderer renderer;
	private List<Light> lights; 
	Player player ;
	Terrain terrain;
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
		InputCallbacks.keyCallBack(display.getWindow(), new KeyboardHandler());
		MousePosition.init();
		
		
		entities = new ArrayList<Entity>();
		
		
		Loader  loader = new Loader();
		
		//---player--//
		ModelData playerData = OBJFileLoader.loadOBJ("lowpolychar");
		RawModel rawPlayer = loader.loadToVAO(playerData.getVertices(), playerData.getTextureCoords(), playerData.getNormals(), playerData.getIndices());	
		TexturedModel playerModel = new TexturedModel(rawPlayer,new ModelTexture(loader.loadTexture("lowpolycharuv")));
		player = new Player(playerModel,new Vector3f(0,0,0),0,0,0,2);
		//-----------//
		
		
		//---terrain---//
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass2"));
		TerrainTexture textureR = new TerrainTexture(loader.loadTexture("grass1"));
		TerrainTexture textureG = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture textureB = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture textureBlendMap = new TerrainTexture(loader.loadTexture("blend2"));
		TerrainTexturePack terrainTexture =new TerrainTexturePack(backgroundTexture,textureR,textureG,textureB);
		
		terrain = new Terrain(0,0,loader,terrainTexture,textureBlendMap);
		
		//-------------//
		
		
		camera = new Camera(player);	
		renderer = new MasterRenderer(loader,camera);
		
		//---light---//
		lights = new ArrayList<Light>();
		Light light = new Light(new Vector3f(102500,150000,-102500),new Vector3f(1f,1f,1f));
		Light light1 = new Light(new Vector3f(player.getPosition().x+20,player.getPosition().y+10,player.getPosition().z+20),new Vector3f(1f,0f,0f),new Vector3f(1f,0.001f,0.002f));
		
		
		
		lights.add(light1);
		lights.add(light);
		//----------//
		
		

		
		
		//----tree----//
		ModelData tree = OBJFileLoader.loadOBJ("tree");
		RawModel rawtree = loader.loadToVAO(tree.getVertices(), tree.getTextureCoords(), tree.getNormals(), tree.getIndices());	
		TexturedModel treeModel = new TexturedModel(rawtree,new ModelTexture(loader.loadTexture("tree")));
		Tree treeEntity = new Tree(treeModel, new Vector3f(10,terrain.getHeightOfTerrain(10, 10),10), 0f, 0f, 0f, 10f);
		
		//-----------//
		entities.add(treeEntity);
		entities.add(player);
		
		
	}
	
	private void loop() {
		GL.createCapabilities();
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
		
		while(!display.isWindowClosed()) {
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			//OpenGL stuff
			
			/*if(KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_W)) {
				System.out.println("key test success");
			}*/
			//glfwSetWindowTitle(display.getWindow(),"DeltaTime "+Display.getDeltaTime());
			camera.move();
			player.move(terrain);
			renderer.renderAll(lights, camera, entities,terrain);
			
			display.updateFrameTime();
			display.swapWindowBuffers();
			display.pollEvents();
		
		}
		
		
	}
	
	
	
	public static void main(String args[]) {
		new Main().run();
		
	}
	
	
	

}
