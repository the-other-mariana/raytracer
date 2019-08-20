package up.edu.hmw;

import java.awt.Color;


public abstract class Object3D {
	private Material material;
	private Vector3 position;
    private Color color;

    /**
     * constructor
     * @param position
     * @param color
     * @param material
     */
    public Object3D(Vector3 position, Color color, Material material){
        setPosition(position);
        setColor(color);
        setMaterial(material);
    }
    
    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    public abstract Intersection getIntersection(Ray ray, Camera camera);

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}
    
}
