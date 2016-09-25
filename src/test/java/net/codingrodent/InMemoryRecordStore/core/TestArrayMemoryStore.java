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
package net.codingrodent.InMemoryRecordStore.core;

import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestArrayMemoryStore {
    private ArrayMemoryStore core = null;

    @Before
    public void setUp() throws Exception {
        core = new ArrayMemoryStore(4);
    }

    @After
    public void tearDown() throws Exception {
        core = null;
    }

    @Test
    public void testMemoryStore() {
        try {
            core = new ArrayMemoryStore(0);
            fail("Memory size of zero should cause an error");
        } catch (final Exception e) {
        }
        //
        try {
            core = new ArrayMemoryStore(-1);
            fail("Negative memory size should cause an error");
        } catch (final Exception e) {
        }
        //
        try {
            core = new ArrayMemoryStore(1 << 30);
            fail("More than 1GB memory should cause an error");
        } catch (final Exception e) {
        }
        //
        try {
            core = new ArrayMemoryStore(4);
        } catch (final Exception e) {
            fail("Failed normal call to memory core constructor");
        }
    }

    @Test
    public void testAlignmentExceptions() {
        // Negative addresses are a no-no
        try {
            core.getWord(-1);
            fail("Negative address should cause an error");
        } catch (final Exception e) {
        }
    }

    @Test
    public void testGetBytes() {
        assertEquals(core.getBytes(), 4 * 4);
    }

    @Test
    public void testGetWords() {
        assertEquals(core.getWords(), 4);
    }

    @Test
    public void testGetWord() {
        try {
            core.setWord(0, 0x00112233);
            core.setWord(4, 0x44556677);
            core.setWord(8, 0x8899AABB);
            core.setWord(12, 0xCCDDEEFF);
            //
            assertEquals(core.getWord(0), 0x00112233);
            assertEquals(core.getWord(1), 0x11223344);
            assertEquals(core.getWord(2), 0x22334455);
            assertEquals(core.getWord(3), 0x33445566);
            //
            assertEquals(core.getWord(4), 0x44556677);
            assertEquals(core.getWord(5), 0x55667788);
            assertEquals(core.getWord(6), 0x66778899);
            assertEquals(core.getWord(7), 0x778899AA);
            //
        } catch (final Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetWord24() {
        try {
            core.setWord(0, 0x00112233);
            core.setWord(4, 0x44556677);
            core.setWord(8, 0x8899AABB);
            core.setWord(12, 0xCCDDEEFF);
            //
            assertEquals(core.getWord24(0), 0x001122);
            assertEquals(core.getWord24(1), 0x112233);
            assertEquals(core.getWord24(2), 0x223344);
            assertEquals(core.getWord24(3), 0x334455);
            //
        } catch (final Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetShortWord() {
        try {
            core.setWord(0, 0x00112233);
            core.setWord(4, 0x44556677);
            core.setWord(8, 0x8899AABB);
            core.setWord(12, 0xCCDDEEFF);
            //
            assertEquals(core.getShortWord(0), (short) 0x0011);
            assertEquals(core.getShortWord(1), (short) 0x1122);
            assertEquals(core.getShortWord(2), (short) 0x2233);
            assertEquals(core.getShortWord(3), (short) 0x3344);
            //
        } catch (final Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetByte() {
        try {
            core.setWord(0, 0x00112233);
            core.setWord(4, 0x44556677);
            core.setWord(8, 0x8899AABB);
            core.setWord(12, 0xCCDDEEFF);
            //
            assertEquals(core.getByte(0), (byte) 0x00);
            assertEquals(core.getByte(1), (byte) 0x11);
            assertEquals(core.getByte(2), (byte) 0x22);
            assertEquals(core.getByte(3), (byte) 0x33);
            //
        } catch (final Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSetWord() {
        try {
            // Test aligned writes
            core.setWord(0, 0x00112233);
            core.setWord(4, 0x44556677);
            core.setWord(8, 0x8899AABB);
            core.setWord(12, 0xCCDDEEFF);
            //
            assertEquals(core.getWord(0), 0x00112233);
            assertEquals(core.getWord(1), 0x11223344);
            assertEquals(core.getWord(2), 0x22334455);
            assertEquals(core.getWord(3), 0x33445566);
            //
            core.setWord(16, 0x33221100);
            assertEquals(core.getWord(16), 0x33221100);
            assertEquals(core.getWord(16), core.getWord(0));
            //
            // Test misaligned writes
            core.setWord(0, 0x00112233);
            core.setWord(1, 0x44556677);
            core.setWord(2, 0x8899AABB);
            core.setWord(3, 0xCCDDEEFF);
            //
            assertEquals(core.getWord(0), 0x004488CC);
            assertEquals(core.getWord(1), 0x4488CCDD);
            assertEquals(core.getWord(2), 0x88CCDDEE);
            assertEquals(core.getWord(3), 0xCCDDEEFF);

        } catch (final Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSetWord24() {
        try {
            core.setWord24(0, 0x001122);
            core.setWord24(3, 0x334455);
            core.setWord24(6, 0x667788);
            core.setWord24(9, 0x99AABB);
            core.setWord24(12, 0xCCDDEE);
            //
            assertEquals(core.getWord(0), 0x00112233);
            assertEquals(core.getWord(1), 0x11223344);
            assertEquals(core.getWord(2), 0x22334455);
            assertEquals(core.getWord(3), 0x33445566);
            //
            core.setWord(16, 0x33221100);
            assertEquals(core.getWord(16), 0x33221100);
            assertEquals(core.getWord(16), core.getWord(0));
        } catch (final Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSetShort() {
        try {
            core.setShort(0, (short) 0x0011);
            core.setShort(2, (short) 0x2233);
            core.setShort(4, (short) 0x4455);
            core.setShort(6, (short) 0x6677);
            core.setShort(8, (short) 0x8899);
            core.setShort(10, (short) 0xAABB);
            core.setShort(12, (short) 0xCCDD);
            core.setShort(14, (short) 0xEEFF);
            //
            assertEquals(core.getWord(0), 0x00112233);
            assertEquals(core.getWord(1), 0x11223344);
            assertEquals(core.getWord(2), 0x22334455);
            assertEquals(core.getWord(3), 0x33445566);
            //
            core.setShort(1, (short) 0x0011);
            core.setShort(3, (short) 0x2233);
            core.setShort(5, (short) 0x4455);
            core.setShort(7, (short) 0x6677);
            core.setShort(9, (short) 0x8899);
            core.setShort(11, (short) 0xAABB);
            core.setShort(13, (short) 0xCCDD);
            core.setShort(15, (short) 0xEEFF);
            //
            assertEquals(core.getWord(0), 0xFF001122);
            assertEquals(core.getWord(1), 0x00112233);
            assertEquals(core.getWord(2), 0x11223344);
            assertEquals(core.getWord(3), 0x22334455);
            //
            core.setShort(16, (short) 0x1100);
            assertEquals(core.getWord(16), 0x11001122);
            assertEquals(core.getWord(16), core.getWord(0));
        } catch (final Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSetByte() {
        try {
            for (int i = 0; i <= 15; i++) {
                core.setByte(i, (byte) i);
            }
            //
            assertEquals(core.getWord(0), 0x00010203);
            assertEquals(core.getWord(1), 0x01020304);
            assertEquals(core.getWord(2), 0x02030405);
            assertEquals(core.getWord(3), 0x03040506);
            //
            core.setByte(16, (byte) 0xFF);
            core.setByte(17, (byte) 0x00);
            //
            assertEquals(core.getWord(0), 0xFF000203);
            assertEquals(core.getWord(1), 0x00020304);
            assertEquals(core.getWord(2), 0x02030405);
            assertEquals(core.getWord(3), 0x03040506);
        } catch (final Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testReset() {
        try {
            for (int i = 0; i <= 15; i++) {
                core.setByte(i, (byte) i);
            }
            //
            assertEquals(core.getWord(0), 0x00010203);
            assertEquals(core.getWord(1), 0x01020304);
            assertEquals(core.getWord(2), 0x02030405);
            assertEquals(core.getWord(3), 0x03040506);
            //
            core.reset();
            core.setByte(17, (byte) 0x00);
            //
            assertEquals(core.getWord(0), 0x00000000);
            assertEquals(core.getWord(1), 0x00000000);
            assertEquals(core.getWord(2), 0x00000000);
            assertEquals(core.getWord(3), 0x00000000);
        } catch (final Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDump() {
        try {
            core.dump(2);
            // core.dump(4);
            // core.dump(8);
        } catch (final Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}
