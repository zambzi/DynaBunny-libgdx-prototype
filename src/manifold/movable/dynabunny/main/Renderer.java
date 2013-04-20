package manifold.movable.dynabunny.main;

import java.io.FileNotFoundException;

import manifold.movable.dynabunny.managers.InputManager;
import manifold.movable.dynabunny.managers.LightManager;
import manifold.movable.dynabunny.managers.PawnManager;
import manifold.movable.dynabunny.shaders.Shaders;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
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
		//ignore lights for now
		setLights(new LightManager(1,cam));
		shaderRenderer = new ShaderRenderer(lights, pawnManager, cam);	
		lights.setupLight(0, new float[]{-1.0f,-1.0f,-1.0f},new float[]{.5f,.5f,.5f,1.0f},new float[]{.9f,.9f,.9f,1.0f},new float[]{1.0f,1.0f,1.0f,1.0f});
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
		vec.mul((float)inputManager.dragX/10);
		cam.translate(vec);
		
		//lights.getLight(0).rotate(1, new Vector3(0,1,0));
		//lights.getLight(0).rotate(1, new Vector3(1,0,0));
		
		pawnManager.getPawn("bunny").rotate(new Vector3(0,1,0), 1);
		pawnManager.getPawn("bunny2").rotate(new Vector3(1,0,0), 1);
		pawnManager.getPawn("bunny3").rotate(new Vector3(0,0,1), 1);
		
		cam.update();
		pawnManager.animatePawns();
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
		cam.position.set(50,50,50);
		cam.direction.set(-1,-1,-1);
	}
	
	
	/**
	 * use to add new light scheme to renderer
	 * @param lights - LightArray object
	 */
	public void setLights(LightManager lights){
		this.lights = lights;
	}	
	
	//TODO: remove if level class is done
	private void createPawns(){
		try {
			pawnManager.addPawn("bunny", "bunny2-small.md2", "bunny-warface.png", cam);
			pawnManager.addPawn("bunny2", "bunny2-small.md2", "bunny.png", cam);
			pawnManager.addPawn("bunny3", "bunny2-small.md2", "bunny.png", cam);
			pawnManager.addPawn("wall", "wall-4.md2", "wall.png", cam);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//By default: x is bunny's front-back; y is bunny's up-down; z is bunny's right-left
		
		pawnManager.getPawn("wall").setAmbientFactor(.5f, .5f, .5f, 1f);
		pawnManager.getPawn("bunny").resize(0.3f);
		pawnManager.getPawn("bunny3").resize(0.3f);
		pawnManager.getPawn("wall").setPosition(new Vector3(0,-20,0));
		pawnManager.getPawn("bunny").setPosition(new Vector3(0,0,10));
		pawnManager.getPawn("bunny3").setPosition(new Vector3(0,0,30));
		//pawnManager.getPawn("bunny").animate("walkCycle", true);
		pawnManager.getPawn("bunny2").resize(0.5f);
		pawnManager.getPawn("bunny2").setPosition(new Vector3(0,0,-10));
		
		
	}

	
}
