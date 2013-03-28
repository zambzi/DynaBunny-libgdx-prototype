#ifdef GL_ES
precision highp float; 
#endif

attribute vec3 a_position;

uniform mat4 u_ModelViewMatrix;
uniform mat4 u_lightView;

varying vec4 v_lightSpacePosition;


void main(void) 
{ 
	gl_Position = u_ModelViewMatrix * vec4(a_position,1.0) ;
	v_lightSpacePosition  = u_lightView * vec4(a_position,1.0) ;
}