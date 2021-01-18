package com.changhong.telnettool.been;

import javafx.util.Pair;

import java.util.Arrays;

public class NodeStrQueue implements Queue<Pair<Long, String>> {
    final long[] array;
    int size;
    final String[] arrayStr;

    public NodeStrQueue(int initialCapacity) {
        this.array = new long[initialCapacity];
        this.arrayStr = new String[initialCapacity];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public synchronized int push(Pair<Long, String> p) {
        for (int i = array.length - 1; i > 0; i--) {
            array[i] = array[i - 1];
        }
        array[0] = p.getKey();
        for (int i = arrayStr.length - 1; i > 0; i--) {
            arrayStr[i] = arrayStr[i - 1];
        }
        arrayStr[0] = p.getValue();
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
            sb.append('{').append(array[i]).append(',').append(arrayStr[i]).append('}');
        }
        sb.append(']');
        return sb.toString();
    }

    public synchronized Pair<Long, String> elementAt(int index) {

        if (index >= size)
            throw new ArrayIndexOutOfBoundsException(index + " >= " + size);

        return new Pair(array[index], arrayStr[index]);
    }

    public synchronized boolean remove(int index) {

        if (index >= size)
            throw new ArrayIndexOutOfBoundsException(index + " >= " + size);

        for (int i = index; i < arrayStr.length - 1; i++) {
            arrayStr[i] = arrayStr[i + 1];
        }
        arrayStr[arrayStr.length - 1] = null;

        for (int i = index; i < array.length - 1; i++) {
            array[i] = array[i + 1];
        }
        array[array.length - 1] = 0;
        --size;

        return true;
    }

    public synchronized void clear() {
        Arrays.fill(array, 0);
        Arrays.fill(arrayStr, null);
        size = 0;
    }

    public synchronized Pair<Long, String> poll() {
        if (isEmpty())
            return null;
        long first = array[0];
        String firstStr = arrayStr[0];
        remove(0);
        return new Pair(first, firstStr);
    }

    public synchronized Pair<Long, String> peek() {
        if (isEmpty())
            return null;
        long first = array[0];
        String firstStr = arrayStr[0];
        return new Pair(first, firstStr);
    }

}
