varying vec3 position;
varying vec2 uv;

#section VERTEX_SHADER

layout (location = 0) in vec3 inPosition;
layout (location = 1) in vec2 inUV;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

void main() {
    position = inPosition;
    uv = inUV;

    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
}

#section FRAGMENT_SHADER

out vec4 FragColor;

uniform sampler2D textureSampler;

void main() {
    FragColor = texture(textureSampler, uv);
}