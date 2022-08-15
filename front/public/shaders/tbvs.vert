#version 300 es

layout(location = 0) in vec4 a_pos;
layout(location = 1) in float rotation;
layout(location = 2) in vec2 scale;

uniform sampler2D symbolTexture;

uniform mat4 u_matrix;
uniform mat4 u_symbolMatrix;

uniform vec2 u_mercatorCenterHigh;
uniform vec2 u_mercatorCenterLow;

uniform vec2 u_bufferSize;
uniform int u_maxInstanceNum;


out vec3 fragColor;




vec2 translateRelativeToEye(vec2 high, vec2 low)
{
    vec2 highDiff = high - u_mercatorCenterHigh;
    vec2 lowDiff = low - u_mercatorCenterLow;

    return highDiff + lowDiff;
}


int rampColors[8] = int[](
0x3288bd,
0x66c2a5,
0xabdda4,
0xe6f598,
0xfee08b,
0xfdae61,
0xf46d43,
0xd53e4f
);

vec3 colorFromInt(int color)
{
    float b = float(color & 0xFF) / 255.0;
    float g = float((color >> 8) & 0xFF) / 255.0;
    float r = float((color >> 16) & 0xFF) / 255.0;

    return vec3(r, g, b);
}

vec3 idColor(float id)
{
    float bottomIndex = floor(id * 10.0);
    float topIndex = mix(bottomIndex + 1.0, 7.0, step(6.0, bottomIndex));
    float interval = mix(1.0, 4.0, step(6.0, bottomIndex));

    vec3 slowColor = colorFromInt(rampColors[int(bottomIndex)]);
    vec3 fastColor = colorFromInt(rampColors[int(topIndex)]);

    return mix(slowColor, fastColor, (id * 10.0 - float(bottomIndex)) / interval);
}

void main() {
    

    vec2 bBoxPos[6] = vec2[](
        vec2(-1.0f, -1.0f), vec2(1.0f, -1.0f), vec2(-1.0f, 1.0f),
        vec2(-1.0f, 1.0f), vec2(1.0f, -1.0f), vec2(1.0f, 1.0f)
    );

    vec2 uv[6] = vec2[](
        vec2(0.0f, 0.0f), vec2(1.0f, 0.0f), vec2(0.0f, 1.0f),
        vec2(0.0f, 1.0f), vec2(1.0f, 0.0f), vec2(1.0f, 1.0f)
    );

    float flag = 0.5;
    vec2 shipMesh[9] = vec2[](
        vec2(-1.0, 1.0),
        vec2(-0.0, 1.5),
        vec2(1.0, 1.0),

        vec2(-1.0, 1.0),
        vec2(1.0, 1.0),
        vec2(-1.0, -1.0),

        vec2(-1.0, -1.0),
        vec2(1.0, 1.0),
        vec2(1.0, -1.0)
    );


    // float width = 256.0;
    // float bunchSize = 8.0;
    // float bunchColCount = width / bunchSize;

    // float originRow = float(floor(index / bunchColCount)) * bunchSize;
    // float originCol = mod(index, bunchColCount) * bunchSize;
    // float dRow = float(floor(float(gl_VertexID + 1) / bunchSize));
    // float dCol = mod(float(gl_VertexID + 1), bunchSize);
    
    // float colorU = (originCol + 0.5) / width;
    // float colorV = (originRow + 0.5) / width;
    // float posU = (originCol + dCol + 0.5) / width;
    // float posV = (originRow + dRow + 0.5) / width;

    // vec4 posColor = texture(symbolTexture, vec2(posU, posV));

    // float x = (posColor.r / 255.0 + posColor.b) * 2.0 - 1.0;
    // float y = (posColor.g / 255.0 + posColor.a) * 2.0 - 1.0;

    // vec4 symbolOffset_ss = u_symbolMatrix * vec4(x, -y, 0.0, 1.0);
    float isStroke = mix(1.15, 1.0, step(1.0, float(gl_VertexID) / 9.0));
    vec2 screenScale = 20.0  *isStroke * vec2(0.5, 1.0) / u_bufferSize;
    vec2 symbolOffset_ss =  shipMesh[gl_VertexID % 9] * screenScale;

    float radius = radians(rotation);
    symbolOffset_ss = vec2(symbolOffset_ss.x * cos(radius) - symbolOffset_ss.y * sin(radius), symbolOffset_ss.x * sin(radius) + symbolOffset_ss.y * cos(radius));
    
    vec4 geoPos_cs = u_matrix * vec4(translateRelativeToEye(a_pos.xy, a_pos.zw), 0.0, 1.0);

    gl_Position = vec4(geoPos_cs.xy + symbolOffset_ss * geoPos_cs.w, geoPos_cs.zw);

    vec3 fillColor = idColor(float(gl_InstanceID) / 10000.0);
    vec3 strokeColor = vec3(58.0, 5.0, 58.0) / 255.0;
    fragColor = mix(strokeColor, fillColor, step(1.0, float(gl_VertexID) / 9.0));
}