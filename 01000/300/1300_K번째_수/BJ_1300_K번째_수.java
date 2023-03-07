// https://www.acmicpc.net/problem/1300

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class BJ_1300_K번째_수 {
    public static class MatrixQuery {
        public static class Result {
            final long totalCount;
            final int boundCount;

            public Result(final long totalCount, final int boundCount) {
                this.totalCount = totalCount;
                this.boundCount = boundCount;
            }
        }

        public static Result perform(final int N, final long k, final long[] bound) {
            var y = k;
            var x = k;
            var numIndex = 0;
            var numElements = y;
            while (0 < y && x <= N) {
                if (bound != null) {
                    bound[numIndex++] = y * x;
                    if (1 < numIndex) {
                        bound[numIndex++] = y * x;
                    }
                }

                if (k * k <= y * (x + 1)) {
                    --y;
                } else {
                    if (++x <= N) {
                        numElements += y;
                    }
                }
            }

            return new Result(2 * numElements - 1 + (k - 1) * (k - 1), numIndex);
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final var N = Integer.parseInt(stdin.readLine());
            final var K = Long.parseLong(stdin.readLine());
            final var bound = new long[2 * N];

            var lb = 1;
            var ub = N + 1;
            var boundIndex = 0;
            while (lb < ub) {
                boundIndex = (lb + ub) / 2;
                final var qres = MatrixQuery.perform(N, boundIndex, null);
                if (qres.totalCount == K) {
                    break;
                }

                if (K <= qres.totalCount) {
                    ub = boundIndex;
                } else {
                    lb = ++boundIndex;
                }
            }

            final var qres = MatrixQuery.perform(N, boundIndex, bound);
            Arrays.sort(bound, 0, qres.boundCount);

            final var k = K - (qres.totalCount - qres.boundCount);
            System.out.println(bound[(int) k - 1]);
        }
    }
}
