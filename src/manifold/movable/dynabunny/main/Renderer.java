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
	private ShaderProgram standardShader;
	private LightManager lights = null;
	private float depthRangeMin = 0.0f;
	private float depthRangeMax = 2.0f;
	
	
	@Override
	public void create() {
		createCamera();
		Shaders shaders = new Shaders();
		shaders.loadShaders("v_standard.glsl", "f_standard.glsl");
		createShader(shaders.vertexShader, shaders.fragmentShader);
		pawnManager = new PawnManager();
		createPawns();
		setLights(new LightManager(1,cam));
		lights.getLight(0).setDirection(1,1,-1);
		inputManager = new InputManager();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		setGLStuff();
		
		//following is just screwing up with camera
		cam.rotateAround(new Vector3(0,0,0), new Vector3(0,1,0), -inputManager.dragY);
		//Camera zoom-on-target functionality:
		Vector3 vec = cam.position.cpy();
		vec.nor();
		vec.mul((float)inputManager.dragX/2);
		cam.translate(vec);
		
		cam.update();
		
		standardShader.begin();
		lights.bind(cam, standardShader);
		//TODO: create batch draw for PawnManager with exclusion system
		pawnManager.getPawn("bunny").draw(cam, standardShader);
		pawnManager.getPawn("bunny1").draw(cam, standardShader);
		pawnManager.getPawn("bunny2").draw(cam, standardShader);
		pawnManager.getPawn("bunny3").draw(cam, standardShader);
		pawnManager.getPawn("bunny4").draw(cam, standardShader);
		pawnManager.getPawn("bunny5").draw(cam, standardShader);
		pawnManager.getPawn("bunny6").draw(cam, standardShader);
		standardShader.end();
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
		standardShader.dispose();
		pawnManager.destroyAll();
	}
	
	private void createCamera(){
		cam = new PerspectiveCamera(90f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(40,40,40);
		cam.direction.set(-1,-1,-1);
	}
	
	private void createShader(String vertex, String fragment){
		standardShader = new ShaderProgram(vertex, fragment);
		if (!standardShader.isCompiled())
			throw new IllegalStateException(standardShader.getLog());
	}
	
	/**
	 * use to add new light scheme to renderer
	 * @param lights - LightArray object
	 */
	public void setLights(LightManager lights){
		this.lights = lights;
	}	
	
	private void createPawns(){
		try {
			pawnManager.addPawn("bunny", "bunny2-small.md2", "bunny-warface.png", cam);
			pawnManager.addPawn("bunny1", "bunny2-small.md2", "bunny-warface.png", cam);
			pawnManager.addPawn("bunny2", "bunny2-small.md2", "bunny.png", cam);
			pawnManager.addPawn("bunny3", "bunny2-small.md2", "bunny.png", cam);
			pawnManager.addPawn("bunny4", "bunny2-small.md2", "bunny.png", cam);
			pawnManager.addPawn("bunny5", "bunny2-small.md2", "bunny.png", cam);
			pawnManager.addPawn("bunny6", "test.md2", "bunny-warface.png", cam);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pawnManager.getPawn("bunny").resize(0.5f);
		pawnManager.getPawn("bunny1").resize(0.5f);
		pawnManager.getPawn("bunny2").resize(0.5f);
		pawnManager.getPawn("bunny3").resize(0.5f);
		pawnManager.getPawn("bunny4").resize(0.5f);
		pawnManager.getPawn("bunny5").resize(0.5f);
		pawnManager.getPawn("bunny6").resize(0.5f);
		pawnManager.getPawn("bunny1").setPosition(new Vector3(-25,0,25));
		pawnManager.getPawn("bunny2").setPosition(new Vector3(-25,0,15));
		pawnManager.getPawn("bunny3").setPosition(new Vector3(-25,0,5));
		pawnManager.getPawn("bunny4").setPosition(new Vector3(-25,0,-5));
		pawnManager.getPawn("bunny5").setPosition(new Vector3(-25,0,-15));
		pawnManager.getPawn("bunny6").setPosition(new Vector3(-25,0,-25));
		
		pawnManager.getPawn("bunny").setAmbientFactor(.1f,.1f,.1f,.1f);
		pawnManager.getPawn("bunny").setDiffuseFactor(.3f,.3f,.3f,.3f);
		pawnManager.getPawn("bunny").setSpecularFactor(1,1,1,1);
		pawnManager.getPawn("bunny").setShininess(25);
		
		pawnManager.getPawn("bunny").animate("walkCycle", true);
		pawnManager.getPawn("bunny1").animate("idle1", true);
		pawnManager.getPawn("bunny2").animate("idle2", true);
		pawnManager.getPawn("bunny3").animate("shake", true);
		pawnManager.getPawn("bunny4").animate("dodge", true);
		pawnManager.getPawn("bunny5").animate("idle1", true);
		pawnManager.getPawn("bunny6").animate("walkCycle", true);
	}
	
	private void setGLStuff(){
		GL20 gl = Gdx.graphics.getGL20();
		gl.glEnable(GL20.GL_DEPTH_TEST);
		gl.glClearDepthf(1.0f);
		gl.glDepthFunc(GL20.GL_LEQUAL);
		gl.glDepthMask(true);
		gl.glDepthRangef(depthRangeMin, depthRangeMax);
		gl.glClearColor(1,1,1,1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		gl.glFrontFace(GL20.GL_CW);
		gl.glEnable(GL20.GL_CULL_FACE);
		gl.glCullFace(GL20.GL_BACK);
	}

	
}
