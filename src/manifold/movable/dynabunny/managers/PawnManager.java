package manifold.movable.dynabunny.managers;

import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Iterator;

import manifold.movable.dynabunny.actors.Pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * 
 * @author zambzi
 *
 * Creates, manages and destroys pawns
 *
 */

public class PawnManager {
	private Hashtable<String, Pawn> pawns;
	ModelManager manager;
	
	public PawnManager(){
		pawns = new Hashtable<String, Pawn>();
		manager = new ModelManager();
	}
	
	/**
	 * Adds new pawn with texture
	 * @param key - key associated with the pawn
	 * @param model - filename of the model in assets/3dModels
	 * @param texture - filename of the texture in assets/data/textures
	 * @throws FileNotFoundException if either texture or model where not found.
	 */
	public void addPawn(String key, String model, String texture, PerspectiveCamera cam) throws FileNotFoundException{
		pawns.put(key, new Pawn(model,texture,cam.combined, manager));
	}
	
	/**
	 * Adds just a texture
	 * @param handle
	 * @throws FileNotFoundException
	 */
	public void addTexture(String handle) throws FileNotFoundException{
		manager.addTexture(handle);
	}
	
	

	public void batchDraw(PerspectiveCamera cam, ShaderProgram shader, LightManager lights, boolean genShadows){
		Iterator<Pawn> itr = pawns.values().iterator();
		while(itr.hasNext()){
			itr.next().draw(cam, shader, lights, genShadows);
		}
	}
	
	/**
	 * Retextures model
	 * @param model - given model from PawnManager
	 * @param texture - new texture from ModelManager
	 * WORK IN PROGRESS
	 */
	public void setTexture(String model, String texture){
		//TODO: add retexturing to Pawn
	}
	
	public ModelManager getModelManager(){
		return manager;
	}
	
	public void destroyPawn(String handle){
		pawns.get(handle).dispose();
	}
	
	public void  destroyTexture(String handle){
		manager.destroyTexture(handle);
	}
	
	public void destroyAll(){
		pawns.clear();
		manager.destroyAll();
	}
	
	public Pawn getPawn(String key){
		return pawns.get(key);
	}
	
	public void animatePawns(){
		Iterator<Pawn> itr = pawns.values().iterator();
		while(itr.hasNext()){
			Pawn pawn = itr.next();
			if(pawn.animationName!="still") pawn.animation();
		}
	}
}
