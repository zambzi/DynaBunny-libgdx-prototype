#ifdef GL_ES
precision highp float; 
#endif

#define MAX_LIGHTS 3

varying vec4 v_lightSpacePosition;
varying vec2 v_texCoord0;
varying vec3 v_eyeVec;

uniform sampler2D s_shadowMap;
uniform sampler2D u_texture;
uniform vec3 direction[MAX_LIGHTS];
uniform vec4 ambientColor[MAX_LIGHTS];
uniform vec4 diffuseColor[MAX_LIGHTS];
uniform vec4 specularColor[MAX_LIGHTS];
uniform vec3 u_camDirection;

struct Material {
	vec4 ambientFactor;
	vec4 diffuseFactor;
	vec4 specularFactor;
	float shininess;
};
uniform Material u_material;

float unpack(vec4 packedZValue)
{	
	//const vec4 unpackFactors = vec4( 1.0 / (256.0 * 256.0 * 256.0), 1.0 / (256.0 * 256.0), 1.0 / 256.0, 1.0 );
	const vec4 unpackFactors = vec4(0.00000006, 0.000015259, 0.00390625, 1);
	return dot(packedZValue,unpackFactors);
}

float getShadowFactor(vec4 lightZ)
{
	vec4 packedZValue = texture2D(s_shadowMap, lightZ.st);
	float unpackedZValue = unpack(packedZValue);
	return float(unpackedZValue > lightZ.z);
}

float addShadows()
{
	float shadowFactor=1.0;	
	vec4 lightZ = v_lightSpacePosition / v_lightSpacePosition.w;
	lightZ = (lightZ + 1.0) / 2.0;
	shadowFactor = getShadowFactor(lightZ);
	return shadowFactor;
}

vec4 addPhongBlinn()
{
	vec3 viewDir = normalize(-u_camDirection);
	vec4 ambientLight = vec4(0.0);
	vec4 diffuseLight = vec4(0.0);
	vec4 specularLight = vec4(0.0);
	
	int i = 0;
	while(i<MAX_LIGHTS){
		
		float diffValue = max(0.0, dot(v_eyeVec, direction[i]));
		vec3 halfVector = normalize(direction[i] + viewDir);
		float specValue = max(0.0, dot(v_eyeVec, halfVector));
		
		ambientLight += ambientColor[i]*u_material.ambientFactor;
		diffuseLight += diffValue * diffuseColor[i] * u_material.diffuseFactor;
		if(specValue > 0.0 && u_material.shininess != 0.0){
			specularLight += pow(specValue, u_material.shininess)*specularColor[i]*u_material.specularFactor;
		}
		++i;
	}
	
	vec4 light = ambientLight+diffuseLight+specularLight;
	return light;
}

vec4 addTexture(void){
	vec4 texCol = texture2D(u_texture, v_texCoord0);
	return texCol;
}

void main(void) 
{	
	vec4 texCol = addTexture();
				
	if(texCol.a < 0.5) discard;
	float shadow = 1.0;
	shadow = addShadows();
	if(shadow==0.0) shadow+=0.4;

	gl_FragColor = texCol*shadow*addPhongBlinn();
}