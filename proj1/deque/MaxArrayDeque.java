package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {

    Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        comparator = c;
    }

    public MaxArrayDeque(T[] a, Comparator<T> c) {
        super(a);
        comparator = c;
    }

    @Override
    public MaxArrayDeque<T> of(T ...argList) {
        return new MaxArrayDeque<T>(argList, comparator);
    }

    /** Returns max item according to default comparator. */
    public T max() {return max(comparator);}


    /**
     * Returns the biggest item in the order defined by comparator C
     * @param c
     * @return
     */
    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }

        T biggest = this.get(0);
        for (T item: this) {
            if (c.compare(item, biggest) > 0) {
                biggest = item;
            }
        }
        return biggest;
    }



    public static class IntegerComparator implements Comparator<Integer> {

        public int compare(Integer a, Integer b) {
            return a - b;
        }
    }

    public static IntegerComparator getIntegerComparator() {
        return new IntegerComparator();
    }


}
