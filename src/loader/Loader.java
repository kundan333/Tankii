package loader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import models.RawModel;
import textures.TextureData;
import static org.lwjgl.stb.STBImage.*;

public class Loader {
	private List<Integer> vaos= new ArrayList<Integer>();
	private List<Integer> vbos= new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();

	
	public RawModel loadToVAO(float[] positions,float[] textureCoords,float[] normals,int[] indices ) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0,3,positions);
		storeDataInAttributeList(1,2,textureCoords);
		storeDataInAttributeList(2,3,normals);
		unbindVAO();
		//positions.length/3 for counting total number of vertices...
		return new RawModel(vaoID,indices.length);
				
	}
	

	
	
	
	public RawModel loadToVAO(float[] positions,float[] textureCoords,float[] normals,int[] indices ,float[] tengents ) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0,3,positions);
		storeDataInAttributeList(1,2,textureCoords);
		storeDataInAttributeList(2,3,normals);
		storeDataInAttributeList(3,3,tengents);
		unbindVAO();
		//positions.length/3 for counting total number of vertices...
		return new RawModel(vaoID,indices.length);
		
		
		
	}
	
	
	public RawModel loadToVAO(float[] positions,int[] indices ) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0,3,positions);
		unbindVAO();
		//positions.length/3 for counting total number of vertices...
		return new RawModel(vaoID,indices.length);
		
		
		
	}
	
	
	public RawModel loadToVAO(float[] positions,int dimensions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0,dimensions,positions);
		unbindVAO();
		//positions.length/3 for counting total number of vertices...
		return new RawModel(vaoID,positions.length / dimensions);
		
		
	}
	
	public int loadToVAO(float[] positions,float[] textureCoords) {
		int vaoID = createVAO();
		storeDataInAttributeList(0,2,positions);
		storeDataInAttributeList(1,2,textureCoords);
		unbindVAO();
		//positions.length/3 for counting total number of vertices...
		return vaoID;
		
	}
	public int createEmptyVbo(int floatCount) {
		int vbo = GL15.glGenBuffers();
		vbos.add(vbo);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER,floatCount *4, GL15.GL_STREAM_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vbo;
		
	}
	
	public void addInstancedAttribute(int vao,int vbo,int attribute,int dataSize,
			int instancedDataLength,int offset) {
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL30.glBindVertexArray(vao);
		GL20.glVertexAttribPointer(attribute, dataSize, GL11.GL_FLOAT,false,instancedDataLength*4 , offset*4);
		GL33.glVertexAttribDivisor(attribute, 1);//after one instance data will change
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	
	
	}
	
	public void updateVbo(int vbo,float[] data,FloatBuffer buffer) {
	
		buffer.clear();
		buffer.put(data);
		buffer.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.capacity()*4, GL15.GL_STREAM_DRAW);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
	}
	
	
	
	public int loadTexture(String filename) {
	//	Texture texture = null ;
		int textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,textureID);
		MemoryStack stack = MemoryStack.stackPush();
		IntBuffer w = stack.mallocInt(1);
		IntBuffer h = stack.mallocInt(1);
		IntBuffer comp = stack.mallocInt(1);
		stbi_set_flip_vertically_on_load(true);
		ByteBuffer image = stbi_load("res/"+filename+".png", w, h, comp, 4);
		if (image == null) {
		    throw new RuntimeException("Failed to load a texture file!"
		            + System.lineSeparator() + stbi_failure_reason());
		}

		int width = w.get();
		int height = h.get();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
		/* Used Slick utils for texture loading  */
		
		/*texture = TextureLoader.getTexture("PNG", new FileInputStream("res/"+filename+".png"));*/
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0);

		if(GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
			float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
		}
		else {
			System.out.println("Not Supported Anisotropic");
		}
		
//		int textureID = texture.getTextureID();
		textures.add(textureID);
		/*
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		   GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		   GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		   GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		*/
		return textureID;
	}
	
	/*public int loadTextureForFont(String filename) {
		Texture texture = null ;
		try {
			 Used Slick utils for texture loading  
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/"+filename+".png"));
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0);
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		    System.out.println("File not Found");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int textureID = texture.getTextureID();
		textures.add(textureID);
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		   GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		   GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		   GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		
		return textureID;
	}
	
	
	public int loadCubeMap(String[] textureFiles) {
		
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
		
		for (int i =0;i<textureFiles.length;i++) {
			TextureData data = decodeTextureFile("res/"+textureFiles[i]+".png");
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i , 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		
		}
		
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		
		textures.add(texID);
		
		return texID;
		
		
	}
	
	
	private TextureData decodeTextureFile(String fileName) {
		int width =0;
		int height = 0;
		
		ByteBuffer buffer = null;
		
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			
			buffer = ByteBuffer.allocateDirect(4*width *height);
			decoder.decode(buffer, width * 4 , Format.RGBA);
			buffer.flip();
			in.close();
			
			
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println("Failed loading "+ fileName +" Texture");
			System.exit(-1);
			
		}
		return new TextureData(width,height,buffer);
		
		
		
		
	}*/
	
	
	public void cleanUP() {
		for (int vao:vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo:vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		for (int texture:textures) {
			GL11.glDeleteTextures(texture);
		}
		
	}
	
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void storeDataInAttributeList(int attributeNumber,int coordinateSize,float[] data ) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer= storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0,0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);   //unbinding
		
	}
	private void unbindVAO() {
		GL30.glBindVertexArray(0);	
	}
	
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		//GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	private IntBuffer storeDataInIntBuffer(int[] data) {
	IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
	buffer.put(data);
	buffer.flip();
	return buffer;
	
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data ) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip(); // flip ---for reading buffer is finised...now we can read buffer
		return buffer;
		
		
	}
	
}
