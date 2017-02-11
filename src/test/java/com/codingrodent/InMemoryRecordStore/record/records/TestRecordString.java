/*
* MIT License
*
*         Copyright (c) 2017
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
@PackRecord
public class TestRecordString {

    public final static int SIZE_A = 5;
    public final static int SIZE_B = 11;
    public final static int SIZE_C = 29;

    @PackString(order = 0, elements = SIZE_A)
    public String a;

    @PackString(order = 0, elements = SIZE_B, bits = 7)
    public String b;

    @PackString(order = 0, elements = SIZE_C, bits = 17)
    public String c;

    public TestRecordString() {
    }

    public TestRecordString(String a, String b, String c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

}
