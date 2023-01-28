// https://www.acmicpc.net/problem/1644

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class BJ_1644_소수의_연속합 {
    public static int lowerBound(final long[] arr, final long value, int lb, int ub) {
        while (lb < ub) {
            final var mid = (lb + ub) / 2;
            if (arr[mid] < value) {
                lb = mid + 1;
            } else {
                ub = mid;
            }
        }
        return lb;
    }

    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = Integer.parseInt(stdin.readLine());

        final var primes = new ArrayList<Integer>();
        final var isNotPrime = new boolean[N + 1];
        for (var i = 2; i <= N; ++i) {
            if (!isNotPrime[i]) {
                primes.add(i);
                for (var j = 2 * i; j <= N; j += i) {
                    isNotPrime[j] = true;
                }
            }
        }

        final var cumsum = new long[primes.size()];
        for (var i = 0; i < primes.size(); ++i) {
            cumsum[i] = primes.get(i) + (0 < i ? cumsum[i - 1] : 0);
        }

        var numConsecutives = 0;
        for (var i = 0; i < cumsum.length; ++i) {
            if (cumsum[i] == N) {
                ++numConsecutives;
            } else if (1 <= i) {
                final var j = Math.min(i - 1, lowerBound(cumsum, cumsum[i] - N, 0, i));
                if (cumsum[i] - cumsum[j] == N) {
                    ++numConsecutives;
                }
            }
        }

        stdout.write(numConsecutives + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
