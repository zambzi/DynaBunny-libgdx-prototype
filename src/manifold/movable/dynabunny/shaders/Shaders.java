package manifold.movable.dynabunny.shaders;

import com.badlogic.gdx.Gdx;

/**
 * 
 * @author zambzi
 *
 * Shader Loader
 *
 */
public final class Shaders {
	public String fPhongBlinn;
	public String vPhongBlinn;
	public String fShadowMap;
	public String vShadowMap;
	public String fShadowGen;
	public String vShadowGen;
	

	public Shaders(){
		fPhongBlinn = Gdx.files.internal("data/shaders/f_phongBlinn.glsl").readString();
		vPhongBlinn = Gdx.files.internal("data/shaders/v_phongBlinn.glsl").readString();
		fShadowMap = Gdx.files.internal("data/shaders/f_shadowMap.glsl").readString();
		vShadowMap = Gdx.files.internal("data/shaders/v_shadowMap.glsl").readString();
		fShadowGen = Gdx.files.internal("data/shaders/f_shadowGen.glsl").readString();
		vShadowGen = Gdx.files.internal("data/shaders/v_shadowGen.glsl").readString();
	}
	
}
