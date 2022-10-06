package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>,Iterable<T>{
    Node<T> head = new Node<>();
    int size;

    public LinkedListDeque() {
        head.prev = head;
        head.next = head;
        size = 0;
    }
    public LinkedListDeque(T item){
        head.next = new Node<>(item,head,head);
        head.prev = head.next;
        size = 1;
    }


    public class Node<T>{
        T item;
        Node<T> prev;
        Node<T> next;

        public Node(T item, Node<T> prev, Node<T> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }

        public Node() {

        }
        public Node(T item){
            this.item = item;
        }
    }
    @Override
    public void addFirst(T item) {
        Node<T> node = new Node<>(item,head,head.next);
        node.next.prev = node;
        node.prev.next = node;
        size += 1;
    }

    @Override
    public void addLast(T item) {
        Node<T> node = new Node<>(item,head.prev,head);
        head.prev.next = node;
        head.prev = node;
        size += 1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0?true:false;

    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node<T> node = head;
        for(int i = 0; i < size; i += 1){
            node = node.next;
            System.out.println(node.item.toString());
        }
    }

    @Override
    public T removeFirst() {
        if(isEmpty()){
            return null;
        }else{
            T res = head.next.item;
            head.next = head.next.next;
            head.next.prev = head;
            size -= 1;
            return res;
        }
    }

    @Override
    public T removeLast() {
        if(isEmpty()){
            return null;
        }else{
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
        if(index < 0 || index > size - 1){
            return null;
        }else{
            Node<T> node = head.next;
            for(int i = 0; i < index; i += 1){
                node = node.next;
            }
            return node.item;
        }
    }

    @Override
    public Iterator iterator() {
        return new LinkedListDequeIterator();
    }
    public class LinkedListDequeIterator implements Iterator<T>{
        Node<T> node;

        public LinkedListDequeIterator() {
            node = head.next;
        }

        @Override
        public boolean hasNext() {
            return node.next != null;
        }

        @Override
        public T next() {
            T res = node.item;
            node = node.next;
            return res;
        }
    }
}
