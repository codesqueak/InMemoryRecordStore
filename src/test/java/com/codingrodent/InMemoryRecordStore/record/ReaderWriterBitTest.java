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
import com.codingrodent.InMemoryRecordStore.record.records.TestRecordBitAligned;
import org.junit.*;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class ReaderWriterBitTest {
    private IMemoryStore memory;

    @Before
    public void setUp() throws Exception {
        memory = new ArrayMemoryStore(1024);
    }

    @Test
    public void writeReadRecord() throws Exception {
        RecordDescriptor descriptor = new RecordDescriptor<>(TestRecordBitAligned.class);
        Writer writer = new Writer(memory, descriptor);
        Reader reader = new Reader(memory, descriptor);
        //
        TestRecordBitAligned write = new TestRecordBitAligned(1, -1, -32768, true, 0x0000_0234L, (short) -307,//
                                                              (short) 0x15, (byte) -11, (char) 65, (char) 1089, 0x0000789A_BCDEF012L, new UUID(0xFFEE_DDCC_BBAA_9988L,
                                                                                                                                               0x7766_5544_3322_1100L));
        writer.putRecord(0, write);

        byte[] packed = { //
                0b00000000, //
                0b00000111, //
                (byte) 0b11111111, //
                (byte) 0b11111000, //
                0b00000000, //
                0b00001000, //
                0b00000010, //
                0b00110100, //
                (byte) 0b10110011, //
                0b01101011, //
                0b01011000, //
                0b00110001, //
                0b00000101, //
                (byte) 0b11100010, //
                0b01101010, //
                (byte) 0b11110011, //
                0b01111011, //
                (byte) 0b11000000, //
                0b01001011, //
                (byte) 0b11111111, //
                (byte) 0b10111011,//
                (byte) 0b01110111,//
                (byte) 0b00110010,//
                (byte) 0b11101110,//
                (byte) 0b10101010,//
                (byte) 0b01100110,//
                (byte) 0b00100001,//
                (byte) 0b11011101,//
                (byte) 0b10011001,//
                (byte) 0b01010101,//
                (byte) 0b00010000,//
                (byte) 0b11001100,//
                (byte) 0b10001000,//
                (byte) 0b01000100,//
                (byte) 0b00000000};
        assertEquals(packed.length, descriptor.getByteLength());
        // Did record pack correctly ?
        for (int i = 0; i < packed.length; i++) {
            assertEquals(packed[i], memory.getByte(i));
        }
    }
}