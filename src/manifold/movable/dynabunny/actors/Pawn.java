package manifold.movable.dynabunny.actors;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import manifold.movable.dynabunny.managers.LightManager;
import manifold.movable.dynabunny.managers.ModelManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.loaders.md2.MD2Loader;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.SubMesh;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedModel;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;


/**
 * 
 * @author zambzi
 *
 * Every map object gets its origins from here.
 * Basically its abridged and less poetic version of book of genesis...
 * Use only with PawnManager!
 *
 */

public class Pawn{
	
		private ModelManager manager;
        private String texture;
    	private String model;
    	private float animTime = 0;
    	private Material material;
    	private Matrix4 transform = new Matrix4(); //for mesh
    	private Matrix4 transProjection = new Matrix4(); //for light effects
    	private List<Matrix4> lightView;
    	private Vector3 position = new Vector3(0,0,0);
    	private float angle = 0;
    	private Vector3 axis = new Vector3(0,0,0);
    	private Vector3 scale = new Vector3(1,1,1);
    	public String animationName = "still";
    	private boolean animLoop = false;
    	
    	//material values
    	private float[] ambientFactor = new float[]{0.5f,0.5f,0.5f,1f};
    	private float[] diffuseFactor = new float[]{0.7f,0.7f,0.7f,1f};
    	private float[] specularFactor = new float[]{0.9f,0.9f,0.9f,1f};
    	private float shininess = 0f;
    	
        
        public Pawn(String modelHandle, String textureHandle, Matrix4 combinedMatrix, ModelManager manager) throws FileNotFoundException{
        	this.manager = manager;
        	this.texture = textureHandle;
        	this.model = modelHandle;
			createModel();
        	createMaterial();
        	transform.set(combinedMatrix);
        	lightView = new ArrayList<Matrix4>();
        }
        
        private void createModel() throws FileNotFoundException{
        	if(manager.getModel(model)==null) manager.addModel(model, 0.1f);
        	if(manager.getTexture(texture)==null) manager.addTexture(texture);
        }
        
        private void createMaterial(){
        	material = new Material("u_material", 
        			new TextureAttribute(manager.getTexture(texture), 0, "u_texture"));
    		manager.getModel(model).setMaterial(material);
        }
        //TODO: Not sure, but this may be a little crunchy
        private void resetTexture(){
        	material.getAttribute(0).set(new TextureAttribute(manager.getTexture(texture), 0, "u_texture"));
        	manager.getModel(model).setMaterial(material);
        }
        
        
        public void draw(PerspectiveCamera cam, ShaderProgram meshShader, LightManager lights, boolean genShadows){
        	if(model == null)
        		throw new IllegalStateException("draw called before a mesh has been created");
        	
        	transformLights(lights.getLights());
        	transformMatrix(cam);
        	if(!genShadows){
        		
        		resetTexture();
            	setUniforms(meshShader);
            	material.bind(meshShader);
        	}
        	meshShader.setUniformMatrix("u_lightView", lightView.get(0), false);//TODO: change to multiple lights
        	
        	manager.getModel(model).render(meshShader);
        }
        
        private void setUniforms(ShaderProgram meshShader){
        	meshShader.setUniformf(	meshShader.getUniformLocation("u_material.ambientFactor"), 
        							ambientFactor[0],
        							ambientFactor[1],
        							ambientFactor[2],
        							ambientFactor[3]	);
    		meshShader.setUniformf(	meshShader.getUniformLocation("u_material.diffuseFactor"), 
    								diffuseFactor[0],
    								diffuseFactor[1],
    								diffuseFactor[2],
    								diffuseFactor[3]	);
    		meshShader.setUniformf(	meshShader.getUniformLocation("u_material.specularFactor"), 
    								specularFactor[0],
    								specularFactor[1],
    								specularFactor[2],
    								specularFactor[3]	);
    		meshShader.setUniformf(	meshShader.getUniformLocation("u_material.shininess"),
    								shininess	);
    		
    		meshShader.setUniformMatrix("u_ModelViewMatrix", transform, false);
    		meshShader.setUniformMatrix("u_ProjectionMatrix", transProjection, false);
        }
        

        
        public void animation(){
        	animTime += Gdx.graphics.getDeltaTime();
    		if (animTime >= manager.getModel(model).getAnimation(animationName).totalDuration) {
    			animTime = 0;
    		}
    		manager.getModel(model).setAnimation(animationName, animTime, animLoop);
        }
        
        /**
         * Sets animation for Pawn.
         * @param name - name of the animation
         * @param loop - specifies whether the animation should loop
         */
        public void animate(String name, boolean loop){
        	animationName = name;
        	animLoop = loop;
        }
        
        public void dispose(){
        	manager.getModel(model).dispose();
        	manager.destroyModel(model);
        }
        
        /**
         * Values are given in range 0..1
         * WARNING - alpha value affects material alpha, not color alpha!
         */
        public void setAmbientFactor(float r, float g, float b, float a){
        	ambientFactor[0] = r;
        	ambientFactor[1] = g;
        	ambientFactor[2] = b;
        	ambientFactor[3] = a;
        }
        
        /**
         * Values are given in range 0..1
         * WARNING - alpha value affects material alpha, not color alpha!
         */
        public void setDiffuseFactor(float r, float g, float b, float a){
        	diffuseFactor[0] = r;
        	diffuseFactor[1] = g;
        	diffuseFactor[2] = b;
        	diffuseFactor[3] = a;
        }
        
        /**
         * Values are given in range 0..1
         * WARNING - alpha value affects material alpha, not color alpha!
         */
        public void setSpecularFactor(float r, float g, float b, float a){
        	specularFactor[0] = r;
        	specularFactor[1] = g;
        	specularFactor[2] = b;
        	specularFactor[3] = a;
        }
        
        /**
         * Values are given in range 0..n
         * 0 - no shine
         * less than 5 - weird effects
         * around 10 - very subtle shine
         * 20-50 - metal like shine
         * 50-100 - shiny plastic
         */
        public void setShininess(float shine){
        	shininess = shine;
        }
        
        public KeyframedModel getModel(){
        	return manager.getModel(model);
        }
        
        public void setPosition(Vector3 position){
        	this.position = position;
        }
        
        
        private void transformMatrix(PerspectiveCamera cam){        	
        	transform.set(cam.combined);
        	transform.translate(position);
        	transform.rotate(axis, angle);
        	transform.scale(scale.x, scale.y, scale.z);
        	transProjection.set(cam.projection);
        	//transProjection.translate(position);
        	transProjection.rotate(axis.x, axis.y, axis.z, angle);
        	//transProjection.scale(scale.x, scale.y, scale.z);
        }
        
        private void transformLights(Light[] lights){
        	lightView.clear();
        	for(int i=0; i<lights.length; ++i){
        		lightView.add(new Matrix4(lights[i].lightView.combined));
        		lightView.get(i).translate(position);
        		lightView.get(i).scale(scale.x, scale.y, scale.z);
        		lightView.get(i).rotate(axis.x, axis.y, axis.z, angle);
        	}
        }
        
        /**
         * Rotates pawn in relation to current orientation
         * (ie. looped use will result in constant rotation)
         * @param axis rotation axis
         * @param angle rotation angle
         */
        public void rotate(Vector3 axis, float angle){
        	this.axis = axis;
        	this.angle += angle;
        	angle = (angle>360 ? angle-360 : angle);
        }
        
        /**
         * Orients pawn in specified direction in relation to world coordinates
         * @param axis rotation axis
         * @param angle rotation angle
         */
        public void orient(Vector3 axis, float angle){
        	this.axis = axis;
        	this.angle = angle;
        }
        
        /**
         * Resizes pawn by desired multiplier
         * @param size - size multiplier
         */
        public void resize(float size){
        	scale.mul(size);
        }
}
