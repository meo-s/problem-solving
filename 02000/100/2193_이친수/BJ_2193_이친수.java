// https://www.acmicpc.net/problem/2193

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_2193_이친수 {
    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = Integer.parseInt(stdin.readLine());
        final var dp = new long[N][2];
        dp[0][0] = 0;
        dp[0][1] = 1;
        for (var i = 1; i < N; ++i) {
            dp[i][0] = dp[i - 1][0] + dp[i - 1][1];
            dp[i][1] = dp[i - 1][0];
        }

        stdout.write((dp[N - 1][0] + dp[N - 1][1]) + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
