// https://www.acmicpc.net/problem/2579

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_2579_계단_오르기 {
    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = Integer.parseInt(stdin.readLine());
        final var stairs = new int[N + 1];
        for (var i = 1; i <= N; ++i) {
            stairs[i] = Integer.parseInt(stdin.readLine());
        }

        final var dp = new int[N + 1];
        for (var i = 1; i <= Math.min(2, N); ++i) {
            dp[i] = dp[i - 1] + stairs[i];
        }
        for (var i = 3; i <= N; ++i) {
            dp[i] = Math.max(dp[i - 2], dp[i - 3] + stairs[i - 1]) + stairs[i];
        }

        stdout.write(dp[N] + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
