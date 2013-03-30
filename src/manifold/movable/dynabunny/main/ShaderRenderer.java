package manifold.movable.dynabunny.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import manifold.movable.dynabunny.managers.LightManager;
import manifold.movable.dynabunny.managers.PawnManager;
import manifold.movable.dynabunny.shaders.Shaders;
/**
 * Added to make Renderer Class more readable
 * @author zambzi
 *
 */
public class ShaderRenderer {
	private LightManager lights;
	private PawnManager pawns;
	private PerspectiveCamera cam;
	private Shaders shaders;
	
	private ShaderProgram phongBlinnSP;
	private ShaderProgram shadowGenSP;
	private ShaderProgram shadowMapSP;
	
	private FrameBuffer shadowBuffer;
	
	
	public ShaderRenderer(LightManager lights, PawnManager pawns, PerspectiveCamera cam){
		this.lights = lights;
		this.pawns = pawns;
		this.cam = cam;
		buildShaders();
		shadowBuffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		lights.setShadowBuffer(shadowBuffer, cam);
	}
	
	public void render(){
		GL20 gl = Gdx.graphics.getGL20();
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL20.GL_DEPTH_TEST);
		
		
		//phongBlinnSP.begin();
	//	lights.bind(cam, phongBlinnSP);
		//pawns.batchDraw(cam, phongBlinnSP);
		//phongBlinnSP.end();
		
		gl.glCullFace(GL20.GL_FRONT);
		
		shadowBuffer.begin();
		shadowGenSP.begin();
		pawns.batchShadows(cam, shadowGenSP, lights, true);
		shadowGenSP.end();
		shadowBuffer.end();
		
		//gl.glDisable(GL20.GL_CULL_FACE);
		gl.glCullFace(GL20.GL_BACK);
		
		shadowMapSP.begin();
		shadowBuffer.getColorBufferTexture().bind(5);
		shadowMapSP.setUniformi("s_shadowMap",5);
		pawns.batchShadows(cam, shadowMapSP, lights, false);
		shadowMapSP.end();
		
	}
	
	private void buildShaders(){
		shaders = new Shaders();
		phongBlinnSP = new ShaderProgram(shaders.vPhongBlinn, shaders.fPhongBlinn);
		shadowGenSP = new ShaderProgram(shaders.vShadowGen, shaders.fShadowGen);
		shadowMapSP = new ShaderProgram(shaders.vShadowMap, shaders.fShadowMap);
		if (!phongBlinnSP.isCompiled())
			throw new IllegalStateException(phongBlinnSP.getLog());
		if (!shadowMapSP.isCompiled())
			throw new IllegalStateException(shadowMapSP.getLog());
		if (!shadowGenSP.isCompiled())
			throw new IllegalStateException(shadowGenSP.getLog());
	}
	
	public void dispose(){
		phongBlinnSP.dispose();
		shadowGenSP.dispose();
		shadowMapSP.dispose();
	}
	
	public FrameBuffer getShadowBuffer(){
		return shadowBuffer;
	}
}
