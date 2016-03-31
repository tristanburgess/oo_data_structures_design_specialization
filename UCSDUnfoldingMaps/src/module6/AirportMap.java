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
		map = new UnfoldingMap(this, 400, 50, 1200, 700);
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
			
			List<Location> locs = route.getLocations();
			double dist = locs.get(0).getDistance(locs.get(1)) * 1.52;
			
			SimpleLinesMarker sl = new SimpleLinesMarker(locs, route.getProperties());

			if (dist <= 500.0) {
				sl.setColor(color(255, 0, 255));
			} else if (dist > 500.0 && dist <= 1000.0) {
				sl.setColor(color(0, 0, 255));
			} else if (dist > 1000.0 && dist <= 4000.0) {
				sl.setColor(color(255, 255, 0));
			} else {
				sl.setColor(color(255, 0, 0));
			}
		
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
		background(0);
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
	
	// helper method to draw key in GUI
		private void addKey() {	
			fill(255, 250, 240);
			
			int xbase = 50;
			int ybase = 50;
			
			rect(xbase, ybase, 250, 400);
			
			fill(0);
			textAlign(LEFT, CENTER);
			textSize(12);
			text("Airport/Route Key", xbase+25, ybase+25);
			
			fill(color(255, 0, 255));
			ellipse(xbase+35, ybase+50, 12, 12);
			fill(color(0, 0, 255));
			ellipse(xbase+35, ybase+70, 12, 12);
			fill(color(255, 255, 0));
			ellipse(xbase+35, ybase+90, 12, 12);
			fill(color(255, 0, 0));
			ellipse(xbase+35, ybase+110, 12, 12);
			
			textAlign(LEFT, CENTER);
			fill(0, 0, 0);
			text("routes = 0", xbase+50, ybase+50);
			text("routes <= 100", xbase+50, ybase+70);
			text("100 < routes <= 500", xbase+50, ybase+90);
			text("routes > 500", xbase+50, ybase+110);
			text("Size ~ Number of routes", xbase+25, ybase+130);
			
			text("Route distance (km)", xbase+25, ybase+170);
			stroke(255, 0, 255);
			line(xbase+15, ybase+190, xbase+45, ybase+190);
			stroke(0, 0, 255);
			line(xbase+15, ybase+210, xbase+45, ybase+210);
			stroke(255, 255, 0);
			line(xbase+15, ybase+230, xbase+45, ybase+230);
			stroke(255, 0, 0);
			line(xbase+15, ybase+250, xbase+45, ybase+250);
			
			text("distance <= 500km", xbase+50, ybase+190);
			text("500km < distance <= 1000km", xbase+50, ybase+210);
			text("1000km < distance <= 4000km", xbase+50, ybase+230);
			text("distance > 4000km", xbase+50, ybase+250);
		}

}