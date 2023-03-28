// https://www.acmicpc.net/problem/1149

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class BJ_1149_RGB거리 {
    public static <T> T swap(final T lhs, final T rhs) {
        return lhs;
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int N = Integer.parseInt(stdin.readLine());
            final int[][] costs = new int[N][3];
            for (int i = 0; i < N; ++i) {
                final String[] tokens = stdin.readLine().split(" ");
                for (int c = 0; c < 3; ++c) {
                    costs[i][c] = Integer.parseInt(tokens[c]);
                }
            }

            final int[][] dp = new int[2][3];
            for (int i = 0; i < N; ++i) {
                for (int c = 0; c < 3; ++c) {
                    dp[0][c] = swap(dp[1][c], dp[1][c] = Integer.MAX_VALUE);
                }

                for (int cc = 0; cc < 3; ++cc) {  // abbreviation for current color
                    for (int pc = 0; pc < 3; ++pc) {  // abbreviation for previous color
                        if (pc != cc) {
                            dp[1][cc] = Math.min(dp[1][cc], dp[0][pc] + costs[i][cc]);
                        }
                    }
                }
            }

            System.out.println(Arrays.stream(dp[1]).min().getAsInt());
        }
    }
}
