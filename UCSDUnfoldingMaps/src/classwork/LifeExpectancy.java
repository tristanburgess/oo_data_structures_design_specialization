/**
 * 
 */
package classwork;

import processing.core.PApplet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;

/**
 * @author tristan.burgess
 *
 */
public class LifeExpectancy extends PApplet {
	UnfoldingMap map;
	Map<String, Float> lifeExpMap;
	List<Feature> countries;
	List<Marker> countryMarkers;
	
	public void setup()
	{
		size(1024, 768, OPENGL);
		
		map = new UnfoldingMap(this, 0, 0, width, height, new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		
		lifeExpMap = loadLifeExpectancyFromCSV("LifeExpectancyWorldBankModule3.csv");
		
		countries = GeoJSONReader.loadData(this, "countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		map.addMarkers(countryMarkers);
		shadeCountries();
	}
	
	public void draw()
	{
		map.draw();
	}
	
	private Map<String, Float> loadLifeExpectancyFromCSV(String filePath)
	{
		Map<String, Float> lifeExpMap = new HashMap<String, Float>();
		
		// Read file
		String[] rows = loadStrings(filePath);
		
		for (String row : rows) {
			String[] cols = row.split(",");
			if (cols.length == 6 && !cols[5].equals("..")) {
				lifeExpMap.put(cols[4], Float.parseFloat(cols[5]));
			}
		}
		
		return lifeExpMap;
	}
	
	private void shadeCountries()
	{
		for (Marker mark : countryMarkers) {
			String countryId = mark.getId();
			
			if (lifeExpMap.containsKey(countryId)) {
				int color = (int)map(lifeExpMap.get(countryId), 40, 90, 10, 255);
				mark.setColor(color(255-color, 100, color));
			} else {
				mark.setColor(color(155, 155, 155));
			}
		}
	}

}
