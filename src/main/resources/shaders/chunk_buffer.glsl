varying vec3 position;

#section VERTEX_SHADER

layout (location = 0) in vec3 inPosition;
layout (location = 1) in int data;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

out vec3 color;

void main() {
    int x = data & 0xFF;
    int y = (data >> 8) & 0xFF;
    int z = (data >> 16) & 0xFF;
    int c = (data >> 24) & 0xFF;
    position = inPosition + vec3(x, y, z);
    color = vec3(0.0, 1.0, c / 255.0);

    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
}

#section FRAGMENT_SHADER

out vec4 FragColor;

in vec3 color;

void main() {
    FragColor = vec4(color, 1.0);
}