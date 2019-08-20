package up.edu.hmw;

import java.awt.Color;
import java.util.ArrayList;

public class Phong extends Material{
	private Camera camera;

	private double ks;
	private double p;

	/**
	 * Class constructor:
	 * 
	 * ks: the bigger the fastest the space will be filled with Lc (burnt), the smaller the softer.
	 * p: the smaller the exponent, the slower decay (the bigger space of specular)
	 * 
	 * @param smooth boolean
	 * @param camera Camera
	 * @param kd double (diffuse coef)
	 * @param ks double (specular coef)
	 * @param p double (exponent)
	 * @param reflective boolean
	 * @param kr double (reflection coef)
	 * @param ka double (ambient coef)
	 * @param refractive boolean
	 * @param ri double (index of refraction)
	 * @param krf double (refraction coef)
	 * @param castShadows boolean
	 * @param biased boolean
	 */
	public Phong(boolean smooth, Camera camera, double kd, double ks, double p, boolean reflective, double kr, double ka, boolean refractive, double ri, double krf, boolean castShadows, boolean biased) {
		super(smooth, kd, reflective, kr, ka, refractive, ri, krf, castShadows, biased);
		setViewDir(camera);

		this.ks = ks;
		this.p = p;
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
		
		return getSpecularPhong(Vector3.toUnit(normal), hit.getPosition(), light, Oc, hit, objects, camera, castShadows);
	}
	
	/**
	 * this function adds to the diffuse color a specular reflection, described by blinn-phong model
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
	private Color getSpecularPhong(Vector3 normal, Vector3 hitPos, Light light, Color Oc, Intersection hit, ArrayList<Object3D> objects, Camera mainCamera, boolean castShadows) {
		Color Lc = light.getColor();
		double projection = light.getProjection(normal, hitPos);
		
		Vector3 viewDir = Vector3.toUnit(Vector3.substract(camera.getPosition(), hitPos));
		
		Vector3 h = Vector3.toUnit(Vector3.sum(viewDir, light.getDirection(hitPos)));
		double phongProj = Math.pow(Vector3.dotProduct(normal, h), p);
		
		double intensity = light.getIntensity() * projection;
		double intensityPhong = light.getIntensity() * phongProj;
		
		if(light instanceof PointLight) {
			double distance = Vector3.magnitude(Vector3.substract(light.getPosition(), hitPos));
			intensity *= 1.00 / (distance * distance);
			intensityPhong *= 1.00 / (distance * distance);
		}
		
		double red = (Oc.getRed() / 255.0) * (this.getKd());
		double green = (Oc.getGreen() / 255.0) * (this.getKd());
		double blue = (Oc.getBlue() / 255.0) * (this.getKd());
		
		red *= intensity * (Lc.getRed() / 255.0);
		green *= intensity * (Lc.getGreen() / 255.0);
		blue *= intensity * (Lc.getBlue() / 255.0);
		
		double redPhong = (ks / 255.0) * intensityPhong * (Lc.getRed() / 255.0);
		double greenPhong = (ks / 255.0) * intensityPhong * (Lc.getRed() / 255.0);
		double bluePhong = (ks / 255.0) * intensityPhong * (Lc.getRed() / 255.0);
		
		
		red += redPhong;
		green += greenPhong;
		blue += bluePhong;
		
		Color pixelColor = new Color(Material.threshold(red, 0, 1), Material.threshold(green, 0, 1), threshold(blue, 0, 1));
		
		if(!castShadows) return pixelColor;
		
		return Material.shadowColor(pixelColor, light, hit, objects, mainCamera);
	}
	
	public void setViewDir(Camera camera) {
		
		this.camera = camera;
	}
}
