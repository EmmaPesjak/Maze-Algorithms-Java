package support;

import java.util.Arrays;

/**
 * Custom-made Binary heap class to be used instead of priority-queue to improve time-complexity,
 * maintaining a dynamic array with O(log n) time complexity.
 */
public class BinaryHeap {
    private int[] heap;
    private int size;

    public BinaryHeap(int capacity) {
        heap = new int[capacity];
        size = 0;
    }

    public void insert(int value) {
        if (size == heap.length) {
            // If the heap is full, resize it to accommodate more elements
            heap = Arrays.copyOf(heap, heap.length * 2);
        }
        heap[size] = value;
        siftUp(size);
        size++;
    }

    public int removeMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        int minValue = heap[0];
        heap[0] = heap[size - 1];
        size--;
        siftDown(0);
        return minValue;
    }

    private void siftUp(int index) {
        int parentIndex = (index - 1) / 2;
        while (index > 0 && heap[index] < heap[parentIndex]) {
            swap(index, parentIndex);
            index = parentIndex;
            parentIndex = (index - 1) / 2;
        }
    }

    private void siftDown(int index) {
        int leftChildIndex = 2 * index + 1;
        int rightChildIndex = 2 * index + 2;
        int smallestIndex = index;

        if (leftChildIndex < size && heap[leftChildIndex] < heap[smallestIndex]) {
            smallestIndex = leftChildIndex;
        }

        if (rightChildIndex < size && heap[rightChildIndex] < heap[smallestIndex]) {
            smallestIndex = rightChildIndex;
        }

        if (smallestIndex != index) {
            swap(index, smallestIndex);
            siftDown(smallestIndex);
        }
    }

    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
}

