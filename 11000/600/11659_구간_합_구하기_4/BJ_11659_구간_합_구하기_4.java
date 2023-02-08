// https:///www.acmicpc.net/problem/11659

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class BJ_11659_구간_합_구하기_4 {
    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final int N, M;
        {
            final var tokens = stdin.readLine().split(" ");
            N = Integer.parseInt(tokens[0]);
            M = Integer.parseInt(tokens[1]);
        }

        final var dp = new long[N + 1];
        {
            final var tokens = new StringTokenizer(stdin.readLine());
            for (int i = 1; i <= N; ++i) {
                dp[i] += dp[i - 1] + Integer.parseInt(tokens.nextToken());
            }
        }

        for (var k = 0; k < M; ++k) {
            final int i, j;
            {
                final var tokens = stdin.readLine().split(" ");
                i = Integer.parseInt(tokens[0]);
                j = Integer.parseInt(tokens[1]);
            }

            stdout.write((dp[j] - dp[i - 1]) + "\n");
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
