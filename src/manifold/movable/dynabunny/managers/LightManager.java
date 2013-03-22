package manifold.movable.dynabunny.managers;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import manifold.movable.dynabunny.actors.Light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.BufferUtils;

public class LightManager {
	private Light[] lights;
	private int lightsAmount;
	
	public LightManager(int lightsAmount, PerspectiveCamera cam){
		this.lightsAmount = lightsAmount;
		lights = new Light[lightsAmount];
		for(int i=0; i<lightsAmount; ++i){
			lights[i] = new Light(new float[]{0,0,-1}, cam);
		}
	}
	
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
	
	public void setupLight(int lightIndex, float[] dir, float color[]){
		lights[lightIndex].setDirection(dir[0], dir[1], dir[2]);
		lights[lightIndex].setAmbientColor(color[0]/3, color[1]/3, color[2]/3, color[3]/3);
		lights[lightIndex].setDiffuseColor(color[0]/2, color[1]/2, color[2]/2, color[3]/2);
		lights[lightIndex].setSpecularColor(color[0], color[1], color[2], color[3]);
	}
	
	public void setupLight(int lightIndex, Light light){
		lights[lightIndex] = light;
	}
	
	public void bind(PerspectiveCamera cam, ShaderProgram shader){
		shader.setUniformi(shader.getUniformLocation("u_numLights"), lightsAmount);
		shader.setUniformf(shader.getUniformLocation("u_camDirection"), cam.direction);
		int bufferLength = lightsAmount*16;
		
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
		
		fillAndPassBuffer(0, lightsAmount, location1, dirBuffer, 3);
		fillAndPassBuffer(1, lightsAmount, location2, ambBuffer, 4);
		fillAndPassBuffer(2, lightsAmount, location3, difBuffer, 4);
		fillAndPassBuffer(3, lightsAmount, location4, specBuffer, 4);

	}
	
	private void fillAndPassBuffer(int type, int lightsAmount, int location, FloatBuffer buffer, int vecType){
		for(int i=0; i<lightsAmount; ++i){
			switch(type){
				case 0 : buffer.put(lights[i].direction); break;
				case 1 : buffer.put(lights[i].ambientColor); break;
				case 2 : buffer.put(lights[i].diffuseColor); break;
				case 3 : buffer.put(lights[i].specColor); break;
			}
		}
		buffer.flip();
		GL20 gl = Gdx.graphics.getGL20();
		switch(vecType){
			case 3 : gl.glUniform3fv(location, buffer.capacity()/vecType, buffer); break;
			case 4 : gl.glUniform4fv(location, buffer.capacity()/vecType, buffer); break;
		}
		
	}
	
	public Light getLight(int index){
		return lights[index];
	}
	
}
