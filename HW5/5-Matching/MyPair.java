

public class MyPair<N extends Comparable<N>> implements Comparable<MyPair<N>> {
    // class for return (i, j)
    private N x;
    private N y;

    MyPair() {
        this.x=null;
        this.y=null;
    }

    MyPair(N x, N y) {
        this.x = x;
        this.y = y;
    }

    public N getX() {
        return this.x;
    }

    public N getY() {
        return this.y;
    }

    public String getString() {
        return "(" + getX().toString() + ", " + getY().toString() + ")";
    }

    @Override
    public int compareTo(MyPair<N> o) {
        if (this.x != o.getX()) {
            return x.compareTo(o.getX());
        } else {
            return y.compareTo(o.getY());
        }

    }


}