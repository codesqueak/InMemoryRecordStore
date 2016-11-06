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
package com.codingrodent.InMemoryRecordStore.record.records;

import com.codingrodent.InMemoryRecordStore.annotations.*;

/**
 * Test data record
 */
@PackRecord(fieldByteAligned = false)
public class TestRecordNoPack {

    @PackField(order = 0, bits = 8)
    public Byte a;

    @PackField(order = 1, bits = 16)
    public Short b;

    @PackField(order = 2, bits = 32)
    public int c;

    @PackField(order = 3, bits = 64)
    public long d;

    @PackField(order = 4, bits = 16)
    public char e;

    public TestRecordNoPack(byte a, short b, int c, long d, char e) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }

    public Byte getA() {
        return a;
    }

    public void setA(final Byte a) {
        this.a = a;
    }

    public Short getB() {
        return b;
    }

    public void setB(final Short b) {
        this.b = b;
    }

    public int getC() {
        return c;
    }

    public void setC(final int c) {
        this.c = c;
    }

    public long getD() {
        return d;
    }

    public void setD(final long d) {
        this.d = d;
    }

    public char getE() {
        return e;
    }

    public void setE(final char e) {
        this.e = e;
    }

}
