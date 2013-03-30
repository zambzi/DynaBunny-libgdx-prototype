attribute vec3 a_position;
attribute vec2 a_texCoord0;
 
varying vec4 v_position;
varying vec2 v_texCoord0;

uniform mat4 u_lightView;

void main(void) 
{   
	gl_Position =  u_lightView * vec4(a_position,1.0) ;
	v_position = gl_Position;
	v_texCoord0 = a_texCoord0;
}