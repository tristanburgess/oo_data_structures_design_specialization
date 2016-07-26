package roadgraph;

import geography.GeographicPoint;

/**
 * @author Tristan Burgess
 * The purpose of this class is to represent a graph edge, with the destination node, and information 
 * about the path between the two nodes as member variables. These graph edges are 
 * stored in a list for each vertex to represent the edges connecting that vertex, which allows us to 
 * retrieve the neighbors as well as the road name, type, and length connecting any two vertices.
 *
 */
public class GraphEdge {
	GeographicPoint destNode;
	String roadName;
	String roadType;
	double length;
	
	/** 
	 * Create a new GraphEdge
	 */
	public GraphEdge(GeographicPoint destNode, String roadName, String roadType, double length)
	{
		this.destNode = destNode;
		this.roadName = roadName;
		this.roadType = roadType;
		this.length = length;
	}
	
	public GeographicPoint getDestNode() 
	{
		return destNode;
	}
	
	public String getRoadName() 
	{
		return roadName;
	}

	public String getRoadType() 
	{
		return roadType;
	}
	
	public double getLength() 
	{
		return length;
	}
	
}
