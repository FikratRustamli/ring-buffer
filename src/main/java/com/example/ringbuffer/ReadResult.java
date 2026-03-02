package com.example.ringbuffer;

import java.util.Objects;
import java.util.Optional;

public final class ReadResult<E> {
    public enum Status { OK, EMPTY, MISSED }

    private final Status status;
    private final E item;
    private final long missedCount;

    private ReadResult(Status status, E item, long missedCount) {
        this.status = Objects.requireNonNull(status);
        this.item = item;
        this.missedCount = missedCount;
    }

    public static <E> ReadResult<E> ok(E item) { return new ReadResult<>(Status.OK, item, 0); }
    public static <E> ReadResult<E> empty() { return new ReadResult<>(Status.EMPTY, null, 0); }
    public static <E> ReadResult<E> missed(long missedCount, E itemOrNull) {
        return new ReadResult<>(Status.MISSED, itemOrNull, missedCount);
    }

    public Status status() { return status; }
    public Optional<E> item() { return Optional.ofNullable(item); }
    public long missedCount() { return missedCount; }

    @Override public String toString() {
        return "ReadResult{status=" + status + ", item=" + item + ", missedCount=" + missedCount + "}";
    }
}