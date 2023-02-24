package bstmap;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.NoSuchElementException;

public class BSTMap<K extends Comparable<K>,V> implements Map61B{
    private Node root;             // root of BST

    @Override
    public Iterator iterator() {
        return new Iterator<K>() {
            @Override
            public boolean hasNext() {
                return size() == 0;
            }

            @Override
            public K next() {
                Node cur = deleteMin(root);
                assert check();
                return cur.K;
            }
        };
    }

    private class Node {
        private K K;           // sorted by K
        private V val;         // associated data
        private Node left, right;  // left and right subtrees
        private int size;          // number of nodes in subtree

        private Node(K K, V val, int size) {
            this.K = K;
            this.val = val;
            this.size = size;
        }
    }

    /**
     * Initializes an empty symbol table.
     */
    private BSTMap() {
    }

    /**
     * Returns true if this symbol table is empty.
     * @return {@code true} if this symbol table is empty; {@code false} otherwise
     */
    private boolean isEmpty() {
        return this.size() ==0;
    }

    @Override
    public void clear() {
        while(!isEmpty()){
            this.deleteMin();
        }
    }

    @Override
    public boolean containsKey(Object K) {
        for(K k : Ks()){
            if(k.equals(K)){
                return true;
            }
        }
        return false;

    }

    @Override
    public Object get(Object K) {
        return get((K) K);
    }

    /**
     * Returns the number of K-V pairs in this symbol table.
     * @return the number of K-V pairs in this symbol table
     */
    public int size() {
        return size(root);
    }

    @Override
    public void put(Object K, Object V) {
        this.put((K)K,(V)V);
    }

    @Override
    public Set keySet() {
        Set<K> set = new HashSet<>();
        for(K k : Ks()){
            set.add(k);
        }
        return set;
    }

    @Override
    public Object remove(Object K) {
        Object obj = get((K) K);
        delete((K)K);
        return obj;
    }

    @Override
    public Object remove(Object K, Object V) {
        return null;
    }

    // return number of K-V pairs in BST rooted at x
    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }

    /**
     * Does this symbol table contain the given K?
     *
     * @param  K the K
     * @return {@code true} if this symbol table contains {@code K} and
     *         {@code false} otherwise
     * @throws IllegalArgumentException if {@code K} is {@code null}
     */
    private boolean contains(K K) {
        if (K == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(K) != null;
    }

    /**
     * Returns the V associated with the given K.
     *
     * @param  K the K
     * @return the V associated with the given K if the K is in the symbol table
     *         and {@code null} if the K is not in the symbol table
     * @throws IllegalArgumentException if {@code K} is {@code null}
     */
    private V get(K K) {
        return get(root, K);
    }

    private V get(Node x, K K) {
        if (K == null) throw new IllegalArgumentException("calls get() with a null K");
        if (x == null) return null;
        int cmp = K.compareTo(x.K);
        if      (cmp < 0) return get(x.left, K);
        else if (cmp > 0) return get(x.right, K);
        else              return x.val;
    }

    /**
     * Inserts the specified K-V pair into the symbol table, overwriting the old
     * V with the new V if the symbol table already contains the specified K.
     * Deletes the specified K (and its associated V) from this symbol table
     * if the specified V is {@code null}.
     *
     * @param  K the K
     * @param  val the V
     * @throws IllegalArgumentException if {@code K} is {@code null}
     */
    private void put(K K, V val) {
        if (K == null) throw new IllegalArgumentException("calls put() with a null K");
/*        if (val == null) {
            delete(K);
            return;
        }*/
        root = put(root, K, val);
        assert check();
    }

    private Node put(Node x, K K, V val) {
        if (x == null) return new Node(K, val, 1);
        int cmp = K.compareTo(x.K);
        if      (cmp < 0) x.left  = put(x.left,  K, val);
        else if (cmp > 0) x.right = put(x.right, K, val);
        else              x.val   = val;
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }


    /**
     * Removes the smallest K and associated V from the symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
    private void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow");
        root = deleteMin(root);
        assert check();
    }

    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    /**
     * Removes the largest K and associated V from the symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
    private void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow");
        root = deleteMax(root);
        assert check();
    }

    private Node deleteMax(Node x) {
        if (x.right == null) return x.left;
        x.right = deleteMax(x.right);
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    /**
     * Removes the specified K and its associated V from this symbol table
     * (if the K is in this symbol table).
     *
     * @param  K the K
     * @throws IllegalArgumentException if {@code K} is {@code null}
     */
    private void delete(K K) {
        if (K == null) throw new IllegalArgumentException("calls delete() with a null K");
        root = delete(root, K);
        assert check();
    }

    private Node delete(Node x, K K) {
        if (x == null) return null;

        int cmp = K.compareTo(x.K);
        if      (cmp < 0) x.left  = delete(x.left,  K);
        else if (cmp > 0) x.right = delete(x.right, K);
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


    /**
     * Returns the smallest K in the symbol table.
     *
     * @return the smallest K in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    private K min() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return min(root).K;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        else                return min(x.left);
    }

    /**
     * Returns the largest K in the symbol table.
     *
     * @return the largest K in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    private K max() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return max(root).K;
    }

    private Node max(Node x) {
        if (x.right == null) return x;
        else                 return max(x.right);
    }

    /**
     * Returns the largest K in the symbol table less than or equal to {@code K}.
     *
     * @param  K the K
     * @return the largest K in the symbol table less than or equal to {@code K}
     * @throws NoSuchElementException if there is no such K
     * @throws IllegalArgumentException if {@code K} is {@code null}
     */
    private K floor(K K) {
        if (K == null) throw new IllegalArgumentException("argument to floor() is null");
        if (isEmpty()) throw new NoSuchElementException("calls floor() with empty symbol table");
        Node x = floor(root, K);
        if (x == null) throw new NoSuchElementException("argument to floor() is too small");
        else return x.K;
    }

    private Node floor(Node x, K K) {
        if (x == null) return null;
        int cmp = K.compareTo(x.K);
        if (cmp == 0) return x;
        if (cmp <  0) return floor(x.left, K);
        Node t = floor(x.right, K);
        if (t != null) return t;
        else return x;
    }

    private K floor2(K K) {
        K x = floor2(root, K, null);
        if (x == null) throw new NoSuchElementException("argument to floor() is too small");
        else return x;

    }

    private K floor2(Node x, K K, K best) {
        if (x == null) return best;
        int cmp = K.compareTo(x.K);
        if      (cmp  < 0) return floor2(x.left, K, best);
        else if (cmp  > 0) return floor2(x.right, K, x.K);
        else               return x.K;
    }

    /**
     * Returns the smallest K in the symbol table greater than or equal to {@code K}.
     *
     * @param  K the K
     * @return the smallest K in the symbol table greater than or equal to {@code K}
     * @throws NoSuchElementException if there is no such K
     * @throws IllegalArgumentException if {@code K} is {@code null}
     */
    private K ceiling(K K) {
        if (K == null) throw new IllegalArgumentException("argument to ceiling() is null");
        if (isEmpty()) throw new NoSuchElementException("calls ceiling() with empty symbol table");
        Node x = ceiling(root, K);
        if (x == null) throw new NoSuchElementException("argument to ceiling() is too large");
        else return x.K;
    }

    private Node ceiling(Node x, K K) {
        if (x == null) return null;
        int cmp = K.compareTo(x.K);
        if (cmp == 0) return x;
        if (cmp < 0) {
            Node t = ceiling(x.left, K);
            if (t != null) return t;
            else return x;
        }
        return ceiling(x.right, K);
    }

    /**
     * Return the K in the symbol table of a given {@code rank}.
     * This K has the property that there are {@code rank} Ks in
     * the symbol table that are smaller. In other words, this K is the
     * ({@code rank}+1)st smallest K in the symbol table.
     *
     * @param  rank the order statistic
     * @return the K in the symbol table of given {@code rank}
     * @throws IllegalArgumentException unless {@code rank} is between 0 and
     *        <em>n</em>–1
     */
    private K select(int rank) {
        if (rank < 0 || rank >= size()) {
            throw new IllegalArgumentException("argument to select() is invalid: " + rank);
        }
        return select(root, rank);
    }

    // Return K in BST rooted at x of given rank.
    // Precondition: rank is in legal range.
    private K select(Node x, int rank) {
        if (x == null) return null;
        int leftSize = size(x.left);
        if      (leftSize > rank) return select(x.left,  rank);
        else if (leftSize < rank) return select(x.right, rank - leftSize - 1);
        else                      return x.K;
    }

    /**
     * Return the number of Ks in the symbol table strictly less than {@code K}.
     *
     * @param  K the K
     * @return the number of Ks in the symbol table strictly less than {@code K}
     * @throws IllegalArgumentException if {@code K} is {@code null}
     */
    private int rank(K K) {
        if (K == null) throw new IllegalArgumentException("argument to rank() is null");
        return rank(K, root);
    }

    // Number of Ks in the subtree less than K.
    private int rank(K K, Node x) {
        if (x == null) return 0;
        int cmp = K.compareTo(x.K);
        if      (cmp < 0) return rank(K, x.left);
        else if (cmp > 0) return 1 + size(x.left) + rank(K, x.right);
        else              return size(x.left);
    }

    /**
     * Returns all Ks in the symbol table in ascending order,
     * as an {@code Iterable}.
     * To iterate over all of the Ks in the symbol table named {@code st},
     * use the foreach notation: {@code for (K K : st.Ks())}.
     *
     * @return all Ks in the symbol table in ascending order
     */
    private Iterable<K> Ks() {
        if (isEmpty()) return new Queue<K>();
        return Ks(min(), max());
    }

    /**
     * Returns all Ks in the symbol table in the given range
     * in ascending order, as an {@code Iterable}.
     *
     * @param  lo minimum endpoint
     * @param  hi maximum endpoint
     * @return all Ks in the symbol table between {@code lo}
     *         (inclusive) and {@code hi} (inclusive) in ascending order
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *         is {@code null}
     */
    private Iterable<K> Ks(K lo, K hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to Ks() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to Ks() is null");

        Queue<K> queue = new Queue<K>();
        Ks(root, queue, lo, hi);
        return queue;
    }

    private void Ks(Node x, Queue<K> queue, K lo, K hi) {
        if (x == null) return;
        int cmplo = lo.compareTo(x.K);
        int cmphi = hi.compareTo(x.K);
        if (cmplo < 0) Ks(x.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(x.K);
        if (cmphi > 0) Ks(x.right, queue, lo, hi);
    }

    /**
     * Returns the number of Ks in the symbol table in the given range.
     *
     * @param  lo minimum endpoint
     * @param  hi maximum endpoint
     * @return the number of Ks in the symbol table between {@code lo}
     *         (inclusive) and {@code hi} (inclusive)
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *         is {@code null}
     */
    private int size(K lo, K hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to size() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to size() is null");

        if (lo.compareTo(hi) > 0) return 0;
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else              return rank(hi) - rank(lo);
    }

    /**
     * Returns the height of the BST (for debugging).
     *
     * @return the height of the BST (a 1-node tree has height 0)
     */
    private int height() {
        return height(root);
    }
    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.left), height(x.right));
    }

    /**
     * Returns the Ks in the BST in level order (for debugging).
     *
     * @return the Ks in the BST in level order traversal
     */
    private Iterable<K> levelOrder() {
        Queue<K> Ks = new Queue<K>();
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) continue;
            Ks.enqueue(x.K);
            queue.enqueue(x.left);
            queue.enqueue(x.right);
        }
        return Ks;
    }

    /*************************************************************************
     *  Check integrity of BST data structure.
     ***************************************************************************/
    private boolean check() {
        if (!isBST())            System.out.println("Not in symmetric order");
        if (!isSizeConsistent()) System.out.println("Subtree counts not consistent");
        if (!isRankConsistent()) System.out.println("Ranks not consistent");
        return isBST() && isSizeConsistent() && isRankConsistent();
    }

    // does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
    private boolean isBST() {
        return isBST(root, null, null);
    }

    // is the tree rooted at x a BST with all Ks strictly between min and max
    // (if min or max is null, treat as empty constraint)
    // Credit: elegant solution due to Bob Dondero
    private boolean isBST(Node x, K min, K max) {
        if (x == null) return true;
        if (min != null && x.K.compareTo(min) <= 0) return false;
        if (max != null && x.K.compareTo(max) >= 0) return false;
        return isBST(x.left, min, x.K) && isBST(x.right, x.K, max);
    }

    // are the size fields correct?
    private boolean isSizeConsistent() { return isSizeConsistent(root); }
    private boolean isSizeConsistent(Node x) {
        if (x == null) return true;
        if (x.size != size(x.left) + size(x.right) + 1) return false;
        return isSizeConsistent(x.left) && isSizeConsistent(x.right);
    }

    // check that ranks are consistent
    private boolean isRankConsistent() {
        for (int i = 0; i < size(); i++)
            if (i != rank(select(i))) return false;
        for (K K : Ks())
            if (K.compareTo(select(rank(K))) != 0) return false;
        return true;
    }


    /**
     * Unit tests the {@code BST} data type.
     *
     * @param args the command-line arguments
     */
}
