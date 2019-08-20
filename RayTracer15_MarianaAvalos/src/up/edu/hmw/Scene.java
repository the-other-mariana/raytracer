package up.edu.hmw;

import java.awt.Color;
import java.util.ArrayList;

public class Scene {
	private Camera camera;
    private ArrayList<Object3D> objects;
    private ArrayList<Light> lights;
    private Color ambientCoef;

    /**
     * class that stores all scene data: lights, ambient level, camera, objects
     */
    public Scene(){
        setObjects(new ArrayList<Object3D>());
        setLights(new ArrayList<Light>());
    }
    
    public void addObject(Object3D object){
        getObjects().add(object);
    }
    
    public ArrayList<Object3D> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<Object3D> objects) {
        this.objects = objects;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
    
    public void addLight(Light light) {
    	this.lights.add(light);
    }
    public void setLights(ArrayList<Light> lights) {
        this.lights = lights;
    }
    public ArrayList<Light> getLights(){
    	return lights;
    }

	public Color getAmbientCoef() {
		return ambientCoef;
	}

	public void setAmbientCoef(Color ambientCoef) {
		this.ambientCoef = ambientCoef;
	}
}
