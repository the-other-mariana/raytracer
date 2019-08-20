package up.edu.hmw;

public class Intersection {
	private double distance;
    private Vector3 normal;
    private Vector3 position;
    private Object3D object;
    
    /**
     * Intersection class: stores main info about a hit point
     * constructor:
     * @param position
     * @param distance
     * @param normal
     * @param object
     */
    public Intersection(Vector3 position, double distance, Vector3 normal, Object3D object){
        setPosition(position);
        setDistance(distance);
        setNormal(normal);
        setObject(object);
    }

    public Object3D getObject() {
        return object;
    }

    public void setObject(Object3D object) {
        this.object = object;
    }
    
    public Vector3 getPosition() {
        return position;
    }

    private void setPosition(Vector3 position) {
        this.position = position;
    }

    public double getDistance() {
        return distance;
    }

    private void setDistance(double distance) {
        this.distance = distance;
    }

    public Vector3 getNormal() {
        return normal;
    }

    public void setNormal(Vector3 normal) {
        this.normal = normal;
    }
}
