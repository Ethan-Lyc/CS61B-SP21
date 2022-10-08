package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private final Node<T> head = new Node<>(null, null, null);
    private int size;

    public LinkedListDeque() {
        head.prev = head;
        head.next = head;
        size = 0;
    }

    private static class Node<T> {
        private final T item;
        private Node<T> prev;
        private Node<T> next;

        Node(T item, Node<T> prev, Node<T> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
        @Override
        public String toString() {
            if (item == null) {
                return "null";
            }
            return item.toString();
        }
    }
    @Override
    public void addFirst(T item) {
        Node<T> node = new Node<>(item, head, head.next);
        node.next.prev = node;
        node.prev.next = node;
        size += 1;
    }

    @Override
    public void addLast(T item) {
        Node<T> node = new Node<>(item, head.prev, head);
        head.prev.next = node;
        head.prev = node;
        size += 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node<T> node = head;
        for (int i = 0; i < size; i += 1) {
            node = node.next;
            System.out.println(node.item.toString());
        }
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        } else {
            T res = head.next.item;
            head.next = head.next.next;
            head.next.prev = head;
            size -= 1;
            return res;
        }
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        } else {
            T res = head.prev.item;
            Node<T> last = head.prev.prev;
            last.next = head;
            head.prev = last;
            size -= 1;
            return res;
        }
    }

    @Override
    public T get(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        } else {
            Node<T> node = head.next;
            for (int i = 0; i < index; i += 1) {
                node = node.next;
            }
            return node.item;
        }
    }
    public T getRecursive(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        } else {
            return getRecursiveHelper(index, head.next);
        }
    }
    private T getRecursiveHelper(int index, Node<T> curnode) {
        if (index == 0) {
            return curnode.item;
        } else {
            return getRecursiveHelper(index - 1, curnode.next);
        }
    }
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof LinkedListDeque)) {
            return false;
        }
        LinkedListDeque<?> lld = (LinkedListDeque<?>) o;
        if (lld.size() != size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (lld.get(i) != get(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }
    private class LinkedListDequeIterator implements Iterator<T> {
        Node<T> node;

        public LinkedListDequeIterator() {
            node = head.next;
        }

        @Override
        public boolean hasNext() {
            return node == head;
        }

        @Override
        public T next() {
            T res = node.item;
            node = node.next;
            return res;
        }
    }
}
