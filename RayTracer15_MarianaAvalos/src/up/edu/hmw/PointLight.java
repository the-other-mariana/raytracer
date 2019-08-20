package up.edu.hmw;

import java.awt.Color;

public class PointLight extends Light{
	
	private Vector3 position;
	
	/**
	 * constructor
	 * @param pos
	 * @param dir
	 * @param color
	 * @param intensity
	 */
	public PointLight(Vector3 pos, Vector3 dir, Color color, double intensity) {
		super(pos, dir, color, intensity);
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}
	
	public Vector3 getDirection(Vector3 hitPos) {
		return Vector3.substract(hitPos, this.getPosition());
	}
	
	public double getProjection(Vector3 normal, Vector3 hitPos) {
		Vector3 lightDirection = Vector3.substract(hitPos, this.getPosition());
		double result = Vector3.dotProduct(normal, Vector3.scale(-1.0, Vector3.toUnit(lightDirection)));
		return Math.max(0.0, result);
		//return Vector3.dotProduct(normal, Vector3.scale(-1.0, Vector3.toUnit(lightDirection)));
	}
	
}
