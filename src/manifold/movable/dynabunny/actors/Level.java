package manifold.movable.dynabunny.actors;

import java.io.FileNotFoundException;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

import manifold.movable.dynabunny.managers.LightManager;
import manifold.movable.dynabunny.managers.PawnManager;

/**
 * 
 * Wczytywanie mapy z pliku. Załóżmy, że treść pliku wygląda tak:
 * 
 * ___________________
 * 00w000K00000
 * 00w000000000
 * 00wwww000000
 * 000000000000
 * 000000000000
 * 000000000000
 * 000000wwwwww
 * 000000000000
 * 00www0000000
 * 000000000000
 * 00000T000000
 * 
 * L1:1.0,1.0,1.0;1.0,1.0,1.0,1.0;1.0,1.0,1.0,1.0;1.0,1.0,1.0,1.0;
 * L2:1.0,1.0,1.0;1.0,1.0,1.0,1.0;1.0,1.0,1.0,1.0;1.0,1.0,1.0,1.0;
 * L3:0;
 *____________________
 *
 * gdzie: 
 * K - królik
 * T - cel
 * w - mur
 * (pozostałe elementy dojdą później)
 * L1,L2,L3 - światła. wartości to kolejno: koordynaty, diffuse, ambient, specular.
 * Załóżmy, że jak po numerze światła wystąpi tylko jedno 0 to światło jest pomijane.
 * 
 * klasa ma drania wczytać do jakiegoś kontenera
 * i udostępnić go dla innych klas.
 *
 */

public class Level {

	private PawnManager pawn;
	private LightManager lights;
	private PerspectiveCamera cam;
	//dodaj jakiś kontener do przechowywania mapy
	
	public Level(PawnManager pawn, LightManager lights, PerspectiveCamera cam){
		this.pawn = pawn;
		this.lights = lights;
		this.cam = cam;
	}
	
	/**
	 * Wczytaj mapę z pliku jako siatkę 20x12 pól
	 * (arbitralna wartość - rozmiary dostosuje się później)
	 * @param fileHandle - nazwa mapy (bez ścieżki)
	 * zakładamy, że ścieżka będzie w /assets/data/maps
	 * @throws FileNotFoundException 
	 */
	public void generateMap(String fileHandle) throws FileNotFoundException{
		
		//Przykłady użycia klasy PawnManager:
		pawn.addPawn("bunny", "bunny.md2", "bunny-warface.png", cam);
		//modyfikacje pawna:
		pawn.getPawn("bunny").setPosition(new Vector3(0,5,5));
		pawn.getPawn("bunny").orient(new Vector3(1,0,0), 30);
		pawn.getPawn("bunny").resize(0.5f);
		
		//Przykłady użycia LightManager
		//Pamiętaj, że mamy maksymalnie 3 światła
		float[] direction = new float[]{1,1,1};
		float[] ambient = new float[]{1,1,1,1};
		float[] diffuse = new float[]{1,1,1,1};
		float[] specular = new float[]{1,1,1,1};
		lights.setupLight(1,direction, ambient, diffuse, specular);
		lights.setupLight(2,direction, ambient, diffuse, specular);
		lights.nullifyLight(3);//zerujemy światło nr3
		
	}
	
	/**
	 * zwróć zawartość danego pola (mur, puste, teleport itd.)
	 * @param x
	 * @param y
	 * @return
	 */
	public char getNode(int x, int y){
		return 'a';
	}
	
}
