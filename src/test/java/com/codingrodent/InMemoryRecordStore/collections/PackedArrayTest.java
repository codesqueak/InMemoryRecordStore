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
package com.codingrodent.InMemoryRecordStore.collections;

import com.codingrodent.InMemoryRecordStore.record.records.*;
import org.junit.*;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PackedArrayTest {

    private final static int RECORDS = 2000;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void negativeSize() {
        try {
            new PackedArray<>(TestRecordBytePack.class, -1);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("The component must have"));
            return;
        }
        fail("IllegalArgumentException expected");
    }

    @Test
    public void overLargeSize() {
        try {
            new PackedArray<>(TestRecordBytePack.class, Integer.MAX_VALUE);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("Maximum storage limit exceeded"));
            return;
        }
        fail("IllegalArgumentException expected");
    }

    @Test
    public void putGetRecordByteAligned() {
        PackedArray<TestRecordBytePack> array = new PackedArray<>(TestRecordBytePack.class, RECORDS);
        // Check each record read & write
        for (int i = 0; i < RECORDS; i++) {
            TestRecordBytePack testRecordBytePack = new TestRecordBytePack(i, 456, -123, true, -12345);
            array.putRecord(i, testRecordBytePack);
            TestRecordBytePack testRecordBytePackGet = array.getRecord(i);
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
            TestRecordBytePack testRecordBytePackGet = array.getRecord(i);
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
        PackedArray<TestRecordBitPack> array = new PackedArray<>(TestRecordBitPack.class, RECORDS);
        // Check each record read & write
        for (int i = 0; i < RECORDS; i++) {
            TestRecordBitPack testRecordbitPack = new TestRecordBitPack(i, 456, -123, true, -12345);
            array.putRecord(i, testRecordbitPack);
            TestRecordBitPack testRecordBitPackGet = array.getRecord(i);
            //
            assertEquals(testRecordbitPack.getA(), testRecordBitPackGet.getA());
            assertEquals(testRecordbitPack.getB(), testRecordBitPackGet.getB());
            assertEquals(testRecordbitPack.getC(), testRecordBitPackGet.getC());
            assertEquals(testRecordbitPack.isD(), testRecordBitPackGet.isD());
            assertEquals(testRecordbitPack.getE(), testRecordBitPackGet.getE());
        }
        // Make sure no record overwrite has happened by re-reading all records
        for (int i = 0; i < RECORDS; i++) {
            TestRecordBitPack testRecordbitPack = new TestRecordBitPack(i, 456, -123, true, -12345);
            TestRecordBitPack testRecordBitPackGet = array.getRecord(i);
            //
            assertEquals(testRecordbitPack.getA(), testRecordBitPackGet.getA());
            assertEquals(testRecordbitPack.getB(), testRecordBitPackGet.getB());
            assertEquals(testRecordbitPack.getC(), testRecordBitPackGet.getC());
            assertEquals(testRecordbitPack.isD(), testRecordBitPackGet.isD());
            assertEquals(testRecordbitPack.getE(), testRecordBitPackGet.getE());
        }
    }

}