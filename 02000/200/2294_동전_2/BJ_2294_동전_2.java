// https://www.acmicpc.net/problem/2294

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.StringTokenizer;

public class BJ_2294_동전_2 {
    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        int N, K;
        {
            final var tokens = new StringTokenizer(stdin.readLine());
            N = Integer.parseInt(tokens.nextToken());
            K = Integer.parseInt(tokens.nextToken());
        }

        final var coins = new HashSet<Integer>();
        for (var i = 0; i < N; ++i) {
            final var coin = Integer.parseInt(stdin.readLine());
            if (coin <= K) {
                coins.add(coin);
            }
        }

        final var dp = new int[K + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        for (final var coin : coins) {
            dp[coin] = 1;
        }

        for (var i = 1; i < K; ++i) {
            if (dp[i] < Integer.MAX_VALUE) {
                for (final var coin : coins) {
                    if (i + coin <= K) {
                        dp[i + coin] = Math.min(dp[i + coin], dp[i] + 1);
                    }
                }
            }
        }

        stdout.write((dp[K] < Integer.MAX_VALUE ? dp[K] : -1) + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
