import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

class RandomizedQueueIterator<Item> implements Iterator<Item> {
    private final Object[] elements;
    private final int size;
    private int current = 0;
    public RandomizedQueueIterator(Object[] elements, int size) {
        this.elements = elements;
        this.size = size;

        StdRandom.shuffle(this.elements, 0, size);
    }

    @Override
    public boolean hasNext() {
        return current != size;
    }

    @Override
    public Item next() {
        if (!hasNext()) throw new NoSuchElementException();

        Item item = (Item) elements[current];
        current++;

        return item;
    }
}