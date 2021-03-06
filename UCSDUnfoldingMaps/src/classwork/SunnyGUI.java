/**
 * 
 */
package classwork;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * @author tristan.burgess
 *
 */
public class SunnyGUI extends PApplet {
	private String URL = "palmTrees.jpg";
	private PImage backImg;
	
    public void setup() 
    {
    	size(400,400);
    	backImg = loadImage(URL, "jpg");
    	backImg.resize(0, height);
    	image(backImg, 0, 0);
    }
    
    public void draw()
    {
    	backImg = loadImage(URL, "jpg");
    	backImg.resize(0, height);
    	image(backImg, 0, 0);
    	
    	int[] colors = getSunColors(second());
    	fill(colors[0], colors[1], colors[2]);
    	ellipse(width/4, height/5, width/4, height/5);
    }
    
    private int[] getSunColors(int seconds)
    {
    	int[] rgb = new int[3];	
    	float ratio = Math.abs(30 - seconds) / 30.0f;
    	rgb[0] = (int)(255 * ratio);
    	rgb[1] = (int)(255 * ratio);
    	rgb[2] = (int)(0 * ratio);
    	
    	return rgb;
    }
}
