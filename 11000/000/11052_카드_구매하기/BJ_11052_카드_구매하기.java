// https://www.acmicpc.net/problem/11052

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class BJ_11052_카드_구매하기 {
    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = Integer.parseInt(stdin.readLine());

        final var cards = new int[N];
        {
            final var tokens = new StringTokenizer(stdin.readLine());
            for (var i = 0; i < N; ++i) {
                cards[i] = Integer.parseInt(tokens.nextToken());
            }
        }

        final var dp = new int[N];
        for (var i = 0; i < N; ++i) {
            dp[i] = cards[i];
        }

        for (var i = 0; i < N; ++i) {
            for (var j = 1; j <= N; ++j) {
                if (i + j < N) {
                    dp[i + j] = Math.max(dp[i + j], dp[i] + cards[j - 1]);
                }
            }
        }

        stdout.write(dp[N - 1] + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
