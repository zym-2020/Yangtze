#version 300 es
precision highp float;

layout(location=0) in float index;
layout(location=1) in vec2 offset;

uniform sampler2D symbolTexture;
uniform float u_pixelRatio;
uniform float u_scale;
uniform vec2 imageScale;

uniform vec2 u_NJ;

uniform mat4 u_matrix;
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

uniform float u_ProjectScale; // zoom level
uniform vec3 u_PixelsPerMerter;
uniform mat4 u_ModelMatrix;

uniform vec3 u_pixels_per_degree;
uniform vec3 u_pixels_per_degree2;
uniform vec2 u_viewport_center;
uniform vec4 u_viewport_center_projection;

uniform vec2 u_mercatorCenterX;
uniform vec2 u_mercatorCenterY;

float TILE_SIZE = 512.0;
float PI = 3.1415926536;
float WORLD_SCALE = TILE_SIZE / (PI * 2.0);


out vec3 fragColor;

vec2 translateRelativeToEye(vec2 high, vec2 low)
{
    vec2 highDiff = high - u_cameraPosHigh;
    vec2 lowDiff = low - u_cameraPosLow;
}

float[6] getDistanceScales(vec2 lnglat)
{
    float DEGREES_TO_RADIANS = PI / 180.0;
    // const RADIANS_TO_DEGREES = 180 / PI;
    float TILE_SIZE = 512.0;
    // Average circumference (40075 km equatorial, 40007 km meridional)
    float EARTH_CIRCUMFERENCE = 40.03e6;
    // Calculate scale from zoom if not provided
    float scale = pow(2.0, u_ProjectScale);

    float worldSize = TILE_SIZE * scale;
    float latCosine = cos(lnglat[1] * DEGREES_TO_RADIANS);

    float pixelsPerDegreeX = worldSize / 360.0;
    float pixelsPerDegreeY = pixelsPerDegreeX / latCosine;
    float altPixelsPerMeter = worldSize / EARTH_CIRCUMFERENCE / latCosine;    
  

  
    float latCosine2 = DEGREES_TO_RADIANS * tan(lnglat[1] * DEGREES_TO_RADIANS) / latCosine;
    float pixelsPerDegreeY2 = pixelsPerDegreeX * latCosine2 / 2.0;
    float altPixelsPerDegree2 = worldSize / EARTH_CIRCUMFERENCE * latCosine2;
  
    // Main results, used for converting meters to latlng deltas and scaling offsets
    return float[6](pixelsPerDegreeX, pixelsPerDegreeY, altPixelsPerMeter, 0.0, pixelsPerDegreeY2, altPixelsPerDegree2);
}

vec2 project_mercator_(vec2 lnglat)
{
    // return vec2((180.0 + lnglat.x), 180.0 - (180.0 / PI * log(tan(PI * 0.25 + lnglat.y * PI / 360.0)))) / 360.0;
    return vec2(
        radians(lnglat.x) + PI,
        PI - log(tan(PI * 0.25 + radians(lnglat.y) * 0.5))
        );
}

vec2 project_mercator_mapbox_(vec2 lnglat)
{
    return vec2((180.0 + lnglat.x), 180.0 - (180.0 / PI * log(tan(PI * 0.25 + lnglat.y * PI / 360.0)))) / 360.0;
}

vec4 project_offset_(vec4 offset) {
    float dy = offset.y;
    dy = clamp(dy, -1., 1.);
    vec3 pixels_per_unit = u_pixels_per_degree + u_pixels_per_degree2 * dy;
    return vec4(offset.xyz * pixels_per_unit, offset.w);
}

vec4 project_offset_inShader(vec4 pOffset)
{
    float[6] xyzComponents = getDistanceScales(u_viewport_center);

    float dy = pOffset.y;
    // dy = clamp(dy, -1., 1.);
    vec3 pixels_per_unit = vec3(xyzComponents[0], xyzComponents[1], xyzComponents[2]) + vec3(xyzComponents[3], xyzComponents[4], xyzComponents[5]) * dy;
    return vec4(pOffset.xyz * pixels_per_unit, pOffset.w);
}

vec4 ss_center_offset_(vec4 position)
{
    float X = position.x - u_viewport_center.x;
    float Y = position.y - u_viewport_center.y;

    // return project_offset_inShader(vec4(u_pixelRatio * 2.0 * X / imageScale.x, u_pixelRatio * 2.0 * Y / imageScale.y, position.zw));
    vec4 result = project_offset_inShader(vec4(X, Y, position.zw));
    result.x = u_pixelRatio * 2.0 * result.x / imageScale.x;
    result.y = u_pixelRatio * 2.0 * result.y / imageScale.y;
    return result;
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

    highp vec4 posColor = texture(symbolTexture, vec2(posU, posV));

    highp float x = (posColor.r / 255.0 + posColor.b) * 2.0 - 1.0;
    highp float y = (posColor.g / 255.0 + posColor.a) * 2.0 - 1.0;

    // vec2 standardScale = imageScale / max(imageScale.x, imageScale.y);
    vec2 standardScale = min(imageScale.x, imageScale.y) / imageScale;
    vec2 pos = vec2(x, y) * 0.000001 + offset;

    vec4 sPos = model * vec4(x, -y, 0.0, 1.0);
    sPos = vec4(sPos.x * standardScale.x, sPos.y * standardScale.y, 0.0, sPos.w);

    vec4 centerPos = u_matrix * vec4(project_mercator_mapbox_(u_viewport_center), 0.0, 1.0);
    highp vec4 geoPos = u_matrix * vec4(offset, 0.0, 1.0);
    vec3 pos_SS = ss_center_offset_(vec4(offset, 0.0, 1.0)).xyz;
    vec4 correctPos = vec4(pos_SS.xy * centerPos.w, 0.0, centerPos.w);
    // correctPos.z = u_viewport_center_projection.z;

    // geoPos = u_viewport_center_projection + cs_center_offset_(vec4(offset, 0.0, 1.0));

    // geoPos.w = u_viewport_center_projection.w;
    // geoPos = cs_center_offset_(geoPos);
    // geoPos = u_matrix * vec4(u_viewport_center, 0.0, 1.0);
    // geoPos = u_viewport_center_projection;

    gl_Position = vec4(pos, 0.0, 0.0) + geoPos;
    gl_Position = u_matrix * vec4(pos, 0.0, 1.0); 
    gl_Position = sPos + correctPos;
    gl_Position = sPos + geoPos;
    fragColor = texture(symbolTexture, vec2(colorU, colorV)).rgb;
}