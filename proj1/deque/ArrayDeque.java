package deque;

import afu.org.checkerframework.checker.oigj.qual.O;
import org.checkerframework.checker.units.qual.A;

import java.util.Iterator;

/* Invariants:
 1. first item always at the position forwards nextFirst;
 2. addFirst always put a new item at the position nextFirst
 3. last item always at the position backwards nextLast
 4. addLast always put a new item at the position nextLast
 */

public class ArrayDeque<T> implements Deque<T> {
    private int _size;
    private T[] items;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        _size = 0;
        items = (T[]) new Object[8];
        nextFirst = 3;
        nextLast = 4;
    }

    public ArrayDeque(T[] a) {
        _size = a.length;
        int capacity = (a.length / 8 + 1) * 8;
        items = (T[]) new Object[capacity];
        System.arraycopy(a, 0, items, 0, a.length);
        nextFirst = items.length - 1;
        nextLast = _size;
    }

    /** Method to create an ArrayDeque from an argument list. */
    public ArrayDeque of(T ...argList) {
        return new ArrayDeque(argList);

    }


    /**
     *      forwards       backwards
     *  <------------     ------------>
     *  [0][1][2][3][4][5][6][7][8][9][10][11]
     *  addFirst always go forwards direction
     *  addLast always go backwards
     */
    private int backwards(int index) {
        return (index + 1) % items.length;
    }

    private int forwards(int index) {
        return Math.floorMod(index - 1, items.length);
    }

    /**
     * Resize the underlying array to the size of Capacity.
     */
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int index = backwards(nextFirst);
        for (int i = 0; i < _size; i += 1) {
            a[i] = items[index];
            index = (index + 1) % items.length;
        }
        items = a;
        nextFirst = capacity - 1;
        nextLast = _size;
    }

    /** Adds an item of type T to the front of the deque. You can assume that item is never null. */
    public void addFirst(T item) {
        if (_size == items.length) {
            resize(items.length * 2);
        }

        items[nextFirst] = item;
        nextFirst = forwards(nextFirst);
        _size += 1;
    }

    /** Adds an item of type T to the back of the deque. You can assume that item is never null. */
    public void addLast(T item) {
        if (_size == items.length) {
            resize(items.length * 2);
        }

        items[nextLast] = item;
        nextLast = backwards(nextLast);
        _size += 1;
    }



    /** Returns the number of items in the deque. */
    public int size() {return _size;}

    /** Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */
    public void printDeque() {
        int index = backwards(nextFirst);
        for (int i = 0; i < size(); i += 1) {
            System.out.print(items[index]);
            System.out.print(' ');
            index = backwards(index);
        }
        System.out.println();
    }

    /** Removes and returns the item at the front of the deque. If no such item exists, returns null. */
    public T removeFirst() {
        if (size() == 0) {
            return null;
        }

        int front = backwards(nextFirst);
        T toBeReturned = items[front];
        items[front] = null;
        nextFirst = front;
        _size -= 1;

        if (items.length > 8 && size() * 1.0 / items.length < 0.25) {
            resize(items.length / 2);
        }

        return toBeReturned;
    }

    /** Removes and returns the item at the back of the deque. If no such item exists, returns null. */
    public T removeLast() {
        if (size() == 0) {
            return null;
        }

        int back = forwards(nextLast);
        T toBeReturned = items[back];
        items[back] = null;
        nextLast = back;
        _size -= 1;

        if (items.length > 8 && size() * 1.0 / items.length < 0.25) {
            resize(items.length / 2);
        }

        return toBeReturned;
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     * @param index
     * @return
     */
    public T get(int index) {
        if (index < 0 || index > size() - 1) {
            return null;
        }

        int front = backwards(nextFirst);
        if (index + front < items.length) {
            return items[index + front];
        } else {
            return items[index + front - items.length];
        }
    }


    /** The Deque objects we’ll make are iterable (i.e. Iterable<T>)
     * so we must provide this method to return an iterator.
     * @return
     */
    @Override
    public Iterator<T> iterator() {return new AIterator();}

    private class AIterator implements Iterator<T> {
        int index = 0;

        public boolean hasNext() {return index < size();}
        public T next() {
            T toBeReturned = get(index);
            index += 1;
            return toBeReturned;
        }
    }

    /** Returns whether or not the parameter o is equal to the Deque. */
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o == null) {
            return false;
        } else if (!(o instanceof ArrayDeque)) {
            return false;
        } else if (size() != ((ArrayDeque<?>) o).size()) {
            return false;
        }

        for (int i = 0; i < size(); i += 1) {
            if (!get(i).equals(((ArrayDeque<?>) o).get(i))) {
                return false;
            }
        }

        return true;
    }
}
