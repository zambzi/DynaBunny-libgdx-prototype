package manifold.movable.dynabunny.managers;

import java.io.FileNotFoundException;
import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g3d.loaders.md2.MD2Loader;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedModel;

//TODO: More optimal way to manage same models
public class ModelManager {
	private Hashtable<String, KeyframedModel> models;
	private Hashtable<String, Texture> textures;
	private MD2Loader loader = null;
	
	public ModelManager(){
		models = new Hashtable<String, KeyframedModel>();
		textures = new Hashtable<String, Texture>();
		loader = new MD2Loader();
	}
	
	public void addModel(String handle, float animationSpeed) throws FileNotFoundException{
		FileHandle file = Gdx.files.internal("3dModels/"+handle);
		if(!file.exists()) throw new FileNotFoundException("Model:"+file.path()+" not found");
		else models.put(handle,loader.load(file, animationSpeed));
	}
	
	public void addTexture(String handle) throws FileNotFoundException{
		FileHandle file = Gdx.files.internal("data/textures/"+handle);
		if(!file.exists()) throw new FileNotFoundException("Texture:"+file.path()+" not found");
		Texture texture = new Texture(file);
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Linear);//TODO: dodac mipmapy do shaderow i zmienic filtry
		textures.put(handle,texture);
	}
	
	public void destroyTexture(String handle){
		textures.remove(handle);
	}
	
	public void destroyModel(String handle){
		models.remove(handle);
	}
	
	public void destroyAll(){
		textures.clear();
		models.clear();
	}
	
	public Texture getTexture(String handle){
		return textures.get(handle);
	}
	
	public KeyframedModel getModel(String handle){
		return models.get(handle);
	}
	
	public boolean textureExists(String handle){
		return textures.containsKey(handle);
	}
	
}
