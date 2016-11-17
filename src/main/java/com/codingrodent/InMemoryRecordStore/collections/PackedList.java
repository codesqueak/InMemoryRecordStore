/*
* MIT License
*
*         Copyright (c) 2016
*
*         Permission is hereby granted, free of charge, to any person obtaining a copy
*         of this software and associated documentation files (the "Software"), to deal
*         in the Software without restriction, including without limitation the rights
*         to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
*         copies of the Software, and to permit persons to whom the Software is
*         furnished to do so, subject to the following conditions:
*
*         The above copyright notice and this permission notice shall be included in all
*         copies or substantial portions of the Software.
*
*         THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
*         IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
*         FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
*         AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
*         LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
*         OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
*         SOFTWARE.
*
*/
package com.codingrodent.InMemoryRecordStore.collections;

/*
 * @param <E> the type of elements held in this collection
 * @see List
 * @see ArrayList
 * @see Vector
 */

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;

public class PackedList<E> extends AbstractSequentialList<E> implements List<E>, Deque<E> {

    private final static int FIRST_MARKER = Integer.MIN_VALUE;
    private final static int LAST_MARKER = Integer.MIN_VALUE + 1;
    //
    private final PackedArray<E> packedArray;
    private final int[] forward;
    private final int[] backward;
    private final int[] free;
    private final int records;
    private final Class<E> clazz;
    //
    private final AtomicInteger modCount = new AtomicInteger();
    private int first = FIRST_MARKER;
    private int last = LAST_MARKER;
    private int size = 0;
    private int next = 0;

    /**
     * Constructs an empty list.
     */
    public PackedList(final Class<E> clazz, final int records) {
        this.packedArray = new PackedArray<>(clazz, records);
        this.records = records;
        this.forward = new int[records];
        this.backward = new int[records];
        int[] free = new int[records];
        for (int i = 0; i < records - 1; i++) {
            free[i] = i + 1;
        }
        free[records - 1] = LAST_MARKER;
        this.free = free;
        this.clazz = clazz;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void addFirst(final E e) {
        if (records == size)
            throw new IllegalStateException("Predefined storage full");
        if (null == e)
            throw new NullPointerException("Deque does not permit null elements");
        if (clazz != e.getClass())
            throw new ClassCastException("Incorrect class type");
        modCount.incrementAndGet();
        packedArray.putRecord(next, e);
        if (0 == size) {
            // set up initial conditions
            forward[next] = LAST_MARKER;
            last = next;
        } else {
            // forward pointers
            forward[next] = first;
            // backward pointers
            backward[first] = next;
        }
        backward[next] = FIRST_MARKER;
        first = next;
        // next free cell
        next = free[next];
        size++;
    }

    @Override
    public void addLast(final E e) {
        if (records == size)
            throw new IllegalStateException("Predefined storage full");
        if (null == e)
            throw new NullPointerException("Deque does not permit null elements");
        if (clazz != e.getClass())
            throw new ClassCastException("Incorrect class type");
        modCount.incrementAndGet();
        packedArray.putRecord(next, e);
        if (0 == size) {
            // set up initial conditions
            backward[next] = FIRST_MARKER;
            first = next;
        } else {
            // forward pointers
            forward[last] = next;
            // backward pointers
            backward[next] = last;
        }
        forward[next] = LAST_MARKER;
        last = next;
        // next free cell
        next = free[next];
        size++;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean offerFirst(final E e) {
        addFirst(e);
        return true;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean offerLast(final E e) {
        addLast(e);
        return true;
    }

    /**
     * @inheritDoc
     */
    @Override
    public E removeFirst() {
        if (size == 0)
            throw new NoSuchElementException();
        modCount.incrementAndGet();
        E result = packedArray.getRecord(first);
        // Add to free list
        free[first] = next;
        next = first;
        // forward pointers
        first = forward[first];
        // backwards pointers
        if (1 == size) {
            last = LAST_MARKER;
        } else {
            backward[first] = FIRST_MARKER;
        }
        size--;
        return result;
    }

    /**
     * @inheritDoc
     */
    @Override
    public E removeLast() {
        if (size == 0)
            throw new NoSuchElementException();
        modCount.incrementAndGet();
        E result = packedArray.getRecord(last);
        // Add to free list
        free[last] = next;
        next = last;
        // backwards pointers
        last = backward[last];
        // forward pointers
        if (1 == size) {
            first = FIRST_MARKER;
        } else {
            forward[last] = LAST_MARKER;
        }
        size--;
        return result;
    }

    /**
     * @inheritDoc
     */
    @Override
    public E pollFirst() {
        if (size == 0)
            return null;
        return removeFirst();
    }

    /**
     * @inheritDoc
     */
    @Override
    public E pollLast() {
        if (size == 0)
            return null;
        return removeLast();
    }

    /**
     * @inheritDoc
     */
    @Override
    public E getFirst() {
        if (size == 0)
            throw new NoSuchElementException();
        return packedArray.getRecord(first);
    }

    /**
     * @inheritDoc
     */
    @Override
    public E getLast() {
        if (size == 0)
            throw new NoSuchElementException();
        return packedArray.getRecord(last);
    }

    /**
     * @inheritDoc
     */
    @Override
    public E peekFirst() {
        if (size == 0)
            return null;
        return getFirst();
    }

    /**
     * @inheritDoc
     */
    @Override
    public E peekLast() {
        if (size == 0)
            return null;
        return getLast();
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation always throws an
     * {@code UnsupportedOperationException}.
     */
    @Override
    public boolean removeFirstOccurrence(final Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation always throws an
     * {@code UnsupportedOperationException}.
     */
    @Override
    public boolean removeLastOccurrence(final Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean offer(final E e) {
        return offerLast(e);
    }

    /**
     * @inheritDoc
     */
    @Override
    public E remove() {
        return removeFirst();
    }

    /**
     * @inheritDoc
     */
    @Override
    public E poll() {
        return pollFirst();
    }

    /**
     * @inheritDoc
     */
    @Override
    public E element() {
        return getFirst();
    }

    /**
     * @inheritDoc
     */
    @Override
    public E peek() {
        return peekFirst();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void push(final E e) {
        addFirst(e);
    }

    /**
     * @inheritDoc
     */
    @Override
    public E pop() {
        return removeFirst();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Iterator<E> descendingIterator() {
        return new PackedListIterator<>(true);
    }

    /**
     * @inheritDoc
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        c.forEach(this::add);
        return true;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean add(final E e) {
        addLast(e);
        return true;
    }

    /**
     * @inheritDoc
     */
    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * @inheritDoc
     */
    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void clear() {
        while (!isEmpty())
            this.remove();

    }

    /**
     * @inheritDoc
     */
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        throw new UnsupportedOperationException();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void sort(Comparator<? super E> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * @inheritDoc
     */
    @Override
    public E get(int index) {
        throw new UnsupportedOperationException();
    }

    /**
     * @inheritDoc
     */
    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Iterator<E> iterator() {
        return new PackedListIterator<>(false);
    }

    /**
     * @inheritDoc
     */
    @Override
    public ListIterator<E> listIterator(final int index) {
        return new PackedListIterator<>(false);
    }

    /////////////////////////////////////////////////////////////////////////////////////

    private class PackedListIterator<T> implements ListIterator<T> {

        private final int mod;
        private final boolean descending;
        private int cursor;

        public PackedListIterator(final boolean descending) {
            if (descending) {
                cursor = LAST_MARKER;
            } else {
                cursor = FIRST_MARKER;
            }
            this.descending = descending;
            this.mod = modCount.get();
        }

        /**
         * @inheritDoc
         */
        @Override
        public boolean hasNext() {
            modCheck();
            if (descending)
                return hasPrevious();
            if ((isEmpty()) || (LAST_MARKER == cursor))
                return false;
            return ((FIRST_MARKER == cursor) || (LAST_MARKER != forward[cursor]));
        }

        /**
         * @inheritDoc
         */
        @Override
        public T next() {
            modCheck();
            if (descending)
                return previous();
            if (hasNext()) {
                if (FIRST_MARKER == cursor) {
                    cursor = first;
                } else {
                    cursor = forward[cursor];
                }
                @SuppressWarnings("unchecked") T record = (T) packedArray.getRecord(cursor);
                if (LAST_MARKER == forward[cursor])
                    cursor = LAST_MARKER;
                return record;
            } else
                throw new NoSuchElementException();
        }

        /**
         * @inheritDoc
         */

        @Override
        public boolean hasPrevious() {
            modCheck();
            if ((isEmpty()) || (FIRST_MARKER == cursor))
                return false;
            return ((LAST_MARKER == cursor) || (FIRST_MARKER != backward[cursor]));
        }

        /**
         * @inheritDoc
         */

        @Override
        public T previous() {
            modCheck();
            if (hasPrevious()) {
                if (LAST_MARKER == cursor) {
                    cursor = last;
                } else {
                    cursor = backward[cursor];
                }
                @SuppressWarnings("unchecked") T record = (T) packedArray.getRecord(cursor);
                if (FIRST_MARKER == backward[cursor])
                    cursor = FIRST_MARKER;
                return record;
            } else
                throw new NoSuchElementException();
        }

        @Override
        public int nextIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int previousIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(final T e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(final T e) {
            throw new UnsupportedOperationException();
        }

        private void modCheck() {
            if (mod != modCount.get())
                throw new ConcurrentModificationException();
        }
    }

}
