// https://www.acmicpc.net/problem/17404

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class BJ_17404_RGB거리_2 {
    private static final int NUM_COLORS = 3;

    public static class StreetPainter {
        private static long paint(final int[][] costs, final long[][] dp) {
            final var N = costs.length;
            for (var i = 1; i <= N; ++i) {
                for (var curColor = 0; curColor < NUM_COLORS; ++curColor) {
                    for (var prevColor = 0; prevColor < NUM_COLORS; ++prevColor) {
                        if (curColor != prevColor) {
                            dp[i][curColor] = Math.min(dp[i][curColor], dp[i - 1][prevColor] + costs[i - 1][curColor]);
                        }
                    }
                }
            }

            return Arrays.stream(dp[N]).min().getAsLong();
        }

        public static long paint(final int[][] costs) {
            final var firstCosts = new int[] { costs[0][0], costs[0][1], costs[0][2] };
            final var dp = new long[costs.length + 1][NUM_COLORS];
            var minCost = Long.MAX_VALUE;
            for (var i = 0; i < 3; ++i) {
                for (var j = 1; j <= costs.length; ++j) {
                    Arrays.fill(dp[j], Integer.MAX_VALUE);
                }

                Arrays.fill(costs[0], Integer.MAX_VALUE);
                costs[0][i] = firstCosts[i];

                final var bannedLastCost = costs[costs.length - 1][i];
                costs[costs.length - 1][i] = Integer.MAX_VALUE;
                minCost = Math.min(minCost, paint(costs, dp));
                costs[costs.length - 1][i] = bannedLastCost;
            }

            return minCost;
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final var N = Integer.parseInt(stdin.readLine());
            final var costs = new int[N][3];
            for (var i = 0; i < N; ++i) {
                final var tokens = stdin.readLine().split(" ");
                for (var j = 0; j < 3; ++j) {
                    costs[i][j] = Integer.parseInt(tokens[j]);
                }
            }

            stdout.write(StreetPainter.paint(costs) + "\n");

            stdout.flush();
        }
    }
}
