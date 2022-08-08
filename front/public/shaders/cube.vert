#version 300 es

layout(location=0) in vec3 a_pos;
layout(location=1) in vec3 a_normal;
layout(location=2) in vec3 a_tangent;
layout(location=3) in vec2 a_texcoord;

uniform mat4 u_model;
uniform mat4 u_view;
uniform mat4 u_projection;

out vec2 v_texcoord;
out vec3 faceColor;

void main()
{
    gl_Position = u_projection * u_view * u_model * vec4(a_pos, 1.0);
    // gl_Position = vec4(a_pos, 1.0);
    v_texcoord = vec2(a_texcoord.x, 1.0 - a_texcoord.y);
    faceColor = vec3(float(gl_VertexID) * 1.0, float(gl_VertexID) * 5.0, float(gl_VertexID) * 12.0) / 36.0;
}