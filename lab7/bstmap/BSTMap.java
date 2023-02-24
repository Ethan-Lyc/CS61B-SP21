package bstmap;

import java.util.*;

public class BSTMap<K extends Comparable<K>,V> implements Map61B<K,V>{
    private Node root;
    private class Node {
        private int size;
        private K key;
        private V val;
        private Node left, right;

        public Node(K key, V val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }

    }

    public BSTMap(){

    }
    private boolean isEmpty(){
        return size() == 0;
    }

    @Override
    public void clear() {
        while(!isEmpty()){
            deleteMin();
        }
    }

    @Override
    public boolean containsKey(K key) {
        if(key == null) throw new IllegalArgumentException("argument to contans() is null!!");
        return containsKeyHelper(root,key) != null;
    }
    private Node containsKeyHelper(Node x,K key){
        if (key == null) throw new IllegalArgumentException("calls get() with a null key");
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) return containsKeyHelper(x.left, key);
        else if (cmp > 0) return containsKeyHelper(x.right, key);
        return x;
    }


    @Override
    public V get(K key) {
        return get(root,key);
    }
    private V get(Node x,K key){
        if (key == null) throw new IllegalArgumentException("calls get() with a null key");
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else              return x.val;
    }

    @Override
    public int size() {
        return size(root);
    }
    private int size(Node x){
        if(x == null) return 0;
        return x.size;
    }

    @Override
    public void put(K key, V value) {
        if(key == null) throw new IllegalArgumentException("calls put() with a null key");
        root = put(root,key,value);
    }
    private Node put(Node x, K key, V val){
        if (x == null) return new Node(key, val, 1);
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left  = put(x.left,  key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else              x.val   = val;
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        keyDFS(root,set);
        return set;
    }
    private void keyDFS(Node x,Set set){
        if(x == null) return;
        set.add(x.key);
        if(x.left != null) keyDFS(x.left,set);
        if(x.right != null) keyDFS(x.right,set);

    }
    private void deleteMin(){
        if(isEmpty()) throw new NoSuchElementException("symbol table underflow");
        root = deleteMin(root);
    }
    private Node deleteMin(Node x){
        if(x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    @Override
    public V remove(K key) {
        if (key == null) throw new IllegalArgumentException("calls delete() with a null key");
        V val = get(key);
        root = removeHelper(root, key);
        return val;
    }
    private Node removeHelper(Node x, K key){
        if (x == null) return null;

        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left  = removeHelper(x.left,  key);
        else if (cmp > 0) x.right = removeHelper(x.right, key);
        else {
            if (x.right == null) return x.left;
            if (x.left  == null) return x.right;
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }
    private K min() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return min(root).key;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        else                return min(x.left);
    }


    @Override
    public V remove(K key, V value) {
        if (key == null) throw new IllegalArgumentException("calls delete() with a null key");
        return removeHelper(root,key).val;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
    public void printInOrder(){
        printInOrder(root);
    }
    private void printInOrder(Node x){
        if(x.left != null) printInOrder(x.left);
        System.out.println(x.val);
        if(x.right != null) printInOrder(x.right);
    }


}
