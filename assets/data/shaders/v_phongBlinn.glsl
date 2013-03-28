//BLINN_PHONG VERTEX SHADER
//Zambzi

attribute vec4 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;

uniform mat4 u_ModelViewMatrix;
uniform mat4 u_ProjectionMatrix;

varying vec3 v_normal;
varying vec3 v_eyeVec;
varying vec2 v_texCoord0;
varying vec4 v_position;

void main()
{
	vec4 pos = a_position;
	vec3 normal = a_normal;
	
	vec3 eyeVec = vec3(u_ProjectionMatrix*vec4(normal, 0.0));
	eyeVec = eyeVec/length(eyeVec);
	v_eyeVec = eyeVec;
	
	vec4 position = u_ModelViewMatrix * a_position;
	gl_Position = position;
	v_texCoord0 = a_texCoord0;
}
	