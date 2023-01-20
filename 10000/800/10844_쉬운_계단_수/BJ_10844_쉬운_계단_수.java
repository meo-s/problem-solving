// https://www.acmicpc.net/problem/10844

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class BJ_10844_쉬운_계단_수 {
    private static final int M = 1_000_000_000;

    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = Integer.parseInt(stdin.readLine());
        final var dp = new long[N][10];
        Arrays.fill(dp[0], 1);
        dp[0][0] = 0;
        for (var i = 1; i < N; ++i) {
            for (var j = 0; j < 10; ++j) {
                dp[i][j] += (1 <= j ? dp[i - 1][j - 1] : 0);
                dp[i][j] += (j <= 8 ? dp[i - 1][j + 1] : 0);
                dp[i][j] %= M;
            }
        }

        stdout.write(Arrays.stream(dp[N - 1]).sum() % M + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
