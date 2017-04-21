#version 120

attribute vec4 vPosition;
attribute vec3 vNormal;
attribute vec2 vTexCoord;

// Light position is given in world space
uniform vec3 theta;
uniform vec4 lightPosition;
uniform float isTex;

varying vec3 lPos;
varying vec3 vPos;
varying vec3 vNorm;
varying vec2 textureCoord;

//The vertex shader
//Implemented by Stephen Yingling
void main()
{
     vec3 angles = radians (theta);
        vec3 c = cos (angles);
        vec3 s = sin (angles);

        // rotation matrices
        mat4 rx = mat4 (1.0,  0.0,  0.0,  0.0,
                        0.0,  c.x,  s.x,  0.0,
                        0.0, -s.x,  c.x,  0.0,
                        0.0,  0.0,  0.0,  1.0);

        mat4 ry = mat4 (c.y,  0.0, -s.y,  0.0,
                        0.0,  1.0,  0.0,  0.0,
                        s.y,  0.0,  c.y,  0.0,
                        0.0,  0.0,  0.0,  1.0);

        mat4 rz = mat4 (c.z, s.z,  0.0,  0.0,
                        -s.z,  c.z,  0.0,  0.0,
                        0.0,  0.0,  1.0,  0.0,
                        0.0,  0.0,  0.0,  1.0);

        gl_Position = rz * ry * rx * vPosition;

    //textured
    if(isTex >0.5){
        textureCoord = vTexCoord;
    }
    
    else{
        mat4 modelMatrix = rx*ry*rz;

        mat4 viewMatrix = mat4 (1.0,  0.0,  0.0,  0.0,
                                0.0,  1.0,  0.0,  0.0,
                                0.0,  0.0,  1.0,  0.0,
                                0.0,  0.0,  0.0,  1.0);

        mat4 projMatrix = mat4 (1.0,  0.0,  0.0,  0.0,
                                0.0,  1.0,  0.0,  0.0,
                                0.0,  0.0,  1.0,  0.0,
                                0.0,  0.0,  0.0,  1.0);

        mat4 modelViewMatrix = viewMatrix * modelMatrix;

        // All vectors need to be converted to eye space and to be normalized
        vec4 vertexInEye = modelViewMatrix * vPosition;
        vec4 lightInEye = viewMatrix * lightPosition;
        vec4 normalInEye = normalize(modelViewMatrix * vec4(vNormal, 0.0));

        // pass our vertex data to the fragment shader
        lPos = lightInEye.xyz;
        vPos = vertexInEye.xyz;
        vNorm = normalInEye.xyz;

        // convert to clip space (like a vertex shader should)
        gl_Position =  projMatrix * viewMatrix *modelMatrix * vPosition;
    }


}
