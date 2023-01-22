// https://www.acmicpc.net/problem/1520

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BJ_1520_내리막_길 {
    public static class Solver {
        private int search(final int[][] heights, final int y, final int x) {
            if (y + x == 0) {
                return 1;
            }
            if (dp[y][x] < 0) {
                dp[y][x] = 0;
                for (var i = 0; i < dy.length; ++i) {
                    if (0 <= y + dy[i] && y + dy[i] < dp.length && 0 <= x + dx[i] && x + dx[i] < dp[0].length) {
                        if (heights[y][x] < heights[y + dy[i]][x + dx[i]]) {
                            dp[y][x] += search(heights, y + dy[i], x + dx[i]);
                        }
                    }
                }
            }
            return dp[y][x];
        }

        public int solve(final int[][] heights) {
            final var H = heights.length;
            final var W = heights[0].length;

            dp = new int[H][W];
            for (final var row : dp) {
                Arrays.fill(row, -1);
            }

            search(heights, H - 1, W - 1);
            return dp[H - 1][W - 1];
        }

        private int[][] dp;
        static final int[] dy = new int[] { 1, -1, 0, 0 };
        static final int[] dx = new int[] { 0, 0, 1, -1 };
    }

    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        int H, W;
        {
            final var tokens = new StringTokenizer(stdin.readLine());
            H = Integer.parseInt(tokens.nextToken());
            W = Integer.parseInt(tokens.nextToken());
        }

        final var heights = new int[H][W];
        for (var h = 0; h < H; ++h) {
            final var tokens = new StringTokenizer(stdin.readLine());
            for (var w = 0; w < W; ++w) {
                heights[h][w] = Integer.parseInt(tokens.nextToken());
            }
        }

        stdout.write(new Solver().solve(heights) + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
