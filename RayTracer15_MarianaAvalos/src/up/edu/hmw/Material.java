package up.edu.hmw;

import java.awt.Color;
import java.util.ArrayList;

public abstract class Material {
	
	private boolean smooth;
	private boolean reflective = false;
	private boolean refractive = false;
	private boolean castShadows = false;
	private boolean biased = true;
	private double kd = 1.0;
	private double kr = 1.0;
	private double ka = 0.0;
	private double ri = 1.0;
	private double krf = 1.0;
	
	/**
	 * class constructor: initializes all common parameters used by all materials
	 * @param smooth boolean
	 * @param reflective boolean
	 * @param kr double
	 * @param ka double
	 * @param refractive boolean
	 * @param ri double
	 * @param krf double
	 * @param castShadows boolean
	 */
	public Material(boolean smooth, double kd, boolean reflective, double kr, double ka, boolean refractive, double ri, double krf, boolean castShadows, boolean biased) {
		setSmooth(smooth);
		setReflective(reflective);
		setRefractive(refractive);
		setCastShadows(castShadows);
		setBiased(biased);
		setKd(kd);
		setKr(kr);
		setKa(ka);
		setRi(ri);
		setKrf(krf);
	}
	
	public boolean isSmooth() {
		return smooth;
	}

	public void setSmooth(boolean smooth) {
		this.smooth = smooth;
	}
	
	/**
	 * each material implements this method, shading in its own way
	 * @param hit
	 * @param light
	 * @param Oc
	 * @return color
	 */
	public abstract Color shade(Intersection hit, Light light, Color Oc, ArrayList<Object3D> objects, Camera camera, boolean castShadows);

	/**
	 * function that updates the face normal with the point normal
	 * @param hit
	 * @return pn (point normal)
	 */
	public static Vector3 smoothShade(Intersection hit) {
		
		if( ((Triangle)(hit.getObject())).s_off ) return ((Triangle)(hit.getObject())).normal;
		
		Vector3 vn1 = ((Vector3)(((Triangle)(hit.getObject())).getVertices()[0])).triangleNormal;
		Vector3 vn2 = ((Vector3)(((Triangle)(hit.getObject())).getVertices()[1])).triangleNormal;
		Vector3 vn3 = ((Vector3)(((Triangle)(hit.getObject())).getVertices()[2])).triangleNormal;
		
		double lamdas[] = new double[3];
			
		toBarycentric(lamdas, hit.getPosition(), ((Triangle)(hit.getObject())).getVertices());
		Vector3 pn = Vector3.sum3(Vector3.scale(lamdas[0], vn1), Vector3.scale(lamdas[1], vn2), Vector3.scale(lamdas[2], vn3));
		return pn;
	}	
	
	/**
	 * updates the input array with the areal proportions of the hit point
	 * @param lamdas
	 * @param p
	 * @param verts
	 */
	private static void toBarycentric(double lamdas[], Vector3 p, Vector3[] verts) {
		
		Vector3 bp = Vector3.substract(p, verts[1]);
		Vector3 cp = Vector3.substract(p, verts[2]);
		Vector3 ba = Vector3.substract(verts[0], verts[1]);
		Vector3 ca = Vector3.substract(verts[0], verts[2]);
		double areaABC = Vector3.magnitude(Vector3.crossProduct(ba, ca));
		
		lamdas[0] = Vector3.magnitude(Vector3.crossProduct(bp, cp)) / areaABC; // toA
		lamdas[1] = Vector3.magnitude(Vector3.crossProduct(ca, cp)) / areaABC; // toB 
		lamdas[2] = 1.00 - lamdas[0] - lamdas[1]; // toC

	}
	
	/**
	 * static function that serves as a tool that makes a color threshold to avoid overflowing the color
	 * @param value
	 * @param min
	 * @param max
	 * @return float value
	 */
	public static float threshold(double value, double min, double max) {
		if (value > max) return (float)max;
		if (value < min) return (float)min;
		
		return (float)value;
	}
	
	/**
	 * function that returns a vector with the reflection direction
	 * @param I
	 * @param normal
	 * @return Vector3
	 */
	public static Vector3 reflectRay(Vector3 I, Vector3 normal) {
		
		return Vector3.substract(I, Vector3.scale(2 * Vector3.dotProduct(I, normal), normal));
	}
	
	/**
	 * function that returns a complete ray with refracted position and direction
	 * @param ray
	 * @param normal
	 * @param hit
	 * @param ri
	 * @return Ray
	 */
	public static Ray refractRay(Ray ray, Vector3 normal, Intersection hit, double ri) {
		double n1, n2, n;
		double cosI = Vector3.dotProduct(ray.getDirection(), normal);
		Vector3 rayOrigin = hit.getPosition();
		Vector3 bias = new Vector3(0.00001, 0.00001, 0.00001); // bias or epsilon for refraction: the smaller the less noise
		
		//outside to inside object
		if(cosI >= 0.0) {
			n1 = ri;
			n2 = 1.0;
			normal = Vector3.scale(-1, normal);

			rayOrigin = Vector3.sum(hit.getPosition(), bias);
		}else {
			//inside object to outside
			n1 = 1.0;
			n2 = ri;
			cosI = -1*cosI;
			
			rayOrigin = Vector3.sum(hit.getPosition(), bias);
		}
		n = n1 / n2;
		Vector3 scaleN = Vector3.scale(cosI, normal);
		Vector3 firstPart = Vector3.scale(n, Vector3.sum(ray.getDirection(), scaleN));
		
		double cosT = 1 - ((n1*n1) / (n2* n2)) * (1 - cosI*cosI);
		
		if(cosT < 0.0) {
			return new Ray(Vector3.ZERO(), Vector3.ZERO());
		}
		
		double sqrt = Math.sqrt(cosT);
		Vector3 secPart = Vector3.scale(sqrt, normal);
		
		Vector3 refrDir = Vector3.substract(firstPart, secPart);
		return new Ray(rayOrigin, Vector3.toUnit(refrDir)); 
		
		
	}
	
	/**
	 * static method that each material calls to add shadow to a pixel, only if user wants so
	 * @param pixelColor
	 * @param light
	 * @param hit
	 * @param objects
	 * @param mainCamera
	 * @return color
	 */
	public static Color shadowColor(Color pixelColor, Light light, Intersection hit, ArrayList<Object3D> objects, Camera mainCamera) {
		Vector3 shadDir = null;
		if(light instanceof PointLight) shadDir = Vector3.toUnit(Vector3.substract(light.getPosition(), hit.getPosition()));
		else if(light instanceof DirectionalLight) shadDir = Vector3.scale(-1.0, light.getDirection(null));
		
		Vector3 shadOrigin = Vector3.sum(Vector3.scale(0.00001, shadDir), hit.getPosition());
		Ray shadRay = new Ray(shadOrigin, shadDir);
		
		Intersection shadHit = RayTracing.raycast(shadRay, objects, hit.getObject(), mainCamera);
		double selfToleranceProjection = Vector3.dotProduct(shadDir, hit.getNormal());
		
		// if shadow ray hits an object, the pixel is in shadow
		if(shadHit != null  && hit.getObject() != shadHit.getObject() && selfToleranceProjection > 0.0001) {
			
			// I assume a shadow weigth of 1.2 and will be substracted to the pixel color
			double shadowWeight = light.getIntensity() * 1.2;
			pixelColor = new Color((int)(Math.max(0, pixelColor.getRed() - shadowWeight)), (int)(Math.max(0, pixelColor.getGreen() - shadowWeight)), (int)(Math.max(0, pixelColor.getBlue() - shadowWeight)));
		}
		return pixelColor;
	}

	public boolean isReflective() {
		return reflective;
	}

	public void setReflective(boolean reflective) {
		this.reflective = reflective;
	}

	public double getKr() {
		return kr;
	}

	public void setKr(double kr) {
		this.kr = kr;
	}

	public double getKa() {
		return ka;
	}

	public void setKa(double ka) {
		this.ka = ka;
	}
	
	public double getRi() {
		return ri;
	}

	public void setRi(double ri) {
		this.ri = ri;
	}

	public boolean isRefractive() {
		return refractive;
	}

	public void setRefractive(boolean refractive) {
		this.refractive = refractive;
	}

	public double getKrf() {
		return krf;
	}

	public void setKrf(double krf) {
		this.krf = krf;
	}

	public boolean isCastShadows() {
		return castShadows;
	}

	public void setCastShadows(boolean castShadows) {
		this.castShadows = castShadows;
	}

	public boolean isBiased() {
		return biased;
	}

	public void setBiased(boolean biased) {
		this.biased = biased;
	}

	public double getKd() {
		return kd;
	}

	public void setKd(double kd) {
		this.kd = kd;
	}
	
}
