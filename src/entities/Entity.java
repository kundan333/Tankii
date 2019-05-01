package entities;

import java.util.HashMap;
import java.util.UUID;

import org.lwjgl.util.vector.Vector3f;

import components.Components;
import models.TexturedModel;

public abstract class Entity {
	
	private TexturedModel model;
	private Vector3f position;
	private float rotX,rotY,rotZ;
	private float scale;
	
	private int textureIndex = 0;

	
	protected UUID id;
	protected static HashMap<Class,HashMap<UUID,? extends Components>> components = new HashMap<>();
	
	protected Entity() {
		this.id = UUID.randomUUID();
		
	}
	
	
	
	
	/**
	 * @param model
	 * @param position
	 * @param rotX
	 * @param rotY
	 * @param rotZ
	 * @param scale
	 */
	protected Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.id = UUID.randomUUID();
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	public float getTextureXOffset() {
		
		int column = textureIndex % model.getTexture().getNumberOfRows();
		
		return (float)column /(float)model.getTexture().getNumberOfRows();
		
	}
	
	public float getTextureYOffset() {
			
			int row = textureIndex / model.getTexture().getNumberOfRows();
			
			return (float)row /(float)model.getTexture().getNumberOfRows();
			
		}
		
	
	protected void increasePosition(float dx ,float dy , float dz) {
		
		this.position.x+=dx;
		this.position.y+=dy;
		this.position.z+=dz;
		
		
	}
	protected void increaseRotation(float dx,float dy,float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz ;
	
	}
	
	


	@SuppressWarnings("unchecked")
	public static <T extends Components> void addComponents(UUID entityUUID,T component) {
		synchronized(components){
			HashMap<UUID,? extends Components> store = components.get(component);
			if(store==null) {
				store = new HashMap<UUID,T>();
				components.put(component.getClass(), store);
				
			}
			//typecasting and storing on store
			((HashMap<UUID,T>) store).put(entityUUID, component);
			
			
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Components> void addComponents(T component) {
		synchronized(components){ 
			HashMap<UUID,? extends Components> store = components.get(component);
			if(store==null) {
				store = new HashMap<UUID,T>();
				components.put(component.getClass(), store);
				
			}
			//typecasting and storing on store
			((HashMap<UUID,T>) store).put(this.id, component);
		
		}
		
	}
	
	
	public static <T> T getComponents(UUID entityUUID,Class<T> component) {

		HashMap<UUID,? extends Components> store = components.get(component);
		
		@SuppressWarnings("unchecked")
		T results = (T)store.get(entityUUID);
		if(results==null) {
			throw new IllegalArgumentException("Get Fail: "+entityUUID.toString() +" does not posses Component of class \n missing "+component);
		}
		return results;
	}
	
	public <T> T getComponents(Class<T> component) {

		HashMap<UUID,? extends Components> store = components.get(component);
		
		@SuppressWarnings("unchecked")
		T results = (T)store.get(id);
		if(results==null) {
			throw new IllegalArgumentException("Get Fail: "+id.toString() +" does not posses Component of class \n missing "+component);
		}
		return results;
	}
	
	public static <T> boolean hasComponents(UUID entityUUID,Class<T> component) {

		HashMap<UUID,? extends Components> store = components.get(component);
		
		@SuppressWarnings("unchecked")
		T results = (T)store.get(entityUUID);
		if(results==null) {
			return false;
		}
		return true;
	}
	
	public <T> boolean hasComponents(Class<T> component) {

		HashMap<UUID,? extends Components> store = components.get(component);
		
		/*T results =null;
		try {	
		results = (T)store.get(id);
		}catch(Exception e) {
			e.printStackTrace();		}
		if(results==null) {
			return false;
		}*/
		if(store==null) {
			return false;
		}

		return true;
	}




	public TexturedModel getModel() {
		return model;
	}




	public void setModel(TexturedModel model) {
		this.model = model;
	}




	public Vector3f getPosition() {
		return position;
	}




	public void setPosition(Vector3f position) {
		this.position = position;
	}




	public float getRotX() {
		return rotX;
	}




	public float getRotY() {
		return rotY;
	}




	public float getRotZ() {
		return rotZ;
	}




	public float getScale() {
		return scale;
	}




	public int getTextureIndex() {
		return textureIndex;
	}
	
	
	
	
	
	

}
