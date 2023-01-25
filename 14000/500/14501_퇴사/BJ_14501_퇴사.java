// https://www.acmicpc.net/problem/14501

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_14501_퇴사 {
    public static class Pair<T, U> {
        public Pair() {
        }

        public Pair(final T one, final U two) {
            this.one = one;
            this.two = two;
        }

        public T one;
        public U two;
    }

    public static class Consult extends Pair<Integer, Integer> {
        public Consult(final int time, final int earn) {
            super(time, earn);
        }

        public int getTime() {
            return one;
        }

        public int getEarn() {
            return two;
        }
    }

    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = Integer.parseInt(stdin.readLine());
        final var consults = new Consult[N];
        for (var i = 0; i < N; ++i) {
            final var tokens = stdin.readLine().split(" ");
            consults[i] = new Consult(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
        }

        final var dp = new int[N + 1];
        var maxEarn = 0;
        for (var i = 0; i < N; ++i) {
            maxEarn = Math.max(maxEarn, dp[i]);

            final var di = consults[i].getTime();
            if (i + di <= N) {
                dp[i + di] = Math.max(dp[i + di], maxEarn + consults[i].getEarn());
            }
        }

        stdout.write(Math.max(maxEarn, dp[N]) + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
