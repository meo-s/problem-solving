import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_1788_피보나치_수의_확장 {
    public static class Fibonacci {
        public static int get(final int N) {
            final var fib = new int[N + 1];
            fib[1] = 1;
            for (var i = 2; i <= N; ++i) {
                fib[i] = (fib[i - 1] + fib[i - 2]) % M;
            }
            return fib[N];
        }

        public static int[] dp = new int[1_000_001];
    }

    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = Integer.parseInt(stdin.readLine());
        if (N == 0) {
            stdout.write("0\n0\n");
        } else {
            stdout.write((0 < N || Math.abs(N) % 2 == 1 ? 1 : -1) + "\n");
            stdout.write(Fibonacci.get(Math.abs(N)) + "\n");
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }

    private static int M = 1_000_000_000;
}
