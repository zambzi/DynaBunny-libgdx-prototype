package manifold.movable.dynabunny.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class InputManager implements InputProcessor {
	private float touchStartX;
	private float touchStartY;
	public float dragX;
	public float dragY;

	public InputManager(){
		Gdx.input.setInputProcessor(this);
	}
	
	public void resetValues(){
		dragX=0;
		dragY=0;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int newParam) {
		touchStartX = x;
		touchStartY = y;
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		dragY += (x - touchStartX);
		dragX += (y - touchStartY);
		touchStartX = x;
		touchStartY = y;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
