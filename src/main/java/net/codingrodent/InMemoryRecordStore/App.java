package net.codingrodent.InMemoryRecordStore;

import net.codingrodent.InMemoryRecordStore.core.*;
import net.codingrodent.InMemoryRecordStore.record.RecordDescriptor;

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
    }
}
