// https://www.acmicpc.net/problem/1922

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BJ_1912_연속합 {
    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = Integer.parseInt(stdin.readLine());
        final var tokens = new StringTokenizer(stdin.readLine());
        final var dp = new int[N];
        dp[0] = Integer.parseInt(tokens.nextToken());
        for (var i = 1; i < N; ++i) {
            final var n = Integer.parseInt(tokens.nextToken());
            dp[i] = Math.max(dp[i - 1] + n, n);
        }

        stdout.write(Arrays.stream(dp).max().getAsInt() + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
