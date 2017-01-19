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

import java.util.UUID;

/**
 * Test data record
 */
@PackRecord
public class TestRecordBytePack {

    @PackField(order = 0, bits = 25)
    public Integer a;

    @PackField(order = 7, bits = 15)
    public int b;

    @PackField(order = 3, bits = 32)
    public int c;

    @PackField(order = 100, bits = 1)
    public boolean d;

    @Padding(order = 4, bits = 8)
    public Void v1;

    @PackField(order = 200, bits = 46)
    public Long e;

    @PackField(order = 201, bits = 4)
    public Boolean f;

    @PackField(order = 202, bits = 40)
    public UUID g;

    @PackArray(order = 301, elements = 10)
    public boolean[] h;

    @PackArray(order = 302, elements = 20)
    public Boolean[] i;

    public TestRecordBytePack() {
    }

    public TestRecordBytePack(Integer a, int b, int c, boolean d, long e, Boolean f, UUID g, boolean[] h, Boolean[] i) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
        this.i = i;
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

    public int getC() {
        return c;
    }

    public boolean isD() {
        return d;
    }

    public long getE() {
        return e;
    }

    public Boolean getF() {
        return f;
    }

    public UUID getG() {
        return g;
    }

    public boolean[] getH() {
        return h;
    }

    public Boolean[] getI() {
        return i;
    }

}
