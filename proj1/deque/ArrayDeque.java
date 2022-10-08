package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private int nextFirst;
    private int size;
    private int nextLast;
    private T[] items;

    public ArrayDeque() {
        this.items = (T[]) new Object[8];
        size = 0;
        nextFirst = 3;
        nextLast = 4;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return this == null;
        } else if (o == this) {
            return true;
        } else if (!(o instanceof Deque)) {
            return false;
        }
        Deque<?> lld = (Deque<?>) o;
        if (lld.size() != this.size) {
            return false;
        }
        for (int i = 0; i < size; i += 1) {
            if (lld.get(i) != this.get(i)) {
                return false;
            }
        }
        return true;
    }
    private void shrinkSize() {
        if (isEmpty()) {
            resize(8);
        } else if (items.length / 4 > size && size >= 4) {
            resize(size * 2);
        }
    }
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int firstPos = Math.abs(capacity - size) / 2;
        System.arraycopy(items, nextFirst + 1, a, firstPos, size);
        items = a;
        nextFirst = firstPos - 1;
        nextLast = firstPos + size;

    }
    @Override
    public void addFirst(T item) {
        items[nextFirst] = item;
        nextFirst -= 1;
        size += 1;
        if (nextFirst == -1) {
            resize(size * 2);
        }

    }

    @Override
    public void addLast(T x) {
        items[nextLast] = x;
        nextLast += 1;
        size += 1;
        if (nextLast == items.length) {
            resize(size * 2);
        }


    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (int i = nextFirst + 1; i < nextLast; i += 1) {
            System.out.println(items[i].toString());
        }
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        nextFirst += 1;
        T item = items[nextFirst];
        items[nextFirst] = null;
        size -= 1;
        shrinkSize();
        return item;
    }
    private T getLast() {
        return items[nextLast - 1];
    }
    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        nextLast -= 1;
        T item = items[nextLast];
        items[nextLast] = null;
        size -= 1;
        shrinkSize();
        return item;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        }
        int itemIndex = nextFirst + 1 + index;
        return items[itemIndex];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }
    private class ArrayDequeIterator implements Iterator<T> {
        int cur = nextFirst;

        @Override
        public boolean hasNext() {
            return cur < nextLast - 1;
        }

        @Override
        public T next() {
            cur += 1;
            return items[cur];
        }
    }

}
