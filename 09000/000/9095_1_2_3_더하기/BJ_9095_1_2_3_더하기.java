// https://www.acmicpc.net/problem/9095

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_9095_1_2_3_더하기 {
    public static int search(final int N) {
        final var dp = new int[N + 1];
        for (var i = 1; i < Math.min(4, dp.length); ++i) {
            dp[i] = 1;
        }
        for (var i = 1; i < N; ++i) {
            for (var di = 1; di <= 3; ++di) {
                if (i + di <= N) {
                    dp[i + di] += dp[i];
                }
            }
        }

        return dp[N];
    }

    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        var t = Integer.parseInt(stdin.readLine());
        while (0 < t--) {
            stdout.write(search(Integer.parseInt(stdin.readLine())) + "\n");
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
