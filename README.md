[![License: MIT](https://img.shields.io/badge/license-MIT-brightgreen.svg)](https://opensource.org/licenses/MIT)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.codingrodent/InMemoryRecordStore/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.codingrodent/InMemoryRecordStore)
[![Maven Central](https://img.shields.io/jenkins/s/https/jenkins.qa.ubuntu.com/precise-desktop-amd64_default.svg)](https://img.shields.io/jenkins/s/https/jenkins.qa.ubuntu.com)
# In Memory Record Storage

In Memory Record Storage is a library to allow object data defined as records to be efficiently stored into memory.  
Each record field may be either bit or byte aligned, with each record being in turn byte aligned.
The types supported are:

* Boolean
* Byte
* Short
* Integer
* Long
* Character
* Void (Special case, used for packing only)

Both primitive and object types may be used interchangeably.

To define a record, the owning class must be annotated with a *@PackRecord* annotation.  For example,
 
**@recordByteAligned(fieldByteAligned=true)**

* fieldByteAligned - Signifies if each field is bit or byte aligned in a record in memory

To define a field in a record, the field must be annotated with a *@PackField* annotation. For example

**@PackField(order = 1, bits = 9)**

This signified that a field is to be packed into 9 bits (if bit aligned) or 2 bytes (if byte aligned). Note that types can be defined to occupy less space (e.g. Integer into 9 bits)
but cannot be more space (e.g. Integer into 33 bits). The maximum bit size is the natural primitive type length.

The order argument of the annotation states in which order the field is to be packed.  Fields are sorted before packing.  Two fields with the same order value will cause an exception to be thrown.

An example record:

```java
@PackRecord
public class Record {
    @PackField(order = 0, bits = 24)
    public Integer a = 0x0012_3456;

    @PackField(order = 7, bits = 16)
    public int b = 0x0000_FECB;

    @PackField(order = 3, bits = 16)
    public int c = 0x0000_789A;

    @PackField(order = 100, bits = 1)
    public boolean d = true;

    @Padding(order = 4, bits = 8)
    public Void v1;

    @PackField(order = 200, bits = 8)
    public int e = 0x0000_0077;

    @PackField(order = 300, bits = 16)
    public int f = -32768;
}

```


# Build a Record Store

The records are held in a data structure complying with the *IMemoryStore* interface.  A basic store, *ArrayMemoryStore* is defined for default use.

The simplest way to use this is to define an empty store and let the RecordManager handle allocation.  For example the following defines storage for 
up to 1000 copies of the Record class.  Read & write operations can now be performed via the record manager which treats storage as an array of records.

```
    RecordDescriptor descriptor = new RecordDescriptor(Record.class);
    RecordManager rm = new RecordManager(new ArrayMemoryStore(), 1000, descriptor);
    //
    rm.putRecord(123, new Record())
    Record record = (Record) rm.getRecord(123)
```

# Collections

Various collections are available.  It is intended to enlarge this list in future releases.

## Array

A simple array store. For example, to store up to 1000 records use:

```
    PackedArray<Record> array = new PackedArray<>(Record.class, 1000);
    Record record = new Record(...); // create
    array.putRecord(1, record); // save it
    record = array.getRecord(i); // recover
```

## LinkedList

A bidirectional linked list. For example, to create a list capable of storing up to 1000 elements use:

```
    List<Record> list = list = new PackedList<>(Record.class, 1000); // create
    deque.addFirst(record1); // add some data
    deque.addFirst(record2);
    deque.addFirst(record3);
    list.stream().map(...).forEach(...); // process records
```


# Restrictions

The following features are not yet available but will included shortly:

* Use of custom memory stores
* use of custom object reader / writer

# Things to add

* Bit record packing
* Support arrays
* Support fixed size strings
* Collection classes (partly implemented)
* Binary file I/O

# Build

(Windows)
gradlew clean build test

(Linux)
./gradlew clean build test


# Using Jenkins

The project includes a Jenkins file to control a pipeline build.






