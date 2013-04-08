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
	
	private FrameBuffer shadowMapBuffer;
	
	GL20 gl = Gdx.graphics.getGL20();
	
	
	public ShaderRenderer(LightManager lights, PawnManager pawns, PerspectiveCamera cam){
		this.lights = lights;
		this.pawns = pawns;
		this.cam = cam;
		shaders = new Shaders();
		buildShaders();
		shadowMapBuffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		lights.setShadowBuffer(shadowMapBuffer, cam);
	}
	
	public void render(){		
		gl.glDepthFunc(GL20.GL_LEQUAL);
		gl.glDepthRangef(0.0f, 1.0f);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
			| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
		gl.glEnable(GL20.GL_CULL_FACE);
		gl.glCullFace(GL20.GL_BACK);
		gl.glEnable(GL20.GL_DEPTH_TEST);
		gl.glFrontFace(GL20.GL_CW);
		
		
		generateShadowMap();
		
		gl.glClearColor(.8f,.8f,.8f,1);

		draw();
		//drawShadowMap(); //uncomment to see light point of view, and depthBuffer at work

	}
	
	
	private void generateShadowMap(){
		shadowMapBuffer.begin();
			gl.glClearColor(1, 1, 1, 1);
			gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
			gl.glClearColor(0, 0, 0, 0);
			shadowGenSP.begin();
				pawns.batchDraw(cam, shadowGenSP, lights, true);
			shadowGenSP.end();
		shadowMapBuffer.end();
	}
	
	/**
	 * only for testing - draws point of view from light camera
	 */
	private void drawShadowMap(){
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		gl.glClearColor(0, 0, 0, 0);
		shadowGenSP.begin();
			pawns.batchDraw(cam, shadowGenSP, lights, true);
		shadowGenSP.end();
	}
	
	private void draw(){
		shadowMapSP.begin();
			shadowMapBuffer.getColorBufferTexture().bind(5);//TODO: this may fuck up texture bindings
			shadowMapSP.setUniformi("s_shadowMap",5);
			lights.bind(cam, shadowMapSP);
			pawns.batchDraw(cam, shadowMapSP, lights, false);
		shadowMapSP.end();
	}
	
	public void buildShaders(){
		shadowGenSP = new ShaderProgram(shaders.vShadowGen, shaders.fShadowGen);
		shadowMapSP = new ShaderProgram(shaders.vShadowMap, shaders.fShadowMap);
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
		return shadowMapBuffer;
	}
}
