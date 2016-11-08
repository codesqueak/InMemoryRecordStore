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
public class TestRecordBitAligned {

    @PackField(order = 0, bits = 14)
    public Integer a;

    @PackField(order = 1, bits = 10) //24
    public int b;

    @PackField(order = 2, bits = 20) // 44
    public int c;

    @PackField(order = 3, bits = 1) // 45
    public boolean d;

    @Padding(order = 4, bits = 7) // 52
    public Void v1;

    @PackField(order = 5, bits = 12) // 64
    public Long e;

    @PackField(order = 6, bits = 10) // 74
    public Short f;

    @PackField(order = 7, bits = 5) // 79
    public Short g;

    @PackField(order = 8, bits = 5) // 84
    public Byte h;

    @PackField(order = 9, bits = 7) // 91
    public char i;

    @PackField(order = 10, bits = 11) // 102
    public char j;

    @PackField(order = 11, bits = 48) // 150
    public long k;

    public TestRecordBitAligned() {
    }

    public TestRecordBitAligned(Integer a, int b, int c, boolean d, long e, short f, short g, byte h, char i, char j, long k) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
        this.h = h;
        this.i = i;
        this.j = j;
        this.k = k;
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

    public short isF() {
        return f;
    }

    public void setF(final short f) {
        this.f = f;
    }

    public short isG() {
        return g;
    }

    public void setG(final short g) {
        this.g = g;
    }

    public Byte isH() {
        return h;
    }

    public void setH(final Byte h) {
        this.h = h;
    }

    public char isI() {
        return i;
    }

    public void setI(final char i) {
        this.i = i;
    }

    public char isJ() {
        return j;
    }

    public void setJ(final char j) {
        this.j = j;
    }

    public long isK() {
        return k;
    }

    public void setK(final long k) {
        this.k = k;
    }

}
