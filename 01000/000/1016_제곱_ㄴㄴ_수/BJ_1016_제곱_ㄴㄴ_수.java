// https://www.acmicpc.net/problem/1016

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

public class BJ_1016_제곱_ㄴㄴ_수 {
    public static long MAX = 1_000_001_000_000L;

    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var isNotPrime = new boolean[(int) Math.floor(Math.sqrt(MAX)) + 1];
        for (var i = 2; i < isNotPrime.length; ++i) {
            if (!isNotPrime[i]) {
                var j = 2;
                while (i * j < isNotPrime.length) {
                    isNotPrime[i * j++] = true;
                }
            }
        }

        final long lb, ub;
        {
            final var tokens = new StringTokenizer(stdin.readLine());
            lb = Long.parseLong(tokens.nextToken());
            ub = Long.parseLong(tokens.nextToken()) + 1;
        }

        final var isYesYes = new boolean[(int) (ub - lb)];
        IntStream.range(2, isNotPrime.length)
                .filter(n -> !isNotPrime[n])
                .mapToLong(Long::valueOf)
                .forEach(n -> {
                    final var nxn = n * n;
                    if (ub <= nxn) {
                        return;
                    }

                    var i = lb / nxn;
                    i = (lb <= nxn * i ? i : i + 1);
                    while (nxn * i < ub) {
                        isYesYes[(int) (nxn * i++ - lb)] = true;
                    }
                });

        stdout.write(IntStream.range(0, isYesYes.length)
                .mapToObj(i -> isYesYes[i])
                .filter(v -> v == false).count()
                + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
