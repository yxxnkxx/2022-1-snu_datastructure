public class Edge implements Comparable<Edge> {
    String start;
    String end;
    int edge;


    Edge(String start, String end, int edge) {
        this.start = start;
        this.end = end;
        this.edge = edge;


    }

    @Override
    public int compareTo(Edge o) {
        return this.edge - o.edge;
    }
}
