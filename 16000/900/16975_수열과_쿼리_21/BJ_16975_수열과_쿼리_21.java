// https://www.acmicpc.net/problem/16975

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_16975_수열과_쿼리_21 {
    public static class FenwickTree {
        private long[] tree;

        public FenwickTree(final int N) {
            tree = new long[N + 1];
        }

        public void update(int index, final long diff) {
            while (index < tree.length) {
                tree[index] += diff;
                index += index & -index;
            }
        }

        public long sum(final int end) {
            var sum = 0L;
            var index = end - 1;
            while (0 < index) {
                sum += tree[index];
                index -= index & -index;
            }

            return sum;
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final var N = Integer.parseInt(stdin.readLine());
            final var fenwickTree = new FenwickTree(N);
            {
                final var tokens = stdin.readLine().split(" ");
                fenwickTree.update(1, Integer.parseInt(tokens[0]));
                for (int i = 1; i < N; ++i) {
                    fenwickTree.update(i + 1, Integer.parseInt(tokens[i]) - Integer.parseInt(tokens[i - 1]));
                }
            }

            final var out = new StringBuilder();
            final var Q = Integer.parseInt(stdin.readLine());
            for (int i = 0; i < Q; ++i) {
                final var tokens = stdin.readLine().split(" ");
                switch (tokens[0]) {
                    case "1":
                        final var beg = Integer.parseInt(tokens[1]);
                        final var end = Integer.parseInt(tokens[2]) + 1;
                        final var x = Integer.parseInt(tokens[3]);
                        fenwickTree.update(beg, x);
                        if (end <= N) {
                            fenwickTree.update(end, -x);
                        }
                        break;

                    case "2":
                        final var index = Integer.parseInt(tokens[1]);
                        out.append(fenwickTree.sum(index + 1));
                        out.append('\n');
                        break;
                }
            }

            System.out.println(out);
        }
    }
}
