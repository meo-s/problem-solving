// https://www.acmicpc.net/problem/11053

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class BJ_11053_가장_긴_증가하는_부분_수열 {
    public static int lowerBound(final int[] a, final int n, int lb, int ub) {
        while (lb < ub) {
            final var mid = (lb + ub - 1) / 2;
            if (a[mid] < n) {
                lb = mid + 1;
            } else {
                ub = mid;
            }
        }

        return lb;
    }

    public static int LIS(final int[] seq) {
        var max_len = 1;
        final var dp = new int[seq.length];
        dp[0] = seq[0];
        for (var i = 1; i < seq.length; ++i) {
            if (dp[max_len - 1] < seq[i]) {
                dp[max_len++] = seq[i];
            } else {
                dp[lowerBound(dp, seq[i], 0, max_len - 1)] = seq[i];
            }
        }

        return max_len;
    }

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

        stdout.write(LIS(seq) + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
