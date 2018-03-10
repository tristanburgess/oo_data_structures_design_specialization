/**
 * 
 */
package graph;

import java.util.*;

/**
 * @author Your name here.
 * 
 * For the warm up assignment, you must implement your Graph in a class
 * named CapGraph.  Here is the stub file.
 *
 */
public class CapGraph implements Graph {

	private HashMap<Integer, HashSet<Integer>> g;

	public CapGraph() {
	    this.g = new HashMap<>();
    }

	/* (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 */
	@Override
	public void addVertex(int num) {
        if (num < 0) {
            throw new IllegalArgumentException("Vertex IDs cannot be less than 0.");
        }

		this.g.put(num, new HashSet<>());
	}

	/* (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) {
	    if (from < 0 || to < 0) {
	        throw new IllegalArgumentException("Edge IDs cannot be less than 0.");
        } else if (!this.g.containsKey(from) || !this.g.containsKey(to)) {
            throw new IllegalArgumentException("Edge IDs must reference valid vertices in the graph.");
        }

	    this.g.get(from).add(to);
	}

	public void transposeGraph() {
        CapGraph transposedG = new CapGraph();
        for (Integer v : this.getVertices()) {
            transposedG.addVertex(v);
        }

        for (Integer v : this.getVertices()) {
            for (Integer e : this.getEdges(v)) {
                transposedG.addEdge(e, v);
            }
        }

        this.g = transposedG.exportGraph();
    }

    public Set<Integer> getVertices() {
        return this.g.keySet();
    }

	public HashSet<Integer> getEdges(int vertex) {
	    return this.g.get(vertex);
    }

    public boolean hasVertex(int vertex) {
	    return this.g.containsKey(vertex);
    }

	/* (non-Javadoc)
	 * @see graph.Graph#getEgonet(int)
	 */
	@Override
	public Graph getEgonet(int center) {
		if (!this.g.containsKey(center)) {
            return new CapGraph();
        }

        // create new graph
        // for each edge in this graph rooted at center
        // add vertices that aren't yet in the graph
        // add the edge from center to vertex and back
        // add any edges between edges in this graph for vertices in the new graph
        CapGraph egoGraph = new CapGraph();
        egoGraph.addVertex(center);
		for (Integer edge : this.g.get(center)) {
            egoGraph.addVertex(edge);
        }

        for (Integer vertex : egoGraph.getVertices()) {
            for (Integer edge : this.getEdges(vertex)) {
                if (egoGraph.hasVertex(edge)) {
                    egoGraph.addEdge(vertex, edge);
                    egoGraph.addEdge(edge, vertex);
                }
            }
        }

		return egoGraph;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 */
	@Override
	public List<Graph> getSCCs() {
        CapGraphSCC gDFSFrontRunner = new CapGraphSCC(this);
        gDFSFrontRunner.dfs();

        // Compute G transpose
        this.transposeGraph();

        CapGraphSCC gDFSBackRunner = new CapGraphSCC(this);
        gDFSBackRunner.vertices = gDFSFrontRunner.finished;
        gDFSBackRunner.dfs();

		return gDFSBackRunner.connectedComponents;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		return this.g;
	}

}
