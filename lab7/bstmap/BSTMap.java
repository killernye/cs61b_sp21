package bstmap;

import edu.princeton.cs.algs4.SET;

import java.util.*;

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
//        boolean containsKey(K key) {
//            int compareResult  = this.key.compareTo(key);
//            if (compareResult == 0) {
//                return true;
//            } else if (compareResult > 0) {
//                if (left != null) {
//                    return left.containsKey(key);
//                } else {
//                    return false;
//                }
//            } else {
//                if (right != null) {
//                    return right.containsKey(key);
//                } else {
//                    return false;
//                }
//            }
//        }

        /* change the value for a specified key, suppose the key exists */
//        void changeValue(K key, V value) {
//            BSTNode ptr = this;
//            while (ptr != null) {
//                int compareResult = ptr.key.compareTo(key);
//                if (compareResult == 0) {
//                    ptr.value = value;
//                    return;
//                } else if (compareResult > 0) {
//                    ptr = ptr.left;
//                } else {
//                    ptr = ptr.right;
//                }
//            }
//        }

        /* insert a new BSTNode to the tree. */
//        void insert(K key, V value) {
//            BSTNode node = new BSTNode(key, value);
//            int compareResult = this.key.compareTo(key);
//            if (compareResult > 0) {
//                if (this.left != null) {
//                    this.left.insert(key, value);
//                } else {
//                    this.left = node;
//                }
//            } else if (compareResult < 0) {
//                if (this.right != null) {
//                    this.right.insert(key, value);
//                } else {
//                    this.right = node;
//                }
//            }
//        }

        /* Returns the value mapping to a specified key. */
//        V get(K key) {
//            int compareResult = this.key.compareTo(key);
//            if (compareResult == 0) {
//                return this.value;
//            } else if (compareResult > 0) {
//                if (this.left == null) {
//                    return null;
//                } else {
//                    return this.left.get(key);
//                }
//            } else {
//                if (this.right == null) {
//                    return null;
//                } else  {
//                    return this.right.get(key);
//                }
//            }
//        }
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
        return containsKey(root, key);
    }

    private boolean containsKey(BSTNode node, K key) {
        if (node == null) {
            return false;
        }

        int compareResult = key.compareTo(node.key);
        if (compareResult == 0) {
            return true;
        } else if (compareResult > 0) {
            return containsKey(node.right, key);
        } else {
            return containsKey(node.left, key);
        }
    }

    @Override
    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        return get(root, key);
    }

    private V get(BSTNode node, K key) {
        if (node == null) {
            return null;
        }

        int compareResult = key.compareTo(node.key);
        if (compareResult == 0) {
            return node.value;
        } else if (compareResult > 0) {
            return get(node.right, key);
        } else {
            return get(node.left, key);
        }
    }



    @Override
    /* Returns the number of key-value mappings in this map. */
    public int size() {return size;}

    @Override
    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        root = put(root, key, value);
    }

    private BSTNode put(BSTNode node, K key, V value) {
        if (node == null) {
            size += 1;
            return new BSTNode(key, value);
        }

        int compareResult = key.compareTo(node.key);
        if (compareResult == 0) {
            node.value= value;
        } else if (compareResult > 0){
            node.right = put(node.right, key, value);
        } else {
            node.left = put(node.left, key, value);
        }

        return node;
    }

    @Override
    /* Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    public Set<K> keySet() {
        List<K> keys = keys();
        return new HashSet<>(keys);
    }

    @Override
    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    public V remove(K key) {
        V res = get(key);
        if (res == null) {
            return null;
        }

        root = remove(root, key);
        size -= 1;
        return res;
    }

    @Override
    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    public V remove(K key, V value) {
        if (get(key).equals(value)) {
            return remove(key);
        }
        return null;
    }

    private BSTNode remove(BSTNode node, K key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp > 0) {
            node.right =  remove(node.right, key);
        } else if (cmp < 0) {
            node.left = remove(node.left, key);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            BSTNode t = node;
            node = min(t.right);
            node.right = deleteMin(t.right);
            node.left = t.left;
        }
        return node;
    }

    private BSTNode deleteMin(BSTNode node) {
        if (node == null) {
            return null;
        }

        if (node.left == null) {
            return node.right;
        } else {
            node.left = deleteMin(node.left);
            return node;
        }
    }

    private BSTNode min(BSTNode node) {
        if (node == null) {
            return null;
        }

        if (node.left == null) {
            return node;
        } else {
            return min(node.left);
        }
    }

    @Override
    /* Returns a Iterator which can iterate through all the keys. */
    public Iterator<K> iterator() {
        return new KeyIterator();
    }

    private class KeyIterator implements Iterator<K> {
        int counter;
        List<K> keys;

        KeyIterator() {
            counter = 0;
            keys = keys();
        }


        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return counter < size();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException("no more elements");
            }

            K res = keys.get(counter);
            counter += 1;
            return res;
        }
    }

    /* prints out your BSTMap in order of increasing Key. */
    public void printInOrder() {
        //printInOrder(root);

        List<K> keys = keys();
        System.out.println(keys);
    }


    private List<K> keys() {
        List<K> ks = new ArrayList<>();
        keys(root, ks);

        return ks;
    }

    private void keys(BSTNode node, List<K> keys) {
        if (node == null) {
            return;
        }

        keys(node.left, keys);
        keys.add(node.key);
        keys(node.right, keys);
    }


    private void printInOrder(BSTNode node) {
        if (node == null) {
            return;
        }

        printInOrder(node.left);
        System.out.println(node.key);
        printInOrder(node.right);
    }
}
