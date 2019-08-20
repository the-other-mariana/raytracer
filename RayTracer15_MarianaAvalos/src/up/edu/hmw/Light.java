package up.edu.hmw;

import java.awt.Color;


public abstract class Light {
	
	private Color color;
	private Vector3 position;
	private double intensity;
	private Vector3 direction;
	
	/**
	 * constructor
	 * @param position
	 * @param direction
	 * @param color
	 * @param intensity
	 */
	public Light(Vector3 position, Vector3 direction, Color color, double intensity) {
		setColor(color);
		setPosition(position);
		setDirection(direction);
		setIntensity(intensity);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 direction) {
		this.position = direction;
	}

	public double getIntensity() {
		return intensity;
	}

	public void setIntensity(double intensity) {
		this.intensity = intensity;
	}
	
	public abstract double getProjection(Vector3 normal, Vector3 hitPos);

	public abstract Vector3 getDirection(Vector3 hitPos);

	public void setDirection(Vector3 direction) {
		this.direction = direction;
	}
}
