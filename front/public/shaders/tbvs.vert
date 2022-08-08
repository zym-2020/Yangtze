#version 300 es

layout(location=0) in float index;
layout(location=1) in vec4 a_pos;

uniform sampler2D symbolTexture;

uniform mat4 u_matrix;
uniform mat4 u_symbolMatrix;

uniform vec2 u_mercatorCenterHigh;
uniform vec2 u_mercatorCenterLow;


out vec3 fragColor;

vec2 translateRelativeToEye(vec2 high, vec2 low)
{
    vec2 highDiff = high - u_mercatorCenterHigh;
    vec2 lowDiff = low - u_mercatorCenterLow;

    return highDiff + lowDiff;
}

void main() {
    float width = 256.0;
    float bunchSize = 8.0;
    float bunchColCount = width / bunchSize;

    float originRow = float(floor(index / bunchColCount)) * bunchSize;
    float originCol = mod(index, bunchColCount) * bunchSize;
    float dRow = float(floor(float(gl_VertexID + 1) / bunchSize));
    float dCol = mod(float(gl_VertexID + 1), bunchSize);
    
    float colorU = (originCol + 0.5) / width;
    float colorV = (originRow + 0.5) / width;
    float posU = (originCol + dCol + 0.5) / width;
    float posV = (originRow + dRow + 0.5) / width;

    vec4 posColor = texture(symbolTexture, vec2(posU, posV));

    float x = (posColor.r / 255.0 + posColor.b) * 2.0 - 1.0;
    float y = (posColor.g / 255.0 + posColor.a) * 2.0 - 1.0;

    vec4 symbolOffset_ss = u_symbolMatrix * vec4(x, -y, 0.0, 1.0);
    vec4 geoPos_cs = u_matrix * vec4(translateRelativeToEye(a_pos.xy, a_pos.zw), 0.0, 1.0);

    gl_Position = vec4(geoPos_cs.xy + symbolOffset_ss.xy * geoPos_cs.w, geoPos_cs.zw);
    fragColor = texture(symbolTexture, vec2(colorU, colorV)).rgb;
}