#ifdef GL_ES
precision highp float; 
#endif

#define MAX_LIGHTS 3

attribute vec3 a_position;
attribute vec2 a_texCoord0;
attribute vec3 a_normal;

varying vec2 v_texCoord0;
varying vec3 v_eyeVec;
varying vec4 v_lightSpacePosition;
varying mat4 v_ModelViewMatrix;

uniform mat3 u_normalMatrix;
uniform mat4 u_ModelViewMatrix;
uniform mat4 u_lightView;
uniform mat4 u_lightProj;




void main(void) 
{
	
	gl_Position = u_ModelViewMatrix * vec4(a_position,1.0) ;
	
	v_lightSpacePosition  = u_lightView * vec4(a_position,1.0) ;
	
	v_texCoord0 = a_texCoord0;
	
	vec3 eyeVec = normalize(u_normalMatrix*a_normal);
	v_eyeVec = eyeVec;
	
	v_ModelViewMatrix = u_ModelViewMatrix;
}
