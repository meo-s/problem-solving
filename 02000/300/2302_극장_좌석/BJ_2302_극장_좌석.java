// https://www.acmicpc.net/problem/2302

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_2302_극장_좌석 {
    public static int solve(final int N, final boolean[] isFixed) {
        if (0 < N && dp[N] == 0) {
            dp[N] += solve(N - 1, isFixed);
            dp[N] += (N <= 1 || (isFixed[N] || isFixed[N - 1]) ? 0 : solve(N - 2, isFixed));
        }
        return 0 < N ? dp[N] : 1;
    }

    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = Integer.parseInt(stdin.readLine());
        final var isFixed = new boolean[N + 1];
        {
            final var M = Integer.parseInt(stdin.readLine());
            for (var i = 0; i < M; ++i) {
                isFixed[Integer.parseInt(stdin.readLine())] = true;
            }
        }

        stdout.write(solve(N, isFixed) + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }

    private static int[] dp = new int[41];
}
