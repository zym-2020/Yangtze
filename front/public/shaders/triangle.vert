#version 300 es

layout(location=0) in vec3 a_pos;
layout(location=1) in vec3 a_texcoord;

uniform mat4 u_model;
uniform mat4 u_view;
uniform mat4 u_projection;

void main()
{
    vec3[3] vertices = vec3[3](
        vec3(-0.5, -0.5, 0.0),
        vec3(0.5, -0.5, 0.0),
        vec3(0.0, 0.5, 0.0)
    );

    // gl_Position = u_projection * u_view * u_model * vec4(vertices[gl_VertexID], 1.0);
    gl_Position = u_projection * u_view * u_model * vec4(a_pos, 1.0);
}