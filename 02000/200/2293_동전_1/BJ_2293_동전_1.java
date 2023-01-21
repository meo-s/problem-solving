// https://www.acmicpc.net/problem/2293

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BJ_2293_동전_1 {
    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        int N, K;
        {
            final var tokens = new StringTokenizer(stdin.readLine());
            N = Integer.parseInt(tokens.nextToken());
            K = Integer.parseInt(tokens.nextToken());
        }

        final var coins = new int[N];
        for (var i = 0; i < N; ++i) {
            coins[i] = Integer.parseInt(stdin.readLine());
        }

        Arrays.sort(coins);
        
        final var dp = new long[K + 1];
        dp[0] = 1;
        for (final var coin : coins) {
            for (var i = 0; i < dp.length; ++i) {
                if (0 < dp[i] && i + coin <= K) {
                    dp[i + coin] += dp[i];
                }
            }
        }

        stdout.write(dp[K] + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
