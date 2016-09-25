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
package net.codingrodent.InMemoryRecordStore.record;

import net.codingrodent.InMemoryRecordStore.annotations.*;
import net.codingrodent.InMemoryRecordStore.core.IMemoryStore;

import java.lang.reflect.Field;
import java.util.*;

/**
 * This class holds the basic type information for an in memory allocation record.
 */
public class RecordDescriptor {

    private Class<?> clazz;
    private Map<Integer, FieldDetails> fieldMap;
    private boolean recordByteAligned;
    private boolean fieldByteAligned;
    private int sizeInBits;
    private int sizeInBytes;

    /**
     * Default constructor. Defines the record characteristics
     *
     * @param clazz Class of record to be handled by this descriptor
     */
    public RecordDescriptor(Class<?> clazz) {
        //
        // Process the class annotation
        this.clazz = clazz;
        PackRecord annotation = (PackRecord) clazz.getAnnotation(PackRecord.class);
        if (null == annotation) {
            throw new IllegalArgumentException("The record must contain a PackRecord annotation");
        }
        this.fieldByteAligned = annotation.fieldAligned();
        this.recordByteAligned = annotation.recordAligned();
        //
        // Recover any field annotations
        List<FieldDetails> fieldList = new LinkedList<>();
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            PackField packFieldAnnotation = (PackField) field.getAnnotation(PackField.class);
            if (null != packFieldAnnotation) {
                fieldList.add(new FieldDetails(field.getType(), field.getName(), packFieldAnnotation.order(), packFieldAnnotation.length(), false));
            } else {
                Padding paddingFieldAnnotation = (Padding) field.getAnnotation(Padding.class);
                if (null != paddingFieldAnnotation) {
                    Class paddingClass = field.getType();
                    if (paddingClass.getTypeName().equals(Void.class.getTypeName())) {
                        fieldList.add(new FieldDetails(paddingClass, field.getName(), paddingFieldAnnotation.order(), paddingFieldAnnotation.length(), true));
                    } else {
                        throw new IllegalArgumentException("@Padding can only be used on Void fields");
                    }
                }
            }
        }
        //
        // Pull out annotation data and sort into layout order
        FieldDetails[] details = fieldList.toArray(new FieldDetails[0]);
        if (0 == details.length) {
            throw new IllegalArgumentException("At least one field must be annotated with @PackField");
        }
        // Sort
        Map<Integer, FieldDetails> fieldMap = new LinkedHashMap<>();
        Arrays.stream(details).sorted((l, r) -> l.getOrder().compareTo(r.getOrder())).forEach(e -> {
            if (null != fieldMap.put(e.getOrder(), e)) {
                throw new IllegalArgumentException("Two (or more) fields at the same ordered position");
            }
        });
        this.fieldMap = fieldMap;
        //
        // Calculate storage requirements
        sizeInBits = 0;
        sizeInBytes = 0;
        fieldMap.entrySet().stream().forEach(f -> {
            if (fieldByteAligned) {
                // Pack at byte level
                sizeInBits = sizeInBits + f.getValue().getByteLength() * 8;
            } else {
                // pack at bit level
                sizeInBits = sizeInBits + f.getValue().getBitLength();
            }
        });
        sizeInBytes = ((sizeInBits - 1) >> 3) + 1;
    }

    public boolean isFieldByteAligned() {
        return fieldByteAligned;
    }

    public boolean isRecordByteAligned() {
        return recordByteAligned;
    }

    public int getSizeInBytes() {
        return sizeInBytes;
    }

    public int getSizeInBits() {
        return sizeInBits;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    private static class FieldDetails {

        private IMemoryStore.Type type;
        private String fieldName;
        private int order;
        private int bitLength;
        private int byteLength;
        private boolean padding;

        FieldDetails(Class<?> clazz, String fieldName, Integer order, Integer length, boolean padding) {
            switch (clazz.getName()) {
                case "boolean":
                case "java.lang.Boolean":
                    type = IMemoryStore.Type.Bit;
                    break;
                case "byte":
                case "java.lang.Byte":
                    type = IMemoryStore.Type.Byte8;
                    break;
                case "short":
                case "java.lang.Short":
                    type = IMemoryStore.Type.Short16;
                    break;
                case "int":
                case "java.lang.Integer":
                    type = IMemoryStore.Type.Word32;
                    break;
                case "long":
                case "java.lang.Long":
                    type = IMemoryStore.Type.Word64;
                    break;
                case "char":
                case "java.lang.Character":
                    type = IMemoryStore.Type.Char16;
                    break;
                case "java.lang.Void":
                    type = IMemoryStore.Type.Void;
                    break;
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

        boolean isPadding() {
            return padding;
        }
    }

}
