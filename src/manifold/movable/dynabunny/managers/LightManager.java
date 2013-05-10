package manifold.movable.dynabunny.managers;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import manifold.movable.dynabunny.actors.Light;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.BufferUtils;

/**
 * 
 *
 * Creates and manages Lights
 * also passes uniforms to fragment shader
 * 
 *  @author zambzi
 *
 */

public class LightManager {
	private Light[] lights;
	private final int MAX_LIGHTS = 3;
	private FrameBuffer shadowBuffer;
	
	public LightManager(PerspectiveCamera cam){
		lights = new Light[MAX_LIGHTS];
		
	}
	
	/**
	 * set light parameters
	 * @param lightIndex - light number from 0 to 3
	 * @param dir - light direction
	 * @param ambCol - ambient color
	 * @param diffCol - diffuse color
	 * @param specCol - specular color
	 */
	public void setupLight(	int lightIndex, 
								float[] dir, 
								float[] ambCol,
								float[] diffCol, 
								float[] specCol){
		lights[lightIndex].setDirection(dir[0], dir[1], dir[2]);
		lights[lightIndex].setAmbientColor(ambCol[0], ambCol[1], ambCol[2], ambCol[3]);
		lights[lightIndex].setDiffuseColor(diffCol[0], diffCol[1], diffCol[2], diffCol[3]);
		lights[lightIndex].setSpecularColor(specCol[0], specCol[1], specCol[2], specCol[3]);
	}
	
	/**
	 * set light parameters
	 * ambient, diffuse and specular factors are derived from color parameter
	 * (ambient = color/3; specular = color; diffuse = color/2) 
	 * @param lightIndex - light number from 0 to 3
	 * @param dir - light direction
	 * @param color - light color
	 */
	public void setupLight(int lightIndex, float[] dir, float color[]){
		lights[lightIndex].setDirection(dir[0], dir[1], dir[2]);
		lights[lightIndex].setAmbientColor(color[0]/3, color[1]/3, color[2]/3, color[3]/3);
		lights[lightIndex].setDiffuseColor(color[0]/2, color[1]/2, color[2]/2, color[3]/2);
		lights[lightIndex].setSpecularColor(color[0], color[1], color[2], color[3]);
	}
	
	public void setupLight(int lightIndex, float color[]){
		lights[lightIndex].setAmbientColor(color[0]/2f, color[1]/2f, color[2]/2f, color[3]/2f);
		lights[lightIndex].setDiffuseColor(color[0]/1.5f, color[1]/1.5f, color[2]/1.5f, color[3]/1.5f);
		lights[lightIndex].setSpecularColor(color[0], color[1], color[2], color[3]);
	}
	
	/**
	 * set light parameters via new light object
	 * @param lightIndex - light number from 0 to 3
	 * @param light - light object
	 */
	public void setupLight(int lightIndex, Light light){
		lights[lightIndex] = light;
	}
	
	/**
	 * Quickly erase light values
	 * @param lightIndex
	 */
	public void nullifyLight(int lightIndex){
		lights[lightIndex] = new Light(new float[]{0,0,0},new float[]{0,0,0,0},new float[]{0,0,0,0},new float[]{0,0,0,0});
	}
	
	
	public void bind(PerspectiveCamera cam, ShaderProgram shader){
		shader.setUniformf(shader.getUniformLocation("u_camDirection"), cam.direction);
		int bufferLength = MAX_LIGHTS*4;
		
		ByteBuffer dirByte = BufferUtils.newByteBuffer(bufferLength*4);
		ByteBuffer ambByte = BufferUtils.newByteBuffer(bufferLength*4);
		ByteBuffer difByte = BufferUtils.newByteBuffer(bufferLength*4);
		ByteBuffer specByte = BufferUtils.newByteBuffer(bufferLength*4);
		FloatBuffer dirBuffer = dirByte.asFloatBuffer();
		FloatBuffer ambBuffer = ambByte.asFloatBuffer();
		FloatBuffer difBuffer = difByte.asFloatBuffer();
		FloatBuffer specBuffer = specByte.asFloatBuffer();
		
		int location1 = shader.getUniformLocation("direction[0]");
		int location2 = shader.getUniformLocation("ambientColor[0]");
		int location3 = shader.getUniformLocation("diffuseColor[0]");
		int location4 = shader.getUniformLocation("specularColor[0]");
		
		fillAndPassBuffer(0, MAX_LIGHTS, location1, dirBuffer);
		fillAndPassBuffer(1, MAX_LIGHTS, location2, ambBuffer);
		fillAndPassBuffer(2, MAX_LIGHTS, location3, difBuffer);
		fillAndPassBuffer(3, MAX_LIGHTS, location4, specBuffer);
		dirBuffer.clear();
	}
	
	
	private void fillAndPassBuffer(int type, int lightsAmount, int location, FloatBuffer buffer){
		for(int i=0; i<lightsAmount; ++i){
			switch(type){
				case 0 : buffer.put(lights[i].direction); buffer.put(0.0f);break;
				case 1 : buffer.put(lights[i].ambientColor); break;
				case 2 : buffer.put(lights[i].diffuseColor); break;
				case 3 : buffer.put(lights[i].specColor); break;
			}
			
		}
		buffer.flip();
		GL20 gl = Gdx.graphics.getGL20();
		gl.glUniform4fv(location, buffer.capacity()/4, buffer);
	}
	
	public Light getLight(int index){
		return lights[index];
	}
	
	public Light[] getLights(){
		return lights;
	}
	
	public void setShadowBuffer(FrameBuffer shadowBuffer, PerspectiveCamera cam){
		this.shadowBuffer = shadowBuffer;
		for(int i=0; i<MAX_LIGHTS; ++i){
			lights[i] = new Light();
			setLightViews(lights[i]);
		}
	}
	
	private void setLightViews(Light light){
		//light.lightView = new OrthographicCamera(shadowBuffer.getWidth()/2, shadowBuffer.getHeight()/2);
		light.lightView = new OrthographicCamera(1000, 1000);
		light.lightView.zoom = 0.04f;
		light.setupLightView();
		
	}
	
}
