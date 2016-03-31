package module6;

import java.util.List;
import java.util.ArrayList;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import processing.core.PConstants;
import processing.core.PGraphics;

/** 
 * A class to represent AirportMarkers on a world map.
 *   
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMarker extends CommonMarker {
	public List<SimpleLinesMarker> routes;
	
	public AirportMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		routes = new ArrayList<SimpleLinesMarker>();
	}
	
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		int outRoutes = routes.size();
		if (outRoutes == 0) {
			pg.fill(255, 0, 255);
		} else if (outRoutes >= 500) {
			pg.fill(255, 0, 0);
		} else if (outRoutes <= 100) {
			pg.fill(0, 0, 255);
		} else {
			pg.fill(255, 255, 0);
		}
		
		if (outRoutes == 0) {
			this.setRadius(3.0f);
		} else {
			this.setRadius(0.1f * routes.size());
		}
		pg.ellipse(x, y, radius, radius);
	}

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		// show rectangle with title
		String title = getName() + " " + getCode() + " " + getCity() + " " + getCountry();
		
		pg.pushStyle();
		
		pg.rectMode(PConstants.CORNER);
		
		pg.stroke(110);
		pg.fill(255,255,255);
		pg.rect(x, y + 15, pg.textWidth(title) +6, 18, 5);
		
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.fill(0);
		pg.text(title, x + 3 , y +18);
		
		
		pg.popStyle();
		// show routes	
	}
	
	public String toString() {
		String title = getName() + " " + getCode() + " " + getCity() + " " + getCountry();
		title = title + " Has " + routes.size() + " routes.";
		return title;
	}
	
	public void addRoute(SimpleLinesMarker sl) {
		routes.add(sl);
	}
	
	public String getName() {
		return getProperty("name").toString();
	}
	
	public String getCode() {
		return getProperty("code").toString();
	}
	
	public String getCity() {
		return getProperty("city").toString();
	}
	
	public String getCountry() {
		return getProperty("country").toString();
	}
}