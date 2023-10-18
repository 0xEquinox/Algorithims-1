class Node<Item> {
    private Item value;
    private Node<Item> next;

    public Node(Item value, Node<Item> next) {
        this.value = value;
        this.next = next;
    }

    public Item getValue() {
        return value;
    }

    public Node<Item> getNext() {
        return next;
    }

    public void setValue(Item value) {
        this.value = value;
    }

    public void setNext(Node<Item> next) {
        this.next = next;
    }
}