package com.codingrodent.InMemoryRecordStore;

import com.codingrodent.InMemoryRecordStore.core.*;
import com.codingrodent.InMemoryRecordStore.record.*;

/**
 * Test twiddling
 */
public class App {

    public static void main(String[] args) {
        App app = new App();
        app.exec();
    }

    public void exec() {
        RecordDescriptor descriptor = new RecordDescriptor(Record.class);
        new RecordManager(new ArrayMemoryStore(), 1024, descriptor);
        //
        Writer writer = new Writer(new ArrayMemoryStore(), descriptor, IMemoryStore.AlignmentMode.BYTE_BYTE);
        writer.putRecord(0, new Record());

    }

}
