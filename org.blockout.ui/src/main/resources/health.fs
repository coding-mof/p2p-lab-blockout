uniform sampler2D Texture0;

uniform vec2  SphereCenter;
uniform float FillSquaredDistance;
uniform float GlowSquaredDistance;
uniform float MaxDist;
uniform vec3  Color;
 
void main(void)
{
 	vec2 TexCoord = vec2( gl_TexCoord[0] );
 
 	float distance = ((gl_FragCoord.x - SphereCenter.x)*(gl_FragCoord.x - SphereCenter.x)) + ((gl_FragCoord.y - SphereCenter.y)*(gl_FragCoord.y - SphereCenter.y));
	
	if(distance <= FillSquaredDistance) {
	
		// Apply Filling
		gl_FragColor  = texture2D(Texture0, TexCoord)  * vec4(Color, 1.0)  + (vec4(Color, 1.0) / 2.0);
		
	} else if(distance > GlowSquaredDistance) {
	
		// Outside of the health status
		gl_FragColor  = texture2D(Texture0, TexCoord);
		
	} else {
	
		// Glow Effect
		float range = GlowSquaredDistance-FillSquaredDistance;
		float real_dist = distance - FillSquaredDistance;
		float factor = (1.0- (real_dist / range));

		vec4 color = vec4(Color, factor);
		if(factor < 0.5) {
			gl_FragColor = texture2D(Texture0, TexCoord);
		} else {
			gl_FragColor =  (color * 1.5) * factor ;
		}
	}
}