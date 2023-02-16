// https://www.acmicpc.net/problem/17406

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.stream.IntStream;

public class BJ_17406_배열_돌리기_4 {
    private static int minArrayValue = Integer.MAX_VALUE;

    private static int[] dy = { 0, 1, 0, -1 };
    private static int[] dx = { 1, 0, -1, 0 };

    public static <T> T swap(final T lhs, final T rhs) {
        return lhs;
    }

    public static int[][] clone(final int[][] a) {
        final int[][] ret = new int[a.length][];
        for (int i = 0; i < a.length; ++i) {
            ret[i] = a[i].clone();
        }
        return ret;
    }

    public static void turnArray(final int[][] a, final int r, final int c, final int s) {
        int y_lb = r - s - 1;
        int x_lb = c - s - 1;
        int y_ub = r + s;
        int x_ub = c + s;

        while (1 < y_ub - y_lb && 1 < x_ub - x_lb) {
            int y = y_lb, x = x_lb;
            int v = a[y + 1][x];
            int dir = 0;
            for (;;) {
                v = swap(a[y][x], a[y][x] = v);

                final int ny = y + dy[dir];
                final int nx = x + dx[dir];
                if (nx == x_lb && ny == y_lb) {
                    break;
                }
                if (ny < y_lb || y_ub <= ny || nx < x_lb || x_ub <= nx) {
                    ++dir;
                }

                y += dy[dir];
                x += dx[dir];
            }

            ++y_lb;
            --y_ub;
            ++x_lb;
            --x_ub;
        }
    }

    public static int getArrayValue(int[][] a) {
        return IntStream.range(0, a.length).map(i -> Arrays.stream(a[i]).sum()).min().getAsInt();
    }

    public static class NextPermutation {
        @FunctionalInterface
        interface Callback {
            void consume(final int[] permutation);
        }

        private final int[] elements;
        private Callback callback;

        private NextPermutation(final int[] elements, final Callback callback) {
            this.elements = elements;
            this.callback = callback;
        }

        public boolean next() {
            callback.consume(elements);

            final int L = elements.length;
            int pivot = L - 2;
            for (; 0 <= pivot; --pivot) {
                if (elements[pivot] < elements[pivot + 1]) {
                    break;
                }
            }

            if (0 <= pivot) {
                int i = L - 1;
                for (; pivot < i; --i) {
                    if (elements[pivot] < elements[i]) {
                        break;
                    }
                }

                elements[i] = swap(elements[pivot], elements[pivot] = elements[i]);
                for (int j = 1; pivot + j < L - j; ++j) {
                    elements[L - j] = swap(elements[pivot + j], elements[pivot + j] = elements[L - j]);
                }
            }

            return 0 <= pivot;
        }

        public static NextPermutation of(final int[] elements, final Callback callback) {
            return new NextPermutation(elements, callback);
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {

            final int H, W, K;
            {
                final String[] tokens = stdin.readLine().split(" ");
                H = Integer.parseInt(tokens[0]);
                W = Integer.parseInt(tokens[1]);
                K = Integer.parseInt(tokens[2]);
            }

            final int[][] array = new int[H][W];
            for (int y = 0; y < H; ++y) {
                final String[] tokens = stdin.readLine().split(" ");
                for (int x = 0; x < W; ++x) {
                    array[y][x] = Integer.parseInt(tokens[x]);
                }
            }

            final int[][] ops = new int[K][3];
            for (int i = 0; i < K; ++i) {
                final String[] tokens = stdin.readLine().split(" ");
                final int r = Integer.parseInt(tokens[0]);
                final int c = Integer.parseInt(tokens[1]);
                final int s = Integer.parseInt(tokens[2]);
                ops[i][0] = r;
                ops[i][1] = c;
                ops[i][2] = s;
            }

            final NextPermutation np = NextPermutation.of(
                    IntStream.range(0, K).toArray(),
                    indices -> {
                        final int[][] a = clone(array);
                        for (final int i : indices) {
                            turnArray(a, ops[i][0], ops[i][1], ops[i][2]);
                        }

                        minArrayValue = Math.min(minArrayValue, getArrayValue(a));
                    });
            while (np.next()) {
                ;
            }

            stdout.write(minArrayValue + "\n");
        }
    }
}