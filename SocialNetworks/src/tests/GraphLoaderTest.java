package tests;

import graph.CapGraph;
import graph.Graph;
import org.junit.jupiter.api.Test;
import util.GraphLoader;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class GraphLoaderTest {

    @Test
    void loadGraph() {
        for(int i = 0; i < 10; i++) {
            Graph g = new CapGraph();
            GraphLoader.loadGraph(g, "data/scc/test_" + (i + 1) + ".txt");
            HashMap<Integer, HashSet<Integer>> expG = g.exportGraph();
            assertTrue(g.exportGraph().equals(expG));

        }
    }
}