package render;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import entities.Light;
import shaders.ShaderProgram;
import shaders.UniformBoolean;
import shaders.UniformFloat;
import shaders.UniformInt;
import shaders.UniformMatrix;
import shaders.UniformSampler;
import shaders.UniformVec2;
import shaders.UniformVec3;
import utils.MyFile;

public class EntityShader extends ShaderProgram{
	private static final int DIFFUSE_TEX_UNIT = 0;
	private static final int MAX_LIGHT = 4;	
	protected UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
	protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	protected UniformMatrix ViewMatrix = new UniformMatrix("viewMatrix");
	protected UniformInt noOfTextureRows  = new  UniformInt("numberOfRows");
	protected UniformFloat shineDumper = new UniformFloat("shineDamper");
	protected UniformFloat reflectivity = new UniformFloat("reflectivity");
	protected UniformBoolean useFakeLighting= new UniformBoolean("useFakeLighting");
	private   UniformSampler diffuseMap = new UniformSampler("modelTexture");
	protected UniformVec2 OffSet =new UniformVec2("OffSet");
	protected UniformVec3 skyColor = new UniformVec3("skyColor");
	
	private static final MyFile FRAGMENT_SHADER = new MyFile("render","EntityFragmentShader.glsl");
	private static final MyFile VERTEX_SHADER = new MyFile("render","EntityVertexShader.glsl");
	
	protected UniformVec3 light[]=new UniformVec3[4]; 
	protected UniformVec3 lightcolor[]=new UniformVec3[4]; 
	protected UniformVec3 attenuation[]=new UniformVec3[4]; 

	
	
	
	public EntityShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, "position","textureCoords","normals");
		// TODO Auto-generated constructor stub
		
		for(int i=0;i<MAX_LIGHT;i++) {
			light[i] = new UniformVec3("lightPosition["+i+"]");
			lightcolor[i] = new UniformVec3("lightColor["+i+"]");
			attenuation[i] = new UniformVec3("attenuation["+i+"]");
		}
		
		super.storeAllUniformLocations(light[0],light[1],light[2],light[3],
				lightcolor[0],lightcolor[1],lightcolor[2],lightcolor[3],
				attenuation[0],attenuation[1],attenuation[2],attenuation[3],
				projectionMatrix, diffuseMap,ViewMatrix ,shineDumper,reflectivity,
				useFakeLighting,
				transformationMatrix,noOfTextureRows,OffSet,skyColor);
		
		connectTextureUnits();
	}
	
	
	
	private void connectTextureUnits() {
		super.start();
		diffuseMap.loadTexUnit(DIFFUSE_TEX_UNIT);
		super.stop();
	}
	
	public void loadLight(List<Light> lights) {
		
		for(int i=0;i<MAX_LIGHT;i++) {
			//may be if lights are less then four
		if(i<lights.size()) {	
		
		light[i].loadVec3(lights.get(i).getPosition());;
		lightcolor[i].loadVec3(lights.get(i).getColor());	
		attenuation[i].loadVec3(lights.get(i).getAttenuation());
		
		}else {
			light[i].loadVec3(new Vector3f(0,0,0));;
			lightcolor[i].loadVec3( new Vector3f(0,0,0));	
			attenuation[i].loadVec3(new Vector3f(1,0,0));	
		}
		
		}
		
	}
	

}
