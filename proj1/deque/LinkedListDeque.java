package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T> {

    /*
    Invariants:
    1. the first item is the item immediately after sentinel.
    2. the item immediately after the last item is sentinel.
    3. the index of the last item is always size - 1
     */

    private static class Node<T> {
        T _item;
        boolean _isSentinel;
        Node _next;
        Node _prev;

        Node(T item, Node prev, Node next, boolean isSentinel) {
            _item = item;
            _prev = prev;
            _next = next;
            _isSentinel = isSentinel;
        }

        /** Returns if this node is a sentinel. */
        boolean isSentinel() {
            return _isSentinel;
        }

        /** Returns whether or not the object o is equal to this node. */
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o == null) {
                return false;
            } else if (!(o instanceof Node)) {
                return false;
            } else if (this.isSentinel() && ((Node<?>) o).isSentinel()) {
                return true;
            } else if (this.isSentinel() || ((Node<?>) o).isSentinel()){
                return false;
            } else if (!_item.equals(((Node<?>) o)._item)) {
                return false;
            } else {
                return true;
            }
        }
    }

    private int _size;
    private final Node sentinel;

    /** Creates an empty Deque. */
    public LinkedListDeque() {
        _size = 0;
        sentinel = new Node(null, null, null, true);
        sentinel._prev = sentinel;
        sentinel._next = sentinel;
    }

    /** Method to create an IntList from an argument list. */
    public LinkedListDeque of(T ...argList) {
        LinkedListDeque deque = new LinkedListDeque();
        for (int i = 0; i < argList.length; i += 1) {
            deque.addLast(argList[i]);
        }
        return deque;
    }

    /** Adds an item of type T to the front of the deque. You can assume that item is never null.
     * @param item
     */
    public void addFirst(T item) {
        Node toBeAdd = new Node(item, sentinel, sentinel._next, false);
        sentinel._next._prev = toBeAdd;
        sentinel._next = toBeAdd;
        _size += 1;
    }

    /** Adds an item of type T to the back of the deque. You can assume that item is never null. */
    public void addLast(T item) {
        Node toBeAdd = new Node(item, sentinel._prev, sentinel, false);
        sentinel._prev._next = toBeAdd;
        sentinel._prev = toBeAdd;
        _size += 1;
    }



    /** Returns the number of items in the deque. */
    public int size() {return _size;}

    /** Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */
    public void printDeque() {
        Node ptr = sentinel._next;
        while (!ptr.isSentinel()) {
            System.out.print(ptr._item);
            System.out.print(' ');
            ptr = ptr._next;
        }
        System.out.println();
    }

    /** Removes and returns the item at the front of the deque. If no such item exists, returns null. */
    public T removeFirst() {
        if (size() == 0) {
            return null;
        }
        T removed = (T) sentinel._next._item;
        sentinel._next._next._prev = sentinel;
        sentinel._next = sentinel._next._next;
        _size -= 1;
        return removed;
    }

    /** Removes and returns the item at the back of the deque. If no such item exists, returns null. */
    public T removeLast() {
        if (size() == 0) {
            return null;
        }
        T removed = (T) sentinel._prev._item;
        sentinel._prev = sentinel._prev._prev;
        sentinel._prev._next = sentinel;
        _size -= 1;
        return removed;
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     * @param index
     * @return
     */
    public T get(int index) {
        if (index < 0 || index > _size - 1) {
            return null;
        }

        Node ptr = sentinel._next;
        while (index != 0) {
            ptr = ptr._next;
            index -= 1;
        }

        return (T) ptr._item;
    }

    public T getRecursive(int index) {
        return getRecursive(index, sentinel._next);
    }

    private T getRecursive(int index, Node n) {
        if (n.isSentinel()) {
            return null;
        } else if (index == 0) {
            return (T) n._item;
        } else {
            return getRecursive(index - 1, n._next);
        }
    }

    /** The Deque objects we’ll make are iterable (i.e. Iterable<T>)
     * so we must provide this method to return an iterator.
     * @return
     */
    public Iterator<T> iterator() {return new AIterator();}

    private class AIterator implements Iterator<T> {
        Node<T> ptr = sentinel._next;

        public boolean hasNext() {
            return ptr != sentinel;
        }

        public T next() {
            T toBeReturned = ptr._item;
            ptr = ptr._next;
            return toBeReturned;
        }
    }

    /** Returns whether or not the parameter o is equal to the Deque.
     * cases that two object are not same type;
     *  1. o is null
     *  2. o is not the same type as this
     *  3. size not the same
     *  3. they have not the same elements in the same order
     * */
    public boolean equals(Object o) {
        if (o == this) {
          return true;
        } else if (o == null) {
            return false;
        } else if (! (o instanceof LinkedListDeque)) {
            return false;
        } else if (this.size() != ((LinkedListDeque<?>) o).size()) {
            return false;
        }

        Node ptr1 = this.sentinel._next;
        Node ptr2 = ((LinkedListDeque<?>) o).sentinel._next;
        while (!ptr1.isSentinel()) {
            if (!ptr1.equals(ptr2)) {
                return false;
            }
            ptr1 = ptr1._next;
            ptr2 = ptr2._next;
        }
        return true;
    }

}
