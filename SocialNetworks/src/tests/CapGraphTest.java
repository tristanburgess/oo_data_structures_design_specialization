package tests;

import graph.CapGraph;
import graph.Graph;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CapGraphTest {

    @org.junit.jupiter.api.Test
    void addVertexPosInt() {
        int key = 0;
        CapGraph g = new CapGraph();
        g.addVertex(key);
        assertTrue(g.exportGraph().containsKey(key));
    }

    @org.junit.jupiter.api.Test
    void addVertexNegInt() {
        int key = -1;
        CapGraph g = new CapGraph();
        assertThrows(IllegalArgumentException.class, ()->{g.addVertex(key);});
    }

    @org.junit.jupiter.api.Test
    void addEdgePosInt() {
        int key1 = 0;
        int key2 = 1;
        CapGraph g = new CapGraph();
        g.addVertex(key1);
        g.addVertex(key2);
        g.addEdge(key1, key2);
        assertTrue(g.exportGraph().get(key1).contains(key2));
    }

    @org.junit.jupiter.api.Test
    void addEdgeNegInt() {
        int key1 = -1;
        int key2 = 0;
        CapGraph g = new CapGraph();
        assertThrows(IllegalArgumentException.class, ()->{g.addEdge(key1, key2);});
    }

    @org.junit.jupiter.api.Test
    void addEdgeVerticesMustExist() {
        CapGraph g = new CapGraph();
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);

        assertThrows(IllegalArgumentException.class, ()->{g.addEdge(5, 2);});
    }

    @org.junit.jupiter.api.Test
    void transposeGraph() {
        CapGraph g = new CapGraph();
        g.addVertex(1);
        g.addVertex(2);
        g.addEdge(1, 2);
        g.transposeGraph();

        assertTrue(g.getEdges(2).contains(1) && !g.getEdges(1).contains(2));
    }

    @org.junit.jupiter.api.Test
    void getEgonet() {
        HashMap<Integer, HashSet<Integer>> answerGraph = new HashMap<>();
        answerGraph.put(0, new HashSet<>());
        answerGraph.put(2, new HashSet<>());
        answerGraph.put(4, new HashSet<>());
        answerGraph.get(0).add(2);
        answerGraph.get(0).add(4);
        answerGraph.get(2).add(0);
        answerGraph.get(2).add(4);
        answerGraph.get(4).add(0);
        answerGraph.get(4).add(2);

        CapGraph egoNetGraph = new CapGraph();
        egoNetGraph.addVertex(0);
        egoNetGraph.addVertex(1);
        egoNetGraph.addVertex(2);
        egoNetGraph.addVertex(3);
        egoNetGraph.addVertex(4);
        egoNetGraph.addEdge(0, 2);
        egoNetGraph.addEdge(0, 4);
        egoNetGraph.addEdge(1, 4);
        egoNetGraph.addEdge(2, 0);
        egoNetGraph.addEdge(2, 3);
        egoNetGraph.addEdge(2, 4);
        egoNetGraph.addEdge(3, 2);
        egoNetGraph.addEdge(4, 0);
        egoNetGraph.addEdge(4, 1);
        egoNetGraph.addEdge(4, 2);

        HashMap<Integer, HashSet<Integer>> candidateGraph = egoNetGraph.getEgonet(0).exportGraph();
        assertTrue(candidateGraph.equals(answerGraph));
    }

    @org.junit.jupiter.api.Test
    void getEgonetVertextNotExist() {
        CapGraph egoNetGraph = new CapGraph();
        egoNetGraph.addVertex(1);
        egoNetGraph.addVertex(2);
        egoNetGraph.addVertex(3);
        egoNetGraph.addEdge(1, 3);

        assertTrue(egoNetGraph.getEgonet(-1).exportGraph().size() == 0);
    }

    @org.junit.jupiter.api.Test
    void getSCCs() {
        List<Graph> answerGraphs = new ArrayList<>();
        CapGraph answerGraph = new CapGraph();
        answerGraph.addVertex(32);
        answerGraphs.add(answerGraph);
        answerGraph = new CapGraph();
        answerGraph.addVertex(44);
        answerGraphs.add(answerGraph);
        answerGraph = new CapGraph();
        answerGraph.addVertex(50);
        answerGraphs.add(answerGraph);
        answerGraph = new CapGraph();
        answerGraph.addVertex(18);
        answerGraph.addVertex(23);
        answerGraph.addVertex(25);
        answerGraph.addVertex(65);
        answerGraph.addEdge(18, 23);
        answerGraph.addEdge(23, 18);
        answerGraph.addEdge(23, 25);
        answerGraph.addEdge(25, 18);
        answerGraph.addEdge(25, 23);
        answerGraph.addEdge(25, 65);
        answerGraph.addEdge(65, 23);
        answerGraphs.add(answerGraph);

        CapGraph sccGraph = new CapGraph();
        sccGraph.addVertex(18);
        sccGraph.addVertex(23);
        sccGraph.addVertex(25);
        sccGraph.addVertex(32);
        sccGraph.addVertex(44);
        sccGraph.addVertex(50);
        sccGraph.addVertex(65);
        sccGraph.addEdge(18, 23);
        sccGraph.addEdge(18, 44);
        sccGraph.addEdge(23, 18);
        sccGraph.addEdge(23, 25);
        sccGraph.addEdge(25, 18);
        sccGraph.addEdge(25, 23);
        sccGraph.addEdge(25, 65);
        sccGraph.addEdge(32, 50);
        sccGraph.addEdge(32, 44);
        sccGraph.addEdge(44, 50);
        sccGraph.addEdge(65, 23);

        List<Graph> candidateGraphs = sccGraph.getSCCs();
        assertTrue(candidateGraphs.equals(answerGraphs));
    }

    @org.junit.jupiter.api.Test
    void exportGraph() {
        CapGraph g = new CapGraph();
        HashMap<Integer, HashSet<Integer>> expG = g.exportGraph();
        assertTrue(g.exportGraph().equals(expG));
    }
}