package com.example.ringbuffer;

public final class Demo {
    public static void main(String[] args) {
        RingBuffer<String> rb = new RingBuffer<>(3);
        var r1 = rb.createReader();
        var r2 = rb.createReader();

        rb.write("A");
        rb.write("B");

        System.out.println(r1.id() + " -> " + r1.read());
        System.out.println(r2.id() + " -> " + r2.read());

        rb.write("C");
        rb.write("D"); // overwrite oldest

        System.out.println(r1.id() + " -> " + r1.read());
        System.out.println(r2.id() + " -> " + r2.read());
        System.out.println(r2.id() + " -> " + r2.read());
        System.out.println(r2.id() + " -> " + r2.read());
    }
}