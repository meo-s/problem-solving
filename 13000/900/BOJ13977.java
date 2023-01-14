// https://www.acmicpc.net/problem/13977

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BOJ13977 {
    final static int P = 1_000_000_007;

    public static class Factorial {
        private static long[] cache = new long[4000001];

        public static long get(int n) {
            if (n == 0) {
                return 1;
            }
            if (cache[n] == 0) {
                cache[n] = (n * Factorial.get(n - 1)) % P;
            }
            return cache[n];
        }
    }

    public static long pow(long n, int k) {
        n %= P;
        switch (k) {
            case 0:
                return 1;
            case 1:
                return n;
            case 2:
                return (n * n) % P;
            default:
                var nxn = pow(pow(n, k / 2), 2);
                nxn = (k % 2 == 1) ? (nxn * n) % P : nxn;
                return nxn;
        }
    }

    public static void main(String[] args) throws IOException {
        final var stdin = new BufferedReader(new InputStreamReader(System.in));
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));

        var n_test_cases = Integer.parseInt(stdin.readLine());
        while (0 < n_test_cases--) {
            final var tokens = stdin.readLine().split(" ");
            final var n = Integer.parseInt(tokens[0]);
            final var r = Integer.parseInt(tokens[1]);
            final var denominator = (Factorial.get(n - r) * Factorial.get(r)) % P;
            final var nr = (Factorial.get(n) * pow(denominator, P - 2)) % P;
            stdout.write(nr + "\n");
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
};
