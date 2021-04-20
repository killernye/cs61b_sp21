package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private BSTNode root;
    private int size;

    public BSTMap() {
        root = null;
        size = 0;
    }

    /* Naked recursive bst structure underlying BSTMap. */
    private class BSTNode {
        K key;
        V value;
        BSTNode right;
        BSTNode left;

        BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
            right = null;
            left = null;
        }

        /* Returns true if this tree contains the specified key. */
        boolean containsKey(K key) {
            int compareResult  = this.key.compareTo(key);
            if (compareResult == 0) {
                return true;
            } else if (compareResult > 0) {
                if (left != null) {
                    return left.containsKey(key);
                } else {
                    return false;
                }
            } else {
                if (right != null) {
                    return right.containsKey(key);
                } else {
                    return false;
                }
            }
        }

        /* change the value for a specified key, suppose the key exists */
        void changeValue(K key, V value) {
            BSTNode ptr = this;
            while (ptr != null) {
                int compareResult = ptr.key.compareTo(key);
                if (compareResult == 0) {
                    ptr.value = value;
                    return;
                } else if (compareResult > 0) {
                    ptr = ptr.left;
                } else {
                    ptr = ptr.right;
                }
            }
        }

        /* insert a new BSTNode to the tree. */
        void insert(K key, V value) {
            BSTNode node = new BSTNode(key, value);
            int compareResult = this.key.compareTo(key);
            if (compareResult > 0) {
                if (this.left != null) {
                    this.left.insert(key, value);
                } else {
                    this.left = node;
                }
            } else if (compareResult < 0) {
                if (this.right != null) {
                    this.right.insert(key, value);
                } else {
                    this.right = node;
                }
            }
        }

        /* Returns the value mapping to a specified key. */
        V get(K key) {
            int compareResult = this.key.compareTo(key);
            if (compareResult == 0) {
                return this.value;
            } else if (compareResult > 0) {
                if (this.left == null) {
                    return null;
                } else {
                    return this.left.get(key);
                }
            } else {
                if (this.right == null) {
                    return null;
                } else  {
                    return this.right.get(key);
                }
            }
        }
    }

    @Override
    /** Removes all of the mappings from this map. */
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        if (root == null) {
            return false;
        }
        return root.containsKey(key);
    }

    @Override
    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        if (root == null) {
            return null;
        }
        return root.get(key);
    }

    @Override
    /* Returns the number of key-value mappings in this map. */
    public int size() {return size;}

    @Override
    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        if (root == null) {
            root = new BSTNode(key, value);
            size += 1;
            return;
        }

        if (root.containsKey(key)) {
            root.changeValue(key, value);
        } else {
            root.insert(key, value);
            size += 1;
        }
    }

    @Override
    /* Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    public Set<K> keySet() {
        throw new UnsupportedOperationException("not implement yet");
    }

    @Override
    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    public V remove(K key) {
        throw new UnsupportedOperationException("not implement yet");
    }

    @Override
    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("not implement yet");
    }

    @Override
    /* Returns a Iterator which can iterate through all the keys. */
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException("not implement yet");
    }

    /* prints out your BSTMap in order of increasing Key. */
    public void printInOrder() {}
}
