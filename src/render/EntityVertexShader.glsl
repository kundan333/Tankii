#version 400 core
//140

in vec3 position; //input
in vec2 textureCoords;
in vec3 normals;


out vec2 pass_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector[4];
out vec3 toCameraVector;
out float visibility;

uniform int numberOfRows;
uniform vec2 OffSet;


uniform  mat4 transformationMatrix;
//4by4 matrix 4D

uniform  mat4 projectionMatrix;
uniform  mat4 viewMatrix;
uniform  vec3 lightPosition[4];
uniform float useFakeLighting;

const float density = 0.0035;
const float gradient = 5;

uniform vec4 plane ;


void main(void){

		vec4 worldPosition = transformationMatrix * vec4(position,1.0);
		//worldPosition is position of object with transformationMatrix
		vec4 positionRelativeToCam = viewMatrix * worldPosition;

		gl_ClipDistance[0]= dot(worldPosition,plane);

	//This is important---- Matrix must be in correct order if they are not result will be different----

		gl_Position = projectionMatrix * positionRelativeToCam;

	    pass_textureCoords =  textureCoords/numberOfRows + OffSet ;

	    vec3 actualNormal = normals;
	    if(useFakeLighting > 0.5){

	    actualNormal = vec3(0.0,1.0,0.0);

	    }

	    surfaceNormal = (transformationMatrix * vec4(actualNormal, 0.0)).xyz;


	    //transformationMatrix is 4D w is included for more search on internet

	    for(int i=0;i<4;i++){

	    toLightVector[i] = lightPosition[i] - worldPosition.xyz;

	    }

	    //worldPosition is 4D vector

	    float distance = length(positionRelativeToCam.xyz);
	    visibility = exp(-pow((distance*density),gradient));
	    visibility = clamp(visibility,0.0,1.0);

	    toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;





}
