
public class MyLinkedList<V> {

    class Node<V> {
        V data;
        Node<V> next;
        Node() {
            this.data=null;
            this.next=null;
        }
        Node(V data) {
            this.data=data;
            this.next=null;
        }

    }
    Node<V> head;
    Node<V> tail;
    int size;

    MyLinkedList() {
        this.head = new Node<V>();
        this.tail=head;
        this.size = 0;
    }
    MyLinkedList(V item) {
        this.head = new Node<V>(item);
        this.tail=head;
        this.size = 1;

    }


    public boolean add(V v) {
        Node<V> newNode = new Node<>(v);
        if (this.size==0) {
            this.head = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
            size++;
            return true;
        }
        return false;



    }

}


