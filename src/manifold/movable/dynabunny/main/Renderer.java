package manifold.movable.dynabunny.main;

import java.io.FileNotFoundException;

import manifold.movable.dynabunny.actors.Level;
import manifold.movable.dynabunny.ai.Ai;
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
 *
 * Main Render Class
 * 
 * @author zambzi
 *
 */

public class Renderer extends Game{
	private PawnManager pawnManager;
	private InputManager inputManager;
	private PerspectiveCamera cam;
	private ShaderRenderer shaderRenderer;
	private LightManager lights = null;
	private Ai ai;
	private Level level;
	
	
	@Override
	public void create() {
		createCamera();
		pawnManager = new PawnManager();
		ai = new Ai(pawnManager, level);
		lights = new LightManager(cam);
		level = new Level(pawnManager,lights,cam);
		shaderRenderer = new ShaderRenderer(lights, pawnManager, cam);
		inputManager = new InputManager();
		try {
			level.generateMap("test.map");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		ai.AiLoop();
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

	
}
