package graph;

import java.util.*;

public class CapGraphSCC {
    public Set<Integer> visited;
    public Stack<Integer> finished;
    public Stack<Integer> vertices;
    public List<Graph> connectedComponents;
    private CapGraph g;

    public CapGraphSCC(CapGraph g) {
        this.visited = new HashSet<>();
        this.finished = new Stack<>();
        this.vertices = new Stack<>();
        this.connectedComponents = new ArrayList<>();
        this.g = g;
    }

    public void dfs() {
        if (vertices.size() == 0)
            this.initVertices();

        while(!this.vertices.isEmpty()) {
            Integer cur = this.vertices.pop();
            if (!this.visited.contains(cur)) {
                CapGraph connectedComponent = new CapGraph();
                connectedComponent.addVertex(cur);
                this.dfsVisit(cur, connectedComponent);
                connectedComponents.add(connectedComponent);
            }
        }
        return;
    }

    private void dfsVisit(Integer cur, CapGraph connectedComponent) {
        this.visited.add(cur);
        for (Integer edge : this.g.getEdges(cur)) {
            if (!this.visited.contains(edge)) {
                connectedComponent.addVertex(edge);
                this.dfsVisit(edge, connectedComponent);
            }
        }
        this.finished.push(cur);
    }

    private void initVertices() {
        ArrayList<Integer> tempVertices = new ArrayList<>(this.g.getVertices());
        Collections.sort(tempVertices, Collections.reverseOrder());

        for (Integer key : tempVertices) {
            this.vertices.push(key);
        }
    }
}
