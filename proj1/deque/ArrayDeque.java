package deque;

import afu.org.checkerframework.checker.igj.qual.I;
import net.sf.saxon.om.Item;

import java.lang.reflect.Array;
import java.util.Iterator;

public class ArrayDeque<Item> implements Deque<Item>,Iterable<Item>{
    int end;
    int size;
    int front;
    Item[] items;

    public ArrayDeque(){
        this.items = (Item[]) new Object[8];
        size = 0;
        front = 0;
        end = 0;
    }
    private void resize(int capacity) {
        Item[] a = (Item[]) new Object[capacity];
        System.arraycopy(items, front, a, 0, size);
        front = 0;
        end = size;
        items = a;
    }
    @Override
    public void addFirst(Item item) {
        if(size == items.length){
            resize(size * 2);
        }
        items[end] = item;
        end += 1;
        size += 1;

    }

    @Override
    public void addLast(Item x) {
        if (size == items.length) {
            resize(size * 2);
        }

        items[end] = x;
        end += 1;
        size = size + 1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0 ? true:false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for(int i = front; i < end; i += 1){
            System.out.println(items[i].toString());
        }
    }

    @Override
    public Item removeFirst() {
        if(size == 0){
            return null;
        }
        if(size < items.length / 4 && size > 8){
            resize(items.length / 4);
        }
        Item res = items[front];
        size -= 1;
        front += 1;
        return res;

    }
    public Item getLast() {
        return items[end - 1];
    }
    @Override
    public Item removeLast() {
        if(size == 0){
            return null;
        }
        if(size < items.length / 4 && size > 8){
            resize(items.length / 4);
        }
        Item x = getLast();
        items[end - 1] = null;
        end -= 1;
        size = size - 1;
        return x;
    }

    @Override
    public Item get(int i) {
        return items[i];
    }

    @Override
    public Iterator<Item> iterator() {
        return new ArrayDequeIterator();
    }
    public class ArrayDequeIterator implements Iterator<Item>{
        int cur = front;

        @Override
        public boolean hasNext() {
            return cur < size?true:false;
        }

        @Override
        public Item next() {
            cur += 1;
            return items[cur - 1];
        }
    }

}
