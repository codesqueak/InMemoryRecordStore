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

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class ReaderWriterArrayTest {
    private Reader reader;
    private Writer writer;
    private IMemoryStore memory;
    private Boolean[] booleanArray = {true, false, true, true, false};
    ;

    @Before
    public void setUp() throws Exception {
        memory = new ArrayMemoryStore(5000);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void writeReadRecord() throws Exception {
        RecordDescriptor descriptor = new RecordDescriptor(TestRecordBytePack.class);
        writer = new Writer(memory, descriptor);
        reader = new Reader(memory, descriptor);
        //
        UUID uuid = new UUID(0x8000_7000_6000_5000l, 0x4000_3000_2000_1000l);
        TestRecordBytePack write = new TestRecordBytePack(1, -1, -32768, true, 0x0000_1234_5678_9ABCL, false, uuid, new boolean[10], booleanArray);
        writer.putRecord(0, write);
        byte[] packed = {0x00, 0x00, 0x00, 0x01, // a
                -1, -1, -128, 0x00,// c
                0x00, // v1
                -1, -1, // b
                0x01, // d
                0x12, 0x34, 0x56, 0x78, (byte) 0x9A, (byte) 0xBC, // e
                0x00, // f
                -128, 0x00, 0x70, 0x00, 0x60, 0x00, 0x50, 0x00, 0x40, 0x00, 0x30, 0x00, 0x20, 0x00, 0x10, 0x00}; // g
        // Did record pack correctly ?
        for (int i = 0; i < packed.length; i++) {
            assertEquals(packed[i], memory.getByte(i));
        }
        //
        // Ok, see if we can get it back
        TestRecordBytePack read = (TestRecordBytePack) reader.getRecord(0);
        assertEquals(read.a, write.a);
        assertEquals(read.b, write.b);
        assertEquals(read.c, write.c);
        assertEquals(read.d, write.d);
        assertEquals(read.e, write.e);
    }

    @Test
    public void writeReadBitArrays() throws Exception {
        RecordDescriptor descriptor = new RecordDescriptor(TestRecordBitArray.class);
        writer = new Writer(memory, descriptor);
        reader = new Reader(memory, descriptor);
        //
    }

}