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
*/
package com.codingrodent.InMemoryRecordStore.collections;

import com.codingrodent.InMemoryRecordStore.record.records.TestRecordBytePack;
import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class PackedListTest {

    private final static int RECORDS = 10;

    private List<TestRecordBytePack> list;
    private Deque<TestRecordBytePack> deque;

    @Before
    public void setUp() throws Exception {
        list = new PackedList<>(TestRecordBytePack.class, RECORDS);
        deque = (Deque) list;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addFirst() {
        assertEquals(list.size(), 0);
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        deque.addFirst(record);
        assertEquals(list.size(), 1);
        record.setA(2);
        deque.addFirst(record);
        assertEquals(list.size(), 2);
    }

    @Test
    public void addLast() {
        assertEquals(list.size(), 0);
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        deque.addLast(record);
        assertEquals(list.size(), 1);
        record.setA(2);
        deque.addLast(record);
        assertEquals(list.size(), 2);
    }

    @Test
    public void removeFirst() throws Exception {
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        record.setA(3);
        deque.addLast(record);
        //
        record = deque.removeFirst();
        assertEquals(record.getA().intValue(), 1);
        assertEquals(list.size(), 2);
        record = deque.removeFirst();
        assertEquals(record.getA().intValue(), 2);
        assertEquals(list.size(), 1);
        record = deque.removeFirst();
        assertEquals(record.getA().intValue(), 3);
        assertEquals(list.size(), 0);
    }

    @Test
    public void removeLast() throws Exception {
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        record.setA(3);
        deque.addLast(record);
        //
        record = deque.removeLast();
        assertEquals(record.getA().intValue(), 3);
        assertEquals(list.size(), 2);
        record = deque.removeLast();
        assertEquals(record.getA().intValue(), 2);
        assertEquals(list.size(), 1);
        record = deque.removeLast();
        assertEquals(record.getA().intValue(), 1);
        assertEquals(list.size(), 0);
    }

    @Test
    public void offerFirst() throws Exception {
        assertEquals(list.size(), 0);
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        assertTrue(deque.offerFirst(record));
        assertEquals(list.size(), 1);
        record.setA(2);
        assertTrue(deque.offerFirst(record));
        assertEquals(list.size(), 2);
    }

    @Test
    public void offerLast() throws Exception {
        assertEquals(list.size(), 0);
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        assertTrue(deque.offerLast(record));
        assertEquals(list.size(), 1);
        record.setA(2);
        assertTrue(deque.offerLast(record));
        assertEquals(list.size(), 2);
    }

    @Test
    public void pollFirst() throws Exception {
        assertNull(deque.pollFirst());
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        record.setA(3);
        deque.addLast(record);
        //
        assertEquals(deque.pollFirst().getA().intValue(), 1);
        assertEquals(deque.pollFirst().getA().intValue(), 2);
        assertEquals(deque.pollFirst().getA().intValue(), 3);
        //
        assertNull(deque.pollFirst());
    }

    @Test
    public void pollLast() throws Exception {
        assertNull(deque.pollFirst());
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        record.setA(3);
        deque.addLast(record);
        //
        assertEquals(deque.pollLast().getA().intValue(), 3);
        assertEquals(deque.pollLast().getA().intValue(), 2);
        assertEquals(deque.pollLast().getA().intValue(), 1);
        //
        assertNull(deque.pollFirst());
    }

    @Test
    public void getFirst() throws Exception {
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        assertEquals(deque.getFirst().getA().intValue(), 1);
    }

    @Test
    public void getLast() throws Exception {
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        assertEquals(deque.getLast().getA().intValue(), 2);
    }

    @Test
    public void peekFirst() throws Exception {
        assertNull(deque.peekFirst());
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        assertEquals(deque.peekFirst().getA().intValue(), 1);
    }

    @Test
    public void peekLast() throws Exception {
        assertNull(deque.peekLast());
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        assertEquals(deque.peekLast().getA().intValue(), 2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeFirstOccurrence() throws Exception {
        deque.removeFirstOccurrence(new TestRecordBytePack(1, 2, -3, true, -4));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeLastOccurrence() throws Exception {
        deque.removeLastOccurrence(new TestRecordBytePack(1, 2, -3, true, -4));
    }

    @Test
    public void offer() throws Exception {
        assertEquals(list.size(), 0);
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        assertTrue(deque.offer(record));
        assertEquals(list.size(), 1);
        record.setA(2);
        assertTrue(deque.offer(record));
        assertEquals(list.size(), 2);
    }

    @Test
    public void peek() throws Exception {
        assertNull(deque.peek());
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        assertEquals(deque.peek().getA().intValue(), 1);
    }

    @Test
    public void element() throws Exception {
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        assertEquals(deque.element().getA().intValue(), 1);
    }

    @Test
    public void poll() throws Exception {
        assertNull(deque.poll());
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        record.setA(3);
        deque.addLast(record);
        //
        assertEquals(deque.poll().getA().intValue(), 1);
        assertEquals(deque.poll().getA().intValue(), 2);
        assertEquals(deque.poll().getA().intValue(), 3);
        //
        assertNull(deque.poll());
    }

    @Test
    public void push() throws Exception {
        assertEquals(list.size(), 0);
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        deque.push(record);
        assertEquals(list.size(), 1);
        record.setA(2);
        deque.push(record);
        assertEquals(list.size(), 2);
    }

    @Test
    public void pop() throws Exception {
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        record.setA(3);
        deque.addLast(record);
        //
        record = deque.pop();
        assertEquals(record.getA().intValue(), 1);
        assertEquals(list.size(), 2);
        record = deque.pop();
        assertEquals(record.getA().intValue(), 2);
        assertEquals(list.size(), 1);
        record = deque.pop();
        assertEquals(record.getA().intValue(), 3);
        assertEquals(list.size(), 0);
    }

    @Test
    public void remove() throws Exception {
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        record.setA(3);
        deque.addLast(record);
        //
        record = deque.remove();
        assertEquals(record.getA().intValue(), 1);
        assertEquals(list.size(), 2);
        record = deque.remove();
        assertEquals(record.getA().intValue(), 2);
        assertEquals(list.size(), 1);
        record = deque.remove();
        assertEquals(record.getA().intValue(), 3);
        assertEquals(list.size(), 0);
    }

    @Test
    public void isEmpty() throws Exception {
        assertTrue(list.isEmpty());
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        deque.addLast(record);
        assertFalse(list.isEmpty());
    }

    @Test
    public void add() throws Exception {
        assertEquals(list.size(), 0);
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        assertTrue(deque.add(record));
        assertEquals(list.size(), 1);
        record.setA(2);
        assertTrue(deque.add(record));
        assertEquals(list.size(), 2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void containsAll() throws Exception {
        list.containsAll(Collections.EMPTY_LIST);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void contains() throws Exception {
        list.contains(new TestRecordBytePack(1, 2, -3, true, -4));
    }

    @Test
    public void addAll() throws Exception {
        LinkedList<TestRecordBytePack> ll = new LinkedList<TestRecordBytePack>();
        TestRecordBytePack record1 = new TestRecordBytePack(1, 2, -3, true, -4);
        TestRecordBytePack record2 = new TestRecordBytePack(2, 2, -3, true, -4);
        TestRecordBytePack record3 = new TestRecordBytePack(3, 2, -3, true, -4);
        ll.add(record1);
        ll.add(record2);
        ll.add(record3);
        assertTrue(list.addAll(ll));
        //
        TestRecordBytePack record = deque.removeLast();
        assertEquals(record.getA().intValue(), 3);
        record = deque.removeLast();
        assertEquals(record.getA().intValue(), 2);
        record = deque.removeLast();
        assertEquals(record.getA().intValue(), 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeAll() throws Exception {
        list.removeAll(Collections.EMPTY_LIST);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void retainAll() throws Exception {
        list.retainAll(Collections.EMPTY_LIST);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void replaceAll() throws Exception {
        list.replaceAll(r -> r);
    }

    @Test
    public void clear() throws Exception {
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        record.setA(3);
        deque.addLast(record);
        assertEquals(list.size(), 3);
        list.clear();
        assertEquals(list.size(), 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void get() throws Exception {
        list.get(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void set() throws Exception {
        list.set(0, new TestRecordBytePack(1, 2, -3, true, -4));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void sort() throws Exception {
        list.sort((a, b) -> a.getA().compareTo(b.getA()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void indexOf() throws Exception {
        list.indexOf(new TestRecordBytePack(1, 2, -3, true, -4));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void lastIndexOf() throws Exception {
        list.lastIndexOf(new TestRecordBytePack(1, 2, -3, true, -4));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void subList() throws Exception {
        list.subList(0, 1);
    }

    @Test
    public void iterator() throws Exception {
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        record.setA(3);
        deque.addLast(record);
        //
        Iterator<TestRecordBytePack> iter = list.iterator();
        int i = 1;
        while (iter.hasNext()) {
            record = iter.next();
            assertEquals(record.getA().intValue(), i++);
        }
    }

    @Test
    public void descendingIterator() throws Exception {
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        record.setA(3);
        deque.addLast(record);
        //
        Iterator<TestRecordBytePack> iter = deque.descendingIterator();
        int i = 3;
        while (iter.hasNext()) {
            record = iter.next();
            assertEquals(record.getA().intValue(), i--);
        }
    }

    @Test
    public void listIterator() throws Exception {
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        record.setA(3);
        deque.addLast(record);
        // setup conditions
        ListIterator<TestRecordBytePack> iter = list.listIterator();
        assertTrue(iter.hasNext());
        assertFalse(iter.hasPrevious());
        // walk up the list
        int i = 1;
        while (iter.hasNext()) {
            record = iter.next();
            assertEquals(record.getA().intValue(), i++);
        }
        // check conditions
        assertFalse(iter.hasNext());
        assertTrue(iter.hasPrevious());
        // walk back
        i = 3;
        while (iter.hasPrevious()) {
            record = iter.previous();
            assertEquals(record.getA().intValue(), i--);
        }
        // check conditions
        assertTrue(iter.hasNext());
        assertFalse(iter.hasPrevious());
    }

    @Test
    public void stream() throws Exception {
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        int sum = 0;
        for (int i = 1; i <= RECORDS; i++) {
            sum += i;
            record.setA(i);
            deque.addLast(record);
        }
        //
        int total = list.stream().mapToInt(TestRecordBytePack::getA).sum();
        assertEquals(sum, total);
    }

    @Test
    public void parallelStream() throws Exception {
        TestRecordBytePack record = new TestRecordBytePack(1, 2, -3, true, -4);
        int sum = 0;
        for (int i = 1; i <= RECORDS; i++) {
            sum += i;
            record.setA(i);
            deque.addLast(record);
        }
        //
        list.parallelStream().mapToInt(TestRecordBytePack::getA).sum();
        int total = list.stream().mapToInt(TestRecordBytePack::getA).sum();
        assertEquals(sum, total);
    }
}