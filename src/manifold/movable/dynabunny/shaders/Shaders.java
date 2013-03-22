package manifold.movable.dynabunny.shaders;

import com.badlogic.gdx.Gdx;

public final class Shaders {
	public String vertexShader;
	public String fragmentShader;

	public Shaders(){}
	
	public void loadShaders(String vertex, String fragment){
		vertexShader = Gdx.files.internal("data/shaders/"+vertex).readString();
		fragmentShader = Gdx.files.internal("data/shaders/"+fragment).readString();
	}
}
