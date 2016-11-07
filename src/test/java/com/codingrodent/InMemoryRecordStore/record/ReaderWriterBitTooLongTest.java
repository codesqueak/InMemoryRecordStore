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

import com.codingrodent.InMemoryRecordStore.core.*;
import com.codingrodent.InMemoryRecordStore.record.records.TestRecordNoPackTooLong;
import com.codingrodent.InMemoryRecordStore.utility.Utilities;
import org.junit.*;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class ReaderWriterBitTooLongTest {
    private Reader reader;
    private Writer writer;
    private IMemoryStore memory;

    @Before
    public void setUp() throws Exception {
        memory = new ArrayMemoryStore(1024);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void writeReadRecord() throws Exception {
        RecordDescriptor descriptor = new RecordDescriptor(TestRecordNoPackTooLong.class);
        writer = new Writer(memory, descriptor);
        reader = new Reader(memory, descriptor, IMemoryStore.AlignmentMode.BIT_BYTE);
        //
        TestRecordNoPackTooLong write = new TestRecordNoPackTooLong((byte) 0x12, (short) 0x3456, 0x789ABCDE, 0x1234_5678_9ABC_DEF0L, 'A', true);
        writer.putRecord(0, write);

        for (int i = 0; i < descriptor.getByteLength(); i++) {
            System.out.print(Utilities.getByte(memory.getByte(i)) + " ");
        }
        System.out.println();

        byte[] packed = {0x12, // 1
                0x34, 0x56, // 3
                0x78, (byte) 0x9A, (byte) 0xBC, (byte) 0xDE, // 7
                0x12, 0x34, 0x56, 0x78, (byte) 0x9A, (byte) 0xBC, (byte) 0xDE, (byte) 0xF0, // 15
                0x00, 0x41, // 17
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 25
                0x01}; // 26

        assertEquals(packed.length, descriptor.getByteLength());

        // Did record pack correctly ?
        for (int i = 0; i < packed.length; i++) {
            assertEquals(packed[i], memory.getByte(i));
        }
    }

}