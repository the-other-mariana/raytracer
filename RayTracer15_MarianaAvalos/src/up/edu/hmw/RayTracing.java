package up.edu.hmw;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class RayTracing {

	public static int RECURSION_DEPTH = 5;
	public static int rounds = 0;
	
public static void main(String[] args) throws IOException {
	
	SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");  
	Date date = new Date(System.currentTimeMillis());  
	System.out.println(formatter.format(date));
	
	/** Sample scenes:
	 * 1 -> refraction scene
	 * 2 -> reflection scene
	 * 3 -> reflection + refraction + rotation scene
	 */
	Scene scene01 = SampleScene.getSampleScene(3);
	
	BufferedImage img = raytrace(scene01);
	saveImage(img, "test" + ".png");
			
	Date date2 = new Date(System.currentTimeMillis());  
	System.out.println(formatter.format(date2));

	}
	
	private static void saveImage(BufferedImage img, String fileName) {
		File outputImage = new File(fileName);
		try {
			ImageIO.write(img, "png", outputImage);
		}catch(IOException ex) {
			System.out.println("error");
		}
	}
	
	public static Intersection raycast(Ray ray, ArrayList<Object3D> objects, Object3D caster, Camera camera) {
		Intersection closestIntersection = null;

		for (int k = 0; k < objects.size(); k++) {
			Object3D currentObj = objects.get(k);

			if (caster == null || !currentObj.equals(caster)) {
				Intersection intersection = currentObj.getIntersection(ray, camera);
				if (intersection != null && !(intersection.getObject() == caster)) {
					double distance = intersection.getDistance();
					
					if (distance >= 0 && (closestIntersection == null || distance < closestIntersection.getDistance()) && (intersection.getPosition().getZ() < camera.getzMaxClippingPlane() && intersection.getPosition().getZ() > camera.getzMinClippingPlane())) {
						closestIntersection = intersection;
					}
				}
			}
		}

		return closestIntersection;
	}
	
	public static BufferedImage raytrace(Scene scene) {
		Camera mainCamera = scene.getCamera();
		BufferedImage image = new BufferedImage(mainCamera.getResolution()[0], mainCamera.getResolution()[1], BufferedImage.TYPE_INT_RGB);
		ArrayList<Object3D> objects = scene.getObjects();
		
		Vector3[][] positionsToRaytrace = mainCamera.calculatePositionsToRay();
		for (int i = 0; i < positionsToRaytrace.length; i++) {
			for (int j = 0; j < positionsToRaytrace[i].length; j++) {
				double x = positionsToRaytrace[i][j].getX() + mainCamera.getPosition().getX();
				double y = positionsToRaytrace[i][j].getY() + mainCamera.getPosition().getY();
				double z = positionsToRaytrace[i][j].getZ() + mainCamera.getPosition().getZ();
				Ray ray = new Ray(mainCamera.getPosition(), new Vector3(x, y, z));

				Intersection closestIntersection = raycast(ray, objects, null, mainCamera);
				Color pixelColor =  Color.BLACK;
				rounds = 0; 
				
				if (closestIntersection != null) {
					
					pixelColor = Color.BLACK;
					for(Light light : scene.getLights()) {
						
						pixelColor = addColor(pixelColor, render(scene, ray, closestIntersection, pixelColor, light, mainCamera, objects, rounds));
						
					}
				}
				image.setRGB(i, j, pixelColor.getRGB());
			}
		}

		return image;
	}
	
	/**
	 * blends a color with another
	 * @param original
	 * @param otherColor
	 * @return color
	 */
	public static Color addColor(Color original, Color otherColor) {
		float red = Material.threshold((original.getRed() / 255.0) + (otherColor.getRed() / 255.0), 0, 1);
		float green = Material.threshold((original.getGreen() / 255.0) + (otherColor.getGreen() / 255.0), 0, 1);
		float blue = Material.threshold((original.getBlue() / 255.0f) + (otherColor.getBlue() / 255.0f), 0, 1);
		return new Color(red, green, blue);
	}
	
	/**
	 * function that multiplies a color x times, usually with rational constants
	 * @param coefficient
	 * @param color
	 * @return color
	 */
	public static Color scaleColor(double coefficient, Color color) {
		Color result = new Color((float)((color.getRed() / 255.0)*coefficient), (float)((color.getGreen() / 255.0)*coefficient), (float)((color.getBlue() / 255.0)*coefficient));
		return result;
	}
	
	/**
	 * Main shading function that manages all the general rendering options: what to do if diffuse/specular, reflection or refraction and what proportion it represents.
	 * Potentially recursive.
	 * @param scene
	 * @param ray
	 * @param closestIntersection
	 * @param pixelColor
	 * @param light
	 * @param mainCamera
	 * @param objects
	 * @param rounds
	 * @return color
	 */
	public static Color render(Scene scene, Ray ray, Intersection closestIntersection, Color pixelColor, Light light, Camera mainCamera, ArrayList<Object3D> objects, int rounds) {
	
		// recursive limit
		if(rounds >= RECURSION_DEPTH) {
			return pixelColor;
		}
		
		// if normal material
		if(!closestIntersection.getObject().getMaterial().isReflective() && !closestIntersection.getObject().getMaterial().isRefractive()) {
			Color shadeColor = closestIntersection.getObject().getMaterial().shade(closestIntersection, light, closestIntersection.getObject().getColor(), objects, mainCamera, closestIntersection.getObject().getMaterial().isCastShadows());
			pixelColor = addColor(pixelColor, shadeColor);
			
			Color ambientColor = scaleColor(closestIntersection.getObject().getMaterial().getKa(), scene.getAmbientCoef());
			pixelColor = addColor(pixelColor, ambientColor);
		}
		
		// if reflective material or refractive material
		if(closestIntersection.getObject().getMaterial().isReflective() || closestIntersection.getObject().getMaterial().isRefractive()) {
			
			
			Color shadeColor = closestIntersection.getObject().getMaterial().shade(closestIntersection, light, closestIntersection.getObject().getColor(), objects, mainCamera, closestIntersection.getObject().getMaterial().isCastShadows());
			shadeColor = scaleColor((1.0 - (closestIntersection.getObject().getMaterial().getKrf() + closestIntersection.getObject().getMaterial().getKr())), shadeColor);
			pixelColor = addColor(pixelColor, shadeColor);
			
			Color ambientColor = scaleColor(closestIntersection.getObject().getMaterial().getKa(), scene.getAmbientCoef());
			pixelColor = addColor(pixelColor, ambientColor);
			
			if(closestIntersection.getObject().getMaterial().isRefractive()) {
				
				Ray refrRay = Material.refractRay(ray, closestIntersection.getNormal(), closestIntersection, closestIntersection.getObject().getMaterial().getRi());
				Intersection refrHit = raycast(refrRay, objects, closestIntersection.getObject(), mainCamera);
				
				if(refrHit != null) {
					
					if(refrHit.getObject().getMaterial().isReflective() || refrHit.getObject().getMaterial().isRefractive()) {
						
						if (closestIntersection.getObject().getMaterial().isBiased()) return render(scene, refrRay, refrHit, pixelColor, light, mainCamera, objects, rounds + 1);
						else {
							Color refrColor = render(scene, refrRay, refrHit, pixelColor, light, mainCamera, objects, rounds + 1);
							Color refrColorRatio = scaleColor(closestIntersection.getObject().getMaterial().getKrf(), refrColor);
							pixelColor = addColor(pixelColor, refrColorRatio);
							return pixelColor;
						}
					}
					
					Color refrColor = refrHit.getObject().getMaterial().shade(refrHit, light, refrHit.getObject().getColor(), objects, mainCamera, refrHit.getObject().getMaterial().isCastShadows());
					Color refrColorRatio = scaleColor(closestIntersection.getObject().getMaterial().getKrf(), refrColor);
					pixelColor = addColor(pixelColor, refrColorRatio);
				}else {
					Color backColor = Color.BLACK;
					Color refColorRatio = scaleColor(closestIntersection.getObject().getMaterial().getKrf(), backColor);
					pixelColor = addColor(pixelColor, refColorRatio);
				}
			}
			
			if(closestIntersection.getObject().getMaterial().isReflective())  {
				
				Vector3 viewDir = Vector3.toUnit(Vector3.substract(closestIntersection.getPosition(), mainCamera.getPosition()));
				Vector3 refDir = Material.reflectRay(viewDir, closestIntersection.getNormal());
				Vector3 refOrigin = Vector3.sum(Vector3.scale(0.00001, refDir), closestIntersection.getPosition());
				Ray refRay = new Ray(refOrigin, refDir);
				
				Intersection refHit = raycast(refRay, objects, closestIntersection.getObject(), mainCamera);
				if(refHit != null) {

					if(refHit.getObject().getMaterial().isReflective() || refHit.getObject().getMaterial().isRefractive()) {
						
						if(closestIntersection.getObject().getMaterial().isBiased()) return render(scene, refRay, refHit, pixelColor, light, mainCamera, objects, rounds + 1);
						else {
							Color refColor = render(scene, refRay, refHit, pixelColor, light, mainCamera, objects, rounds + 1);
							Color refColorRatio = scaleColor(closestIntersection.getObject().getMaterial().getKr(), refColor);
							pixelColor = addColor(pixelColor, refColorRatio);
							return pixelColor;
						}
					}
					
					Color refColor = refHit.getObject().getMaterial().shade(refHit, light, refHit.getObject().getColor(), objects, mainCamera, refHit.getObject().getMaterial().isCastShadows());
					Color refColorRatio = scaleColor(closestIntersection.getObject().getMaterial().getKr(), refColor);
					pixelColor = addColor(pixelColor, refColorRatio);
					
				}else {
					Color backColor = Color.BLACK;
					Color refColorRatio = scaleColor(closestIntersection.getObject().getMaterial().getKr(), backColor);
					pixelColor = addColor(pixelColor, refColorRatio);
				}
											
			}
		}

		return pixelColor;
	}
	
	
	
	
	
	
}
