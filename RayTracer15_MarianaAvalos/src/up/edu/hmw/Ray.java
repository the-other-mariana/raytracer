package up.edu.hmw;

public class Ray {
	Vector3 origin;
	Vector3 direction;
	
	/**
	 * constructor
	 * @param origin
	 * @param direction
	 */
	public Ray(Vector3 origin, Vector3 direction) {
        setOrigin(origin);
        setDirection(direction);
    }

    public Vector3 getOrigin() {
        return origin;
    }

    public void setOrigin(Vector3 origin) {
        this.origin = origin;
    }

    public Vector3 getDirection() {
        return Vector3.toUnit(direction);
    }

    public void setDirection(Vector3 direction) {
        this.direction = direction;
    }
}
