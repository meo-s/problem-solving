// https://www.acmicpc.net/problem/1456

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.util.stream.IntStream;

public class BJ_1456_거의_소수 {
    public static int[] findPrimes(final int max) {
        final var isNotPrime = new boolean[max + 1];
        isNotPrime[0] = isNotPrime[1] = true;
        for (var i = 2; i <= max; ++i) {
            if (!isNotPrime[i]) {
                for (var j = 2; j * i <= max; ++j) {
                    isNotPrime[j * i] = true;
                }
            }
        }

        return IntStream.range(2, max + 1).filter(i -> !isNotPrime[i]).toArray();
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final BigInteger lb, ub;
            {
                final var tokens = stdin.readLine().split(" ");
                lb = new BigInteger(tokens[0]);
                ub = new BigInteger(tokens[1]).add(BigInteger.ONE);
            }

            final var primes = findPrimes(10_000_000);

            var numPrimes = 0;
            for (int i = 0; i < primes.length; ++i) {
                final var prime = BigInteger.valueOf(primes[i]);

                var n = prime.multiply(prime);
                if (0 <= n.compareTo(ub)) {
                    break;
                }

                while (n.compareTo(lb) < 0) {
                    n = n.multiply(prime);
                }
                while (n.compareTo(ub) < 0) {
                    ++numPrimes;
                    n = n.multiply(prime);
                }
            }

            stdout.write(numPrimes + "\n");
            stdout.flush();
        }
    }
}
