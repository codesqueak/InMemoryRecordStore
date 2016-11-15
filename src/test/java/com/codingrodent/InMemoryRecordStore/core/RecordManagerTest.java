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
package com.codingrodent.InMemoryRecordStore.core;

import com.codingrodent.InMemoryRecordStore.record.RecordDescriptor;
import com.codingrodent.InMemoryRecordStore.record.records.*;
import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecordManagerTest {

    private final static int BYTES_PER_RECORD = 10;
    private final static int RECORDS = 32;

    private IMemoryStore memoryStore;
    private RecordDescriptor recordDescriptor;

    @Before
    public void setUp() throws Exception {
        memoryStore = mock(IMemoryStore.class);
        //
        recordDescriptor = mock(RecordDescriptor.class);
        when(recordDescriptor.getByteLength()).thenReturn(BYTES_PER_RECORD); // make records 10 bytes long
    }

    @Test(expected = IllegalArgumentException.class)
    public void validationSize() throws Exception {
        RecordManager recordManager = new RecordManager(memoryStore, 1, recordDescriptor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validationMemory() throws Exception {
        RecordManager recordManager = new RecordManager(memoryStore, Integer.MAX_VALUE, recordDescriptor);
    }

    @Test
    public void getLength() {
        RecordManager recordManager = new RecordManager(memoryStore, RECORDS, recordDescriptor);
        //
        assertEquals(recordManager.getLengthInBytes(), RECORDS * BYTES_PER_RECORD);
        assertEquals(recordManager.getLengthInWords(), (((RECORDS * BYTES_PER_RECORD) - 1) >> 2) + 1);
    }

    @Test
    public void putGetRecordByteAligned() {
        RecordDescriptor recordDescriptor = new RecordDescriptor(TestRecordBytePack.class);
        assertEquals(recordDescriptor.getByteLength(), 15);
        IMemoryStore memoryStore = new ArrayMemoryStore();
        RecordManager recordManager = new RecordManager(memoryStore, RECORDS, recordDescriptor);
        //
        // Check each record read & write
        for (int i = 0; i < RECORDS; i++) {
            TestRecordBytePack testRecordBytePack = new TestRecordBytePack(i, 456, -123, true, -12345);
            recordManager.putRecord(i, testRecordBytePack);
            TestRecordBytePack testRecordBytePackGet = (TestRecordBytePack) recordManager.getRecord(i);
            //
            assertEquals(testRecordBytePack.getA(), testRecordBytePackGet.getA());
            assertEquals(testRecordBytePack.getB(), testRecordBytePackGet.getB());
            assertEquals(testRecordBytePack.getC(), testRecordBytePackGet.getC());
            assertEquals(testRecordBytePack.isD(), testRecordBytePackGet.isD());
            assertEquals(testRecordBytePack.getE(), testRecordBytePackGet.getE());
        }
        // Make sure no record overwrite has happened by re-reading all records
        for (int i = 0; i < RECORDS; i++) {
            TestRecordBytePack testRecordBytePack = new TestRecordBytePack(i, 456, -123, true, -12345);
            TestRecordBytePack testRecordBytePackGet = (TestRecordBytePack) recordManager.getRecord(i);
            //
            assertEquals(testRecordBytePack.getA(), testRecordBytePackGet.getA());
            assertEquals(testRecordBytePack.getB(), testRecordBytePackGet.getB());
            assertEquals(testRecordBytePack.getC(), testRecordBytePackGet.getC());
            assertEquals(testRecordBytePack.isD(), testRecordBytePackGet.isD());
            assertEquals(testRecordBytePack.getE(), testRecordBytePackGet.getE());
        }
    }

    @Test
    public void putGetRecordBitAligned() {
        RecordDescriptor recordDescriptor = new RecordDescriptor(TestRecordBitPack.class);
        assertEquals(recordDescriptor.getByteLength(), 14);
        IMemoryStore memoryStore = new ArrayMemoryStore();
        RecordManager recordManager = new RecordManager(memoryStore, RECORDS, recordDescriptor);
        //
        // Check each record read & write
        for (int i = 0; i < RECORDS; i++) {
            TestRecordBitPack testRecordbitPack = new TestRecordBitPack(i, 456, -123, true, -12345);
            recordManager.putRecord(i, testRecordbitPack);
            TestRecordBitPack testRecordbitPackkGet = (TestRecordBitPack) recordManager.getRecord(i);
            //
            assertEquals(testRecordbitPack.getA(), testRecordbitPack.getA());
            assertEquals(testRecordbitPack.getB(), testRecordbitPack.getB());
            assertEquals(testRecordbitPack.getC(), testRecordbitPack.getC());
            assertEquals(testRecordbitPack.isD(), testRecordbitPack.isD());
            assertEquals(testRecordbitPack.getE(), testRecordbitPack.getE());
        }
        // Make sure no record overwrite has happened by re-reading all records
        for (int i = 0; i < RECORDS; i++) {
            TestRecordBitPack testRecordbitPack = new TestRecordBitPack(i, 456, -123, true, -12345);
            TestRecordBitPack testRecordbitPackkGet = (TestRecordBitPack) recordManager.getRecord(i);
            //
            assertEquals(testRecordbitPack.getA(), testRecordbitPack.getA());
            assertEquals(testRecordbitPack.getB(), testRecordbitPack.getB());
            assertEquals(testRecordbitPack.getC(), testRecordbitPack.getC());
            assertEquals(testRecordbitPack.isD(), testRecordbitPack.isD());
            assertEquals(testRecordbitPack.getE(), testRecordbitPack.getE());
        }
    }
}