// https://www.acmicpc.net/problem/2482

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class BJ_2482_색상환 {
    private static int M = 1_000_000_003;

    private static class ColorPicker {
        private static long[][] dp;

        static {
            dp = new long[1001][1001];
            for (final var dpRow : dp) {
                Arrays.fill(dpRow, -1);
            }
        }

        private static long _pick(final int N, final int K) {
            if (N < 0 || N < 2 * K - 1) {
                return 0;
            }

            if (dp[N][K] < 0) {
                if (N == 0 || K == 0 || N == 2 * K - 1) {
                    dp[N][K] = 1;
                } else {
                    dp[N][K] = (_pick(N - 1, K) + _pick(N - 2, K - 1)) % M;
                }
            }

            return dp[N][K];
        }

        public static long pick(final int N, final int K) {
            return (_pick(N - 1, K) + _pick(N - 3, K - 1)) % M;
        }
    }

    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = Integer.parseInt(stdin.readLine());
        final var K = Integer.parseInt(stdin.readLine());
        stdout.write(ColorPicker.pick(N, K) + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
