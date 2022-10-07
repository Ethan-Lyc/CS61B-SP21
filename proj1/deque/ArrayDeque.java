package deque;

import afu.org.checkerframework.checker.igj.qual.I;
import net.sf.saxon.om.Item;

import java.lang.reflect.Array;
import java.util.Iterator;

public class ArrayDeque<Item> implements Deque<Item>,Iterable<Item>{
    int nextFirst;
    int size;
    int nextLast;
    Item[] items;

    public ArrayDeque(){
        this.items = (Item[]) new Object[8];
        size = 0;
        nextFirst = 3;
        nextLast = 4;
    }
    public void shrinkSize(){
        if(isEmpty()){
            resize(8);
        }else if(items.length / 4 > size && size >= 4){
            resize(size * 2);
        }
    }
    public void resize(int capacity) {
        Item[] a = (Item[]) new Object[capacity];
        int firstPos = Math.abs(capacity - size)/ 2;
        System.arraycopy(items,nextFirst + 1,a,firstPos,size);
        items = a;
        nextFirst = firstPos - 1;
        nextLast = firstPos + size;

    }
    @Override
    public void addFirst(Item item) {
        items[nextFirst] = item;
        nextFirst -= 1;
        size += 1;
        if(nextFirst == -1){
            resize(size * 2);
        }

    }

    @Override
    public void addLast(Item x) {
        items[nextLast] = x;
        nextLast += 1;
        size += 1;
        if(nextLast == items.length){
            resize(size * 2);
        }


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
        for(int i = nextFirst + 1; i < nextLast ; i += 1){
            System.out.println(items[i].toString());
        }
    }

    @Override
    public Item removeFirst() {
        if(isEmpty()){
            return null;
        }
        nextFirst += 1;
        Item item = items[nextFirst];
        items[nextFirst] = null;
        size -= 1;
        shrinkSize();
        return item;
    }
    public Item getLast() {
        return items[nextLast - 1];
    }
    @Override
    public Item removeLast() {
        if(isEmpty()){
            return null;
        }
        nextLast -= 1;
        Item item = items[nextLast];
        items[nextLast] = null;
        size -= 1;
        shrinkSize();
        return item;
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
        int cur = nextFirst;

        @Override
        public boolean hasNext() {
            return cur < nextLast - 1?true:false;
        }

        @Override
        public Item next() {
            cur += 1;
            return items[cur];
        }
    }

}
