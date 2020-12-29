package com.changhong.telnettool.been;

import java.util.List;

public interface Queue<T> {

    /**
     * Returns the number of elements in this Queue.  If this queue contains
     * more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this queue
     */
    int size();

    /**
     * Returns <tt>true</tt> if this list contains no elements.
     *
     * @return <tt>true</tt> if this list contains no elements
     */
    boolean isEmpty();

    /**
     * push integer to queue header;
     *
     * @param t the object will insert to the queue header.
     * @return the size of queue
     */
    int push(T t);
    /**
     * Returns the component at the specified index.
     *
     * @param      index   an index into this vector
     * @return     the component at the specified index
     * @throws ArrayIndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index >= size()})
     */
    T elementAt(int index);
    /**
     * Removes the element at the specified position in this Queue.
     * Shifts any subsequent elements to the left (subtracts one from their
     * indices).  Returns the element that was removed from the Queue.
     *
     * @throws ArrayIndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index >= size()})
     * @param index the index of the element to be removed
     * @return element that was removed
     */
    boolean remove(int index);
    /**
     * Removes all of the elements from this Queue.  The Queue will
     * be empty after this call returns (unless it throws an exception).
     */
    void clear();
    /**
     * Retrieves and removes the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    T poll();
    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    T peek();

}
