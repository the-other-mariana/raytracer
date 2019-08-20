package up.edu.hmw;

import java.awt.Color;
import java.util.ArrayList;

public class Lambert extends Material{
	public double DIFFUSE_COLOR;

	/**Constructor
	 * @param smooth boolean
	 * @param kd double (diffuse coef) 
	 * @param reflective boolean
	 * @param kr double (reflection coef) 
	 * @param ka double (ambient coef)
	 * @param refractive boolean
	 * @param ri double (index of refraction)
	 * @param krf double (refraction coef)
	 * @param castShadows boolean
	 * @param biased boolean
	 */
	public Lambert(boolean smooth, double kd, boolean reflective, double kr, double ka, boolean refractive, double ri, double krf, boolean castShadows, boolean biased) {
		super(smooth, kd, reflective, kr, ka, refractive, ri, krf, castShadows, biased);
		DIFFUSE_COLOR = 0.3;
	}
	
	/**
	 * this function decides what normal to use: either the face normal or the interpolated normal (smooth shading)
	 */
	public Color shade(Intersection hit, Light light, Color Oc, ArrayList<Object3D> objects, Camera camera, boolean castShadows) {
		Vector3 normal;
		normal = hit.getNormal();
		
		if(super.isSmooth()) {

			normal = super.smoothShade(hit);
			hit.setNormal(normal);
		}
		
		return getDiffuseLambert(Vector3.toUnit(normal), hit.getPosition(), light, Oc, hit, objects, camera, castShadows);
	}
	
	/**
	 * This function creates a Lambertian shading, which is equal to a diffuse shading without specular reflections
	 * @param normal
	 * @param hitPos
	 * @param light
	 * @param Oc
	 * @param hit
	 * @param objects
	 * @param mainCamera
	 * @param castShadows
	 * @return color
	 */
	private Color getDiffuseLambert(Vector3 normal, Vector3 hitPos, Light light, Color Oc, Intersection hit, ArrayList<Object3D> objects, Camera mainCamera, boolean castShadows) {
		
		Color Lc = light.getColor();
		double projection = light.getProjection(normal, hitPos);
		double intensity = light.getIntensity() * projection;
		
		// light decay
		if(light instanceof PointLight) {
			double distance = Vector3.magnitude(Vector3.substract(light.getPosition(), hitPos));
			intensity *= 1.00 / (distance * distance);
		}
		
		double red = (Oc.getRed() / 255.0) * (this.getKd());
		double green = (Oc.getGreen() / 255.0) * (this.getKd());
		double blue = (Oc.getBlue() / 255.0) * (this.getKd());
		
		red *= intensity * (Lc.getRed() / 255.0);
		green *= intensity * (Lc.getGreen() / 255.0);
		blue *= intensity * (Lc.getBlue() / 255.0);

		Color pixelColor = new Color(Material.threshold(red, 0, 1), Material.threshold(green, 0, 1), threshold(blue, 0, 1));
		
		if(!castShadows) return pixelColor;
		
		return Material.shadowColor(pixelColor, light, hit, objects, mainCamera);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
