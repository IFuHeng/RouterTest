package com.changhong.telnettool.been;

import java.util.*;

public class NodeQueue implements Queue<Integer> {
    final int[] array;
    int size;

    public NodeQueue(int initialCapacity) {
        this.array = new int[initialCapacity];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public synchronized int push(Integer integer) {
        for (int i = array.length - 1; i > 0; i--) {
            array[i] = array[i - 1];
        }
        array[0] = integer;
        size = Math.min(array.length, size + 1);
        return size;
    }

    public String toString() {
        if (isEmpty())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0)
                sb.append(',').append(' ');
            sb.append(array[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    /**
     * Returns the component at the specified index.
     *
     * @param index an index into this vector
     * @return the integer at the specified index
     */
    public synchronized Integer elementAt(int index) {

        if (index >= size)
            throw new ArrayIndexOutOfBoundsException(index + " >= " + size);

        return array[index];
    }

    public synchronized boolean remove(int index) {
        if (index >= size)
            throw new ArrayIndexOutOfBoundsException(index + " >= " + size);

        for (int i = index; i < array.length - 1; i++) {
            array[i] = array[i + 1];
        }
        array[array.length - 1] = 0;
        --size;

        return true;
    }

    public synchronized void clear() {
        Arrays.fill(array, 0);
        size = 0;
    }

    public synchronized Integer poll() {
        if (isEmpty())
            return null;
        int first = array[0];
        remove(0);
        return first;
    }

    public synchronized Integer peek() {
        if (isEmpty())
            return null;
        return array[0];
    }

}
