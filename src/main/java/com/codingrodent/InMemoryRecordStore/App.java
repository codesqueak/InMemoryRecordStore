package com.codingrodent.InMemoryRecordStore;

import com.codingrodent.InMemoryRecordStore.core.*;
import com.codingrodent.InMemoryRecordStore.record.*;

import java.util.LinkedList;

/**
 * Test twiddling
 */
public class App {

    public static void main(String[] args) {
        App app = new App();
        app.exec();
    }

    public void exec() {
        try {
            RecordDescriptor descriptor = new RecordDescriptor(Record.class);
            new RecordManager(new ArrayMemoryStore(), 1024, descriptor);
            //
            IMemoryStore memory = new ArrayMemoryStore(1024);
            Writer writer = new Writer(memory, descriptor);
            Reader reader = new Reader(memory, descriptor);
            System.out.println("-- writer --");
            writer.putRecord(0, new Record());
            System.out.println("-- reader --");
            reader.getRecord(0);

            LinkedList ll = new LinkedList();
            ll.iterator();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
