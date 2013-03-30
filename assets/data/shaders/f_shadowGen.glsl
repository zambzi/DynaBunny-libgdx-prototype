#ifdef GL_ES
precision highp float; 
#endif

varying vec4 v_position;
varying vec2 v_texCoord0;

uniform sampler2D u_texture;

void main(void)
{
	vec4 texCol = texture2D(u_texture, v_texCoord0);
	if(texCol.a < 0.5) discard;
	
	float normDistance  = v_position.z / v_position.w;
	normDistance = (normDistance + 1.0) / 2.0;
	normDistance += 0.001;
	
	const vec4 packFactors = vec4(256.0 * 256.0 * 256.0, 256.0 * 256.0, 256.0, 1.0);
	const vec4 bitMask     = vec4(0.0 , 1.0 / 256.0, 1.0 / 256.0, 1.0 / 256.0);
	vec4 packedValue = vec4(fract(packFactors*normDistance));
	packedValue -= packedValue.xxyz * bitMask;
	vec4 color = vec4(0,0.3,0,1);
	gl_FragColor  = packedValue;

}