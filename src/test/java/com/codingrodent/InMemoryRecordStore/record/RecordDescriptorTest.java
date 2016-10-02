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
package com.codingrodent.InMemoryRecordStore.record;

import org.junit.*;

import java.util.Iterator;

import static org.junit.Assert.*;

/**
 *
 */
public class RecordDescriptorTest {

    private RecordDescriptor recordDescriptor;

    @Before
    public void setUp() throws Exception {
        recordDescriptor = new RecordDescriptor(Record1.class);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void isFieldByteAligned() throws Exception {
        assertTrue(recordDescriptor.isFieldByteAligned());
    }

    @Test
    public void isRecordByteAligned() throws Exception {
        assertTrue(recordDescriptor.isRecordByteAligned());
    }

    @Test
    public void getSizeInBytes() throws Exception {
        assertEquals(recordDescriptor.getSizeInBytes(), 9);
    }

    @Test
    public void getSizeInBits() throws Exception {
        assertEquals(recordDescriptor.getSizeInBits(), 9 * 8);
    }

    @Test
    public void getClazz() throws Exception {
        assertEquals(recordDescriptor.getClazz(), Record1.class);
    }

    @Test
    public void getFieldNames() throws Exception {
        Iterator<String> names = recordDescriptor.getFieldNames().iterator();
        assertEquals(names.next(), "a");
        assertEquals(names.next(), "c");
        assertEquals(names.next(), "v1");
        assertEquals(names.next(), "b");
        assertEquals(names.next(), "d");
        assertFalse(names.hasNext());
    }

}