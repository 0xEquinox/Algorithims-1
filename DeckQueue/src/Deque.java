import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> firstNode;
    private int length;

    public Deque() {
        this.firstNode = null;
        this.length = 0;
    }

    public boolean isEmpty() {
        return length == 0;
    }

    public int size() {
        return length;
    }

    public void addFirst(Item item) {
        this.firstNode = new Node<>(item, firstNode);

        length += 1;
    }

    public void addLast(Item item) {
        // Is it empty? If so just add first
        if (isEmpty()) {
            this.firstNode = new Node<>(item, null);
            length += 1;
            return;
        }

        Node<Item> last = get(length - 1);

        length += 1;
        last.setNext(new Node<>(item, null));
    }

    public Item removeFirst() {
        if (length == 0) throw new NoSuchElementException();

        Node<Item> second = firstNode.getNext();
        Item firstValue = firstNode.getValue();

        firstNode = second;
        length -= 1;

        return firstValue;
    }

    public Item removeLast() {
        if (length == 0) throw new NoSuchElementException();

        if (length == 1) {
            Item last = firstNode.getValue();
            firstNode.setValue(null);

            length -= 1;
            return last;
        }

        Node<Item> secondToLast = get(length - 2);
        Item lastValue = get(length - 1).getValue();

        secondToLast.setNext(null);
        length -= 1;

        return lastValue;
    }

    private Node<Item> get(int n) {
        //Loop until at n element
        Node<Item> current = firstNode;
        for (int i = 0; i < n; i++) {
            current = current.getNext();
        }
        return current;
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeueIterator<>(firstNode);
    }

    public static void main(String[] args) {

    }
}
