// https://www.acmicpc.net/problem/2230

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class BJ_2230_수_고르기 {
    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int N, M;
            {
                final var tokens = stdin.readLine().split(" ");
                N = Integer.parseInt(tokens[0]);
                M = Integer.parseInt(tokens[1]);
            }

            final var seq = new int[N];
            for (var i = 0; i < N; ++i) {
                seq[i] = Integer.parseInt(stdin.readLine());
            }

            Arrays.sort(seq);

            var lb = 0;
            var ub = 2;
            while (seq[ub - 1] - seq[lb] < M) {
                ++ub;
            }

            var minDiff = seq[ub - 1] - seq[lb];
            while (ub <= N) {
                while (2 <= ub - (lb + 1) && M <= seq[ub - 1] - seq[lb + 1]) {
                    ++lb;
                }

                minDiff = Math.min(minDiff, seq[ub++ - 1] - seq[lb]);
            }

            System.out.print(minDiff + "\n");
        }
    }
}
