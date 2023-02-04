// https://www.acmicpc.net/problem/2023

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class BJ_2023_신기한_소수 {

    public static boolean isPrime(final long n) {
        if (n == 1) {
            return false;
        }
        if (n == 2 || n == 3) {
            return true;
        }

        boolean isPrime_ = ((n - 1) % 6 == 0 || (n + 1) % 6 == 0);
        if (isPrime_) {
            for (var i = 2L; i < Math.ceil(Math.sqrt((double) n)) + 1; ++i) {
                if (n % i == 0) {
                    isPrime_ = false;
                    break;
                }
            }
        }

        return isPrime_;
    }

    public static class AmazingPrimeFinder {
        public AmazingPrimeFinder() {
            amazings = new ArrayList<>();
            amazings.add(0L);
        }

        public void next() {
            final var nextAmazings = new ArrayList<Long>();
            for (final var prevAmazing : amazings) {
                for (var i = 1; i < 10; ++i) {
                    final var n = (prevAmazing * 10) + i;
                    if (isPrime(n)) {
                        nextAmazings.add(n);
                    }
                }
            }

            amazings = nextAmazings;
        }

        public ArrayList<Long> getAmazings() {
            return amazings;
        }

        private ArrayList<Long> amazings;
    }

    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var amazingPrimeFinder = new AmazingPrimeFinder();
        var N = Integer.parseInt(stdin.readLine());
        while (0 < N--) {
            amazingPrimeFinder.next();
        }

        for (final var amazing : amazingPrimeFinder.getAmazings()) {
            stdout.write(amazing + "\n");
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
