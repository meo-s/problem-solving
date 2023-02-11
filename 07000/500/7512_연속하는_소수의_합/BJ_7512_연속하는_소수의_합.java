// https://www.acmicpc.net/problem/7512

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.stream.IntStream;

public class BJ_7512_연속하는_소수의_합 {
    private static class PrimeCache {
        private PrimeCache(final int maxN) {
            isNotPrime = new boolean[maxN + 1];
            isNotPrime[0] = isNotPrime[1] = true;
        }

        public boolean isPrime(final int n) {
            return !isNotPrime[n];
        }

        public int[] toArray() {
            return IntStream.range(2, isNotPrime.length).filter(i -> !isNotPrime[i]).toArray();
        }

        public static PrimeCache cache(final int maxN) {
            final var cache = new PrimeCache(maxN);

            final var isNotPrime = cache.isNotPrime;
            for (var i = 2; i < isNotPrime.length; ++i) {
                if (!isNotPrime[i]) {
                    for (var j = 2; i * j < isNotPrime.length; ++j) {
                        isNotPrime[i * j] = true;
                    }
                }
            }

            return cache;
        }

        private boolean[] isNotPrime;
    }

    private static class SummationCache {
        private SummationCache(final int N) {
            dp = new int[N + 1];
        }

        public int query(final int lb, final int len) {
            return dp[(lb + 1) + (len - 1)] - dp[lb];
        }

        public static SummationCache cache(final int[] seq) {
            final var cache = new SummationCache(seq.length);
            for (var i = 0; i < seq.length; ++i) {
                cache.dp[i + 1] = seq[i] + cache.dp[i];
            }

            return cache;
        }

        private int[] dp;
    }

    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var primeCache = PrimeCache.cache(10_000_000);

        final var primes = primeCache.toArray();
        final var primeRangeSum = SummationCache.cache(primes);

        final var T = Integer.parseInt(stdin.readLine());
        for (var t = 1; t <= T; ++t) {
            final var numWindows = Integer.parseInt(stdin.readLine());
            final var windowLens = new int[numWindows];
            {
                final var tokens = stdin.readLine().split(" ");
                for (var i = 0; i < numWindows; ++i) {
                    windowLens[i] = Integer.parseInt(tokens[i]);
                }
            }

            final var windowOffsets = new int[numWindows];

            for (;;) {
                while (!primeCache.isPrime(primeRangeSum.query(windowOffsets[0], windowLens[0]))) {
                    ++windowOffsets[0];
                }

                final var targetPrime = primeRangeSum.query(windowOffsets[0], windowLens[0]);
                for (var i = 1; i < numWindows; ++i) {
                    while (primeRangeSum.query(windowOffsets[i], windowLens[i]) < targetPrime) {
                        ++windowOffsets[i];
                    }
                }

                var isAllWindowsAligned = true;
                for (var i = 1; i < numWindows; ++i) {
                    if (primeRangeSum.query(windowOffsets[i], windowLens[i]) != targetPrime) {
                        isAllWindowsAligned = false;
                        break;
                    }
                }

                if (isAllWindowsAligned) {
                    break;
                }

                ++windowOffsets[0];
            }

            final var answer = primeRangeSum.query(windowOffsets[0], windowLens[0]);
            stdout.write(String.format("Scenario %d:\n%d\n" + (t < T ? "\n" : ""), t, answer));
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
