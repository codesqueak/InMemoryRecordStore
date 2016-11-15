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
public class TestRecordBitPack {

    @PackField(order = 0, bits = 24)
    public Integer a;

    @PackField(order = 7, bits = 16)
    public int b;

    @PackField(order = 3, bits = 16)
    public int c;

    @PackField(order = 100, bits = 1)
    public boolean d;

    @Padding(order = 4, bits = 8)
    public Void v1;

    @PackField(order = 200, bits = 46)
    public Long e;

    public TestRecordBitPack() {
    }

    public TestRecordBitPack(Integer a, int b, int c, boolean d, long e) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }

    public Integer getA() {
        return a;
    }

    public void setA(final Integer a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(final int b) {
        this.b = b;
    }

    public int getC() {
        return c;
    }

    public void setC(final int c) {
        this.c = c;
    }

    public long getE() {
        return e;
    }

    public void setE(final long e) {
        this.e = e;
    }

    public boolean isD() {
        return d;
    }

    public void setD(final boolean d) {
        this.d = d;
    }

    public Void getV1() {
        return v1;
    }

    public void setV1(final Void v1) {
        this.v1 = v1;
    }
}
