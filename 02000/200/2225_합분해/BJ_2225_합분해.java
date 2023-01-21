// https://www.acmicpc.net/problem/2225

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BJ_2225_합분해 {
    private static final int M = 1_000_000_000;

    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        int N, K;
        {
            final var tokens = new StringTokenizer(stdin.readLine());
            N = Integer.parseInt(tokens.nextToken());
            K = Integer.parseInt(tokens.nextToken());
        }

        final var dp = new long[N][K];
        Arrays.fill(dp[0], 1);
        for (var i = 1; i < N; ++i) {
            dp[i][0] = Arrays.stream(dp[i - 1]).sum() % M;
            for (var j = 1; j < K; ++j) {
                dp[i][j] = (dp[i][j - 1] - dp[i - 1][j - 1] + M) % M;
            }
        }

        stdout.write(Arrays.stream(dp[N - 1]).sum() % M + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
