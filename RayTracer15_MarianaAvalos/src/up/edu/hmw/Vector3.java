package up.edu.hmw;


public class Vector3 {
	
	/**
	 * class that contains all vector arithmetic for ray tracing
	 */
	
	private static final Vector3 ZERO = new Vector3(0.0, 0.0, 0.0);
    private static final Vector3 FORWARD = new Vector3(0.0, 0.0, 1.0);
    private double x, y, z;
    
    //when vector3 is seen as vertex normal
    public Vector3 triangleNormal;
	
	public Vector3(double x, double y, double z) {
		setX(x);
        setY(y);
        setZ(z);
        triangleNormal = null;
	}
	
	/**
	 * accumulates the normal of each face that uses this vertex
	 * resulting in the vertex normal
	 * @param normal
	 */
	public void addTriangleNormal(Vector3 normal) {
		if(this.triangleNormal == null) {
			this.triangleNormal = normal;
			this.triangleNormal = Vector3.toUnit(this.triangleNormal);
		}
		else {
			this.triangleNormal = Vector3.sum(this.triangleNormal, normal);
			this.triangleNormal = Vector3.toUnit(this.triangleNormal);
		}
		
	}
	
	public static double dotProduct(Vector3 a, Vector3 b) {
		double result = (a.x*b.x) + (a.y*b.y)+ (a.z*b.z);
		return result;
	}
	
	public static Vector3 crossProduct(Vector3 a, Vector3 b) {
        Vector3 result = new Vector3((a.y * b.z) - (a.z * b.y), (a.z * b.x) - (a.x * b.z), (a.x * b.y) - (a.y * b.x));
        return result;
    }
	
	public static float angleBetween(Vector3 a, Vector3 b) {
		return (float)Math.acos(dotProduct(a, b) / (magnitude(a) * magnitude(b)));
	}
	
	public static Vector3 sum(Vector3 a, Vector3 b) {
        Vector3 result = new Vector3(a.x + b.x, a.y + b.y, a.z + b.z);
        return result;
    }
	
	public static Vector3 sum3(Vector3 a, Vector3 b, Vector3 c) {
        Vector3 result = new Vector3(a.x + b.x + c.x, a.y + b.y + c.y, a.z + b.z + c.z);
        return result;
    }
	
	public static Vector3 substract(Vector3 a, Vector3 b) {
        Vector3 result = new Vector3(a.x - b.x, a.y - b.y, a.z - b.z);
        return result;
    }
	
	public static Vector3 scale(double constant, Vector3 a) {
		Vector3 result = new Vector3(constant * a.x, constant * a.y, constant * a.z);
		return result;
	}
	public static Vector3 invert(Vector3 a) {
		return new Vector3(-1*a.x, -1*a.y, -1*a.z);
	}
	
	public static double magnitude(Vector3 a) {
        return Math.sqrt((a.x * a.x) + (a.y * a.y) + (a.z * a.z));
    }

	public static Vector3 toUnit(Vector3 a) {
		Vector3 result = new Vector3(a.x / Vector3.magnitude(a), a.y / Vector3.magnitude(a), a.z / Vector3.magnitude(a));
        return result;
	}
	public static void printMe(Vector3 a) {
		System.out.println("(" + a.x + ", " + a.y + ", " + a.z + ")");
	}
	
	public Vector3 clone(){
        return new Vector3(getX(), getY(), getZ());
    }
    
    public static Vector3 ZERO(){
        return ZERO.clone();
    }
    
    public static Vector3 FORWARD(){
        return FORWARD.clone();
    }
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
}
