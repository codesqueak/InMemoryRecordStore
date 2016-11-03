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
import com.codingrodent.InMemoryRecordStore.record.records.*;
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

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void writeReadRecord() throws Exception {
        RecordDescriptor descriptor = new RecordDescriptor(TestRecord.class);
        writer = new Writer(memory, descriptor, IMemoryStore.AlignmentMode.BYTE_BYTE);
        reader = new Reader(memory, descriptor, IMemoryStore.AlignmentMode.BYTE_BYTE);
        //
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

    @Test
    public void writeReadRecordLongPos() throws Exception {
        RecordDescriptor descriptor = new RecordDescriptor(TestRecordLong.class);
        writer = new Writer(memory, descriptor, IMemoryStore.AlignmentMode.BYTE_BYTE);
        reader = new Reader(memory, descriptor, IMemoryStore.AlignmentMode.BYTE_BYTE);
        //
        TestRecordLong write = new TestRecordLong(1, 0x00_FFL, 0x0000_FFFFL, 0x1122_3344_5566_7700L);
        writer.putRecord(0, write);
        byte[] packed = {0x01, // a
                0x00, -1, // b
                0x00, 0x00, -1, -1, // c
                0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0x00 // d
        };
        // Did record pack correctly ?
        for (int i = 0; i < packed.length; i++) {
            assertEquals(packed[i], memory.getByte(i));
        }
        //
        // Ok, see if we can get it back
        TestRecordLong read = (TestRecordLong) reader.getRecord(0);
        assertEquals(read.a, write.a);
        assertEquals(read.b, write.b);
        assertEquals(read.c, write.c);
        assertEquals(read.d, write.d);
    }

    @Test
    public void writeReadRecordLongNeg() throws Exception {
        RecordDescriptor descriptor = new RecordDescriptor(TestRecordLong.class);
        writer = new Writer(memory, descriptor, IMemoryStore.AlignmentMode.BYTE_BYTE);
        reader = new Reader(memory, descriptor, IMemoryStore.AlignmentMode.BYTE_BYTE);
        //
        TestRecordLong write = new TestRecordLong(-1, -2, -3, -4);
        writer.putRecord(0, write);
        byte[] packed = {-1, // a
                -1, -2, // b
                -1, -1, -1, -3, // c
                -1, -1, -1, -1, -1, -1, -1, -4// d
        };
        // Did record pack correctly ?
        for (int i = 0; i < packed.length; i++) {
            assertEquals(packed[i], memory.getByte(i));
        }
        //
        // Ok, see if we can get it back
        TestRecordLong read = (TestRecordLong) reader.getRecord(0);
        assertEquals(read.a, write.a);
        assertEquals(read.b, write.b);
        assertEquals(read.c, write.c);
        assertEquals(read.d, write.d);
    }

    @Test
    public void writeReadRecordShortPos() throws Exception {
        RecordDescriptor descriptor = new RecordDescriptor(TestRecordShort.class);
        writer = new Writer(memory, descriptor, IMemoryStore.AlignmentMode.BYTE_BYTE);
        reader = new Reader(memory, descriptor, IMemoryStore.AlignmentMode.BYTE_BYTE);
        //
        TestRecordShort write = new TestRecordShort((short) 1, (short) 0x00_FFL);
        writer.putRecord(0, write);
        byte[] packed = {0x01, // a
                0x00, -1 // b
        };
        // Did record pack correctly ?
        for (int i = 0; i < packed.length; i++) {
            assertEquals(packed[i], memory.getByte(i));
        }
        //
        // Ok, see if we can get it back
        TestRecordShort read = (TestRecordShort) reader.getRecord(0);
        assertEquals(read.a, write.a);
        assertEquals(read.b, write.b);
    }

    @Test
    public void writeReadRecordShortNeg() throws Exception {
        RecordDescriptor descriptor = new RecordDescriptor(TestRecordShort.class);
        writer = new Writer(memory, descriptor, IMemoryStore.AlignmentMode.BYTE_BYTE);
        reader = new Reader(memory, descriptor, IMemoryStore.AlignmentMode.BYTE_BYTE);
        //
        TestRecordShort write = new TestRecordShort((short) -1, (short) -2);
        writer.putRecord(0, write);
        byte[] packed = {-1, // a
                -1, -2 // b
        };
        // Did record pack correctly ?
        for (int i = 0; i < packed.length; i++) {
            assertEquals(packed[i], memory.getByte(i));
        }
        //
        // Ok, see if we can get it back
        TestRecordShort read = (TestRecordShort) reader.getRecord(0);
        assertEquals(read.a, write.a);
        assertEquals(read.b, write.b);
    }

    @Test
    public void writeReadRecordBytePos() throws Exception {
        RecordDescriptor descriptor = new RecordDescriptor(TestRecordByte.class);
        writer = new Writer(memory, descriptor, IMemoryStore.AlignmentMode.BYTE_BYTE);
        reader = new Reader(memory, descriptor, IMemoryStore.AlignmentMode.BYTE_BYTE);
        //
        TestRecordByte write = new TestRecordByte((byte) 1);
        writer.putRecord(0, write);
        assertEquals(1, memory.getByte(0));
        //
        // Ok, see if we can get it back
        TestRecordByte read = (TestRecordByte) reader.getRecord(0);
        assertEquals(read.a, write.a);
    }

    @Test
    public void writeReadRecordByteNeg() throws Exception {
        RecordDescriptor descriptor = new RecordDescriptor(TestRecordByte.class);
        writer = new Writer(memory, descriptor, IMemoryStore.AlignmentMode.BYTE_BYTE);
        reader = new Reader(memory, descriptor, IMemoryStore.AlignmentMode.BYTE_BYTE);
        //
        TestRecordByte write = new TestRecordByte((byte) -1);
        writer.putRecord(0, write);
        assertEquals(-1, memory.getByte(0));
        //
        // Ok, see if we can get it back
        TestRecordByte read = (TestRecordByte) reader.getRecord(0);
        assertEquals(read.a, write.a);
    }

    @Test
    public void writeReadRecordChar() throws Exception {
        RecordDescriptor descriptor = new RecordDescriptor(TestRecordChar.class);
        writer = new Writer(memory, descriptor, IMemoryStore.AlignmentMode.BYTE_BYTE);
        reader = new Reader(memory, descriptor, IMemoryStore.AlignmentMode.BYTE_BYTE);
        //
        TestRecordChar write = new TestRecordChar((char) 65, (char) 0x1234);
        writer.putRecord(0, write);
        byte[] packed = {0x41, // a
                0x12, 0x34 // b
        };
        // Did record pack correctly ?
        for (int i = 0; i < packed.length; i++) {
            assertEquals(packed[i], memory.getByte(i));
        }
        //
        // Ok, see if we can get it back
        TestRecordChar read = (TestRecordChar) reader.getRecord(0);
        assertEquals(read.a, write.a);
        assertEquals(read.b, write.b);
    }
}