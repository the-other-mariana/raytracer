package up.edu.hmw;

import java.awt.Color;

public class Sphere extends Object3D{
	private float radius;


	/**
	 * constructor
	 * @param center
	 * @param radius
	 * @param color
	 * @param material
	 */
    public Sphere(Vector3 center, float radius, Color color, Material material) {
        super(center, color, material);
        setRadius(radius);
    }

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	/**
	 * gets the intersection of ray-sphere
	 */
	
	public Intersection getIntersection(Ray ray, Camera camera) {
        double distance = -1;
        Vector3 normal = Vector3.ZERO();
        Vector3 position = Vector3.ZERO();
        
        Vector3 directionSphereRay = Vector3.substract(ray.getOrigin(), getPosition());
        double b = 2 * Vector3.dotProduct(ray.getDirection(), directionSphereRay);
		double a = Vector3.dotProduct(ray.getDirection(), ray.getDirection());
		double c = Vector3.dotProduct(directionSphereRay, directionSphereRay) - (getRadius()*getRadius());
		
		double discriminant = (b*b) - (4 * a * c);

        if (discriminant >= 0) {
        	double denom = 2 * a;
        	double sqrt = Math.sqrt(discriminant);
            double part1 = (-b + sqrt) / denom;
            double part2 = (-b - sqrt) / denom;
            
            distance = Math.min(part1, part2); //distance from ray origin to collision
            position = Vector3.sum(ray.getOrigin(), Vector3.scale(distance, ray.getDirection())); // real position
            
            //if(!insideClippingPlanes(position, camera)) return null;
            
            normal = Vector3.toUnit(Vector3.substract(position, getPosition()));
        } else {
            return null;
        }
        
        return new Intersection(position, distance, normal, this);
    }
	
	public boolean insideClippingPlanes(Vector3 position, Camera camera) {
    	float angle = Vector3.angleBetween(position, Vector3.FORWARD());
        
        double tValue = Vector3.magnitude(position) * Math.cos(angle);
        if(tValue < camera.getzMinClippingPlane() || tValue >= camera.getzMaxClippingPlane()) return false;
    	return true;
    }
}
