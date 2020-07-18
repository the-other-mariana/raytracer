# Raytracing Algorithm for Object Visualization

This project consists of a raytracer implementation in Java. It renders .obj files and spheres with either a Lambert or Blinn-Phong shaders. The program includes reflection (recursive), refraction, multiple lights, shadows (optional) and also affine transformations of the objects such as three-axis rotation, scaling and translation.

## Specifications

Java version (JRE or JDK): `jre1.8.0_241`<br />
Execution environment: `JavaSE-1.8`<br />


## Usage

Download the folder `RayTracer15_MarianaAvalos` and store it in your computer. This is all the source code.<br />
Import the folder to Eclipse IDE.<br />
Run `RayTracing.java`.<br />
On the `files` folder you can add .OBJ files to render.<br />
On the class `SampleScene.java` you can change the objects that are rendered in the scene.<br />

The pdf file `RayTracingReport_MarianaAvalos.pdf` explains the theory I used for the rendering algorithm.

## Sample output
The program outputs three different scenes. <br />The first scene focuses mainly on the rendering of refraction. This is the output of the first scene:<br />

![alt text](https://github.com/the-other-mariana/raytracer/blob/master/RayTracer15_MarianaAvalos/test1.png?raw=true)<br />

The second scene focuses on reflection, potentially recursive when you have two reflective objects in front of each other. The output of the second scene is the following:<br />

![alt text](https://github.com/the-other-mariana/raytracer/blob/master/RayTracer15_MarianaAvalos/test2.png?raw=true)<br />

The third scene focuses on rendering reflection and refraction combined in one material. For this, I coded a loop for 360 frames and created a video that shows how translation, rotation and scaling is also implemented as one of the features. Download the output video [here](https://github.com/the-other-mariana/raytracer/blob/master/prisms-video.mp4). However, a sample frame from that video is stored in this repo and its gif version is the following: <br />

![alt text](https://github.com/the-other-mariana/raytracer/blob/master/prisms-gif.gif)<br />

## Recognition

This is one of my favorite projects and I am very honored that it was chosen for display at SIGGRAPH 2019 Student Work Exhibit, celebrated in Los Angeles, California. <br />

![alt text](https://github.com/the-other-mariana/raytracer/blob/master/SIGGRAPH-2019.png?raw=true)<br />




