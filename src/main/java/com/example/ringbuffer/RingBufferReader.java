package com.example.ringbuffer;

import com.example.ringbuffer.ReadResult.Status;

public final class RingBufferReader<E> {
    private final RingBuffer<E> buffer;
    private final String id;
    private long nextSeq = 0;

    RingBufferReader(RingBuffer<E> buffer, String id) {
        this.buffer = buffer;
        this.id = id;
    }

    public String id() { return id; }
    public long position() { return nextSeq; }

    public ReadResult<E> read() {
        while (true) {
            long pub = buffer.publishedSeq();
            if (pub < 0 || nextSeq > pub) return ReadResult.empty();

            long minAvail = buffer.minAvailableSeq();
            if (nextSeq < minAvail) {
                long missed = minAvail - nextSeq;
                nextSeq = minAvail;

                ReadResult<E> rr = tryReadCurrent();
                if (rr.status() == Status.OK) {
                    return ReadResult.missed(missed, rr.item().orElse(null));
                }
                continue;
            }

            ReadResult<E> rr = tryReadCurrent();
            if (rr.status() != Status.EMPTY) return rr;
        }
    }

    private ReadResult<E> tryReadCurrent() {
        long expected = nextSeq;
        if (expected > buffer.publishedSeq()) return ReadResult.empty();

        Slot<E> slot = buffer.slotAt(expected);
        if (slot.seq != expected) return ReadResult.empty();

        E value = slot.value;
        nextSeq = expected + 1;
        return ReadResult.ok(value);
    }
}