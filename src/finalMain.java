/**
 *  
 *
 *  
 *
 */

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.Buffer;

public class finalMain implements GLEventListener, KeyListener
{

    /**
     * buffer info 
     */
    private int vbuffer[];
    private int ebuffer[];
    private int numVerts[];

    //parameter of angle and lighting
    public float angles[];
    public float lightPosition[];
    public int specialExponent;

    public int specUnit= 1;
    private float angleUnit = 5.0f;
    public int theta;

    /**
     * program and variable IDs
     */
    public int shaderProgID;
    // texture parameter
    public textureParams texture;

    /**
     * shape info
     */
    cgShape myShape;

    //Lighting parameter
    lightingParams PhongModel;

    /**
     * my canvas
     */
    GLCanvas myCanvas;
    /**
     * Called by the drawable immediately after the OpenGL context is
     * initialized. 
     */
    /**
     * constructor
     *
     * Rotation and light position added 
     */
    public finalMain(GLCanvas G)
    {
        vbuffer = new int [1];
        ebuffer = new int[1];
        numVerts = new int [1];
        //original rotation values
        angles = new float[]{-5.0f,-10.0f,5.0f};
        //original light position coordinates
        lightPosition = new float[]{0.6f,0.8f,1.0f,1.0f};
        specialExponent = 5;
        myCanvas = G;
        texture = new textureParams();
        PhongModel = new lightingParams();
        G.addGLEventListener (this);
        G.addKeyListener (this);
    }
    private void errorCheck (GL2 gl2)
    {
        int code = gl2.glGetError();
        if (code == GL.GL_NO_ERROR) 
            System.err.println ("All is well");
        else
            System.err.println ("Problem - error code : " + code);
            
    }
    /**
     * Displays a shade
     * @param gl2 The GL2 instance to use
     *
     */
    public void displayShade(GL2 gl2){
        gl2.glBindBuffer ( GL.GL_ARRAY_BUFFER, vbuffer[0]);
        // bind your element array buffer
        gl2.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, ebuffer[0]);

        // set up your attribute variables
        gl2.glUseProgram(shaderProgID);
        long dataSize = myShape.getNVerts() * 4l * 4l;
        int  vPosition = gl2.glGetAttribLocation (shaderProgID, "vPosition");
        gl2.glEnableVertexAttribArray ( vPosition );
        gl2.glVertexAttribPointer (vPosition, 4, GL.GL_FLOAT, false,0, 0l);
        int  vNormal = gl2.glGetAttribLocation (shaderProgID, "vNormal");
        gl2.glEnableVertexAttribArray ( vNormal );
        gl2.glVertexAttribPointer(vNormal, 3, GL.GL_FLOAT, false,0, dataSize);
        PhongModel.setUpPhong (shaderProgID, gl2);
        // draw your shapes
        gl2.glDrawElements ( GL.GL_TRIANGLES, numVerts[0],
                GL.GL_UNSIGNED_SHORT, 0l);
    }
    
    
    /**
     * Displays a texture 
     * @param gl2 The GL2 instance 
     *
     */
    public void displayTexture(GL2 gl2){
        // bind your vertex buffer
        gl2.glBindBuffer ( GL.GL_ARRAY_BUFFER, vbuffer[0]);
        // bind your element array buffer
        gl2.glBindBuffer ( GL.GL_ELEMENT_ARRAY_BUFFER, ebuffer[0]);
        // set up your attribute variables
        gl2.glUseProgram (shaderProgID);
        long dataSize = myShape.getNVerts() * 4l * 4l;
        int  vPosition = gl2.glGetAttribLocation (shaderProgID, "vPosition");
        gl2.glEnableVertexAttribArray ( vPosition );
        gl2.glVertexAttribPointer (vPosition, 4, GL.GL_FLOAT, false, 0, 0l);
        int  vTex = gl2.glGetAttribLocation (shaderProgID, "vTexCoord");
        gl2.glEnableVertexAttribArray ( vTex );
        gl2.glVertexAttribPointer (vTex, 2, GL.GL_FLOAT, false, 0, dataSize);
        // setup uniform variables for texture
        texture.setUpTextures (shaderProgID, gl2);
        theta = gl2.glGetUniformLocation (shaderProgID, "theta");
        gl2.glUniform3fv (theta, 1, angles, 0);
        // draw your shapes
        gl2.glDrawElements ( GL.GL_TRIANGLES, numVerts[0],GL.GL_UNSIGNED_SHORT, 0l);
    }
    
    /**
     * Called by the drawable to initiate OpenGL rendering by the client.
     * Creates the wooden blocks, then the colored (shaded) ones.
     *
     */
    public void display(GLAutoDrawable drawable)
    {
        // get GL
        GL2 gl2 = (drawable.getGL()).getGL2();
        // clear your frame buffers
        gl2.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
        //create texture cubes
        createTextureCubes(gl2);
        displayTexture(gl2);

        //Prepare for Shading
        PhongModel.setLightpos(lightPosition);
        PhongModel.setSpecularExponent(specialExponent);

        //make cube
        float diffusered[] = {1f,0f,0f,1f};
        PhongModel.setDiffuse(diffusered);
        createRedCube(gl2);
        displayShade(gl2);

        float diffusePink[] = {1.0f,0.2f,0.9804f,1f};
        PhongModel.setDiffuse(diffusePink);
        createPinkCube(gl2);
        displayShade(gl2);
        
        float diffusePurple[] = {0.6f,0.2f,0.9804f,1f};
        PhongModel.setDiffuse(diffusePurple);
        createPurpleCube(gl2);
        displayShade(gl2);
        
        float diffuseDarkBlue[] = {0.0f,0.0f,1.0f,1f};
        PhongModel.setDiffuse(diffuseDarkBlue);
        createDarkBlueCube(gl2);
        displayShade(gl2);
        
        float diffuseLightBlue[] = {0.0f,0.5509f,1f,1f};
        PhongModel.setDiffuse(diffuseLightBlue);
        createLightBlueCube(gl2);
        displayShade(gl2);
        
        float diffuseCyan[] = {0.4980f,1.0f,1.0f,1f};
        PhongModel.setDiffuse(diffuseCyan);
        createCyanCube(gl2);
        displayShade(gl2);
        
        float diffuseGreen[] = {0.0f,1.0f,0.5f,1f};
        PhongModel.setDiffuse(diffuseGreen);
        createGreenCube(gl2);
        displayShade(gl2);
        
        float diffuseLightGreen[] = {0.7f,0.931f,0.2f,1f};
        PhongModel.setDiffuse(diffuseLightGreen);
        createLightGreenCube(gl2);
        displayShade(gl2);
        
        float diffuseYellow[] = {0.7843f,0.6078f,0.2509f,1f};
        PhongModel.setDiffuse(diffuseYellow);
        createYellowCube(gl2);
        displayShade(gl2);
        
        float diffuseBisque[] = {1.0f,0.6509f,0.3922f,1f};
        PhongModel.setDiffuse(diffuseBisque);
        createBisqueCube(gl2);
        displayShade(gl2);
        
        float diffuseOrange[] = {1.0f,0.6431f,0f,1f};
        PhongModel.setDiffuse(diffuseOrange);
        createOrangeCube(gl2);
        displayShade(gl2);
        
        float diffuseWatermelonRed[] = {1.0f,0.0f,0.5f,1f};
        PhongModel.setDiffuse(diffuseWatermelonRed);
        createWatermelonRedCube(gl2);
        displayShade(gl2);
    }
    
    /**
     * Notifies the listener to perform the release of all OpenGL 
     * resources per GLContext, such as memory buffers and GLSL 
     * programs.
     */
    public void dispose(GLAutoDrawable drawable){}
    /**
     * Verify shader creation
     */
    private void checkShaderError( shaderSetup myShaders, int program,
        String which )
    {
        if( program == 0 ) {
            System.err.println( "Error setting " + which +
                " shader - " +
                myShaders.errorString(myShaders.shaderErrorCode)
            );
            System.exit( 1 );
        }
    }
    public void init(GLAutoDrawable drawable)
    {
        // get the gl object
        GL2 gl2 = drawable.getGL().getGL2();

        // Load shaders
        shaderSetup myShaders = new shaderSetup();
        shaderProgID = myShaders.readAndCompile (gl2, "shader.vert", "shader.frag");
        if (shaderProgID == 0) {
            System.err.println (
	      myShaders.errorString(myShaders.shaderErrorCode)
	    );
            System.exit (1);
        }
        // Other GL initialization
        gl2.glEnable (GL.GL_DEPTH_TEST);
        gl2.glEnable (GL.GL_CULL_FACE);
        gl2.glCullFace ( GL.GL_BACK );
        gl2.glFrontFace(GL.GL_CCW);
        gl2.glClearColor (1.0f, 1.0f, 1.0f, 1.0f);
        gl2.glDepthFunc (GL.GL_LEQUAL);
        gl2.glClearDepth (1.0f);
        // load the texture image
        texture.loadTexture ("texture.jpg");
    }
    
    
    /**
     * Called by the drawable during the first repaint after the component
     * has been resized.
     */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
                     int height)
    {
    }
    /**
     * refer create shade 
     * @param gl2
     *
     */
     public void referShade(GL2 gl2){
        // get your vertices and elements
        Buffer points = myShape.getVertices();
        Buffer elements = myShape.getElements();
        Buffer normals = myShape.getNormals();

        // set up the vertex buffer
        int bf[] = new int[1];
        gl2.glGenBuffers(1, bf, 0);
        vbuffer[0] = bf[0];
        long vertBsize = myShape.getNVerts() * 4l * 4l;
        long ndataSize = myShape.getNVerts() * 3l * 4l;
        gl2.glBindBuffer(GL.GL_ARRAY_BUFFER, vbuffer[0]);
        gl2.glBufferData(GL.GL_ARRAY_BUFFER, vertBsize + ndataSize,null, GL.GL_STATIC_DRAW);
        gl2.glBufferSubData ( GL.GL_ARRAY_BUFFER, 0, vertBsize, points);
        gl2.glBufferSubData ( GL.GL_ARRAY_BUFFER, vertBsize, ndataSize,normals);
        gl2.glGenBuffers (1, bf, 0);
        ebuffer[0] = bf[0];
        long eBuffSize = myShape.getNVerts() * 2l;
        gl2.glBindBuffer ( GL.GL_ELEMENT_ARRAY_BUFFER, ebuffer[0]);
        gl2.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, eBuffSize, elements,GL.GL_STATIC_DRAW);
        numVerts[0] = myShape.getNVerts();
    }

    /**
     * refer create texture 
     * @param gl2 The GL2 instance 
     *
     */
    public void referTexture(GL2 gl2){
        // get your vertices and elements
        Buffer points = myShape.getVertices();
        Buffer elements = myShape.getElements();
        Buffer texCoords = myShape.getUV();

        // set up the vertex buffer
        int bf[] = new int[1];
        gl2.glGenBuffers (1, bf, 0);
        vbuffer[0] = bf[0];
        long vertBsize = myShape.getNVerts() * 4l * 4l;
        long tdataSize = myShape.getNVerts() * 2l * 4l;
        gl2.glBindBuffer ( GL.GL_ARRAY_BUFFER, vbuffer[0]);
        gl2.glBufferData ( GL.GL_ARRAY_BUFFER, vertBsize + tdataSize,null, GL.GL_STATIC_DRAW);
        gl2.glBufferSubData ( GL.GL_ARRAY_BUFFER, 0, vertBsize, points);
        gl2.glBufferSubData ( GL.GL_ARRAY_BUFFER, vertBsize, tdataSize,texCoords);
        gl2.glGenBuffers (1, bf, 0);
        ebuffer[0] = bf[0];
        long eBuffSize = myShape.getNVerts() * 2l;
        gl2.glBindBuffer ( GL.GL_ELEMENT_ARRAY_BUFFER, ebuffer[0]);
        gl2.glBufferData ( GL.GL_ELEMENT_ARRAY_BUFFER, eBuffSize,elements,GL.GL_STATIC_DRAW);
        numVerts[0] = myShape.getNVerts();
    }
    //red
    public void createRedCube(GL2 gl2){
        myShape = new cgShape();
        myShape.makeCube(-0.7f,-0.4f,-0.5f,0.30f);
        referShade(gl2);
    }
    //pink
    public void createPinkCube(GL2 gl2){
        myShape = new cgShape();
        myShape.makeCube(-0.5f,-0.6f,-0.5f,0.30f);
        referShade(gl2);
    }
    //purple
    public void createPurpleCube(GL2 gl2){
        myShape = new cgShape();
        myShape.makeCube(-0.2f,-0.7f,-0.5f,0.30f);
        referShade(gl2);
    }
    //darkblue
    public void createDarkBlueCube(GL2 gl2){
        myShape = new cgShape();
        myShape.makeCube(0.1f,-0.6f,-0.5f,0.30f);
        referShade(gl2);
    }
    //lightblue
    public void createLightBlueCube(GL2 gl2){
    	myShape = new cgShape();
        myShape.makeCube(0.3f,-0.4f,-0.5f,0.30f);
        referShade(gl2);
    }
    //cyan
    public void createCyanCube(GL2 gl2){
    	myShape = new cgShape();
        myShape.makeCube(0.4f,-0.1f,-0.5f,0.30f);
        referShade(gl2);
    }
    //green
    public void createGreenCube(GL2 gl2){
    	myShape = new cgShape();
        myShape.makeCube(0.3f,0.2f,-0.5f,0.30f);
        referShade(gl2);
    }
    //lightgreen
    public void createLightGreenCube(GL2 gl2){
    	myShape = new cgShape();
        myShape.makeCube(0.1f,0.4f,-0.5f,0.30f);
        referShade(gl2);
    }
    //yellow
    public void createYellowCube(GL2 gl2){
        myShape = new cgShape();
        myShape.makeCube(-0.2f,0.5f,-0.5f,0.30f);
        referShade(gl2);
    }
    //bisque
    public void createBisqueCube(GL2 gl2){
    	myShape = new cgShape();
        myShape.makeCube(-0.5f,0.4f,-0.5f,0.30f);
        referShade(gl2);
    }
    // orange
    public void createOrangeCube(GL2 gl2){
    	myShape = new cgShape();
        myShape.makeCube(-0.7f,0.2f,-0.5f,0.30f);
        referShade(gl2);
    }
    //watermelonRed
    public void createWatermelonRedCube(GL2 gl2){
    	myShape = new cgShape();
        myShape.makeCube(-0.8f,-0.1f,-0.5f,0.30f);
        referShade(gl2);
    }
   
  
    /**
     * Creates the texture cube
     *
     */
    public void createTextureCubes(GL2 gl2)
    {
        myShape = new cgShape();
        myShape.clear();
        myShape.makeCube(-0.25f,-.15f,-.5f,.40f);
        referTexture(gl2);
    }

    

    /**
     * Because I am a Key Listener...we'll only respond to key presses
     */
    public void keyTyped(KeyEvent e){}
    public void keyReleased(KeyEvent e){}

    /** 
     * Invoked when a key has been pressed.
     */
    public void keyPressed(KeyEvent e)
    {
        // Get the key that was pressed
        char key = e.getKeyChar();
        // Respond appropriately
        switch( key ) {
            case 'a': angles[0] -= angleUnit; break;
            case 'b': angles[1] -= angleUnit; break;
            case 'c': angles[2] -= angleUnit; break;
            case 'A': angles[0] += angleUnit; break;
            case 'B': angles[1] += angleUnit; break;
            case 'C': angles[2] += angleUnit; break;
            case 'D': specialExponent -= specUnit; break;
            case 'd': specialExponent += specUnit; break;
            case 'q': case 'Q':
            System.exit( 0 );
            break;
        }

        // do a redraw
        myCanvas.display();
    }

    /**
     * main program
     */
    public static void main(String [] args)
    {
        // GL setup
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);
        finalMain finalmain = new finalMain(canvas);
        Frame frame = new Frame("CG - Final Project");
        frame.setSize(640, 640);
        frame.add(canvas);
        frame.setVisible(true);

        // by default, an AWT Frame doesn't do anything when you click
        // the close button; this bit of code will terminate the program when
        // the window is asked to close
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
