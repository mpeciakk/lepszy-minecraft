varying vec3 position;
varying vec2 uv;

#section VERTEX_SHADER

layout (location = 0) in vec2 inPosition;
layout (location = 1) in vec2 inUv;
layout (location = 2) in int data;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

vec3 rotateQuad(vec2 pos, int normal) {
    vec3 rotatedPos;

    if (normal == 0) {
        rotatedPos = vec3(pos.x, pos.y, 0.5);
    } else if (normal == 1) {
        rotatedPos = vec3(-pos.x, pos.y, -0.5);
    } else if (normal == 2) {
        rotatedPos = vec3(-0.5, pos.y, -pos.x);
    } else if (normal == 3) {
        rotatedPos = vec3(0.5, pos.y, pos.x);
    } else if (normal == 4) {
        rotatedPos = vec3(pos.x, 0.5, -pos.y);
    } else if (normal == 5) {
        rotatedPos = vec3(pos.x, -0.5, pos.y);
    }

    return rotatedPos;
}

void main() {
    int x = data & 0x1F;
    int y = (data >> 5) & 0x1F;
    int z = (data >> 10) & 0x1F;
    int n = (data >> 15) & 0x1F;
    position = rotateQuad(inPosition, n) + vec3(x, y, z);
    uv = inUv;

    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
}

#section FRAGMENT_SHADER

out vec4 FragColor;

uniform sampler2D textureSampler;

void main() {
    FragColor = vec4(1.0, 1.0, 1.0, 1.0);
}