#version 330

layout(point = 0) out vec4 o_color;

in vec2 f_uv;
in vec3 f_norm;

uniform sampler2D u_color_tex;
uniform vec3 u_color_mul;

void main() {
    float light = -dot(f_norm, vec3(1, 1, -2) / 2.449);
    if (light < 0) light = 0;
    light = light * .8 + .2;
    vec4 color = texture(u_color_tex, vec2(f_uv.x, 1 - f_uv.y)).gbar;
    color.rgb *= u_color_mul;
    color.rgb *= light;
    o_color = color;
}
