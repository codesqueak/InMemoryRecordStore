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
import org.junit.*;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class ReaderWriterTest {
    private Reader reader;
    private Writer writer;
    private IMemoryStore memory;

    @Before
    public void setUp() throws Exception {

        memory = new ArrayMemoryStore(1024);
        RecordDescriptor descriptor = new RecordDescriptor(TestRecord.class);
        writer = new Writer(memory, descriptor, IMemoryStore.AlignmentMode.BYTE_BYTE);
        reader = new Reader(memory, descriptor, IMemoryStore.AlignmentMode.BYTE_BYTE);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void writeReadRecord() throws Exception {
        TestRecord write = new TestRecord(1, -1, -32768, true, 0x0000_1234_5678_9ABCL);
        writer.putRecord(0, write);
        byte[] packed = {0x00, 0x00, 0x01, // a
                -128, 0x00, // c
                0x00, // v1
                -1, -1, // b
                0x01, // d
                0x12, 0x34, 0x56, 0x78, (byte) 0x9A, (byte) 0xBC//
        };
        // Did record pack correctly ?
        for (int i = 0; i < packed.length; i++) {
            assertEquals(packed[i], memory.getByte(i));
        }
        //
        // Ok, see if we can get it back
        TestRecord read = (TestRecord) reader.getRecord(0);
        assertEquals(read.a, write.a);
        assertEquals(read.b, write.b);
        assertEquals(read.c, write.c);
        assertEquals(read.d, write.d);
        assertEquals(read.e, write.e);
    }

}