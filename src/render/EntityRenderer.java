package render;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import textures.ModelTexture;

public class EntityRenderer {
	
	
	
	private EntityShader shader;
	
	
	public EntityRenderer(EntityShader shader , Matrix4f projectionMatrix) {
		this.shader= shader;
		
		shader.start();
		shader.projectionMatrix.loadMatrix(projectionMatrix);
		shader.stop();
		
	}
	

	
	
	public void render(Map<TexturedModel,List<Entity>> entities) {
		
		for(TexturedModel model:entities.keySet()) {
			prepareTextureModel(model);
			List <Entity> batch = entities.get(model);
			for (Entity entity:batch) {
				prepareInstances(entity);
				
				GL11.glDrawElements(GL11.GL_TRIANGLES,model.getRawModel().getVertexCount(),GL11.GL_UNSIGNED_INT, 0);
				
			}

			unbindTexturedModel();
			
			
		}
		
	}
	private void prepareTextureModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		ModelTexture texture = model.getTexture();
		
		shader.noOfTextureRows.loadInt(texture.getNumberOfRows()); 		
		//System.out.println(texture.getNumberOfRows());
		if(texture.isHasTransparency()) {
			
			MasterRenderer.disableCulling();			
			
		}
		
		
		
		shader.shineDumper.loadFloat(texture.getShineDamper());  
		shader.reflectivity.loadFloat(texture.getReflectivity());
		shader.useFakeLighting.loadBoolean(texture.isFakeLighting());
		
		
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());

		
		
		
		
	}
	
	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	
		
	}
	private void prepareInstances(Entity entity) {
		Matrix4f transformationMatrix = utils.Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.transformationMatrix.loadMatrix(transformationMatrix);
		shader.OffSet.loadVec2(entity.getTextureXOffset(), entity.getTextureYOffset()); 
		
		
	}
	
	
	
	
	
	
	
}
