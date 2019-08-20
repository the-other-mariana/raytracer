package up.edu.hmw;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Polygon class: a polygon is a 3d object that has triangle faces
 * @author Mariana
 */

public class Polygon extends Object3D{
	private ArrayList<Triangle> faces = new ArrayList<Triangle>();
	private ArrayList<Vector3> vertices = new ArrayList<Vector3>();
	private ArrayList<Vector3> vertNormals;
	Material material;
	public String name = "";
	
	// map that will contain the polygon vertices for each smoothing group
	public Map<String, ArrayList<Vector3>> mapVerts = new HashMap<String, ArrayList<Vector3>>();
	
	
	public Polygon(Vector3 position, Color color, Material material) {
		super(position, color, material);
	}
	
	public void addFace(Triangle triangle) {
		faces.add(triangle);
	}
	
	/**
	 * function that creates a face and accumulates the normal to obtain vertex normal
	 * @param index1
	 * @param index2
	 * @param index3
	 * @param group
	 */
	public void createFace(int index1, int index2, int index3, String group) {
		
		Triangle face = new Triangle(mapVerts.get(group).get(index1), mapVerts.get(group).get(index2), mapVerts.get(group).get(index3), super.getColor(), super.getMaterial());
		face.parent = this;
		face.group = group;
		faces.add(face);
		
		if(group.equals("off")) face.s_off = true;
		
		mapVerts.get(group).get(index1).addTriangleNormal(face.normal);
		mapVerts.get(group).get(index2).addTriangleNormal(face.normal);
		mapVerts.get(group).get(index3).addTriangleNormal(face.normal);
		
	}
	
	public ArrayList<Vector3> getVertices(){
		return vertices;
	}
	
	public void addVert(Vector3 vert) {
		this.vertices.add(vert);
	}

	public ArrayList<Triangle> getFaces() {
		return faces;
	}

	public void setFaces(ArrayList<Triangle> faces) {
		this.faces = faces;
	}
	
	/**
	 * loop over the polygon's faces and get the closest intersection
	 * return intersection
	 */
	public Intersection getIntersection(Ray ray, Camera camera) {
		Intersection closestIntersection = null;
		
		for(int i = 0; i < faces.size(); i++) {
			Intersection intersection = faces.get(i).getIntersection(ray, camera);
			
			if(intersection != null) {
				if (intersection != null) {
					double distance = intersection.getDistance();
					
					if (distance >= 0 && (closestIntersection == null || distance < closestIntersection.getDistance()) && (intersection.getPosition().getZ() < camera.getzMaxClippingPlane() && intersection.getPosition().getZ() > camera.getzMinClippingPlane())) {
						closestIntersection = intersection;
					}
				}
			}
			
		}
		return closestIntersection;
	}

	public ArrayList<Vector3> getVertNormals() {
		return vertNormals;
	}

	public void setVertNormals(ArrayList<Vector3> vertNormals) {
		this.vertNormals = vertNormals;
	}
	

}
