// https://www.acmicpc.net/problem/1904

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_1904_01타일 {
    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = Integer.parseInt(stdin.readLine());
        final var dp = new int[N + 1];
        dp[0] = 1;
        for (var i = 0; i < dp.length; ++i) {
            for (var di = 1; di <= 2; ++di) {
                if (i + di <= N) {
                    dp[i + di] += dp[i];
                    dp[i + di] %= 15746;
                }
            }
        }

        stdout.write(dp[N] + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
