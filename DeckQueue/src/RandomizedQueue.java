import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item>  {
    private Object[] elements;
    private int size = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        elements = new Object[10];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    private void realloc() {
        Object[] newArray = new Object[elements.length + 10];

        System.arraycopy(elements, 0, newArray, 0, elements.length);

        this.elements = newArray;
    }

    private void realloc(int newSize) {
        Object[] newArray = new Object[newSize];

        System.arraycopy(elements, 0, newArray, 0, elements.length);

        this.elements = newArray;
    }

    // add the item
    public void enqueue(Item item) {
        if (size == elements.length) realloc();

        elements[size] = item;
        size += 1;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }

        int idx = StdRandom.uniformInt(size);
        Item val = (Item) elements[idx];
        elements[idx] = elements[size - 1];
        elements[size - 1] = null;
        size -= 1;

//        if (size <= elements.length / 4)
//            realloc(elements.length / 2);

        return val;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        int randIndex = StdRandom.uniformInt(0, size);

        return (Item) elements[randIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator<>(elements, size);
    }

    // unit testing (required)
    public static void main(String[] args) {

    }
}
