/**
 * cgShape.java
 *
 * This class implementations the tessellation of cube
 * @author nacui
 * @version 1
 */

public class cgShape extends simpleShape
{
    /**
     * 3D point class
     */
    public class vertexCoordinate{
    	public float x,y,z;
        /**
         * 3D vertex coordinate
         * @param x    x axis of the vertex
         * @param y    y axis of the vertex
         * @param z    z axis of the vertex
         */
        public vertexCoordinate(float x, float y, float z){
            this.x = x;
            this.y = y;
            this.z = z;
        }
        /**
		 * use the default construction
		 */
		public vertexCoordinate(){}
		/** 
         * calculate the new extension vertex coordinate base on the prior vertex's coordinate add the distance
         * @param prior - The point to add to this one
         * @return the new vertex coordinate
         */
        public vertexCoordinate extension(vertexCoordinate prior){
        	vertexCoordinate newVertexCoordinate = new vertexCoordinate();
        	newVertexCoordinate.x = this.x + prior.x;
        	newVertexCoordinate.y = this.y + prior.y;
        	newVertexCoordinate.z = this.z + prior.z;
            return newVertexCoordinate;
        }

        /**
         * scale the vertex coordinate 
         * @param scalar   scale value
         * @return the scaled vertex coordinate
         */
        public vertexCoordinate scale(float scalar){
        	vertexCoordinate newVertexCoordinate = new vertexCoordinate();
        	newVertexCoordinate.x = x * scalar;
        	newVertexCoordinate.y = y * scalar;
        	newVertexCoordinate.z = z * scalar;
            return newVertexCoordinate;
        }
    }
    /**
     * constructor
     */
    public cgShape(){}
    
    /**
     * makeCube - Create a unit cube, centered at the origin, with a given
     * number of subdivisions in each direction on each face.
     * @param frontLowerLeftX       the value of front face lower left x
     * @param frontLowerLeftY		the value of front face lower left y
     * @param frontLowerLeftZ       the value of front face lower left z
     * @param length                The length of each side
     */
    public void makeCube (float frontLowerLeftX, float frontLowerLeftY, float lowerFrontZ, float length)
    {
        int subdivisions = 8;
        //make aLowerLeft the vertexes of cube
        vertexCoordinate frontLowerLeft = new vertexCoordinate(frontLowerLeftX,frontLowerLeftY,lowerFrontZ);
        vertexCoordinate frontLowerRight = new vertexCoordinate(frontLowerLeftX+length,frontLowerLeftY,lowerFrontZ);
      	vertexCoordinate frontUpperRight = new vertexCoordinate(frontLowerLeftX+length,frontLowerLeftY+length,lowerFrontZ);
        vertexCoordinate frontUpperLeft = new vertexCoordinate(frontLowerLeftX,frontLowerLeftY+length,lowerFrontZ);
       	vertexCoordinate backLowerLeft = new vertexCoordinate(frontLowerLeftX,frontLowerLeftY,lowerFrontZ+length);
       	vertexCoordinate backUpperLeft = new vertexCoordinate(frontLowerLeftX,frontLowerLeftY+length,lowerFrontZ+length);
        vertexCoordinate backLowerRight = new vertexCoordinate(frontLowerLeftX+length,frontLowerLeftY,lowerFrontZ+length);
        vertexCoordinate backUpperRight = new vertexCoordinate(frontLowerLeftX+length,frontLowerLeftY+length,lowerFrontZ+length);
        //front face, back face, left face, right face, top face, bottom face
        makeQuad(frontLowerLeft,frontLowerRight,frontUpperRight,frontUpperLeft, subdivisions,0.5f,0.5f,length);
        makeQuad(backLowerRight, backLowerLeft, backUpperLeft,backUpperRight, subdivisions,0.75f,0.5f,length);
        makeQuad(backLowerLeft,frontLowerLeft,frontUpperLeft,backUpperLeft, subdivisions,0,0.5f,length);
        makeQuad(frontLowerRight, backLowerRight, backUpperRight,frontLowerLeft, subdivisions,0.5f,0.5f,length);
        makeQuad(frontUpperLeft, frontUpperRight, backUpperRight,backUpperLeft, subdivisions,0.5f,0.75f,length);
        makeQuad(backLowerLeft, backLowerRight, frontLowerRight,frontLowerLeft, subdivisions,0.5f,0.5f,length);
    }

    /**
     * make a face of the cube
     * @param lowerLeft                  The lower left corner of the square
     * @param lowerRight                 The lower right corner of the square
     * @param upperRight                 The upper right corner of the square
     * @param upperLeft                  The upper left corner of the square
     * @param subdivisions               The number of subdivisions for the square
     * @param textureLowerLeftX          The x coordinate of lower left vertex for texture 
     * @param textureLowerLeftY          The y coordinate of lower left vertex for texture 
     * @param length                     the length of the side 
     */
    public void makeQuad(vertexCoordinate lowerLeft, vertexCoordinate LowerRight, vertexCoordinate UpperRight, vertexCoordinate UpperLeft, int subdivisions,float textureLowerLeftX, float textureLowerLeftY, float length ){
        float subsize = 1.0f/subdivisions;
        for(int i=0; i<subdivisions; i++){
            float n = subsize * i;
            vertexCoordinate qUpperLeft = UpperLeft.scale(1f-n).extension(UpperRight.scale(n));
            vertexCoordinate rLowerLeft = lowerLeft.scale(1f-n).extension(LowerRight.scale(n));
            vertexCoordinate qUpperLeftPrime = UpperLeft.scale(1f-(n + subsize)).extension(UpperRight.scale(n + subsize));
            vertexCoordinate rLowerLeftprime = lowerLeft.scale(1f-(n + subsize)).extension(LowerRight.scale(n + subsize));
            makeQuadCol(qUpperLeft,rLowerLeft,qUpperLeftPrime,rLowerLeftprime,subdivisions,textureLowerLeftX,textureLowerLeftY,lowerLeft.x,lowerLeft.y,lowerLeft.z, length);
        }
    }

    /**
     * Draw a column of face
     * @param ulvc      The upper left vertex of the column
     * @param llvc      The lower left vertex of the column
     * @param urvc      The upper right vertex of the column
     * @param lrvc      The lower right vertex of the column
     * @param numunits  The number of rows for this column
     * @param textureLowerLeftX     x value of the bottom left vertex of the texture 
     * @param textureLowerLeftY     y value of the bottom left vertex of the texture 
     * @param vx                    x value of the lower left vertex 
     * @param vy  					y value of the lower left vertex 
     * @param vz  					z value of the lower left vertex 
     * @param length                the length of each side
     */
    public void makeQuadCol(vertexCoordinate ulvc, vertexCoordinate llvc, vertexCoordinate urvc, vertexCoordinate lrvc, int numunit,
                            float textureLowerLeftX, float textureLowerLeftY, float vx, float vy, float vz, float length){
    	float sublength = 1f/numunit;
        for(int i=0; i < numunit; i++){
            vertexCoordinate v1 = ulvc.scale(1-(i*sublength)).extension(llvc.scale(i*sublength));
            vertexCoordinate v2= urvc.scale(1-(i*sublength)).extension(lrvc.scale(i*sublength));
          	vertexCoordinate v3 = ulvc.scale(1-((i*sublength)+sublength)).extension(llvc.scale((i*sublength)+sublength));
            vertexCoordinate p4 = urvc.scale(1-((i*sublength)+sublength)).extension(lrvc.scale((i*sublength)+sublength));
            addTriangle(v3,p4,v2,textureLowerLeftX,textureLowerLeftY,vx,vy,vz,length);
            addTriangle(v3,v2,v1,textureLowerLeftX,textureLowerLeftY,vx,vy,vz,length);
        }
    }

    /**
     * normalize 
     *
     * @param value     The value to normalize
     * @param max       The max possible value
     * @param min       The min possible value
     * @return          The normalized value
     */
    public float normalize(float value, float max, float min){
    	float normalized = (value-min)/(max-min);
    	return normalized;
    }

    /**
     * override addTriangle function
     * @param v1  The first point of the triangle
     * @param v2  The second point of the triangle
     * @param v3  The third point of the triangle
     */
    public void addTriangle(vertexCoordinate v1, vertexCoordinate v2, vertexCoordinate v3,float txLowerLeftx, float txLowerLefty,float vx, float vy, float vz, float length){
        if(v1.x == v2.x && v2.x == v3.x){
            float zmin = vz;
            float zmax = vz+length;
            float ymin = vy;
            float ymax = vy+length;
            addTriangle(v1.x, v1.y, v1.z,-0.5f*normalize(v1.z, zmax, zmin)+(txLowerLeftx),0.5f*normalize(v1.y,ymax,ymin)+txLowerLefty,
                    v2.x, v2.y, v2.z, -0.5f*normalize(v2.z,zmax,zmin)+(txLowerLeftx),0.5f*normalize(v2.y,ymax,ymin)+txLowerLefty,
                    v3.x, v3.y, v3.z, -0.5f*normalize(v3.z,zmax,zmin)+(txLowerLeftx), 0.5f*normalize(v3.y,ymax,ymin)+txLowerLefty);
        }
        if(v1.y == v2.y && v1.y == v3.y) {
            float xmin = vx;
            float xmax=vx+length;
            float zmin = vz;
            float zmax=vz+length;
            addTriangle(v1.x,v1.y,v1.z,0.5f*normalize(v1.x, xmax, xmin)+txLowerLeftx,0.5f*normalize(v1.z, zmax, zmin)+txLowerLefty,
                    v2.x,v2.y,v2.z, 0.5f*normalize(v2.x,xmax,xmin)+txLowerLeftx,0.5f*normalize(v2.z,zmax,zmin)+txLowerLefty,
                    v3.x,v3.y,v3.z, 0.5f*normalize(v3.x,xmax,xmin)+txLowerLeftx,0.5f*normalize(v3.z,zmax,zmin)+txLowerLefty);
        }
        if(v1.z == v2.z && v2.z == v3.z){
            float xmin = vx, xmax = xmin+length;
            float ymin = vy, ymax = ymin+length;
            addTriangle(v1.x, v1.y, v1.z,0.5f*normalize(v1.x,xmax,xmin)+txLowerLeftx,0.5f*normalize(v1.y, ymax, ymin)+txLowerLefty,
                v2.x, v2.y, v2.z, 0.5f*normalize(v2.x, xmax, xmin)+txLowerLeftx,0.5f*normalize(v2.y, ymax, ymin)+txLowerLefty,
                v3.x, v3.y, v3.z, 0.5f*normalize(v3.x, xmax, xmin)+txLowerLeftx,0.5f*normalize(v3.y, ymax, ymin)+txLowerLefty);
        }
    }
}
