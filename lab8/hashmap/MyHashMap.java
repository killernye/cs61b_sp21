package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /** Default setting. */
    private static final int INITIAL_SIZE = 16;
    private static final double INITIAL_LOAD_FACTOR = 0.75;

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    int size;
    private double loadFactor;
    private Set<K> keySet;

    /** Constructors */
    public MyHashMap() {
        this(INITIAL_SIZE, INITIAL_LOAD_FACTOR);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, INITIAL_LOAD_FACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        loadFactor = maxLoad;
        size = 0;
        keySet = createKeySet();
    }


    /**
     * Returns a HashSet to store all the keys.
     */
    private Set<K> createKeySet() {
        return new HashSet<>();
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    /**
     * Resize the Map.
     */
    private void resize(int capacity) {
        Collection<Node>[] arr = createTable(capacity);

        for (Collection<Node> bucket: buckets) {
            if (bucket != null) {
                for (Node n: bucket) {

                    int bucketNum = Math.floorMod(n.key.hashCode(),buckets.length);
                    if (arr[bucketNum] == null) {
                        arr[bucketNum] = createBucket();
                    }

                    arr[bucketNum].add(n);
                }
            }
        }

        buckets = arr;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!


    @Override
    /** Removes all of the mappings from this map. */
    public void clear() {
        size = 0;
        buckets = createTable(INITIAL_SIZE);
    }

    @Override
    /** Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        int bucketNum = Math.floorMod(key.hashCode(),buckets.length);
        Collection<Node> bucket = buckets[bucketNum];

        if (bucket == null) {
            return false;
        }

        for (Node n: bucket) {
            if (n.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        int bucketNum = Math.floorMod(key.hashCode(),buckets.length);
        Collection<Node> bucket = buckets[bucketNum];

        if (bucket == null) {
            return null;
        }

        for (Node n: bucket) {
            if (n.key.equals(key)) {
                return n.value;
            }
        }
        return null;
    }

    @Override
    /** Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    @Override
    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    public void put(K key, V value){
        if (size() * 1.0 / buckets.length > loadFactor) {
            resize(buckets.length * 2);
        }

        int bucketNum = Math.floorMod(key.hashCode(),buckets.length);
        Collection<Node> bucket = buckets[bucketNum];

        if (bucket == null) {
            bucket = createBucket();
        }

        for (Node n: bucket) {
            if (n.key.equals(key)) {
                n.value = value;
                return;
            }
        }

        Node n = createNode(key, value);
        bucket.add(n);
        keySet.add(key);
        size += 1;
        buckets[bucketNum] = bucket;
    }

    @Override
    /** Returns a Set view of the keys contained in this map. */
    public Set<K> keySet() {
        return keySet;
    }

    @Override
    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    public V remove(K key) {

        int bucketNum = Math.floorMod(key.hashCode(),buckets.length);
        Collection<Node> bucket = buckets[bucketNum];
        for (Node n: bucket) {
            if (n.key.equals(key)) {
                bucket.remove(n);
                size -= 1;
                return n.value;
            }
        }
        return null;
    }

    @Override
    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    public V remove(K key, V value) {
        int bucketNum = Math.floorMod(key.hashCode(),buckets.length);
        Collection<Node> bucket = buckets[bucketNum];
        for (Node n: bucket) {
            if (n.key.equals(key) && n.value.equals(value)) {
                bucket.remove(n);
                size -= 1;
                return n.value;
            }
        }
        return null;
    }

    @Override
    /**
     * Implement Iterable interface.
     */
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
