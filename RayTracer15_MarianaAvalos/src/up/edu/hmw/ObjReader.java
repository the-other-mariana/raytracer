package up.edu.hmw;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ObjReader {
	private String fileName;
	private Color color;
	private Vector3 offset;
	private Vector3 rotation;
	private Vector3 scale;
	private Material material;
	
	String currKey = "-1";
	
	/**
	 * class constructor: path, color, position, rotation angle in Y axis and material
	 * @param fileName
	 * @param color
	 * @param offset
	 * @param rotationY
	 * @param material
	 */
	public ObjReader(String fileName, Color color, Vector3 offset, Vector3 rotation, Vector3 scale, Material material){
		this.color = color;
		this.offset = offset;
		this.rotation = rotation;
		this.scale = scale;
		setFileName(fileName);
		setMaterial(material);
	}

	public Material getMaterial() {
		return material;
	}
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * Function that read OBJ file and creates faces, smoothing groups, etc and makes them a polygon 
	 * @return polygon
	 * @throws IOException
	 */
	public Polygon createObject() throws IOException{
		
			Polygon polygon = new Polygon(null, this.color, this.material);
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			
			String line;
			while ((line = reader.readLine()) != null) {
				
				String[] lineParts = line.split(" ");
				String identifier = lineParts[0];
				
				// when finding a vertex
				if (identifier.equals("v")){
					
					double xComp = Double.parseDouble(lineParts[lineParts.length - 3]);
					double yComp = Double.parseDouble(lineParts[lineParts.length - 2]);
					double zComp = Double.parseDouble(lineParts[lineParts.length - 1]);
					
					Vector3 vert = new Vector3(xComp, yComp, zComp);
					
					//apply transformations: scale and rotate
					if(rotation.getX() != 0.0 || rotation.getY() != 0.0 || rotation.getZ() != 0.0 || scale.getX() != 1.0 || scale.getY() != 1.0 || scale.getZ() != 1.0 ) {
						double[] rotatedVert = new double[3];
						double[] vertVec = new double[3];
						
						vertVec[0] = this.scale.getX() * xComp;
						vertVec[1] = this.scale.getY() * yComp;
						vertVec[2] = this.scale.getZ() * zComp;
						
						rotate(this.rotation, rotatedVert, vertVec);
						vert = new Vector3(rotatedVert[0], rotatedVert[1], rotatedVert[2]);
					}
					
					vert = new Vector3(vert.getX() + offset.getX(), vert.getY() + offset.getY(), vert.getZ() + offset.getZ());

					if(polygon.getPosition() == null) {
						polygon.setPosition(vert);
					}
					
					polygon.addVert(vert);
				}
				
				// condition that uses smoothing groups
				if(identifier.equals("s")) {
					currKey = lineParts[1];
					
					if(polygon.mapVerts.get(currKey) == null) {
						ArrayList<Vector3> vertCopy = copyVerts(polygon.getVertices());
						polygon.mapVerts.put(currKey, vertCopy);
					}
				}
				
				// when finding a face
				if (identifier.equals("f")) {
					if(lineParts.length == 4) {
						
						int index1 = Integer.parseInt((lineParts[1].split("/"))[0]) - 1;
                    	int index2 = Integer.parseInt((lineParts[2].split("/"))[0]) - 1;
                    	int index3 = Integer.parseInt((lineParts[3].split("/"))[0]) - 1;
                    	
                    	//CCW order: 2, 1, 3 //1 0 2, 2 1 3
                    	polygon.createFace(index2, index1, index3, currKey);
					}
					if (lineParts.length == 5){
						//mantain CCW order: 3,4,1 and 1,2,3
                        polygon.createFace(Integer.parseInt((lineParts[3].split("/"))[0]) - 1, Integer.parseInt((lineParts[4].split("/"))[0]) - 1, Integer.parseInt((lineParts[1].split("/"))[0]) - 1, currKey);
                        polygon.createFace(Integer.parseInt((lineParts[1].split("/"))[0]) - 1, Integer.parseInt((lineParts[2].split("/"))[0]) - 1, Integer.parseInt((lineParts[3].split("/"))[0]) - 1, currKey);
                    }
				}
			}
			reader.close();
			return polygon;
	}
	
	/**
	 * function that applies dot product to each given vertex in order to rotate the object
	 * @param degrees in all axes
	 * @param rotPoint
	 * @param point
	 */
	private void rotate(Vector3 degrees, double[] rotPoint, double[] point) {

		double[] rotPointFinal = new double[3];
		
		double angleY = (degrees.getY() * Math.PI) / 180.0;
		double angleX = (degrees.getX() * Math.PI) / 180.0;
		double angleZ = (degrees.getZ() * Math.PI) / 180.0;
		
		double[][] rotMatrixY = {{ Math.cos(angleY), 0.0, Math.sin(angleY) }, 
								{ 0.0, 1.0, 0.0 }, 
								{ -Math.sin(angleY), 0.0, Math.cos(angleY) }};
		double[][] rotMatrixZ =  {{ Math.cos(angleZ),-Math.sin(angleZ), 0.0 }, 
								{ Math.sin(angleZ), Math.cos(angleZ), 0.0 }, 
								{ 0.0, 0.0, 1.0 }};
		double[][] rotMatrixX =  {{1.0, 0.0, 0.0 }, 
								{ 0.0, Math.cos(angleX),-Math.sin(angleX) }, 
								{ 0.0, Math.sin(angleX), Math.cos(angleX) }};
		
		double[][] matXY = new double[3][3];
		double[][] matXYZ = new double[3][3];
		dotMatrix(matXY, rotMatrixY, rotMatrixX);
		dotMatrix(matXYZ, rotMatrixZ, matXY);
		
		for (int r = 0; r < 3; r++) {
			rotPointFinal[r] = 0;
			for (int c = 0; c < 3; c++) {
				rotPointFinal[r] += matXYZ[r][c] * point[c];
			}
		}
		rotPoint[0] = rotPointFinal[0];
		rotPoint[1] = rotPointFinal[1];
		rotPoint[2] = rotPointFinal[2];
		
	}
	/**
	 * function that makes a dot product of two 3x3 matrices
	 * @param result
	 * @param A
	 * @param B
	 */
	private static void dotMatrix(double[][] result, double[][] A, double[][] B) {
		int dim = 2;
		for (int r = 0; r < dim + 1; r++) {
			for (int c = 0; c < dim + 1; c++) {
				for (int k = 0; k < dim + 1; k++) {
					result[r][c] += A[r][k] * B[k][c];
				}
			}
		}
	}
	
	/**
	 * function that copies all vertexes into each smoothing group
	 * @param verts
	 * @return vertex copy
	 */
	private ArrayList<Vector3> copyVerts(ArrayList<Vector3> verts){
		ArrayList<Vector3> vertCopy = new ArrayList<Vector3>();
		for(int i = 0; i < verts.size(); i++) {
			Vector3 vert = new Vector3(verts.get(i).getX(), verts.get(i).getY(), verts.get(i).getZ());
			vertCopy.add(vert);
		}
		return vertCopy;
	}
	
}
