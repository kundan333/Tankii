package terrain;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Light;
import shaders.ShaderProgram;
import shaders.UniformFloat;
import shaders.UniformMatrix;
import shaders.UniformSampler;
import shaders.UniformVec2;
import shaders.UniformVec3;
import utils.MyFile;

public class TerrainShader extends ShaderProgram {

	private static final int MAX_LIGHT = 4;	
	private static final MyFile FRAGMENT_SHADER = new MyFile("terrain","TerrainFragmentShader.glsl");
	private static final MyFile VERTEX_SHADER = new MyFile("terrain","TerrainVertexShader.glsl");

	protected UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
	protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	public UniformMatrix ViewMatrix = new UniformMatrix("viewMatrix");
	
	protected UniformSampler backgroundTexture = new UniformSampler("backgroundTexture");
	protected UniformSampler rTexture = new UniformSampler("rTexture");
	protected UniformSampler gTexture = new UniformSampler("gTexture");
	protected UniformSampler bTexture = new UniformSampler("bTexture");
	protected UniformSampler blendMap = new UniformSampler("blendMap");
	
	protected UniformFloat shineDumper = new UniformFloat("shineDamper");
	protected UniformFloat reflectivity = new UniformFloat("reflectivity");
	
	public UniformVec3 skyColor = new UniformVec3("skyColor");
	protected UniformVec3 light[]=new UniformVec3[4]; 
	protected UniformVec3 lightcolor[]=new UniformVec3[4]; 
	protected UniformVec3 attenuation[]=new UniformVec3[4]; 


	
	public TerrainShader() {
		
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
				transformationMatrix,projectionMatrix,ViewMatrix,backgroundTexture,rTexture,gTexture
				,bTexture,blendMap,shineDumper,reflectivity,skyColor

				
				
				);
		
		connectTextureUnits();
		
	
	}
	
	
	public void connectTextureUnits() {
		super.start();
		backgroundTexture.loadTexUnit(0);
		rTexture.loadTexUnit(1);
		gTexture.loadTexUnit(2);
		bTexture.loadTexUnit(3);
		blendMap.loadTexUnit(4);
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
