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
package com.codingrodent.InMemoryRecordStore.record;

import com.codingrodent.InMemoryRecordStore.annotations.*;
import com.codingrodent.InMemoryRecordStore.core.IMemoryStore;

import java.lang.reflect.Field;
import java.util.*;

/**
 * This class holds the basic type information for an in memory allocation record.
 */
public class RecordDescriptor<T> {
    private final Class<T> clazz;
    private final boolean fieldByteAligned;
    private final int lengthInBits;
    private final int lengthInBytes;
    private final HashMap<String, FieldDetails> fieldDetailsMap;
    private final List<String> fieldNames;

    /**
     * Default constructor. Defines the record characteristics
     *
     * @param clazz Class of record to be handled by this descriptor
     */
    public RecordDescriptor(Class<T> clazz) {
        //
        // Process the class annotation
        this.clazz = clazz;
        PackRecord annotation = clazz.getAnnotation(PackRecord.class);
        if (null == annotation) {
            throw new IllegalArgumentException("The record must contain a PackRecord annotation");
        }
        this.fieldByteAligned = annotation.fieldByteAligned();
        //
        // Recover any field annotations
        List<FieldDetails> fieldList = new LinkedList<>();
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            // Pack annotation
            PackField packFieldAnnotation = field.getAnnotation(PackField.class);
            if (null != packFieldAnnotation) {
                Class packClass = field.getType();
                if (!packClass.getTypeName().equals(Void.class.getTypeName())) {
                    FieldDetails fieldDetails = new FieldDetails(field.getType(), field.getName(), packFieldAnnotation.order(), packFieldAnnotation.bits());
                    fieldList.add(fieldDetails);
                } else {
                    throw new IllegalArgumentException("@Pack cannot be used on Void fields");
                }
            }
            // Padding annotation
            Padding paddingFieldAnnotation = field.getAnnotation(Padding.class);
            if (null != paddingFieldAnnotation) {
                Class<?> paddingClass = field.getType();
                if (paddingClass.getTypeName().equals(Void.class.getTypeName())) {
                    FieldDetails fieldDetails = new FieldDetails(paddingClass, field.getName(), paddingFieldAnnotation.order(), paddingFieldAnnotation.bits());
                    fieldList.add(fieldDetails);
                } else {
                    throw new IllegalArgumentException("@Padding can only be used on Void fields");
                }
            }
        }
        // Pull out annotation data and sort into layout order
        FieldDetails[] fieldDetails = fieldList.toArray(new FieldDetails[0]);
        // Sort
        Arrays.sort(fieldDetails, (x, y) -> x.getOrder().compareTo(y.getOrder()));
        List<String> fieldNames = new ArrayList<>(fieldDetails.length);
        //
        // Calculate storage requirements
        int lengthInBits = 0;
        HashMap<String, FieldDetails> fieldDetailsMap = new HashMap<>();
        for (FieldDetails field : fieldDetails) {
            fieldNames.add(field.getFieldName());
            if (fieldByteAligned) {
                // pack at byte level
                lengthInBits = lengthInBits + field.getByteLength() * 8;
            } else {
                // pack at bit level
                lengthInBits = lengthInBits + field.getBitLength();
            }
            fieldDetailsMap.put(field.getFieldName(), field);
        }
        this.lengthInBits = lengthInBits;
        this.lengthInBytes = ((lengthInBits - 1) >> 3) + 1;
        this.fieldDetailsMap = fieldDetailsMap;
        this.fieldNames = Collections.unmodifiableList(fieldNames);
    }

    public boolean isFieldByteAligned() {
        return fieldByteAligned;
    }

    public int getByteLength() {
        return lengthInBytes;
    }

    public int getBitLength() {
        return lengthInBits;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public List<String> getFieldNames() {
        return fieldNames;
    }

    public FieldDetails getFieldDetails(final String fieldName) {
        return fieldDetailsMap.get(fieldName);
    }

    static class FieldDetails {

        private IMemoryStore.Type type;
        private String fieldName;
        private int order;
        private int bitLength;
        private int byteLength;

        /**
         * Create details for one annotated field
         *
         * @param clazz     Class of field
         * @param fieldName Name of field
         * @param order     Position in packing order
         * @param length    Length in bits
         */
        FieldDetails(Class<?> clazz, String fieldName, int order, int length) {
            switch (clazz.getName()) {
                case "boolean":
                case "java.lang.Boolean":
                    type = IMemoryStore.Type.Bit;
                    length = length > 8 ? 8 : length;
                    break;
                case "byte":
                case "java.lang.Byte":
                    type = IMemoryStore.Type.Byte8;
                    length = length > 8 ? 8 : length;
                    break;
                case "short":
                case "java.lang.Short":
                    type = IMemoryStore.Type.Short16;
                    length = length > 16 ? 16 : length;
                    break;
                case "int":
                case "java.lang.Integer":
                    type = IMemoryStore.Type.Word32;
                    length = length > 32 ? 32 : length;
                    break;
                case "long":
                case "java.lang.Long":
                    type = IMemoryStore.Type.Word64;
                    length = length > 64 ? 64 : length;
                    break;
                case "char":
                case "java.lang.Character":
                    type = IMemoryStore.Type.Char16;
                    length = length > 16 ? 16 : length;
                    break;
                case "java.lang.Void":
                    type = IMemoryStore.Type.Void;
                    length = length > 64 ? 64 : length;
                    break;
                case "java.util.UUID":
                    type = IMemoryStore.Type.UUID;
                    length = 128;
                    break;
                case "java.lang.Double":
                case "double":
                case "java.lang.Float":
                case "float":
                default:
                    throw new IllegalArgumentException("Unsupported packing type. " + clazz.getName());
            }
            this.fieldName = fieldName;
            this.order = order;
            this.bitLength = length;
            this.byteLength = ((length - 1) >> 3) + 1;
        }

        IMemoryStore.Type getType() {
            return type;
        }

        Integer getOrder() {
            return order;
        }

        int getBitLength() {
            return bitLength;
        }

        int getByteLength() {
            return byteLength;
        }

        String getFieldName() {
            return fieldName;
        }
    }
}
