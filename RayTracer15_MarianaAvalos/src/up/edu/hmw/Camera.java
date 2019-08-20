package up.edu.hmw;

import java.awt.Color;

public class Camera extends Object3D{

		private float[] fieldOfView = new float[2];
		private float defaultZ = 15f;
		private int[] resolution;
		private double zMinClippingPlane;
		private double zMaxClippingPlane;
		private double INFINITY = 300.0;
		
		/**
		 * constructor
		 * @param position
		 * @param fieldOfViewHorizontal
		 * @param fieldOfViewVertical
		 * @param widthResolution
		 * @param heightResolution
		 * @param zMinClippingPlane
		 * @param zMaxClippingPlane
		 */
		public Camera(Vector3 position, float fieldOfViewHorizontal, float fieldOfViewVertical, int widthResolution, int heightResolution, double zMinClippingPlane, double zMaxClippingPlane) {
			super(position, Color.black, null);
			setFieldOfViewHorizontal(fieldOfViewHorizontal);
			setFieldOfViewVertical(fieldOfViewVertical);
			setResolution(new int[] { widthResolution, heightResolution });
			setzMinClippingPlane(zMinClippingPlane);
			setzMaxClippingPlane(zMaxClippingPlane);
		}

		public double getINFINITY() {
			return INFINITY;
		}

		public void setINFINITY(double iNFINITY) {
			INFINITY = iNFINITY;
		}

		public double getzMaxClippingPlane() {
			return zMaxClippingPlane;
		}

		public void setzMaxClippingPlane(double zMaxClippingPlane) {
			this.zMaxClippingPlane = zMaxClippingPlane;
		}

		public double getzMinClippingPlane() {
			return zMinClippingPlane;
		}

		public void setzMinClippingPlane(double zMinClippingPlane) {
			this.zMinClippingPlane = zMinClippingPlane;
		}

		public float getDefaultZ() {
			return defaultZ;
		}

		public void setDefaultZ(float defaultZ) {
			this.defaultZ = defaultZ;
		}

		public int[] getResolution() {
			return resolution;
		}

		public void setResolution(int[] resolution) {
			this.resolution = resolution;
		}

		public float getFieldOfViewHorizontal() {
			return fieldOfView[0];
		}

		public float getFieldOfViewVertical() {
			return fieldOfView[1];
		}

		public void setFieldOfViewHorizontal(float fov) {
			fieldOfView[0] = fov;
		}

		public void setFieldOfViewVertical(float fov) {
			fieldOfView[1] = fov;
		}

		public float[] getFieldOfView() {
			return fieldOfView;
		}

		
		public Vector3[][] calculatePositionsToRay() {
			float angleMaxX = 90 - (getFieldOfViewHorizontal() / 2f);
			float radiusMaxX = getDefaultZ() / (float) Math.cos(Math.toRadians(angleMaxX));

			float maxX = (float) Math.sin(Math.toRadians(angleMaxX)) * radiusMaxX;
			float minX = -maxX;

			float angleMaxY = 90 - (getFieldOfViewVertical() / 2f);
			float radiusMaxY = getDefaultZ() / (float) Math.cos(Math.toRadians(angleMaxY));

			float maxY = (float) Math.sin(Math.toRadians(angleMaxY)) * radiusMaxY;
			float minY = -maxY;

			Vector3[][] positions = new Vector3[getResolution()[0]][getResolution()[1]];
			for (int x = 0; x < positions.length; x++) {
				for (int y = 0; y < positions[x].length; y++) {
					float posX = minX + (((maxX - minX) / (float) positions.length) * x);
					float posY = maxY - (((maxY - minY) / (float) positions[x].length) * y);
					float posZ = getDefaultZ();
					positions[x][y] = new Vector3(posX, posY, posZ);
				}
			}

			return positions;
		}
		
		@Override
		public Intersection getIntersection(Ray ray, Camera camera) {
			return new Intersection(Vector3.ZERO(), -1, Vector3.ZERO(), null);
		}
}
