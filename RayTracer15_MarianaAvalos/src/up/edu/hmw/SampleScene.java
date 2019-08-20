package up.edu.hmw;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class SampleScene {
	
	/**
	 * This class enables the user to choose which scene to render
	 */
	public SampleScene() {
		
	}
	
	public static Scene getSampleScene(int number) throws IOException{
		
		Scene sample = new Scene();
		
		switch(number) {
		case 1:
			sample = getScene01();
			break;
		case 2:
			sample = getScene02();
			break;
		case 3:
			sample = getScene03();
			break;
		default:
			System.out.println("Not a valid scene index.");
			break;
			
		}
		
		return sample;
	}
	
	/**
	 * each of these functions returns an example scene
	 * @return scene
	 * @throws IOException
	 */
	public static Scene getScene01() throws IOException{
		
		PointLight light3 = new PointLight(new Vector3(1.8, 6.0, -2), Vector3.ZERO(), Color.WHITE, 28.0);
		PointLight light2 = new PointLight(new Vector3(0.0, 5.0, -4), Vector3.ZERO(), Color.WHITE, 8.0);

		Scene scene01 = new Scene();
		scene01.addLight(light2);
		scene01.addLight(light3);
		scene01.setAmbientCoef(Color.WHITE);
		
		scene01.setCamera(new Camera(new Vector3(0, 0, -8f), 160, 160, 1300, 1300, -6, 300));
		
		Lambert planeMat = new Lambert(true, 1.0, false, 0.0, 0.0, false, 0.0, 0.0, true, false);
		Phong colMat =  new Phong(true, scene01.getCamera(), 1.0, 100.0, 300.0, false, 0.0, 0.01, true, 1.5, 0.5, true, false);
		Lambert stairsMat =  new Lambert(true, 1.0, false, 0.0, 0.01, true, 1.05, 0.5, true, false);
		
		ObjReader plane1 = new ObjReader("files/" + "plane2.obj", Color.RED, new Vector3(-0.1, -2.3, 1), new Vector3(0.0, 0.0, 0.0), new Vector3(1.0, 1.0, 1.0), planeMat);
		ObjReader stairs = new ObjReader("files/" + "stairs.obj",  Color.RED, new Vector3(3.0f, -3.0f, 0.0f), new Vector3(0.0, -65.0, 0.0), new Vector3(0.8, 0.8, 0.8), stairsMat);
		ObjReader column = new ObjReader("files/" + "column.obj",  Color.RED, new Vector3(-1.8f, 0.95f, -0.2f), new Vector3(0.0, -65.0, 0.0), new Vector3(0.5, 0.65, 0.5),colMat);
		ObjReader column2 = new ObjReader("files/" + "column.obj",  Color.RED, new Vector3(-0.1f, 1.8f, 4.0f), new Vector3(0.0, -65.0, 0.0), new Vector3(0.5, 0.85, 0.5), colMat);
		ObjReader column3 = new ObjReader("files/" + "column.obj",  Color.RED, new Vector3(2.5f, 1.8f, 5.0f), new Vector3(0.0, -65.0, 0.0), new Vector3(0.5, 0.85, 0.5), colMat);
		ObjReader teapot1 = new ObjReader("files/" + "teapot.obj",  new Color(230, 230, 230), new Vector3(1.3, -0.7, -1.5), new Vector3(-20.0, 20.0, -20.0), new Vector3(0.5, 0.5, 0.5), new Phong(true, scene01.getCamera(), 1.0, 1000.0, 300.0, true, 0.2, 0.0, false, 0.0, 0.0, false, false));
		
		scene01.addObject(column.createObject());
		scene01.addObject(column2.createObject());
		scene01.addObject(column3.createObject());
		scene01.addObject(stairs.createObject());
		scene01.addObject(plane1.createObject());
		scene01.addObject(teapot1.createObject());
		
		scene01.addObject(new Sphere(new Vector3(0.5f, 1.2f, 0.2f), 0.7f, Color.WHITE,  new Phong(false, scene01.getCamera(), 1.0, 1000.0, 300.0, false, 0.0, 0.0, true, 1.05, 0.5, true, false)));
		
		return scene01;
		
	}
	
	public static Scene getScene02() throws IOException{
		
		PointLight light3 = new PointLight(new Vector3(1.8, 6.0, -2), Vector3.ZERO(), Color.WHITE, 28.0);
		PointLight light2 = new PointLight(new Vector3(0.0, 5.0, -4), Vector3.ZERO(), Color.WHITE, 8.0);

		Scene scene01 = new Scene();
		scene01.addLight(light2);
		scene01.addLight(light3);
		scene01.setAmbientCoef(Color.WHITE);

		scene01.setCamera(new Camera(new Vector3(0, 0, -8f), 160, 160, 1300, 1300, -6, 300));
		
		Lambert planeMat = new Lambert(true, 1.0, true, 0.6, 0.0, false, 0.0, 0.0, true, true);
		Phong phongMat = new Phong(false, scene01.getCamera(), 1.0, 800.0, 100.0, true, 0.2, 0.01, false, 0.0, 0.0, true, true);
		Phong glassPhong = new Phong(true, scene01.getCamera(), 1.0, 800.0, 100.0, true, 0.2, 0.01, false, 0.0, 0.0, false, true);
		Phong simplePhong = new Phong(true, scene01.getCamera(), 1.0, 1000.0, 300.0, false, 0.0, 0.01, false, 0.0, 0.0, false, true);
		Phong refrPhong = new Phong(false, scene01.getCamera(), 1.0, 1000.0, 500.0, false, 0.0, 0.01, true, 1.05, 0.4, true, true);
		
		ObjReader plane1 = new ObjReader("files/" + "plane2.obj", Color.WHITE, new Vector3(-0.1, -2.3, 1), new Vector3(0.0, 0.0, 0.0), new Vector3(1.0, 1.0, 1.0), planeMat);
		ObjReader teapot2 = new ObjReader("files/" + "teapot.obj",  Color.GRAY, new Vector3(2.6, -2.15, 5.3), new Vector3(0.0, 55.0, 0.0), new Vector3(1.5, 1.5, 1.5), new Phong(true, scene01.getCamera(), 1.0, 800.0, 100.0, true, 0.2, 0.01, false, 0.0, 0.0, false, true));
		ObjReader hose = new ObjReader("files/" + "hose1.obj",  Color.PINK, new Vector3(-4.3f, -2.5f, 4.5f), new Vector3(0.0, 0.0, 0.0),new Vector3(1.5, 1.5, 1.5), glassPhong);
		ObjReader knot = new ObjReader("files/" + "knot2.obj", Color.ORANGE, new Vector3(-2.0, -1.5, 0.5), new Vector3(0.0, 0.0, 0.0),new Vector3(1.5, 1.5, 1.5), simplePhong);
		ObjReader hedra = new ObjReader("files/" + "hedra1.obj", new Color(255, 0, 157), new Vector3(2.65f, 0.0f, -1.0f), new Vector3(35.0, 15.0, -15.0),new Vector3(1.0, 1.0, 1.0), refrPhong);
		
		scene01.addObject(plane1.createObject());
		scene01.addObject(new Sphere(new Vector3(0.0f, -1.0f, 5.0f), 1.0f, Color.MAGENTA, phongMat));
		scene01.addObject(new Sphere(new Vector3(-2.0f, -1.0f, 4.5f), 1.0f, Color.YELLOW, phongMat));
		scene01.addObject(teapot2.createObject());
		scene01.addObject(knot.createObject());
		scene01.addObject(hose.createObject());
		scene01.addObject(hedra.createObject());
		
		return scene01;
	}
	
	public static Scene getScene03() throws IOException{
		
		PointLight light3 = new PointLight(new Vector3(-1.8, 5.0, -2), Vector3.ZERO(), Color.WHITE, 28.0);
		PointLight light2 = new PointLight(new Vector3(-1.0, 4.0, -4), Vector3.ZERO(), Color.WHITE, 8.0);

		Scene scene01 = new Scene();
		scene01.addLight(light2);
		scene01.addLight(light3);
		scene01.setAmbientCoef(Color.WHITE);
		
		scene01.setCamera(new Camera(new Vector3(0, 0, -8f), 160, 160, 1200, 1200, -6, 300));
		
		Lambert planeMat = new Lambert(true, 1.0, false, 0.6, 0.0, false, 0.0, 0.0, true, false);
		Phong glassPhong = new Phong(true, scene01.getCamera(), 1.0, 1000.0, 300.0, true, 0.4, 0.01, true, 1.1, 0.5, false, false);
		Phong glassPhong2 = new Phong(true, scene01.getCamera(), 1.0, 1000.0, 300.0, true, 0.4, 0.01, true, 1.1, 0.5, false, false);
		
		ObjReader plane1 = new ObjReader("files/" + "plane2.obj", new Color(0, 255, 229), new Vector3(-0.1, -2.3, 1), new Vector3(0.0, 0.0, 0.0),new Vector3(1.0, 1.0, 1.0), planeMat);
		ObjReader rflPrism1 = new ObjReader("files/" + "prism5.obj", Color.YELLOW, new Vector3(1.0, -1.5, 0.0), new Vector3(0.0, 45.0, 18.0), new Vector3(0.5, 0.4, 0.5), new Lambert(true, 1.0, false, 0.0, 0.01, false, 0.0, 0.0, true, false));
		ObjReader rflPrism2 = new ObjReader("files/" + "prism5.obj", Color.YELLOW, new Vector3(1.85, 0.0, 0.1), new Vector3(0.0, 45.0, 45.0), new Vector3(0.5, 0.35, 0.5), new Lambert(true, 1.0, false, 0.0, 0.01, false, 0.0, 0.0, true, false));
		ObjReader rfPrism1 = new ObjReader("files/" + "prism5.obj", Color.BLUE, new Vector3(-1.5, -2.0,4.0), new Vector3(0.0, 45.0, 0.0),new Vector3(1.0, 1.5, 1.0), new Lambert(true, 1.0, false, 0.0, 0.01, false, 0.0, 0.0, true, false));
		ObjReader rfPrism2 = new ObjReader("files/" + "prism5.obj", Color.BLUE, new Vector3(2.3, 2.0,4.0), new Vector3(0.0, 40.0, 0.0),new Vector3(1.0, 1.0, 1.0), new Lambert(true, 1.0, false, 0.0, 0.01, false, 0.0, 0.0, true, false));
		ObjReader glass1 = new ObjReader("files/" + "Cube.obj",  Color.WHITE, new Vector3(-0.8f, -2.5f, 0.5f), new Vector3(0.0, 40.0, 0.0),new Vector3(1.0, 1.8, 1.0), glassPhong);
		ObjReader glass2 = new ObjReader("files/" + "Cube.obj",  Color.WHITE, new Vector3(0.5, 0.8f, 0.5f), new Vector3(0.0, 50.0, -65.0),new Vector3(1.0, 1.8, 1.0), glassPhong2);

		scene01.addObject(rflPrism1.createObject());
		scene01.addObject(rflPrism2.createObject()); 
		scene01.addObject(rfPrism1.createObject());
		scene01.addObject(rfPrism2.createObject());
		scene01.addObject(glass1.createObject());
		scene01.addObject(glass2.createObject());
		scene01.addObject(plane1.createObject()); 
		
		return scene01;
	}
}
