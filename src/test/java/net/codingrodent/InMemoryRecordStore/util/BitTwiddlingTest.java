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
package net.codingrodent.InMemoryRecordStore.util;

import org.junit.*;

import static net.codingrodent.InMemoryRecordStore.util.BitTwiddling.extend;
import static net.codingrodent.InMemoryRecordStore.util.BitTwiddling.shrink;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class BitTwiddlingTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void extendShrinkByte() throws Exception {
        byte x = extend((byte) 0b00011011, 5);
        assertEquals(x, -5);
        byte y = shrink(x, 5);
        assertEquals(y, 27);
        //
        x = extend((byte) 0b00001011, 4);
        assertEquals(x, -5);
        y = shrink(x, 4);
        assertEquals(y, 11);
        //
        x = extend((byte) 0b00111011, 6);
        assertEquals(x, -5);
        y = shrink(x, 6);
        assertEquals(y, 59);
    }

    @Test
    public void extendShrinkShort() throws Exception {
        short x = extend((short) 0b00011011, 5);
        assertEquals(x, -5);
        short y = shrink(x, 5);
        assertEquals(y, 27);
        //
        x = extend((short) 0b00001011, 4);
        assertEquals(x, -5);
        y = shrink(x, 4);
        assertEquals(y, 11);
        //
        x = extend((short) 0b00111011, 6);
        assertEquals(x, -5);
        y = shrink(x, 6);
        assertEquals(y, 59);
    }

    @Test
    public void extendShrinkInt() throws Exception {
        int x = extend(0b00011011, 5);
        assertEquals(x, -5);
        int y = shrink(x, 5);
        assertEquals(y, 27);
        //
        x = extend(0b00001011, 4);
        assertEquals(x, -5);
        y = shrink(x, 4);
        assertEquals(y, 11);
        //
        x = extend(0b00111011, 6);
        assertEquals(x, -5);
        y = shrink(x, 6);
        assertEquals(y, 59);
    }

}