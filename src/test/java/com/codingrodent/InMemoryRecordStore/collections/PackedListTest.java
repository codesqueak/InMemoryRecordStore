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
    private TestRecordBytePack record;
    private final Boolean[] booleanArray = {true, false, true, true, false};

    @Before
    public void setUp() throws Exception {
        list = new PackedList<>(TestRecordBytePack.class, RECORDS);
        deque = (Deque) list;
        record = new TestRecordBytePack(1, 2, -3, true, -4, false, UUID.randomUUID(), new boolean[10], booleanArray);
    }


    @Test
    public void addFirst() {
        assertEquals(list.size(), 0);
        deque.addFirst(record);
        assertEquals(list.size(), 1);
        record.setA(2);
        deque.addFirst(record);
        assertEquals(list.size(), 2);
    }

    @Test
    public void addLast() {
        assertEquals(list.size(), 0);
        deque.addLast(record);
        assertEquals(list.size(), 1);
        record.setA(2);
        deque.addLast(record);
        assertEquals(list.size(), 2);
    }

    @Test
    public void removeFirst1() throws Exception {
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

    @Test(expected = NoSuchElementException.class)
    public void removeFirst2() throws Exception {
        deque.removeFirst();
    }

    @Test
    public void removeLast1() throws Exception {
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

    @Test(expected = NoSuchElementException.class)
    public void removeLast2() throws Exception {
        deque.removeLast();
    }

    @Test
    public void offerFirst() throws Exception {
        assertEquals(list.size(), 0);
        assertTrue(deque.offerFirst(record));
        assertEquals(list.size(), 1);
        record.setA(2);
        assertTrue(deque.offerFirst(record));
        assertEquals(list.size(), 2);
    }

    @Test
    public void offerLast() throws Exception {
        assertEquals(list.size(), 0);
        assertTrue(deque.offerLast(record));
        assertEquals(list.size(), 1);
        record.setA(2);
        assertTrue(deque.offerLast(record));
        assertEquals(list.size(), 2);
    }

    @Test
    public void pollFirst() throws Exception {
        assertNull(deque.pollFirst());
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
        assertNull(deque.pollLast());
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
    public void getFirst1() throws Exception {
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        assertEquals(deque.getFirst().getA().intValue(), 1);
    }

    @Test(expected = NoSuchElementException.class)
    public void getFirst2() throws Exception {
        deque.getFirst();
    }

    @Test
    public void getLast1() throws Exception {
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        assertEquals(deque.getLast().getA().intValue(), 2);
    }

    @Test(expected = NoSuchElementException.class)
    public void getLast2() throws Exception {
        deque.getLast();
    }

    @Test
    public void peekFirst() throws Exception {
        assertNull(deque.peekFirst());
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        assertEquals(deque.peekFirst().getA().intValue(), 1);
    }

    @Test
    public void peekLast() throws Exception {
        assertNull(deque.peekLast());
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        assertEquals(deque.peekLast().getA().intValue(), 2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeFirstOccurrence() throws Exception {
        deque.removeFirstOccurrence(record);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeLastOccurrence() throws Exception {
        deque.removeLastOccurrence(record);
    }

    @Test
    public void offer() throws Exception {
        assertEquals(list.size(), 0);
        assertTrue(deque.offer(record));
        assertEquals(list.size(), 1);
        record.setA(2);
        assertTrue(deque.offer(record));
        assertEquals(list.size(), 2);
    }

    @Test
    public void peek() throws Exception {
        assertNull(deque.peek());
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        assertEquals(deque.peek().getA().intValue(), 1);
    }

    @Test
    public void element() throws Exception {
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        assertEquals(deque.element().getA().intValue(), 1);
    }

    @Test
    public void poll() throws Exception {
        assertNull(deque.poll());
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
        deque.push(record);
        assertEquals(list.size(), 1);
        record.setA(2);
        deque.push(record);
        assertEquals(list.size(), 2);
    }

    @Test
    public void pop() throws Exception {
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
        deque.addLast(record);
        assertFalse(list.isEmpty());
    }

    @Test
    public void add() throws Exception {
        assertEquals(list.size(), 0);
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
        list.contains(new TestRecordBytePack(1, 2, -3, true, -4, false, UUID.randomUUID(), new boolean[10], booleanArray));
    }

    @Test
    public void addAll() throws Exception {
        LinkedList<TestRecordBytePack> ll = new LinkedList<>();
        TestRecordBytePack record1 = new TestRecordBytePack(1, 2, -3, true, -4, false, UUID.randomUUID(), new boolean[10], booleanArray);
        TestRecordBytePack record2 = new TestRecordBytePack(2, 2, -3, true, -4, false, UUID.randomUUID(), new boolean[10], booleanArray);
        TestRecordBytePack record3 = new TestRecordBytePack(3, 2, -3, true, -4, false, UUID.randomUUID(), new boolean[10], booleanArray);
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
        list.set(0, new TestRecordBytePack(1, 2, -3, true, -4, false, UUID.randomUUID(), new boolean[10], booleanArray));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void sort() throws Exception {
        list.sort(Comparator.comparing(TestRecordBytePack::getA));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void indexOf() throws Exception {
        list.indexOf(new TestRecordBytePack(1, 2, -3, true, -4, false, UUID.randomUUID(), new boolean[10], booleanArray));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void lastIndexOf() throws Exception {
        list.lastIndexOf(new TestRecordBytePack(1, 2, -3, true, -4, false, UUID.randomUUID(), new boolean[10], booleanArray));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void subList() throws Exception {
        list.subList(0, 1);
    }

    @Test
    public void iterator1() throws Exception {
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        record.setA(3);
        deque.addLast(record);
        //
        Iterator<TestRecordBytePack> it = list.iterator();
        int i = 1;
        while (it.hasNext()) {
            record = it.next();
            assertEquals(record.getA().intValue(), i++);
        }
    }

    @Test
    public void iterator2() throws Exception {
        Iterator<TestRecordBytePack> it = list.iterator();
        assertFalse(it.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void iterator3() throws Exception {
        Iterator<TestRecordBytePack> it = list.iterator();
        it.next();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void iterator4() throws Exception {
        Iterator<TestRecordBytePack> it = list.iterator();
        deque.addLast(record);
        it.next();
    }

    @Test
    public void descendingIterator1() throws Exception {
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        record.setA(3);
        deque.addLast(record);
        //
        Iterator<TestRecordBytePack> it = deque.descendingIterator();
        int i = 3;
        while (it.hasNext()) {
            record = it.next();
            assertEquals(record.getA().intValue(), i--);
        }
    }

    @Test
    public void descendingIterator2() throws Exception {
        Iterator<TestRecordBytePack> it = deque.descendingIterator();
        assertFalse(it.hasNext());
    }

    @Test
    public void listIterator1() throws Exception {
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        record.setA(3);
        deque.addLast(record);
        // setup conditions
        ListIterator<TestRecordBytePack> it = list.listIterator();
        assertTrue(it.hasNext());
        assertFalse(it.hasPrevious());
        // walk up the list
        int i = 1;
        while (it.hasNext()) {
            record = it.next();
            assertEquals(record.getA().intValue(), i++);
        }
        assertEquals(i, 4);
        // check conditions
        assertFalse(it.hasNext());
        assertTrue(it.hasPrevious());
        // walk back
        i = 3;
        while (it.hasPrevious()) {
            record = it.previous();
            assertEquals(record.getA().intValue(), i--);
        }
        assertEquals(i, 0);
        // check conditions
        assertTrue(it.hasNext());
        assertFalse(it.hasPrevious());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void listIterator2() throws Exception {
        ListIterator<TestRecordBytePack> it = list.listIterator();
        it.nextIndex();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void listIterator3() throws Exception {
        ListIterator<TestRecordBytePack> it = list.listIterator();
        it.previousIndex();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void listIterator4() throws Exception {
        ListIterator<TestRecordBytePack> it = list.listIterator();
        it.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void listIterator5() throws Exception {
        ListIterator<TestRecordBytePack> it = list.listIterator();
        it.set(record);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void listIterator6() throws Exception {
        ListIterator<TestRecordBytePack> it = list.listIterator();
        it.add(record);
    }

    @Test(expected = NoSuchElementException.class)
    public void listIterator7() throws Exception {
        ListIterator<TestRecordBytePack> it = list.listIterator();
        it.previous();
    }

    @Test
    public void listIterator8() throws Exception {
        // Check single item list works as expected
        deque.addLast(record);
        ListIterator<TestRecordBytePack> it = list.listIterator();
        assertTrue(it.hasNext());
        record = it.next();
        assertEquals(record.getA().intValue(), 1);
        record = it.previous();
        assertEquals(record.getA().intValue(), 1);
        record = it.next();
        assertEquals(record.getA().intValue(), 1);
        record = it.previous();
        assertEquals(record.getA().intValue(), 1);
    }

    @Test
    public void listIterator9() throws Exception {
        // Check two  item list works as expected
        deque.addLast(record);
        record.setA(2);
        deque.addLast(record);
        ListIterator<TestRecordBytePack> it = list.listIterator();
        assertTrue(it.hasNext());
        record = it.next();
        assertEquals(record.getA().intValue(), 1);
        record = it.previous();
    }

    @Test
    public void stream() throws Exception {
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
        int sum = 0;
        for (int i = 1; i <= RECORDS; i++) {
            sum += i;
            record.setA(i);
            deque.addLast(record);
        }
        //
        int total = list.parallelStream().mapToInt(TestRecordBytePack::getA).sum();
        assertEquals(sum, total);
        total = list.stream().mapToInt(TestRecordBytePack::getA).sum();
        assertEquals(sum, total);
    }

    @Test(expected = IllegalStateException.class)
    public void exceptionIllegalStateException1() throws Exception {
        // Fill storage
        for (int i = 1; i <= RECORDS; i++) {
            deque.addLast(record);
        }
        // Full - should fail
        deque.addLast(record);
    }

    @Test(expected = IllegalStateException.class)
    public void exceptionIllegalStateException2() throws Exception {
        // Fill storage
        for (int i = 1; i <= RECORDS; i++) {
            deque.addLast(record);
        }
        // Full - should fail
        deque.addFirst(record);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerException1() throws Exception {
        deque.addFirst(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerException2() throws Exception {
        deque.addLast(null);
    }

    @Test(expected = ClassCastException.class)
    public void classCastException1() throws Exception {
        Deque deque = new PackedList<>(TestRecordBytePack.class, RECORDS);
        deque.addFirst(0);
        assertEquals(deque.size(), 1);
    }

    @Test(expected = ClassCastException.class)
    public void classCastException2() throws Exception {
        Deque deque = new PackedList<>(TestRecordBytePack.class, RECORDS);
        deque.addLast(0);
        assertEquals(deque.size(), 1);
    }

}