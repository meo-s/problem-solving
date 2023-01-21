// https://www.acmicpc.net/problem/2133

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_2133_타일_채우기 {
    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = Integer.parseInt(stdin.readLine());
        final var dp = new long[N + 1];
        dp[0] = 1;
        for (var i = 0; i <= N - 2; ++i) {
            dp[i + 2] += dp[i] * 3;
            for (var j = 2; i + 2 * j <= N; ++j) {
                dp[i + 2 * j] += dp[i] * 2;
            }
        }

        stdout.write(dp[N] + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
