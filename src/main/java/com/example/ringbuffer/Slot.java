package com.example.ringbuffer;

final class Slot<E> {
    volatile long seq = -1;
    volatile E value;
}