// https://www.acmicpc.net/problem/9084

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class BJ_9084_동전 {
    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        var T = Integer.parseInt(stdin.readLine());
        while (0 < T--) {
            final int N = Integer.parseInt(stdin.readLine());
            final var coins = new int[N];
            {
                final var tokens = new StringTokenizer(stdin.readLine());
                for (var i = 0; i < N; ++i) {
                    coins[i] = Integer.parseInt(tokens.nextToken());
                }
            }

            final var GOAL = Integer.parseInt(stdin.readLine());
            final var dp = new int[GOAL + 1];
            dp[0] = 1;
            for (final var coin : coins) {
                for (var i = 0; i <= GOAL - coin; ++i) {
                    if (0 < dp[i]) {
                        dp[i + coin] += dp[i];
                    }
                }
            }

            stdout.write(dp[GOAL] + "\n");
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
