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
	private float depthRangeMin = 0.0f;
	private float depthRangeMax = 2.0f;
	
	
	@Override
	public void create() {
		createCamera();
		pawnManager = new PawnManager();
		createPawns();
		setLights(new LightManager(1,cam));
		lights.getLight(0).setDirection(0,0,-1);
		inputManager = new InputManager();
		shaderRenderer = new ShaderRenderer(lights, pawnManager, cam);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		GL20 gl = Gdx.graphics.getGL20();
		setGLStuff();
		//following is just screwing up with camera
		cam.rotateAround(new Vector3(0,0,0), new Vector3(0,1,0), -inputManager.dragY);
		//cam.rotateAround(new Vector3(0,0,0), new Vector3(1,0,0), -inputManager.dragX);
		//Camera zoom-on-target functionality:
		Vector3 vec = cam.position.cpy();
		vec.nor();
		vec.mul((float)inputManager.dragX/10);
		cam.translate(vec);
		lights.getLight(0).rotate(1, new Vector3(0,1,0));
		lights.getLight(0).rotate(1, new Vector3(1,0,0));
		
		cam.update();
		
		shaderRenderer.render();
		inputManager.resetValues();
	}
	
	private void setGLStuff(){
		GL20 gl = Gdx.graphics.getGL20();
		gl.glClearDepthf(1.0f);
		gl.glDepthFunc(GL20.GL_LEQUAL);
		gl.glDepthMask(true);
		gl.glDepthRangef(depthRangeMin, depthRangeMax);
		gl.glClearColor(.8f,.8f,.8f,1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		gl.glFrontFace(GL20.GL_CW);
		gl.glEnable(GL20.GL_CULL_FACE);
		gl.glCullFace(GL20.GL_BACK);
		//gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
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
		cam.position.set(40,40,40);
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
			pawnManager.addPawn("wall", "wall-4.md2", "wall.png", cam);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pawnManager.getPawn("bunny").resize(0.3f);
		pawnManager.getPawn("wall").setPosition(new Vector3(0,-20,0));
		pawnManager.getPawn("bunny").setPosition(new Vector3(0,0,5));
		pawnManager.getPawn("bunny").animate("walkCycle", true);
	}

	
}
