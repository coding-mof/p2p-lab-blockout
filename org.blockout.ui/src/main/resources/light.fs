uniform sampler2D Texture0;

uniform vec2 PlayerPos;
uniform float InnerSquaredDistance;
uniform float OuterSquaredDistance;
 
void main(void)
{
 	vec2 TexCoord = vec2( gl_TexCoord[0] );
 
 	float distance = ((gl_FragCoord.x - PlayerPos.x)*(gl_FragCoord.x - PlayerPos.x)) + ((gl_FragCoord.y - PlayerPos.y)*(gl_FragCoord.y - PlayerPos.y));
	float factor = 0.6;
	if(distance < OuterSquaredDistance) {
		factor = 1.6 - (distance / OuterSquaredDistance);
	} else if(distance < InnerSquaredDistance) {
		factor = 1.0;
	}

 
 	gl_FragColor  = texture2D(Texture0, TexCoord) * factor;
}