// https://www.acmicpc.net/problem/4883

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BJ_4883_삼각_그래프 {
    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        var test_case = 1;
        for (int N; 0 < (N = Integer.parseInt(stdin.readLine()));) {
            final var costs = new int[N][3];
            for (var row = 0; row < N; ++row) {
                final var tokens = new StringTokenizer(stdin.readLine());
                for (var col = 0; col < 3; ++col) {
                    costs[row][col] = Integer.parseInt(tokens.nextToken());
                }
            }

            final var dp = new int[N][3];
            for (final var dpRow : dp) {
                Arrays.fill(dpRow, 1_000_001);
            }

            dp[0][1] = costs[0][1];
            for (var row = 0; row < N; ++row) {
                for (var col = 0; col < 3; ++col) {
                    dp[row][col] = (0 < col
                            ? Math.min(dp[row][col], dp[row][col - 1] + costs[row][col])
                            : dp[row][col]);
                    if (0 < row) {
                        dp[row][col] = Math.min(dp[row][col], dp[row - 1][col] + costs[row][col]);
                        dp[row][col] = (0 < col
                                ? Math.min(dp[row][col], dp[row - 1][col - 1] + costs[row][col])
                                : dp[row][col]);
                        dp[row][col] = (col < 2
                                ? Math.min(dp[row][col], dp[row - 1][col + 1] + costs[row][col])
                                : dp[row][col]);
                    }
                }
            }

            stdout.write(String.format("%d. %d\n", test_case++, dp[N - 1][1]));
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
