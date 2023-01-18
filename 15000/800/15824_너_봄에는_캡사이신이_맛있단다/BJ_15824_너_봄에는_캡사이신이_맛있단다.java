// https://www.acmicpc.net/problem/15824

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BJ_15824_너_봄에는_캡사이신이_맛있단다 {
    private static long M = 1_000_000_007;

    protected static class Two {
        public static long pow(int k) {
            if (dp[k] == 0) {
                switch (k) {
                    case 0:
                    case 1:
                    case 2:
                        dp[k] = (long)Math.pow(2, k);
                        break;
                    default:
                        var nxn = pow(k / 2);
                        nxn = (k % 2 == 1 ? nxn * nxn * 2 : nxn * nxn);
                        dp[k] = nxn % M;
                }
            }

            return dp[k];
        }

        private static long[] dp = new long[300000];
    }

    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = Integer.parseInt(stdin.readLine());

        final var scobills = new long[N];
        {
            final var tokens = new StringTokenizer(stdin.readLine());
            for (var i = 0; i < N; ++i) {
                scobills[i] = Integer.parseInt(tokens.nextToken());
            }
        }

        Arrays.sort(scobills);
        var juheonScobill = 0L;
        if (1 < N) {
            final var sum = Arrays.stream(scobills).sum();
            var curSum = (sum - scobills[0]) - (sum - scobills[N - 1]);
            for (var i = 0; i < N - 1; ++i) {
                juheonScobill = (juheonScobill + ((curSum % M) * Two.pow(i))) % M;
                curSum += scobills[(N - 1) - (i + 1)] - scobills[i + 1];
            }
        }

        stdout.write(juheonScobill + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
