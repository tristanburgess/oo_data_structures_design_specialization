package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4.0f;
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5.0f;
	// Less than this threshold is a moderate earthquake
    public static final float THRESHOLD_STRONG = 6.0f;
    // Less than this threshold is a strong earthquake
    public static final float THRESHOLD_MAJOR = 7.0f;
    // Less than this threshold is a major earthquake
    public static final float THRESHOLD_GREAT = 8.0f;
	
	// Violet for minor earthquakes
	public static final float MINOR_MARKER_RADIUS = 6.0f;
	public static final int MINOR_MARKER_COLOR = 0xFFFF00FF;	
	// Blue for light earthquakes
	public static final float LIGHT_MARKER_RADIUS = 8.0f;
	public static final int LIGHT_MARKER_COLOR = 0xFF0000FF;
	// Yellow for moderate earthquakes
	public static final float MODERATE_MARKER_RADIUS = 10.0f;
	public static final int MODERATE_MARKER_COLOR = 0xFFFFFF00;
	// Light orange for strong earthquakes
	public static final float STRONG_MARKER_RADIUS = 12.0f;
	public static final int STRONG_MARKER_COLOR = 0xFFFF9900;
	// Dark orange for major earthquakes
	public static final float MAJOR_MARKER_RADIUS = 14.0f;
	public static final int MAJOR_MARKER_COLOR = 0xFFFF5500;
	// Red for great earthquakes
	public static final float GREAT_MARKER_RADIUS = 16.0f;
	public static final int GREAT_MARKER_COLOR = 0xFFFF0000;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
	    if (earthquakes.size() > 0) {
	    	List<Marker> markers = createEarthquakeMarkers(earthquakes);   
		    map.addMarkers(markers);
	    } 
	}
		
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private List<Marker> createEarthquakeMarkers(List<PointFeature> features)
	{		
		List<Marker> markers = new ArrayList<Marker>();
		
		for (PointFeature f : features){
			float markerRadius = 0.0f;
 	        int markerColor = 0;
 	        
    	    Object magObj = f.getProperty("magnitude");
    	    float mag = Float.parseFloat(magObj.toString());

    	    if (mag < THRESHOLD_LIGHT) {
    	    	markerRadius = MINOR_MARKER_RADIUS;
    	    	markerColor = MINOR_MARKER_COLOR;
    	    } 
    	    else if (mag < THRESHOLD_MODERATE) {
    	    	markerRadius = LIGHT_MARKER_RADIUS;
    	    	markerColor = LIGHT_MARKER_COLOR;
    	    } 
    	    else if (mag < THRESHOLD_STRONG) {
    	    	markerRadius = MODERATE_MARKER_RADIUS;
    	    	markerColor = MODERATE_MARKER_COLOR;
    	    } 
    	    else if (mag < THRESHOLD_MAJOR) {
    	    	markerRadius = STRONG_MARKER_RADIUS;
    	    	markerColor = STRONG_MARKER_COLOR;
    	    } 
    	    else if (mag < THRESHOLD_GREAT) {
    	    	markerRadius = MAJOR_MARKER_RADIUS;
    	    	markerColor = MAJOR_MARKER_COLOR;
    	    } 
    	    else {
    	    	markerRadius = GREAT_MARKER_RADIUS;
    	    	markerColor = GREAT_MARKER_COLOR;
    	    }
    	    
    	    SimplePointMarker marker = new SimplePointMarker(f.getLocation());   
    	    marker.setRadius(markerRadius);
    	    marker.setColor(markerColor);
    	    markers.add(marker);
		}
		
		return markers;
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}

	// helper method to draw key in GUI
	private void addKey() 
	{	
	    rect(10, 50, 185, 500);
	    fill(color(255, 255, 255));
	    text("Earthquake Key", 55, 75);
	    
	    text("Great: 8+", 65, 125);
	    text("Major: 7 - 7.9", 65, 155);
	    text("Strong: 6 - 6.9", 65, 185);
	    text("Moderate: 5 - 5.9", 65, 215);
	    text("Light: 4 - 4.9", 65, 245);
	    text("Minor: 3 - 3.9", 65, 275);
	    
	    fill(GREAT_MARKER_COLOR);
	    ellipse(50, 120, GREAT_MARKER_RADIUS, GREAT_MARKER_RADIUS);

	    fill(MAJOR_MARKER_COLOR);
	    ellipse(50, 150, MAJOR_MARKER_RADIUS, MAJOR_MARKER_RADIUS);
	    
	    fill(STRONG_MARKER_COLOR);
	    ellipse(50, 180, STRONG_MARKER_RADIUS, STRONG_MARKER_RADIUS);

	    fill(MODERATE_MARKER_COLOR);
	    ellipse(50, 210, MODERATE_MARKER_RADIUS, MODERATE_MARKER_RADIUS);
	    
	    fill(LIGHT_MARKER_COLOR);
	    ellipse(50, 240, LIGHT_MARKER_RADIUS, LIGHT_MARKER_RADIUS);
	    
	    fill(MINOR_MARKER_COLOR);
	    ellipse(50, 270, MINOR_MARKER_RADIUS, MINOR_MARKER_RADIUS);
    
	    fill(color(0, 0, 0));
	}
}
