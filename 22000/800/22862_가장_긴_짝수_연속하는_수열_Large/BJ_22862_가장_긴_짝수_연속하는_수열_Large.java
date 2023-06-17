// https://www.acmicpc.net/problem/22862

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class BJ_22862_가장_긴_짝수_연속하는_수열_Large {
    public static void main(final String[] args) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {

            final int N, K;
            {
                final var tokens = stdin.readLine().split(" ");
                N = Integer.parseInt(tokens[0]);
                K = Integer.parseInt(tokens[1]);
            }

            final int[] S = new int[N];
            {
                final var tokens = new StringTokenizer(stdin.readLine());
                for (var i = 0; i < N; ++i) {
                    S[i] = Integer.parseInt(tokens.nextToken());
                }
            }

            var k = 0; // 제거된 홀수의 개수
            var lb = 0;
            var ub = 0;
            var numEvens = 0;
            var maxEvens = 0;
            while (ub < S.length) {
                k += S[++ub - 1] % 2;
                numEvens += 1 - (S[ub - 1] % 2);
                while (K < k) {
                    numEvens -= 1 - (S[lb] % 2);
                    k -= S[lb++] % 2;
                }

                maxEvens = Math.max(maxEvens, numEvens);
            }

            stdout.write(maxEvens + "\n");
            stdout.flush();
        }
    }
}
