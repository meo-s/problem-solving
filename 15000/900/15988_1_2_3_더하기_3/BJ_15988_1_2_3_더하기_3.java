// https://www.acmicpc.net/problem/15988

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_15988_1_2_3_더하기_3 {
    private static int M = 1_000_000_009;

    public static class OneTwoThreeCounter {
        public static long count(final int N) {
            if (dp[N] == 0) {
                dp[0] = 1;
                for (var i = 0; i < N; ++i) {
                    for (var di = 1; di <= 3; ++di) {
                        if (i + di <= N) {
                            dp[i + di] += dp[i];
                            dp[i + di] %= M;
                        }
                    }
                }
            }

            return dp[N];
        }

        private static long[] dp = new long[1_000_001];
    }

    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        OneTwoThreeCounter.count(1_000_000);
        var T = Integer.parseInt(stdin.readLine());
        while (0 < T--) {
            final var N = Integer.parseInt(stdin.readLine());
            stdout.write(OneTwoThreeCounter.count(N) + "\n");
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
