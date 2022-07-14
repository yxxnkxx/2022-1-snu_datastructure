
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<T extends Comparable<T>> implements ListInterface<T> {
	// dummy head
	Node<T> head;
	int numItems;

	public MyLinkedList() {
		head = new Node<>(null);
	}

	public MyLinkedList(T genre) {
		head = new Node<T>(genre);
		numItems++; }

    /**
     * {@code Iterable<T>}를 구현하여 iterator() 메소드를 제공하는 클래스의 인스턴스는
     * 다음과 같은 자바 for-each 문법의 혜택을 볼 수 있다.
     * 
     * <pre>
     *  for (T item: iterable) {
     *  	item.someMethod();
     *  }
     * </pre>
     * 
     * @see PrintCmd#apply(MovieDB)
     * @see SearchCmd#apply(MovieDB)
     * @see java.lang.Iterable#iterator()
     */
    public final Iterator<T> iterator() {
    	return new MyLinkedListIterator<T>(this);
    }



	@Override
	public boolean isEmpty() {
		return head.getNext() == null;
	}

	@Override
	public int size() {
		return numItems;
	}

	@Override
	public T first() {
		return head.getNext().getItem();
	}

	@Override
	public void add(T item) {

		Node<T> last = head;

		if (numItems==0) {
			last.insertNext(item);
			numItems++;
			return;
		}


		while (last.getNext()!=null && last.getNext().getItem().compareTo(item) < 0) {
			last = last.getNext();
		}
		last.insertNext(item);
		numItems += 1;
	}

	@Override
	public void removeAll() {
		head.setNext(null);
	}

	public Genre find (Genre obj) {
		Iterator findIter = new MyLinkedListIterator(this);
		while (findIter.hasNext()) {
			Genre item = (Genre) findIter.next();

			if (item.genreName.equals(obj.genreName)) {
				return item;
			}
		}
		return null;
	}

	public boolean removeItem(T item) {
		Node<T> currNode = head;
		for (int i=0; i<=numItems-1; i++) {
			Node<T> prevNode = currNode;
			currNode=prevNode.getNext();
			if (currNode.getItem().compareTo(item)==0) {
				prevNode.setNext(currNode.getNext());
				numItems--;
				return true;
			}
		}
		return false;

	}

	public String find(String obj) {
		Iterator findIter = new MyLinkedListIterator(this);
		while (findIter.hasNext()) {
			String item = (String) findIter.next();
			if (item.equals(obj)) {
				return item;
			}
		}
		return null;
	}

}

class MyLinkedListIterator<T extends Comparable<T>> implements Iterator<T> {
	// FIXME implement this
	// Implement the iterator for MyLinkedList.
	// You have to maintain the current position of the iterator.
	private MyLinkedList<T> list;
	private Node<T> curr;
	private Node<T> prev;

	public MyLinkedListIterator(MyLinkedList<T> list) {
		this.list = list;
		this.curr = list.head;
		this.prev = null;
	}

	@Override
	public boolean hasNext() {
		return curr.getNext() != null;
	}

	@Override
	public T next() {
		if (!hasNext())
			throw new NoSuchElementException();

		prev = curr;
		curr = curr.getNext();

		return curr.getItem();
	}

	@Override
	public void remove() {
		if (prev == null)
			throw new IllegalStateException("next() should be called first");
		if (curr == null)
			throw new NoSuchElementException();
		prev.removeNext();
		list.numItems -= 1;
		curr = prev;
		prev = null;
	}
}