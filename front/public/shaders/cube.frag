#version 300 es
precision highp float;

in vec2 v_texcoord;
in vec3 faceColor;

uniform sampler2D texture0;
uniform sampler2D texture1;

out vec4 fragColor;

void main() 
{
    vec4 color0 = texture(texture0, v_texcoord);
    vec4 color1 = texture(texture1, v_texcoord);
    fragColor = mix(color1, color0, 0.2);
    // fragColor = vec4(1.0);
    // fragColor = vec4(faceColor, 1.0);
}