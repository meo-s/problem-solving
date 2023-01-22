// https://www.acmicpc.net/problem/11055

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BJ_11055_가장_큰_증가_부분_수열 {
    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = Integer.parseInt(stdin.readLine());
        final var seq = new int[N];
        {
            final var tokens = new StringTokenizer(stdin.readLine());
            for (var i = 0; i < N; ++i) {
                seq[i] = Integer.parseInt(tokens.nextToken());
            }
        }

        final var dp = new int[N];
        dp[0] = seq[0];
        for (var i = 1; i < N; ++i) {
            var maxIndex = i;
            for (var j = 0; j < i; ++j) {
                if (seq[j] < seq[i] && dp[maxIndex] < dp[j]) {
                    maxIndex = j;
                }
            }

            dp[i] = dp[maxIndex] + seq[i];
        }

        stdout.write(Arrays.stream(dp).max().getAsInt() + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
