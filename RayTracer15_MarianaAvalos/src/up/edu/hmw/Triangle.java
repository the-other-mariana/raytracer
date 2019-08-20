package up.edu.hmw;

import java.awt.Color;



/**
 * front facing triangle:
 * 			a
 * 		   / \
 * 		  /   \
 * 		 b --- c
 * 	   [x]	  [-x]
 * 
 * back facing triangle:
 * 			a
 * 		   / \
 * 		  /   \
 * 		 c --- b
 * 	   [-x]	  [x]
 */

/**
 * notes:
 * if u create triangles like: (vector as a)(vector with smaller x as b)(vector with biggest x as c) 
 * normals that look 'positive' or towards camera must be calculated as edge2 x edge1
 * 
 * if u create triangles like: (vector as a)(vector with biggest x as b)(vector with smallest x as c) 
 * normals that look 'positive' or towards camera must be calculated as edge1 x edge2
 * @author Mariana
 *
 */

public class Triangle extends Object3D{
	private Vector3 a;
	private Vector3 b;
	private Vector3 c;
	private Vector3[] vertices = new Vector3[3];
	double EPSILON = 0.000001; // tolerance
	public Vector3 normal;
	
	public Object3D parent; // polygon containing this triangle
	public String group = "";
	public boolean s_off = false;
	
	
	/**
	 * class constructor: sets parameters and calculates the triangle normal
	 * @param a
	 * @param b
	 * @param c
	 * @param color
	 * @param material
	 */
	public Triangle(Vector3 a, Vector3 b, Vector3 c, Color color, Material material) {
		super(a, color, material);
		this.a = a;
		this.b = b;
		this.c = c;
		calculatePosition();
		
		vertices[0] = a;
		vertices[1] = b;
		vertices[2] = c;
		
		Vector3 edge2 = Vector3.substract(this.b, this.a); // vector  a to b 
        Vector3 edge1 = Vector3.substract(this.c, this.a); // vector a to c
        this.normal = Vector3.toUnit(Vector3.crossProduct(edge1, edge2));
	}
	
	/**
	 * the position of a triangle is its center
	 */
	public void calculatePosition() {
		Vector3 avg = Vector3.sum3(this.a, this.b, this.c);
		avg = Vector3.scale(1.0 / 3.0, avg);
		
		super.setPosition(avg);
	}
	
	public Vector3[] getVertices() {
		return this.vertices;
	}
	
	/**
	 * Triangle intersection
	 * get intersection with plane of triangle
	 * apply barycentric coordinates
	 */
	public Intersection getIntersection(Ray ray, Camera camera) {
		boolean culling = false;
		double t;
        Vector3 normal = Vector3.ZERO();
        Vector3 position = Vector3.ZERO();
        
        // normally: edge 1 = b - a / edge 2 = c - a
        // but changed because looking 'positive' is now towards -z 
        // counter clock wise makes the cross product go inverse: ab x ac = cw (normal system)/ ac x ab = ccw (my system)

        Vector3 edge2 = Vector3.substract(this.b, this.a); // vector  a to b 
        Vector3 edge1 = Vector3.substract(this.c, this.a); // vector a to c
        
        Vector3 P = Vector3.crossProduct(ray.getDirection(), edge2);
        double det = Vector3.dotProduct(P, edge1);
        
        if(culling) {
        	
	        normal = Vector3.toUnit(Vector3.crossProduct(edge1, edge2));
	        Vector3 T = Vector3.substract(ray.getOrigin(), this.a); // vector from obj pos to camera
	        
	        if(det < EPSILON) return null;
	        
	        if(Vector3.dotProduct(normal, T) <= EPSILON) {
	        	return null;
	        }
	        
	        double numU = Vector3.dotProduct(P, T);
	        if (numU < 0 || numU > det + EPSILON) {
	        	return null;
	        }
	        
	        Vector3 Q = Vector3.crossProduct(T, edge1);
	        double numV = Vector3.dotProduct(Q, ray.getDirection());
	        if(numV < 0 || numU + numV > det + EPSILON) {
	        	return null;
	        }
	        
	        double denom = 1.0 / det;
	        t = (Vector3.dotProduct(Q, edge2)) * denom;
	
	        position = Vector3.sum(ray.getOrigin(), Vector3.scale(t, ray.getDirection())); // real position
	        
        }else {
        	normal = Vector3.toUnit(Vector3.crossProduct(edge1, edge2));
        	double denom = 1.0 / det;
        	Vector3 T = Vector3.substract(ray.getOrigin(), this.a);
        	
        	double u = Vector3.dotProduct(P, T) * denom;
        	if(u < 0 || u > (1.0 + EPSILON)) return null;
        	
        	Vector3 Q = Vector3.crossProduct(T, edge1);
        	double v = Vector3.dotProduct(Q, ray.getDirection()) * denom;
        	if(v < 0 || u + v > (1.0 + EPSILON)) return null;
        	
        	t = (Vector3.dotProduct(Q, edge2)) * denom; // t is distance
        }
        this.normal = normal;
        position = Vector3.sum(ray.getOrigin(), Vector3.scale(t, ray.getDirection()));
        return new Intersection(position, t, normal, this);
	}

}
