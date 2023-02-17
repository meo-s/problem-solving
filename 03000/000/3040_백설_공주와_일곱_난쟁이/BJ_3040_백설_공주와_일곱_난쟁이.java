// https://www.acmicpc.net/problem/3040

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.stream.IntStream;

public class BJ_3040_백설_공주와_일곱_난쟁이 {
    public static class Combination {
        @FunctionalInterface
        public static interface Callback {
            boolean consume(final int bitmask);
        }

        private final int n;
        private final int r;
        private final Callback callback;

        private Combination(final int n, final int r, final Callback callback) {
            this.n = n;
            this.r = r;
            this.callback = callback;
        }

        public boolean pick(final int offset, final int depth, final int bitmask) {
            if (depth == r) {
                return callback.consume(bitmask);
            } else {
                boolean cont = true;
                for (int i = offset; cont && i < n; ++i) {
                    cont = cont && pick(i + 1, depth + 1, bitmask | (1 << i));
                }

                return cont;
            }
        }

        public void pick() {
            pick(0, 0, 0);
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {

            final int[] nums = new int[9];
            for (int i = 0; i < nums.length; ++i) {
                nums[i] = Integer.parseInt(stdin.readLine());
            }

            new Combination(9, 7, bitmask -> {
                final int sumOfNums = IntStream.range(0, 10)
                        .filter(i -> (bitmask & (1 << i)) != 0)
                        .map(i -> nums[i])
                        .sum();

                if (sumOfNums != 100) {
                    return true;
                }

                IntStream.range(0, 10)
                        .filter(i -> (bitmask & (1 << i)) != 0)
                        .map(i -> nums[i])
                        .forEach(n -> {
                            try {
                                stdout.write(n + "\n");
                            } catch (IOException e) {
                            }
                        });
                return false;
            }).pick();

            stdout.flush();
        }
    }
}
