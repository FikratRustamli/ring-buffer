package com.example.ringbuffer;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public final class RingBuffer<E> {
    private final int capacity;
    private final Slot<E>[] slots;

    private volatile long publishedSeq = -1;
    private final AtomicInteger readerCounter = new AtomicInteger(0);

    @SuppressWarnings("unchecked")
    public RingBuffer(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity must be > 0");
        this.capacity = capacity;
        this.slots = (Slot<E>[]) new Slot<?>[capacity];
        for (int i = 0; i < capacity; i++) slots[i] = new Slot<>();
    }

    public int capacity() { return capacity; }

    // Single writer only
    public void write(E item) {
        Objects.requireNonNull(item, "item");
        long nextSeq = publishedSeq + 1;
        int idx = (int) (nextSeq % capacity);

        Slot<E> slot = slots[idx];
        slot.value = item;
        slot.seq = nextSeq;
        publishedSeq = nextSeq;
    }

    public RingBufferReader<E> createReader() {
        return new RingBufferReader<>(this, "reader-" + readerCounter.incrementAndGet());
    }

    long publishedSeq() { return publishedSeq; }

    long minAvailableSeq() {
        long pub = publishedSeq;
        long min = pub - capacity + 1;
        return Math.max(0, min);
    }

    Slot<E> slotAt(long seq) {
        return slots[(int) (seq % capacity)];
    }
}