// https://www.acmicpc.net/problem/10986

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class BJ_10986_나머지_합 {
    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final int N, M;
        {
            final var tokens = new StringTokenizer(stdin.readLine());
            N = Integer.parseInt(tokens.nextToken());
            M = Integer.parseInt(tokens.nextToken());
        }

        final var tokens = new StringTokenizer(stdin.readLine());
        final var dp = new long[M];

        var sumOfRemainers = 0;
        var numZeroPairs = 0L;
        for (var i = 1; i <= N; ++i) {
            sumOfRemainers = (sumOfRemainers + Integer.parseInt(tokens.nextToken()) % M) % M;
            ++dp[sumOfRemainers];
            numZeroPairs = numZeroPairs + (dp[sumOfRemainers] - (sumOfRemainers != 0 ? 1 : 0));
        }

        stdout.write(numZeroPairs + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
