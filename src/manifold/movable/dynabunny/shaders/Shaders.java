package manifold.movable.dynabunny.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * 
 *
 * Shader Loader
 * 
 * @author zambzi
 *
 */
public final class Shaders {
	public String fShadowMap;
	public String vShadowMap;
	public String fShadowGen;
	public String vShadowGen;
	

	public Shaders(){
		fShadowMap = Gdx.files.internal("data/shaders/f_shadowMap.glsl").readString();
		vShadowMap = Gdx.files.internal("data/shaders/v_shadowMap.glsl").readString();
		fShadowGen = Gdx.files.internal("data/shaders/f_shadowGen.glsl").readString();
		vShadowGen = Gdx.files.internal("data/shaders/v_shadowGen.glsl").readString();
	}
	
}
