attribute vec4 vPosition;
attribute vec4 vCoord;
attribute vec4 vCoord2;
uniform mat4 vMatrix;

varying vec2 textureCoordinate;
varying vec2 textureCoordinate2;

void main(){
    gl_Position = vMatrix*vPosition;;
    textureCoordinate = vCoord.xy;
    textureCoordinate2 = vCoord2.xy;
}