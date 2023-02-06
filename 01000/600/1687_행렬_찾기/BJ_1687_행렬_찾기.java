// https://www.acmicpc.net/problem/1687

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_1687_행렬_찾기 {
    public static int findMaxArea(final int[][] dp, final int y, final int x) {
        var maxArea = 0;

        var dy = 0;
        var w = Integer.MAX_VALUE;
        while (0 <= y - dy && 0 < dp[y - dy][x]) {
            w = Math.min(w, dp[y - dy][x]);
            maxArea = Math.max(maxArea, (dy + 1) * w);
            ++dy;
        }

        return maxArea;
    }

    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final int H, W;
        {
            final var tokens = stdin.readLine().split(" ");
            H = Integer.parseInt(tokens[0]);
            W = Integer.parseInt(tokens[1]);
        }

        final var m = new byte[H][W];
        for (var y = 0; y < H; ++y) {
            final var colVec = stdin.readLine();
            for (var x = 0; x < W; ++x) {
                m[y][x] = (byte) (colVec.charAt(x) - '0');
            }
        }

        var maxArea = 0;
        final var dp = new int[H][W];
        for (var y = 0; y < H; ++y) {
            for (var x = 0; x < W; ++x) {
                if (m[y][x] == 0) {
                    dp[y][x] = (0 < x ? dp[y][x - 1] + 1 : 1);
                    maxArea = Math.max(maxArea, findMaxArea(dp, y, x));
                }
            }
        }

        stdout.write(maxArea + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
