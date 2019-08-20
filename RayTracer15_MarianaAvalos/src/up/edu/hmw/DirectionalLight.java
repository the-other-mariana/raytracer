package up.edu.hmw;

import java.awt.Color;

public class DirectionalLight extends Light{
	private Vector3 direction;
	
	/**
	 * constructor
	 * @param pos
	 * @param dir
	 * @param color
	 * @param intensity
	 */
	public DirectionalLight(Vector3 pos, Vector3 dir, Color color, double intensity) {
		super(pos, dir, color, intensity);
		direction = dir;
	}
	
	public Vector3 getDirection(Vector3 hitPos) {
		return this.direction;
	}
	
	public double getProjection(Vector3 normal, Vector3 hitPos) {
		return Vector3.dotProduct(normal, Vector3.scale(-1.0, Vector3.toUnit(this.getDirection(hitPos))));
	}

	
}
