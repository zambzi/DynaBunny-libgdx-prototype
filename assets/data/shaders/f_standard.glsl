#ifdef GL_ES
precision mediump float;
#endif

#define MAX_LIGHTS 8

uniform vec3 direction[MAX_LIGHTS];
uniform vec4 ambientColor[MAX_LIGHTS];
uniform vec4 diffuseColor[MAX_LIGHTS];
uniform vec4 specularColor[MAX_LIGHTS];
uniform sampler2D u_texture;
uniform int u_numLights;
uniform vec3 u_camDirection;

struct Material {
	vec4 ambientFactor;
	vec4 diffuseFactor;
	vec4 specularFactor;
	float shininess;
};
uniform Material u_material;

varying vec3 v_eyeVec;
varying vec2 v_texCoord0;

void main()
{
	vec3 viewDir = normalize(-u_camDirection);
	vec4 ambientLight = vec4(0.0);
	vec4 diffuseLight = vec4(0.0);
	vec4 specularLight = vec4(0.0);
	
	int i = 0;
	while(i<u_numLights || i<MAX_LIGHTS){
		
		float diffValue = max(0.0, dot(v_eyeVec, direction[i]));
		vec3 halfVector = normalize(direction[i] + viewDir);
		float specValue = max(0.0, dot(v_eyeVec, halfVector));
		
		ambientLight += ambientColor[i]*u_material.ambientFactor;
		diffuseLight += diffValue * diffuseColor[i] * u_material.diffuseFactor;
		if(specValue > 0.0){
			specularLight += pow(specValue, u_material.shininess)*specularColor[i]*u_material.specularFactor;
		}
		++i;
	}
	
	vec4 light = ambientLight+diffuseLight+specularLight;
	vec4 texCol = texture2D(u_texture, v_texCoord0);
	gl_FragColor = light*texCol;
		
}