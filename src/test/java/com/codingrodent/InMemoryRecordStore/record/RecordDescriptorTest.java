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

import com.codingrodent.InMemoryRecordStore.record.records.*;
import org.junit.*;

import java.util.Iterator;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;

/**
 *
 */
public class RecordDescriptorTest {

    private RecordDescriptor recordDescriptor;

    @Before
    public void setUp() throws Exception {
        recordDescriptor = new RecordDescriptor(TestRecord.class);
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
        assertEquals(recordDescriptor.getByteLength(), 15);
    }

    @Test
    public void getSizeInBits() throws Exception {
        assertEquals(recordDescriptor.getBitLength(), 15 * 8);
    }

    @Test
    public void getClazz() throws Exception {
        assertEquals(recordDescriptor.getClazz(), TestRecord.class);
    }

    @Test
    public void getFieldNames() throws Exception {
        Iterator<String> names = recordDescriptor.getFieldNames().iterator();
        assertEquals(names.next(), "a");
        assertEquals(names.next(), "c");
        assertEquals(names.next(), "v1");
        assertEquals(names.next(), "b");
        assertEquals(names.next(), "d");
        assertEquals(names.next(), "e");
        assertFalse(names.hasNext());
    }

    @Test
    public void exceptions() throws Exception {
        // Wrong record type
        try {
            recordDescriptor = new RecordDescriptor(Integer.class);
            fail("Expecting IllegalArgumentException to be thrown");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "The record must contain a PackRecord annotation");
        }
        // Padding annotation in wrong place
        try {
            recordDescriptor = new RecordDescriptor(TestRecordBadPadding.class);
            fail("Expecting IllegalArgumentException to be thrown");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "@Padding can only be used on Void fields");
        }
        // Pack annotation in wrong place
        try {
            recordDescriptor = new RecordDescriptor(TestRecordBadPack.class);
            fail("Expecting IllegalArgumentException to be thrown");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "@Pack cannot be used on Void fields");
        }
        // Pack annotation on unsupported type
        //
        try {
            recordDescriptor = new RecordDescriptor(TestRecordUnsupportedPack.class);
            fail("Expecting IllegalArgumentException to be thrown");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Unsupported packing type. java.lang.String");
        }

    }
}