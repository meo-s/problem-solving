// https://www.acmicpc.net/problem/2291

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class BJ_2291_Sequence {
    public static class SequenceIndexer {
        private static long count(final int n, final int r) {
            if (dp[n][r] < 0) {
                if (n <= 1) {
                    dp[n][r] = 1;
                } else {
                    dp[n][r] = 0;
                    for (int i = 0; 0 <= r - i * n; ++i) {
                        dp[n][r] += count(n - 1, r - i * n);
                    }
                }
            }

            return dp[n][r];
        }

        private static String get(final int n, int m, int k, int digit) {
            while (count(n - 1, m) < k) {
                ++digit;
                k -= count(n - 1, m);
                m -= n;
            }

            return 1 < n ? String.format("%d %s", digit, get(n - 1, m, k, digit)) : Integer.toString(digit + m);
        }

        public static String get(final int n, int m, int k) {
            return get(n, m - n, k, 1);
        }

        private static final long[][] dp;
        static {
            dp = new long[11][221];
            for (final var dpRow : dp) {
                Arrays.fill(dpRow, -1);
            }
        }
    }

    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final int N, M, K;
        {
            final var tokens = stdin.readLine().split(" ");
            N = Integer.parseInt(tokens[0]);
            M = Integer.parseInt(tokens[1]);
            K = Integer.parseInt(tokens[2]);
        }

        stdout.write(SequenceIndexer.get(N, M, K) + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
