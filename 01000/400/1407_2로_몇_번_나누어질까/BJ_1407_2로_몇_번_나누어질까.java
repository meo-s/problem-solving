// https://www.acmicpc.net/problem/1407

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class BJ_1407_2로_몇_번_나누어질까 {
    public static class Two {
        public static long pow(int k) {
            if (k < 2) {
                return (k == 1) ? 2 : 1;
            }

            if (dp[k] == 0) {
                var nxn = pow(k / 2);
                nxn = nxn * nxn;
                nxn = (k % 2 == 1) ? nxn * 2 : nxn;
                dp[k] = nxn;
            }

            return dp[k];
        }

        private static long[] dp = new long[50];
    }

    public static class f {
        public f() {
            dp = new long[50];
            dp[0] = 1;
            for (var i = 1; i < dp.length; ++i) {
                dp[i] = dp[i - 1] * 2 + Two.pow(i - 1);
            }
        }

        public long apply(long x) {
            var divisorSum = 0L;
            for (var i = dp.length - 1; 0 <= i; --i) {
                if (Two.pow(i) <= x) {
                    x -= Two.pow(i);
                    divisorSum += dp[i];
                }
            }
            return divisorSum;
        }

        private long[] dp;
    };

    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        long A, B;
        {
            final var tokens = new StringTokenizer(stdin.readLine());
            A = Long.parseLong(tokens.nextToken());
            B = Long.parseLong(tokens.nextToken());
        }

        final var f = new f();
        stdout.write((f.apply(B) - f.apply(A - 1)) + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
