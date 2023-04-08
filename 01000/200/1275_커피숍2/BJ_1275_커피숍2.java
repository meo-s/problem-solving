// https://www.acmicpc.net/problem/1275

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_1275_커피숍2 {
    public static class FenwickTree {
        private final long[] tree;

        public FenwickTree(final int N) {
            tree = new long[N + 1];
        }

        public void update(int index, final long diff) {
            while (index < tree.length) {
                tree[index] += diff;
                index += index & -index;
            }
        }

        public long sum(int end) {
            var sum = 0L;
            var index = end - 1;
            while (0 < index) {
                sum += tree[index];
                index -= index & -index;
            }
            return sum;
        }

        public long sum(final int beg, final int end) {
            return sum(end) - sum(beg);
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final var out = new StringBuilder();

            final int N, Q;
            {
                final var tokens = stdin.readLine().split(" ");
                N = Integer.parseInt(tokens[0]);
                Q = Integer.parseInt(tokens[1]);
            }

            final var a = new long[N];
            final var fenwickTree = new FenwickTree(N);
            {
                final var tokens = stdin.readLine().split(" ");
                for (int i = 0; i < N; ++i) {
                    a[i] = Integer.parseInt(tokens[i]);
                    fenwickTree.update(i + 1, a[i]);
                }
            }

            for (int i = 0; i < Q; ++i) {
                final var tokens = stdin.readLine().split(" ");

                final var beg = Integer.parseInt(tokens[0]);
                final var end = Integer.parseInt(tokens[1]);
                out.append(fenwickTree.sum(Math.min(beg, end), Math.max(beg, end) + 1));
                out.append('\n');

                final var index = Integer.parseInt(tokens[2]);
                final var value = Integer.parseInt(tokens[3]);
                final var diff = value - a[index - 1];
                a[index - 1] += diff;
                fenwickTree.update(index, diff);
            }

            System.out.println(out);
        }
    }
}
