#ifdef GL_ES
precision highp float; 
#endif

#define MAX_LIGHTS 3

varying vec4 v_lightSpacePosition;
varying vec2 v_texCoord0;
varying vec3 v_eyeVec;
varying mat4 v_ModelViewMatrix;
varying vec4 test;

uniform sampler2D s_shadowMap;
uniform sampler2D u_texture;
uniform vec4 direction[MAX_LIGHTS];
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
	//values: 1/256^3; 1/256^2; 1/256; 1
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

vec4 addPhongBlinn(float shadow)
{
	vec3 viewDir = normalize(-u_camDirection);
	vec4 ambientLight = vec4(0.0);
	vec4 diffuseLight = vec4(0.0);
	vec4 specularLight = vec4(0.0);
	
	int i = 0;
	while(i<MAX_LIGHTS){
		vec4 dir = normalize(direction[i]);
		float diffValue = abs(min(0.0, dot(v_eyeVec, dir.xyz)));
		vec3 halfVector = normalize(dir.xyz + viewDir);
		float specValue = max(0.0, dot(v_eyeVec, halfVector));
		
		ambientLight += ambientColor[i]*u_material.ambientFactor;
		diffuseLight += diffValue * diffuseColor[i] * u_material.diffuseFactor;
		if(specValue > 0.0 && u_material.shininess != 0.0){
			specularLight += pow(specValue, u_material.shininess)*specularColor[i]*u_material.specularFactor;
		}
		++i;
	}
	//shadow +=1.0; //uncomment to hide shadows
	vec4 light = (shadow==0.0 ? ambientLight: ambientLight+diffuseLight+specularLight);
	//light*=0.001; //uncomment to disable Phong-Blinn Lights
	//light+=0.999;
	return light;
}

vec4 addTexture(void){
	vec4 texCol = texture2D(u_texture, v_texCoord0);
	return texCol;
}

void main(void) 
{	
	float shadow = 1.0;
	vec4 color = addTexture();
	//color*=0.001; //uncomment to disable textures
	//color+=0.999;
	if(color.a < 0.5) discard; //walkaround for transparency blending
	shadow = addShadows();
	color *= addPhongBlinn(shadow);
	//color *= shadow; //uncomment to enable black test-shadows
	//color *= test; // uncomment to enable colorized normal test
	gl_FragColor = color;
}
