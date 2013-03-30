package manifold.movable.dynabunny.actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * 
 * @author zambzi
 *
 * Simple class that holds single Light data
 * Use only with LightManager!
 *
 */

public class Light {
	public float[] direction = new float[3];
	public float[] ambientColor = new float[4];
	public float[] diffuseColor = new float[4];
	public float[] specColor = new float[4];
	public OrthographicCamera lightView1;
	public PerspectiveCamera lightView;
	/**
	 * Values are given in range: 0..1
	 * @param dir - light direction, float[3]
	 * @param ambCol - ambient color, float[4]
	 * @param diffCol - diffuse color, float[4]
	 * @param specCol - specular color, float[4]
	 * @param cam - camera object
	 */
	public Light(float[] dir, float[] ambCol, float[] diffCol, float[] specCol, PerspectiveCamera cam){
		direction = dir;
		ambientColor = ambCol;
		diffuseColor = diffCol;
		specColor = specCol;
	}
	
	public Light(float[] dir, PerspectiveCamera cam){
		direction = dir;
		ambientColor = new float[]{.5f,.5f,.5f,1.0f};
		diffuseColor = new float[]{.7f,.7f,.7f,1.0f};
		specColor = new float[]{1.0f,1.0f,1.0f,1.0f};
	}
	
	public void setAmbientColor(float r, float g, float b, float a){
		ambientColor = new float[]{r,g,b,a};
	}
	
	public void setDiffuseColor(float r, float g, float b, float a){
		diffuseColor = new float[]{r,g,b,a};
	}
	public void setSpecularColor(float r, float g, float b, float a){
		specColor = new float[]{r,g,b,a};
	}
	
	public void setDirection(float x, float y, float z){
		direction = new float[]{x,y,z};
	}
	
	public void rotate(float angle, Vector3 axis){
		Vector3 dir = new Vector3(direction[0], direction[1], direction[2]);
		dir.rotate(axis, angle);
		lightView.lookAt(dir.x, dir.y, dir.z);
		lightView.rotateAround(lightView.direction, axis, angle);
		direction[0] = dir.x;
		direction[1] = dir.y;
		direction[2] = dir.z;
		lightView.update();
	}
}
