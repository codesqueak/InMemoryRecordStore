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

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;

/**
 *
 */
public class ReaderWriterBitArrayTest {
    private Reader reader;
    private Writer writer;
    private IMemoryStore memory;
    private final boolean[] bitArray = {true, true, false, false, true, true, false, false, true, true};
    private final Boolean[] booleanArray = {true, false, true, true, false};

    @Before
    public void setUp() throws Exception {
        memory = new ArrayMemoryStore(5000);
    }

    @Test
    public void writeReadRecord() throws Exception {
        RecordDescriptor descriptor = new RecordDescriptor<>(TestRecordBitPack.class);
        writer = new Writer(memory, descriptor);
        reader = new Reader(memory, descriptor);
        //
        UUID uuid = new UUID(0x8000_7000_6000_5000L, 0x4000_3000_2000_1000L);
        TestRecordBitPack write = new TestRecordBitPack(1, -1, -32768, true, 0x0000_1234_5678_9ABCL, false, uuid, bitArray, booleanArray);
        writer.putRecord(0, write);

        byte[] packed = { //
                0b00000000, //
                0b00000000, //
                0b00011000, //
                0b00000000, //
                0b00000000, //
                0b00001111, //
                (byte) 0b11111111, //
                (byte) 0b11111010, //
                (byte) 0b01000110, //
                (byte) 0b10001010, //
                (byte) 0b11001111, //
                0b00010011, //
                0b01010111, //
                (byte) 0b10000001, //
                0b00000000, //
                0b00000000, //
                (byte) 0b11100000, //
                0b00000000, //
                (byte) 0b11000000, //
                0b00000000, //
                (byte) 0b10100000,//
                (byte) 0b00000000,//
                (byte) 0b10000000,//
                (byte) 0b00000000,//
                (byte) 0b01100000,//
                (byte) 0b00000000,//
                (byte) 0b01000000,//
                (byte) 0b00000000,//
                (byte) 0b00100000,//
                (byte) 0b00000000,//
                (byte) 0b01001000,//
                (byte) 0b00000100,//
                (byte) 0b10000000,//
                (byte) 0b01001000,//
                (byte) 0b01000000,//
                (byte) 0b00010000,//
                (byte) 0b10000000};
        //
        assertEquals(packed.length, descriptor.getByteLength());
        // Did record pack correctly ?
        for (int i = 0; i < packed.length; i++) {
            assertEquals(packed[i], memory.getByte(i));
        }
        //
        // Ok, see if we can get it back
        TestRecordBitPack read = (TestRecordBitPack) reader.getRecord(0);
        assertEquals(read.a, write.a);
        assertEquals(read.b, write.b);
        assertEquals(read.c, write.c);
        assertEquals(read.d, write.d);
        assertEquals(read.e, write.e);
        assertEquals(read.f, write.f);
        assertEquals(read.g, write.g);
        // Arrays
        assertArrayEquals(read.h, write.h);
        assertArrayEquals(read.i, write.i);
    }

    @Test(expected = IllegalArgumentException.class)
    public void badSize() throws Exception {
        RecordDescriptor descriptor = new RecordDescriptor<>(TestRecordBitPack.class);
        writer = new Writer(memory, descriptor);
        reader = new Reader(memory, descriptor);
        //
        UUID uuid = new UUID(0x8000_7000_6000_5000L, 0x4000_3000_2000_1000L);
        boolean[] badArray = {true, true, false, false, true, true, false, false, true}; // only 9 elements
        TestRecordBitPack write = new TestRecordBitPack(1, -1, -32768, true, 0x0000_1234_5678_9ABCL, false, uuid, badArray, booleanArray);
        writer.putRecord(0, write);
        fail("IllegalArgumentException expected");
    }

    @Test
    public void tooManyBitsSpecified() throws Exception {
        RecordDescriptor descriptor = new RecordDescriptor<>(TestRecordBitArray.class);
        writer = new Writer(memory, descriptor);
        reader = new Reader(memory, descriptor);
        //
        TestRecordBitArray write = new TestRecordBitArray(bitArray, booleanArray);
        writer.putRecord(0, write);
        byte[] packed = {1, 1, 0, 0, 1, 1, 0, 0, 1, 1, // 1
                1, 0, 1, 1, 0}; // b
        // Did record pack correctly ?
        for (int i = 0; i < packed.length; i++) {
            assertEquals(packed[i], memory.getByte(i));
        }
        //
        // Ok, see if we can get it back
        TestRecordBitArray read = (TestRecordBitArray) reader.getRecord(0);
        assertArrayEquals(read.a, write.a);
        assertArrayEquals(read.b, write.b);
    }

}