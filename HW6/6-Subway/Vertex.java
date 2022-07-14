public class Vertex implements Comparable<Vertex>{
    String code;
    String name;
    String line;
    boolean check; //check if visited
    long distance; //for relaxation
    int index; //for check path

    Vertex(String code, String name, String line, int index) {
        this.code=code;
        this.name=name;
        this.line=line;
        this.check = false;
        this.distance = Subway.INF;
        this.index=index;
    }

    @Override
    public int compareTo(Vertex o) {
        return (int) (this.distance - o.distance);
    }


}
