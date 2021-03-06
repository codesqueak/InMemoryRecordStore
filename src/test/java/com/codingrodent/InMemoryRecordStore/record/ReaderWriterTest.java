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
public class ReaderWriterTest {
    private IMemoryStore memory;
    private final Boolean[] booleanArray = {true, false, true, true, false};

    @Before
    public void setUp() throws Exception {
        memory = new ArrayMemoryStore(1024);
    }

    @Test
    public void writeReadRecord() throws Exception {
        RecordDescriptor<TestRecordBytePack> descriptor = new RecordDescriptor<>(TestRecordBytePack.class);
        Writer<TestRecordBytePack> writer = new Writer<>(memory, descriptor);
        Reader<TestRecordBytePack> reader = new Reader<>(memory, descriptor);
        //
        UUID uuid = new UUID(0x8000_7000_6000_5000L, 0x4000_3000_2000_1000L);
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
        TestRecordBytePack read = reader.getRecord(0);
        assertEquals(read.a, write.a);
        assertEquals(read.b, write.b);
        assertEquals(read.c, write.c);
        assertEquals(read.d, write.d);
        assertEquals(read.e, write.e);
    }

    @Test
    public void writeReadRecordLongPos() throws Exception {
        RecordDescriptor<TestRecordLong> descriptor = new RecordDescriptor<>(TestRecordLong.class);
        Writer<TestRecordLong> writer = new Writer<>(memory, descriptor);
        Reader<TestRecordLong> reader = new Reader<>(memory, descriptor);
        //
        TestRecordLong write = new TestRecordLong(1L, 0x00_FFL, 0x0000_FFFFL, 0x1122_3344_5566_7700L);
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
        TestRecordLong read = reader.getRecord(0);
        assertEquals(read.a, write.a);
        assertEquals(read.b, write.b);
        assertEquals(read.c, write.c);
        assertEquals(read.d, write.d);
    }

    @Test
    public void writeReadRecordLongNeg() throws Exception {
        RecordDescriptor<TestRecordLong> descriptor = new RecordDescriptor<>(TestRecordLong.class);
        Writer<TestRecordLong> writer = new Writer<>(memory, descriptor);
        Reader<TestRecordLong> reader = new Reader<>(memory, descriptor);
        //
        TestRecordLong write = new TestRecordLong(-1L, -2, -3, -4);
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
        TestRecordLong read = reader.getRecord(0);
        assertEquals(read.a, write.a);
        assertEquals(read.b, write.b);
        assertEquals(read.c, write.c);
        assertEquals(read.d, write.d);
    }

    @Test
    public void writeReadRecordShortPos() throws Exception {
        RecordDescriptor<TestRecordShort> descriptor = new RecordDescriptor<>(TestRecordShort.class);
        Writer<TestRecordShort> writer = new Writer<>(memory, descriptor);
        Reader<TestRecordShort> reader = new Reader<>(memory, descriptor);
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
        TestRecordShort read = reader.getRecord(0);
        assertEquals(read.a, write.a);
        assertEquals(read.b, write.b);
    }

    @Test
    public void writeReadRecordShortNeg() throws Exception {
        RecordDescriptor<TestRecordShort> descriptor = new RecordDescriptor<>(TestRecordShort.class);
        Writer<TestRecordShort> writer = new Writer<>(memory, descriptor);
        Reader<TestRecordShort> reader = new Reader<>(memory, descriptor);
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
        TestRecordShort read = reader.getRecord(0);
        assertEquals(read.a, write.a);
        assertEquals(read.b, write.b);
    }

    @Test
    public void writeReadRecordBytePos() throws Exception {
        RecordDescriptor<TestRecordByte> descriptor = new RecordDescriptor<>(TestRecordByte.class);
        Writer<TestRecordByte> writer = new Writer<>(memory, descriptor);
        Reader<TestRecordByte> reader = new Reader<>(memory, descriptor);
        //
        TestRecordByte write = new TestRecordByte((byte) 1);
        writer.putRecord(0, write);
        assertEquals(1, memory.getByte(0));
        //
        // Ok, see if we can get it back
        TestRecordByte read = reader.getRecord(0);
        assertEquals(read.a, write.a);
    }

    @Test
    public void writeReadRecordByteNeg() throws Exception {
        RecordDescriptor<TestRecordByte> descriptor = new RecordDescriptor<>(TestRecordByte.class);
        Writer<TestRecordByte> writer = new Writer<>(memory, descriptor);
        Reader<TestRecordByte> reader = new Reader<>(memory, descriptor);
        //
        TestRecordByte write = new TestRecordByte((byte) -1);
        writer.putRecord(0, write);
        assertEquals(-1, memory.getByte(0));
        //
        // Ok, see if we can get it back
        TestRecordByte read = reader.getRecord(0);
        assertEquals(read.a, write.a);
    }

    @Test
    public void writeReadRecordChar() throws Exception {
        RecordDescriptor<TestRecordChar> descriptor = new RecordDescriptor<>(TestRecordChar.class);
        Writer<TestRecordChar> writer = new Writer<>(memory, descriptor);
        Reader<TestRecordChar> reader = new Reader<>(memory, descriptor);
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
        TestRecordChar read = reader.getRecord(0);
        assertEquals(read.a, write.a);
        assertEquals(read.b, write.b);
    }

    @Test
    public void writeReadRecordBitAligned() throws Exception {
        RecordDescriptor<TestRecordBitAligned> descriptor = new RecordDescriptor<>(TestRecordBitAligned.class);
        Writer<TestRecordBitAligned> writer = new Writer<>(memory, descriptor);
        Reader<TestRecordBitAligned> reader = new Reader<>(memory, descriptor);
        //
        TestRecordBitAligned write = new TestRecordBitAligned(123, 45, 6, true, 789L, (short) 12, (short) 3, (byte) 4, 'A', 'Z', 0x0000789A_BCDEF012L, UUID.randomUUID());
        writer.putRecord(0, write);
        //
        // Ok, see if we can get it back
        TestRecordBitAligned read = reader.getRecord(0);
        assertEquals(read.a, write.a);
        assertEquals(read.b, write.b);
        assertEquals(read.c, write.c);
        assertEquals(read.d, write.d);
        assertEquals(read.e, write.e);
        assertEquals(read.f, write.f);
        assertEquals(read.g, write.g);
        assertEquals(read.h, write.h);
        assertEquals(read.i, write.i);
        assertEquals(read.j, write.j);
        assertEquals(read.k, write.k);
        assertEquals(read.l, write.l);
    }

    @Test
    public void writeReadRecordBitAlignedNegative() throws Exception {
        RecordDescriptor<TestRecordBitAligned> descriptor = new RecordDescriptor<>(TestRecordBitAligned.class);
        Writer<TestRecordBitAligned> writer = new Writer<>(memory, descriptor);
        Reader<TestRecordBitAligned> reader = new Reader<>(memory, descriptor);
        //
        TestRecordBitAligned write = new TestRecordBitAligned(-123, -45, -6, false, -789L, (short) -12, (short) -3, (byte) -4, 'A', (char) 0x07FF, 0xFFFF_F89A_BCDE_F012L, UUID
                .randomUUID());
        writer.putRecord(0, write);
        //
        // Ok, see if we can get it back
        TestRecordBitAligned read = reader.getRecord(0);
        assertEquals(read.a, write.a);
        assertEquals(read.b, write.b);
        assertEquals(read.c, write.c);
        assertEquals(read.d, write.d);
        assertEquals(read.e, write.e);
        assertEquals(read.f, write.f);
        assertEquals(read.g, write.g);
        assertEquals(read.h, write.h);
        assertEquals(read.i, write.i);
        assertEquals(read.j, write.j);
        assertEquals(read.k, write.k);
        assertEquals(read.l, write.l);
    }

    @Test
    public void writeReadFloatBitRecord() throws Exception {
        RecordDescriptor<TestRecordFloatBitPack> descriptor = new RecordDescriptor<>(TestRecordFloatBitPack.class);
        assertEquals(descriptor.getBitLength(), 89);
        //
        Writer<TestRecordFloatBitPack> writer = new Writer<>(memory, descriptor);
        Reader<TestRecordFloatBitPack> reader = new Reader<>(memory, descriptor);
        //
        TestRecordFloatBitPack write = new TestRecordFloatBitPack(1, 0.12345f, -32768);
        writer.putRecord(0, write);
        //
        // Ok, see if we can get it back
        TestRecordFloatBitPack read = reader.getRecord(0);
        assertEquals(read.a, write.a);
        assertEquals(read.b, write.b, 0.00001);
        assertEquals(read.c, write.c);
    }

    @Test
    public void writeReadFloatByteRecord() throws Exception {
        RecordDescriptor<TestRecordFloatBytePack> descriptor = new RecordDescriptor<>(TestRecordFloatBytePack.class);
        assertEquals(descriptor.getByteLength(), 12);
        //
        Writer<TestRecordFloatBytePack> writer = new Writer<>(memory, descriptor);
        Reader<TestRecordFloatBytePack> reader = new Reader<>(memory, descriptor);
        TestRecordFloatBytePack write = new TestRecordFloatBytePack(123, 0.678901f, -3210);
        writer.putRecord(0, write);
        //
        // Ok, see if we can get it back
        TestRecordFloatBytePack read = reader.getRecord(0);
        assertEquals(read.a, write.a);
        assertEquals(read.b, write.b, 0.00001);
        assertEquals(read.c, write.c);
    }

    @Test
    public void writeReadDoubleBitRecord() throws Exception {
        RecordDescriptor<TestRecordDoubleBitPack> descriptor = new RecordDescriptor<>(TestRecordDoubleBitPack.class);
        assertEquals(descriptor.getBitLength(), 121);
        //
        Writer<TestRecordDoubleBitPack> writer = new Writer<>(memory, descriptor);
        Reader<TestRecordDoubleBitPack> reader = new Reader<>(memory, descriptor);
        //
        TestRecordDoubleBitPack write = new TestRecordDoubleBitPack(1, 0.12345, -32768);
        writer.putRecord(0, write);
        //
        // Ok, see if we can get it back
        TestRecordDoubleBitPack read = reader.getRecord(0);
        assertEquals(read.a, write.a);
        assertEquals(read.b, write.b, 0.00001);
        assertEquals(read.c, write.c);
    }

    @Test
    public void writeReadDoubleByteRecord() throws Exception {
        RecordDescriptor<TestRecordDoubleBytePack> descriptor = new RecordDescriptor<>(TestRecordDoubleBytePack.class);
        assertEquals(descriptor.getByteLength(), 16);
        //
        Writer<TestRecordDoubleBytePack> writer = new Writer<>(memory, descriptor);
        Reader<TestRecordDoubleBytePack> reader = new Reader<>(memory, descriptor);
        TestRecordDoubleBytePack write = new TestRecordDoubleBytePack(123, 0.678901, -3210);
        writer.putRecord(0, write);
        //
        // Ok, see if we can get it back
        TestRecordDoubleBytePack read = reader.getRecord(0);
        assertEquals(read.a, write.a);
        assertEquals(read.b, write.b, 0.00001);
        assertEquals(read.c, write.c);
    }

}