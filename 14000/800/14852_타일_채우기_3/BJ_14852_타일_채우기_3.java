// https://www.acmicpc.net/problem/14852

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_14852_타일_채우기_3 {
    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = Integer.parseInt(stdin.readLine());
        final var cumsum = new long[N + 1];
        final var dp = new long[N + 1];
        dp[0] = 1;
        dp[1] = 2;
        for (var i = 2; i <= N; ++i) {
            cumsum[i] = (3 <= i ? cumsum[i - 1] + dp[i - 3] : 0) % M;
            dp[i] = (2 * dp[i - 1] + 3 * dp[i - 2] + 2 * cumsum[i]) % M;
        }

        stdout.write(dp[N] + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }

    private static final int M = 1_000_000_007;
}
