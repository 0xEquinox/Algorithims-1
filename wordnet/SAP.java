/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private final Digraph graph;
    private final int numVerticies;

    public SAP(Digraph graph) {
        if (graph == null) throw new IllegalArgumentException("Graph is null");

        this.graph = new Digraph(graph);
        this.numVerticies = graph.V();
    }

    private void validateVertex(int verticie) {
        if (verticie < 0 || verticie >= numVerticies)
            throw new IndexOutOfBoundsException(
                    "vertex " + verticie + " is not between 0 and " + (numVerticies - 1));
    }

    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) throw new NullPointerException("argument is null");
        vertices.forEach(verticie -> {
            if (verticie < 0 || verticie >= numVerticies) throw new IndexOutOfBoundsException(
                    "vertex " + vertices + " is not between 0 and " + (numVerticies - 1));
        });
    }

    private int[] shortest(BreadthFirstDirectedPaths bfsv, BreadthFirstDirectedPaths bfsw) {
        int shortestLen = Integer.MAX_VALUE;
        int shortestAncestor = -1;

        for (int i = 0; i < numVerticies; i++) {
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
                int len = bfsv.distTo(i) + bfsw.distTo(i);
                if (len < shortestLen) {
                    shortestLen = len;
                    shortestAncestor = i;
                }
            }
        }

        return new int[] { (shortestAncestor == -1) ? -1 : shortestLen, shortestAncestor };
    }

    private int[] shortest(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(graph, w);

        return shortest(bfsv, bfsw);
    }

    private int[] shortest(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(graph, w);

        return shortest(bfsv, bfsw);
    }

    public int length(int v, int w) {
        return shortest(v, w)[0];
    }

    public int ancestor(int v, int w) {
        return shortest(v, w)[1];
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return shortest(v, w)[0];
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return shortest(v, w)[1];
    }
}