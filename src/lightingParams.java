/**
 *
 * lightingParams.java
 *
 * Simple class for setting up the viewing and projection transforms
 * for the Shading Assignment.
 *
 * Students are to complete this class.
 *
 */


import javax.media.opengl.GL2;

public class lightingParams
{
    // lighting parameter
	float lightColor[] = {1.0f, 1.0f, 1.0f, 1.0f};
    float lightpos[] = {0.5f,5.0f, 2.0f, 1.0f};
    float diffuse[] = {1f, 0.0f, 0.0f, 1.0f};
    float specLight[] = {1.0f,1.0f,1.0f,1.0f};
    int specularExponent= 10;
    
    /**
     * constructor
     */
    public lightingParams()
    {
        
    }

    public void setDiffuse(float[] diffuseColor){
            diffuse = diffuseColor;
    }

    public void setLightpos(float[] lightPostion){
            lightpos = lightPostion;
    }

    public void setSpecularExponent(int spExponent){
        specularExponent = spExponent;
    }
    /**
     * This functions sets up the lighting, material, and shading parameters
     * for the Phong shader.
     *
     * You will need to write this function, and maintain all of the values
     * needed to be sent to the vertex shader.
     *
     * @param program - The ID of an OpenGL (GLSL) shader program to which
     * parameter values are to be sent
     *
     * @param gl2 - GL2 object on which all OpenGL calls are to be made
     *
     */
    public void setUpPhong (int program, GL2 gl2)
    {
        int lightP = gl2.glGetUniformLocation( program , "lightPosition" );
        int lightC = gl2.glGetUniformLocation( program , "lightColor" );
        int diffC = gl2.glGetUniformLocation( program , "diffuseColor" );
        int spec = gl2.glGetUniformLocation( program , "specColor" );
        int specEx= gl2.glGetUniformLocation( program , "specExp" );
        int isTexture = gl2.glGetUniformLocation(program,"isTex");

        gl2.glUniform4fv( lightP , 1 , lightpos, 0 );
        gl2.glUniform4fv( lightC , 1 , lightColor, 0 );
        gl2.glUniform4fv( diffC , 1 , diffuse, 0 );
        gl2.glUniform4fv( spec , 1 , specLight, 0 );
        gl2.glUniform1i( specEx, specularExponent);
        gl2.glUniform1f( isTexture, 0);
 
    }
}


