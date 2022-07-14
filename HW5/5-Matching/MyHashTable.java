import java.util.ArrayList;

public class MyHashTable<K extends Comparable<K>, V extends Comparable<V>> {
    static final int CAPACITY = 100;
    int numItems;
    ArrayList<AVLTree<K, V>> hashTable = new ArrayList<>(CAPACITY); //=hashTable


    MyHashTable() {
        for (int i = 0; i < CAPACITY; i++) {
            hashTable.add(new AVLTree<>()); //each slot=AVL Tree
        }
        numItems = 0;
    }


    //	public K search (K k, int hash) {
//
//	}
    public void insert(K key, V val, int hash) {

        hashTable.get(hash).insert(key, val);
    }

    public void printData(int hash) {

        AVLTree<K, V> slot = hashTable.get(hash);
        if (slot.root.key == null) {
            System.out.println("EMPTY");
        } else {
            StringBuilder sb = new StringBuilder();
            preOrder(slot, slot.root, sb);
            System.out.println(sb);
        }

    }

    public String preOrder(AVLTree<K, V> slot, AVLNode<K, V> node, StringBuilder sb) {
        if (node != slot.NIL) {
            sb.append(node.key);
            if (node.left != slot.NIL) {
                sb.append(" ");
                preOrder(slot, node.left, sb);
            }
            if (node.right != slot.NIL) {
                sb.append(" ");
                preOrder(slot, node.right, sb);
            }
        }
        return sb.toString();
    }


    public AVLTree<K, V> search(int hash) {
        AVLTree<K, V> slot = hashTable.get(hash);
        if (slot.root.key == null) {
            return null;
        } else {
            return slot;
        }
    }
}