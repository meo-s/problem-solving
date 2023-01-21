// https://www.acmicpc.net/problem/1890

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.StringTokenizer;


public class BJ_1890_점프 {
    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = Integer.parseInt(stdin.readLine());
        final var dp = new BigInteger[N][N];
        for (var h = 0; h < N; ++h) {
            Arrays.fill(dp[h], BigInteger.ZERO);
        }

        dp[0][0] = BigInteger.ONE;
        for (var h = 0; h < N; ++h) {
            final var tokens = new StringTokenizer(stdin.readLine());
            for (var w = 0; w < N; ++w) {
                final var delta = Integer.parseInt(tokens.nextToken());
                if (delta == 0) {
                    continue;
                }
                if (dp[h][w].equals(BigInteger.ZERO)) {
                    continue;
                }
                if (h + delta < N) {
                    dp[h + delta][w] = dp[h + delta][w].add(dp[h][w]);
                }
                if (w + delta < N) {
                    dp[h][w + delta] = dp[h][w + delta].add(dp[h][w]);
                }
            }
        }

        stdout.write(dp[N - 1][N - 1] + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
