package net.codingrodent.InMemoryRecordStore;

import net.codingrodent.InMemoryRecordStore.core.*;
import net.codingrodent.InMemoryRecordStore.core.IMemoryStore.AlignmentMode;
import net.codingrodent.InMemoryRecordStore.record.*;

import static net.codingrodent.InMemoryRecordStore.util.BitTwiddling.extend;
import static net.codingrodent.InMemoryRecordStore.util.BitTwiddling.shrink;

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
        Writer writer = new Writer(new ArrayMemoryStore(), descriptor, AlignmentMode.BYTE_BYTE);
        writer.putRecord(0, new Record());
        //

        process((byte) 0b00011011, (byte) 5);
        process((byte) 0b01111011, (byte) 7);
        process((byte) 0b00001011, (byte) 4);
        //
        process((byte) 0b00000001, (byte) 2);
        process((byte) 0b00000010, (byte) 3);
        process((byte) 0b00000100, (byte) 4);

    }

    void process(byte v, byte bits) {
        System.out.println("v=" + v);
        byte x = extend(v, bits);
        byte y = shrink(x, bits);
        System.out.println(x + ":" + y);

    }
}
