package manifold.movable.dynabunny.main;

import java.io.FileNotFoundException;

import manifold.movable.dynabunny.managers.InputManager;
import manifold.movable.dynabunny.managers.LightManager;
import manifold.movable.dynabunny.managers.PawnManager;
import manifold.movable.dynabunny.shaders.Shaders;

import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedAnimation;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

/**
 * 
 * @author zambzi
 *
 * Main Render Class
 *
 */

public class Renderer extends Game{
	private PawnManager pawnManager;
	private InputManager inputManager;
	private PerspectiveCamera cam;
	private ShaderRenderer shaderRenderer;
	private LightManager lights = null;
	
	
	@Override
	public void create() {
		createCamera();
		pawnManager = new PawnManager();
		createPawns();
		lights = new LightManager(1,cam);
		shaderRenderer = new ShaderRenderer(lights, pawnManager, cam);
		setLights();
		inputManager = new InputManager();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		//following is just screwing up with camera
		cam.rotateAround(new Vector3(0,0,0), new Vector3(0,1,0), -inputManager.dragY);
		//cam.rotateAround(new Vector3(0,0,0), new Vector3(1,0,0), -inputManager.dragX);
		
		//Camera zoom-on-target functionality:
		Vector3 vec = cam.position.cpy();
		vec.nor();
		vec.mul((float)inputManager.dragX/50);
		cam.translate(vec);
		
		cam.update();
		shaderRenderer.render();
		inputManager.resetValues();
	}
	
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		shaderRenderer.dispose();
		pawnManager.destroyAll();
	}
	
	private void createCamera(){
		cam = new PerspectiveCamera(90f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(10,10,10);
		cam.direction.set(-1,-1,-1);
	}
	
	
	/**
	 * use to add new light scheme to renderer
	 * @param lights - LightArray object
	 */
	public void setLights(){
		lights.setupLight(0, new float[]{-1.0f,-1.0f,-1.0f},new float[]{.5f,.5f,.5f,1.0f},new float[]{.9f,.9f,.9f,1.0f},new float[]{1.0f,1.0f,1.0f,1.0f});
	}	
	
	//TODO: remove if level class is done
	private void createPawns(){
		try {
			pawnManager.addPawn("bunny1", "bunny.md2", "bunny-warface.png", cam);
			pawnManager.addPawn("bunny2", "bunny.md2", "bunny.png", cam);
			pawnManager.addPawn("bunny3", "bunny.md2", "bunny.png", cam);
			pawnManager.addPawn("wall", "wall-4.md2", "wall.png", cam);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//By default: x is bunny's front-back; y is bunny's up-down; z is bunny's right-left
		
		pawnManager.getPawn("wall").resize(8f);
		
		pawnManager.getPawn("wall").setPosition(new Vector3(0,-12.5f,0));
		pawnManager.getPawn("bunny1").setPosition(new Vector3(0,5,5));
		pawnManager.getPawn("bunny3").setPosition(new Vector3(5,5.5f,0));
		pawnManager.getPawn("bunny2").setPosition(new Vector3(0,5,-5));	
		
		pawnManager.getPawn("bunny2").animate("idle1", true);
		pawnManager.getPawn("bunny3").animate("shake", true);
		pawnManager.getPawn("bunny1").animate("walkCycle", true);
		
		//let's make bunny3 a little more colorful:
		pawnManager.getPawn("bunny3").setDiffuseFactor(0.5f, 0.8f, 0.5f, 1);
		pawnManager.getPawn("bunny3").setAmbientFactor(0.3f, 0.1f, 0.1f, 1);
			
	}

	
}
