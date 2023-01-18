// https://www.acmicpc.net/problem/1328

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class BJ_1328_고층_빌딩 {
    private static int M = 1_000_000_007;

    public static long pow(long n, final int k) {
        n %= M;
        switch (k) {
            case 0:
                return 1;
            case 1:
                return n;
            case 2:
                return (n * n) % M;
            default:
                var nxn = pow(pow(n, k / 2), 2);
                return k % 2 == 1 ? (nxn * n) % M : nxn;
        }
    }

    public static class Factorial {
        public static long get(final int n) {
            if (dp[n] == 0) {
                dp[n] = (1 < n ? n * get(n - 1) : 1) % M;
            }

            return dp[n];
        }

        private static long[] dp = new long[100];
    }

    public static class Combination {
        public static long get(final int n, final int r) {
            final var divisor = (Factorial.get(r) * Factorial.get(n - r)) % M;
            return (Factorial.get(n) * pow(divisor, M - 2)) % M;
        }
    }

    public static class Permutation {
        public static long get(final int n, final int k) {
            if (dp[n][k] == 0) {
                dp[n][k] = (1 < k ? (get(n, k - 1) * (n - k + 1)) % M : (k == 1 ? n : 1));
            }

            return dp[n][k];
        }

        private static long[][] dp = new long[100][100];
    }

    public static class CityPlanner {
        public static void init() {
            for (var i = 0; i < dp.length; ++i) {
                for (var j = 0; j < dp[0].length; ++j) {
                    dp[i][j] = -1;
                }
            }
        }

        public static long simulate(final int n_buildings, final int n_viewables) {
            if (n_buildings == n_viewables) {
                return 1;
            }

            if (dp[n_buildings][n_viewables] < 0) {
                dp[n_buildings][n_viewables] = 0;
                if (0 < n_viewables && n_viewables < n_buildings) {
                    for (var i = n_viewables; i <= n_buildings; ++i) {
                        final var nPr = Permutation.get(n_buildings - 1, n_buildings - i);
                        dp[n_buildings][n_viewables] += (simulate(i - 1, n_viewables - 1) * nPr) % M;
                        dp[n_buildings][n_viewables] %= M;
                    }
                }
            }
            return dp[n_buildings][n_viewables];
        }

        private static long[][] dp = new long[100][100];
    }

    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));
        CityPlanner.init();

        int N, L, R;
        {
            final var tokens = new StringTokenizer(stdin.readLine());
            N = Integer.parseInt(tokens.nextToken());
            L = Integer.parseInt(tokens.nextToken());
            R = Integer.parseInt(tokens.nextToken());
        }

        var n_blueprints = 0L;
        for (var i = L - 1; i <= N - R; ++i) {
            final var nr = Combination.get(N - 1, i);
            n_blueprints += ((nr * CityPlanner.simulate(i, L - 1)) % M) * CityPlanner.simulate((N - 1) - i, R - 1);
            n_blueprints %= M;
        }

        stdout.write(n_blueprints + "\n");

        stdout.flush();
        stdout.close();
    }
}
