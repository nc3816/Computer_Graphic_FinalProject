/**
 *
 * textureParams.java
 *
 * Simple class for setting up the textures for the textures
 * Assignment.
 *
 *
 */

import com.jogamp.opengl.util.texture.*;
import javax.media.opengl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class textureParams
{
	Texture textureID;
	/**
	 * constructor
	 */
	public textureParams()
	{
        
	}
    
    /**
     * This functions loads texture data to the GPU.
     *
     * You will need to write this function, and maintain all of the values needed
     * to be sent to the various shaders.
     *
     * @param filename - The name of the texture file.
     *
     */
    public void loadTexture (String filename)
    {
        try{
            InputStream stream = new FileInputStream(filename);
            textureID = TextureIO.newTexture(stream,false,"jpg");
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    
    /**
     * This functions sets up the parameters
     * for texture use.
     *
     * You will need to write this function, and maintain all of the values needed
     * to be sent to the various shaders.
     *
     * @param program - The ID of an OpenGL (GLSL) program to which parameter values
     *    are to be sent
     *
     * @param gl2 - GL2 object on which all OpenGL calls are to be made
     *
     */
    public void setUpTextures (int program, GL2 gl2)
    {
    	textureID.enable(gl2);
    	textureID.bind(gl2);
        int isTex = gl2.glGetUniformLocation(program,"isTex");
        gl2.glUniform1f(isTex,1);
    }
}
