#version 330

layout(point = 0) in vec3 v_pos;
layout(point = 1) in vec3 v_norm;
layout(point = 2) in vec2 v_tex;

uniform vec4 u_map_size;
uniform vec2 u_vert_range;
uniform mat4 u_model_trans;

out vec2 f_uv;
out vec3 f_norm;

void main() {
    vec4 pos = u_model_trans * vec4(v_pos, 1);
    pos = vec4(pos.xzy, 1);
    pos.xy += u_map_size.xy;
    pos.xy *= u_map_size.zw;

    float scale = u_vert_range.y / (u_vert_range.y - u_vert_range.x);

    pos.z = u_vert_range.y - pos.z;
    pos.z /= (u_vert_range.y - u_vert_range.x);
    pos.xy /= pos.z;
    pos.xy *= scale;

    f_uv = v_tex;
    f_norm = normalize((u_model_trans * vec4(v_norm, 0)).xzy);

    gl_Position = pos;
}
