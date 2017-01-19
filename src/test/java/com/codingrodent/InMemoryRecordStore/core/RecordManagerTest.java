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

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecordManagerTest {

    private final static int BYTES_PER_RECORD = 10;
    private final static int RECORDS = 32;

    private IMemoryStore memoryStore;
    private RecordDescriptor recordDescriptor;
    private Boolean[] booleanArray = {true, false, true, true, false};
    ;

    @Before
    public void setUp() throws Exception {
        memoryStore = mock(IMemoryStore.class);
        //
        recordDescriptor = mock(RecordDescriptor.class);
        when(recordDescriptor.getByteLength()).thenReturn(BYTES_PER_RECORD); // make records 10 bytes long
    }

    @Test(expected = IllegalArgumentException.class)
    public void validationSize() throws Exception {
        new RecordManager(memoryStore, 1, recordDescriptor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validationMemory() throws Exception {
        new RecordManager(memoryStore, Integer.MAX_VALUE, recordDescriptor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getRecord1() throws Exception {
        RecordDescriptor<TestRecordBytePack> recordDescriptor = new RecordDescriptor<>(TestRecordBytePack.class);
        IMemoryStore memoryStore = new ArrayMemoryStore();
        RecordManager<TestRecordBytePack> recordManager = new RecordManager<>(memoryStore, RECORDS, recordDescriptor);
        recordManager.getRecord(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getRecord2() throws Exception {
        RecordDescriptor<TestRecordBytePack> recordDescriptor = new RecordDescriptor<>(TestRecordBytePack.class);
        IMemoryStore memoryStore = new ArrayMemoryStore();
        RecordManager<TestRecordBytePack> recordManager = new RecordManager<>(memoryStore, RECORDS, recordDescriptor);
        recordManager.getRecord(RECORDS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void putRecord1() throws Exception {
        RecordDescriptor<TestRecordBytePack> recordDescriptor = new RecordDescriptor<>(TestRecordBytePack.class);
        IMemoryStore memoryStore = new ArrayMemoryStore();
        RecordManager<TestRecordBytePack> recordManager = new RecordManager<>(memoryStore, RECORDS, recordDescriptor);
        recordManager.putRecord(-1, new TestRecordBytePack(1, 456, -123, true, -12345, false, UUID.randomUUID(), new boolean[10], booleanArray));
    }

    @Test(expected = IllegalArgumentException.class)
    public void putRecord2() throws Exception {
        RecordDescriptor<TestRecordBytePack> recordDescriptor = new RecordDescriptor<>(TestRecordBytePack.class);
        IMemoryStore memoryStore = new ArrayMemoryStore();
        RecordManager<TestRecordBytePack> recordManager = new RecordManager<>(memoryStore, RECORDS, recordDescriptor);
        recordManager.putRecord(RECORDS, new TestRecordBytePack(1, 456, -123, true, -12345, false, UUID.randomUUID(), new boolean[10], booleanArray));
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
        RecordDescriptor<TestRecordBytePack> recordDescriptor = new RecordDescriptor<>(TestRecordBytePack.class);
        assertEquals(recordDescriptor.getByteLength(), 50);
        IMemoryStore memoryStore = new ArrayMemoryStore();
        RecordManager<TestRecordBytePack> recordManager = new RecordManager<>(memoryStore, RECORDS, recordDescriptor);
        assertEquals(RECORDS, recordManager.getRecords());
        //
        // Check each record read & write
        for (int i = 0; i < RECORDS; i++) {
            TestRecordBytePack testRecordBytePack = new TestRecordBytePack(i, 456, -123, true, -12345, false, new UUID(i, i + 1), new boolean[10], booleanArray);
            recordManager.putRecord(i, testRecordBytePack);
            TestRecordBytePack testRecordBytePackGet = recordManager.getRecord(i);
            //
            assertEquals(testRecordBytePack.getA(), testRecordBytePackGet.getA());
            assertEquals(testRecordBytePack.getB(), testRecordBytePackGet.getB());
            assertEquals(testRecordBytePack.getC(), testRecordBytePackGet.getC());
            assertEquals(testRecordBytePack.isD(), testRecordBytePackGet.isD());
            assertEquals(testRecordBytePack.getE(), testRecordBytePackGet.getE());
            assertEquals(testRecordBytePack.getF(), testRecordBytePackGet.getF());
            assertEquals(testRecordBytePack.getG(), testRecordBytePackGet.getG());
        }
        // Make sure no record overwrite has happened by re-reading all records
        for (int i = 0; i < RECORDS; i++) {
            TestRecordBytePack testRecordBytePack = new TestRecordBytePack(i, 456, -123, true, -12345, false, new UUID(i, i + 1), new boolean[10], booleanArray);
            TestRecordBytePack testRecordBytePackGet = (TestRecordBytePack) recordManager.getRecord(i);
            //
            assertEquals(testRecordBytePack.getA(), testRecordBytePackGet.getA());
            assertEquals(testRecordBytePack.getB(), testRecordBytePackGet.getB());
            assertEquals(testRecordBytePack.getC(), testRecordBytePackGet.getC());
            assertEquals(testRecordBytePack.isD(), testRecordBytePackGet.isD());
            assertEquals(testRecordBytePack.getE(), testRecordBytePackGet.getE());
            assertEquals(testRecordBytePack.getF(), testRecordBytePackGet.getF());
            assertEquals(testRecordBytePack.getG(), testRecordBytePackGet.getG());
        }
    }

    @Test
    public void putGetRecordBitAligned() {
        RecordDescriptor<TestRecordBitPack> recordDescriptor = new RecordDescriptor<>(TestRecordBitPack.class);
        assertEquals(recordDescriptor.getByteLength(), 30);
        IMemoryStore memoryStore = new ArrayMemoryStore();
        RecordManager<TestRecordBitPack> recordManager = new RecordManager<>(memoryStore, RECORDS, recordDescriptor);
        assertEquals(RECORDS, recordManager.getRecords());
        //
        // Check each record read & write
        for (int i = 0; i < RECORDS; i++) {
            TestRecordBitPack testRecordbitPack = new TestRecordBitPack(i, 456, -123, true, -12345, false, new UUID(i, i + 1));
            recordManager.putRecord(i, testRecordbitPack);
            TestRecordBitPack testRecordBitPackGet = recordManager.getRecord(i);
            //
            assertEquals(testRecordbitPack.getA(), testRecordBitPackGet.getA());
            assertEquals(testRecordbitPack.getB(), testRecordBitPackGet.getB());
            assertEquals(testRecordbitPack.getC(), testRecordBitPackGet.getC());
            assertEquals(testRecordbitPack.isD(), testRecordBitPackGet.isD());
            assertEquals(testRecordbitPack.getE(), testRecordBitPackGet.getE());
            assertEquals(testRecordbitPack.getF(), testRecordBitPackGet.getF());
            assertEquals(testRecordbitPack.getG(), testRecordBitPackGet.getG());
        }
        // Make sure no record overwrite has happened by re-reading all records
        for (int i = 0; i < RECORDS; i++) {
            TestRecordBitPack testRecordbitPack = new TestRecordBitPack(i, 456, -123, true, -12345, false, new UUID(i, i + 1));
            TestRecordBitPack testRecordBitPackGet = recordManager.getRecord(i);
            //
            assertEquals(testRecordbitPack.getA(), testRecordBitPackGet.getA());
            assertEquals(testRecordbitPack.getB(), testRecordBitPackGet.getB());
            assertEquals(testRecordbitPack.getC(), testRecordBitPackGet.getC());
            assertEquals(testRecordbitPack.isD(), testRecordBitPackGet.isD());
            assertEquals(testRecordbitPack.getE(), testRecordBitPackGet.getE());
            assertEquals(testRecordbitPack.getF(), testRecordBitPackGet.getF());
            assertEquals(testRecordbitPack.getG(), testRecordBitPackGet.getG());
        }
    }
}