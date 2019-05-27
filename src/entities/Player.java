package entities;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector3f;

import display.Display;
import input.KeyboardHandler;
import models.TexturedModel;
import terrain.Terrain;
import utils.Lerp;

public class Player extends Entity {
	

	private float RUN_SPEED_GOAL = 0;
	private static final float RUN_SPEED = 50;
	//private float TURN_SPEED_GOAL ;
	private static final float TURN_SPEED = 160;
	public  static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;
	//private static final float TERRAIN_HEIGHT =0;
	
	
	private float currentSpeed = 0;
	private float currentTurnSpeed =0; 
	
	private float upwardsSpeed = 0;   
	
	private boolean inAir = false;
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		// TODO Auto-generated constructor stub
	}
	
	public void move(Terrain terrain) {
		//System.out.println("called");
		checkInputs();
		
		currentSpeed = Lerp.interpolate(RUN_SPEED_GOAL, currentSpeed, Display.getDeltaTime()*120f);
		//currentTurnSpeed= Lerp.interpolate(TURN_SPEED_GOAL, currentTurnSpeed, DisplayManager.getFrameTimeSeconds()*80f);
		super.increaseRotation(0,currentTurnSpeed * Display.getDeltaTime()  , 0);
		float distance = currentSpeed * Display.getDeltaTime()  ;
		
		//Now we will find the dx and dz with sin and cos function for finding player translation   
	
		float dx = (float)(distance * Math.sin(Math.toRadians(super.getRotY()))) ;
		float dz = (float)(distance * Math.cos(Math.toRadians(super.getRotY()))) ;
		super.increasePosition(dx, 0, dz);
		
		upwardsSpeed += GRAVITY * Display.getDeltaTime();
		
		super.increasePosition(0,upwardsSpeed *Display.getDeltaTime() , 0);
		
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		
		if(super.getPosition().y < terrainHeight) {
			upwardsSpeed = 0;
			inAir = false;
			super.getPosition().y = terrainHeight;
		}
		
	}
	
	private void jump() {
		if(!inAir) {
		this.upwardsSpeed = JUMP_POWER;
		inAir = true;
		}
	}
	
	private void checkInputs() {
		
		
		if(KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_W)) {
			//System.out.println("w is down");
			//this.currentSpeed = RUN_SPEED;
			this.RUN_SPEED_GOAL = RUN_SPEED;
			
		}
		else if(KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_S)) {
			//this.currentSpeed = -RUN_SPEED;
			this.RUN_SPEED_GOAL =  -RUN_SPEED;
			
		}else {
			
			//this.currentSpeed = 0;
			this.RUN_SPEED_GOAL = 0;
		}
		
		if(KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_D)) {
			
			this.currentTurnSpeed = -TURN_SPEED;
			//this.TURN_SPEED_GOAL = -TURN_SPEED;
		}
		else if(KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_A)) {
			this.currentTurnSpeed = TURN_SPEED;
			//this.TURN_SPEED_GOAL = TURN_SPEED;
			
			
		}else {
			//this.TURN_SPEED_GOAL =0;
			this.currentTurnSpeed = 0;
		}
		
		if(KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
			jump();
		}
		
		
		
		
	}
	
	
	
	
	

}
