package module6;

import java.util.ArrayList;
import java.util.*;

import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;

/** An applet that shows airports (and routes)
 * on a world map.  
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */

//Features to add: Size of airport marker - number of routes
//Click on airport - show airport routes
//Color of route = distance of travel
//City markers
//Click on city - show airports nearby
public class AirportMap extends PApplet {
	
	UnfoldingMap map;
	private List<Marker> airportList;
	List<Marker> routeList;
	private HashMap<Integer, AirportMarker> airportsRoutes;
	
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	
	public void setup() {
		// setting up PAppler
		size(1280, 800, OPENGL);
		
		// setting up map and default events
		map = new UnfoldingMap(this, 200, 50, 850, 650);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// get features from airport data
		List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
		
		// list for markers, hashmap for quicker access when matching with routes
		airportList = new ArrayList<Marker>();
		HashMap<Integer, Location> airports = new HashMap<Integer, Location>();
		airportsRoutes = new HashMap<Integer, AirportMarker>();
		
		// create markers from features
		for(PointFeature feature : features) {
			AirportMarker m = new AirportMarker(feature);
			//System.out.println(m);
	
			airportList.add(m);
			
			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
			airportsRoutes.put(Integer.parseInt(feature.getId()), m);
		
		}
		
		
		// parse route data
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
		routeList = new ArrayList<Marker>();
		for(ShapeFeature route : routes) {
			
			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));
			
			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
			}
			
			SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
		
			//System.out.println(sl.getProperties());
			
			sl.setHidden(true);
			routeList.add(sl);
			if (airportsRoutes.containsKey(source) && airportsRoutes.containsKey(dest)) {
				airportsRoutes.get(source).addRoute(sl);
				airportsRoutes.get(dest).addRoute(sl);
			}
			
		}
		
		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
		map.addMarkers(routeList);
		
		map.addMarkers(airportList);
		
	}
	
	public void draw() {
		background(200);
		map.draw();
		addKey();
		
	}
	
	/** Event handler that gets called automatically when the 
	 * mouse moves.
	 */
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		selectMarkerIfHover(airportList);
	}
	
	// If there is a marker selected 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}
		
		for (Marker m : markers) 
		{
			CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}
	
	@Override
	public void mouseClicked()
	{
		if (lastClicked != null) {
			setInitialHiddenMarkers();
			lastClicked = null;
		}
		else if (lastClicked == null) 
		{
			checkMarkersForClick();
		}
	}
	
	private void setInitialHiddenMarkers() {
		for (Marker marker : airportList) {
			marker.setHidden(false);
		}
		
		for (Marker marker : routeList) {
			marker.setHidden(true);
		}
	}
	
	private void checkMarkersForClick() {
		if (lastClicked != null) return;
		//List<Marker> notHiddenAirportMarker = new ArrayList<Marker>();
		for (Marker marker : airportList) {
			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
				lastClicked = (CommonMarker)marker;
				for (Marker sl : ((AirportMarker)lastClicked).routes) {
					sl.setHidden(false);
					//int source = Integer.parseInt((String)sl.getProperty("source"));
					//int dest = Integer.parseInt((String)sl.getProperty("destination"));
					//notHiddenAirportMarker.add(airportsRoutes.get(source));
					//notHiddenAirportMarker.add(airportsRoutes.get(dest));
				}
				for (Marker mk : airportList) {
					if (mk != lastClicked) {
						mk.setHidden(true);
					}
				}
				return;
			}
		}
	}
	
	private void addKey() {	
		// Remember you can use Processing's graphics methods here
		fill(200);
		
		int xbase = 20;
		int ybase = 20;
		
		rect(xbase, ybase, 150, 200);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Airport Key", xbase+13, ybase+13);
		
		fill(204, 255, 255);
		stroke(204, 255, 255);
		ellipse(xbase+8, 
				ybase+52, 
				3, 
				3);
		
		fill(0, 153, 153);
		stroke(0, 153, 153);
		ellipse(xbase+8, 
				ybase+82, 
				3, 
				3);
		
		fill(0, 51, 51);
		stroke(0, 51, 51);
		ellipse(xbase+8, 
				ybase+112, 
				3, 
				3);

		fill(0, 0, 0);
		textAlign(LEFT, CENTER);
		textSize(11);
		text("Airport with less than", xbase+20, ybase+50);
		text("10 routes", xbase+20, ybase+65);
		text("Airport with more than", xbase+20, ybase+80);
		text("10, less than 30 routes", xbase+20, ybase+95);
		text("Airport with more than", xbase+20, ybase+110);
		text("30 routes", xbase+20, ybase+125);
		
	}

}