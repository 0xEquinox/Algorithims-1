import java.util.Iterator;
import java.util.NoSuchElementException;

class DequeueIterator<Item> implements Iterator<Item> {
    private Node<Item> current;

    public DequeueIterator(Node<Item> first) {
        this.current = first;
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public Item next() {
        if (!hasNext()) throw new NoSuchElementException();

        Item item = current.getValue();
        current = current.getNext();
        return item;
    }
}
