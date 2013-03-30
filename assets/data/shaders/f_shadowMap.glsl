#ifdef GL_ES
precision highp float; 
#endif

varying vec4 v_lightSpacePosition;
varying vec2 v_texCoord0;

uniform sampler2D s_shadowMap;
uniform sampler2D u_texture;

float unpack(vec4 packedZValue)
{	
	const vec4 unpackFactors = vec4( 1.0 / (256.0 * 256.0 * 256.0), 1.0 / (256.0 * 256.0), 1.0 / 256.0, 1.0 );
	return dot(packedZValue,unpackFactors);
}

float getShadowFactor(vec4 lightZ)
{
	vec4 packedZValue = texture2D(s_shadowMap, lightZ.st);
	float unpackedZValue = unpack(packedZValue);
	return float(unpackedZValue > lightZ.z);
}

void main(void) 
{	
	vec4 texCol = texture2D(u_texture, v_texCoord0);
	if(texCol.a < 0.5) discard;
	float shadowFactor=1.0;				
	vec4 lightZ = v_lightSpacePosition / v_lightSpacePosition.w;
	lightZ = (lightZ + 1.0) / 2.0;
	shadowFactor = getShadowFactor(lightZ);

	gl_FragColor = vec4(0.7,0.0,0.0,1.0)*shadowFactor;
}