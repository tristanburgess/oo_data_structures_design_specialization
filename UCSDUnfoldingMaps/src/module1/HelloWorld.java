package module1;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

/** HelloWorld
  * An application with two maps side-by-side zoomed in on different locations.
  * Author: UC San Diego Coursera Intermediate Programming team
  * @author Your name here
  * Date: July 17, 2015
  * */
public class HelloWorld extends PApplet
{
	/** Your goal: add code to display second map, zoom in, and customize the background.
	 * Feel free to copy and use this code, adding to it, modifying it, etc.  
	 * Don't forget the import lines above. */

	// You can ignore this.  It's to keep eclipse from reporting a warning
	private static final long serialVersionUID = 1L;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// IF YOU ARE WORKING OFFLINE: Change the value of this variable to true
	private static final boolean offline = false;
	
	// For our background gradient
	private int startColor, endColor;
	
	/** The map we use to display our home town: La Jolla, CA */
	UnfoldingMap map1;
	
	/** The map you will use to display your home town */ 
	UnfoldingMap map2;

	public void setup() {
		size(800, 600, P2D);  // Set up the Applet window to be 800x600
		                      // The OPENGL argument indicates to use the 
		                      // Processing library's 2D drawing
		                      // You'll learn more about processing in Module 3
		
		// Select a map provider
		AbstractMapProvider provider = new Google.GoogleTerrainProvider();
		// Set a zoom level
		int zoomLevel = 10;
		
		// Background gradient colors
		startColor = 0xFFAA00FF;
		endColor = 0xFF00FF8A;
		// Draw the gradient background!
 		setGradient(0, 0, width, height, startColor, endColor);

		if (offline) {
			// If you are working offline, you need to use this provider 
			// to work with the maps that are local on your computer.  
			provider = new MBTilesMapProvider(mbTilesString);
			// 3 is the maximum zoom level for working offline
			zoomLevel = 3;
		}
		
		// Create a new UnfoldingMap to be displayed in this window.  
		// The 2nd-5th arguments give the map's x, y, width and height
		// When you create your map we want you to play around with these 
		// arguments to get your second map in the right place.
		// The 6th argument specifies the map provider.  
		// There are several providers built-in.
		// Note if you are working offline you must use the MBTilesMapProvider
		map1 = new UnfoldingMap(this, 25, 50, 350, 500, provider);

		// The next line zooms in and centers the map at 
	    // 32.9 (latitude) and -117.2 (longitude)
	    map1.zoomAndPanTo(zoomLevel, new Location(32.9f, -117.2f));

		// This line makes the map interactive
		MapUtils.createDefaultEventDispatcher(this, map1);
		
		// Create the second map, focused at Manchester, NH
 		map2 = new UnfoldingMap(this, 425, 50, 350, 500, provider);
 	    map2.zoomAndPanTo(zoomLevel, new Location(43.0f, -71.46f));
 		MapUtils.createDefaultEventDispatcher(this, map2);
	}

	/** Draw the Applet window.  */
	public void draw() {
		map1.draw();
		map2.draw();
	}
	
	// Draw the gradient to the screen
	public void setGradient(int x, int y, float w, float h, int c1, int c2) {
      for (int i = x; i <= x + w; i++) {
    	  // Interpolate horizontally between x and width
          float interp = map(i, x, x + w, 0, 1);
          // next interpolated color
          int c = lerpColor(c1, c2, interp);
          stroke(c);
          line(i, y, i, y+h);
      }
	}
	
}
