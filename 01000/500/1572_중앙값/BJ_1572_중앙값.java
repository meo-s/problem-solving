// https://www.acmicpc.net/problem/1572

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class BJ_1572_중앙값 {
    private static short IN_MIN_HEAP = 1;
    private static short IN_MAX_HEAP = 2;

    public static class Number implements Comparable<Number> {
        public Number(final int value, final int index) {
            this.value = value;
            this.index = index;
        }

        @Override
        public int compareTo(final Number rhs) {
            if (value == rhs.value) {
                return 0;
            }
            return value < rhs.value ? -1 : 1;
        }

        public int value;
        public int index;
    }

    public static class NumberHeap {
        public NumberHeap(final PriorityQueue<Number> heap) {
            this.heap = heap;
            this.numExpireds = 0;
        }

        public void add(final Number n) {
            this.heap.add(n);
        }

        public Number peek(final int minOffset) {
            while (0 < heap.size() && heap.peek().index < minOffset) {
                heap.poll();
                --numExpireds;
            }
            return heap.peek();
        }

        public Number poll(final int minOffset) {
            while (0 < heap.size() && heap.peek().index < minOffset) {
                heap.poll();
                --numExpireds;
            }
            return heap.poll();
        }

        public int size() {
            return heap.size() - numExpireds;
        }

        public void expireOnce() {
            ++numExpireds;
        }

        private PriorityQueue<Number> heap;
        private int numExpireds;
    }

    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final int N, K;
        {
            final var tokens = new StringTokenizer(stdin.readLine());
            N = Integer.parseInt(tokens.nextToken());
            K = Integer.parseInt(tokens.nextToken());
        }

        final var minHeap = new NumberHeap(new PriorityQueue<Number>());
        final var maxHeap = new NumberHeap(new PriorityQueue<Number>(Collections.reverseOrder()));
        final var numberState = new short[N];
        var sumOfMidValues = BigInteger.ZERO;
        Number mid = null;
        for (int i = 0; i < N; ++i) {
            if (0 <= i - K && numberState[i - K] != 0) {
                (numberState[i - K] == IN_MAX_HEAP ? maxHeap : minHeap).expireOnce();
            }

            if (mid != null && mid.index <= i - K) {
                mid = null;
                if (0 < maxHeap.size() + minHeap.size()) {
                    if (minHeap.size() <= maxHeap.size()) {
                        mid = maxHeap.poll(i - K + 1);
                        numberState[mid.index] = 0;
                    } else {
                        mid = minHeap.poll(i - K + 1);
                        numberState[mid.index] = 0;
                    }
                }
            }

            final var n = new Number(Integer.parseInt(stdin.readLine()), i);
            if (mid == null) {
                mid = n;
            } else {
                if (n.compareTo(mid) <= 0) {
                    maxHeap.add(n);
                    numberState[n.index] = IN_MAX_HEAP;
                } else {
                    minHeap.add(n);
                    numberState[n.index] = IN_MIN_HEAP;
                }

                while (minHeap.size() < maxHeap.size() + 1) {
                    minHeap.add(mid);
                    numberState[mid.index] = IN_MIN_HEAP;

                    mid = maxHeap.poll(i - K + 1);
                    numberState[mid.index] = 0;
                }
                while (maxHeap.size() + 1 < minHeap.size()) {
                    maxHeap.add(mid);
                    numberState[mid.index] = IN_MAX_HEAP;

                    mid = minHeap.poll(i - K + 1);
                    numberState[mid.index] = 0;
                }
            }

            if (K - 1 <= i) {
                sumOfMidValues = sumOfMidValues.add(BigInteger.valueOf(mid.value));
            }
        }

        stdout.write(sumOfMidValues + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
