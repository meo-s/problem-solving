// https://www.acmicpc.net/problem/11049

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class BJ_11049_행렬_곱셈_순서 {
    public static class OptimalMatMul {

        public static int find(final int[][] matrices) {
            final var dp = new int[matrices.length][matrices.length + 1];
            for (var i = 1; i < dp.length; ++i) {
                Arrays.fill(dp[i], Integer.MAX_VALUE);
            }

            for (var k = 1; k < matrices.length; ++k) {
                for (var i = 0; i + k < matrices.length; ++i) {
                    for (var j = 0; j < k; ++j) {
                        final var mergeOps = matrices[i][0] * matrices[i + j][1] * matrices[i + k][1];
                        dp[k][i] = Math.min(dp[k][i], dp[j][i] + dp[k - (j + 1)][i + j + 1] + mergeOps);
                    }
                }
            }

            return dp[matrices.length - 1][0];
        }
    }

    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = Integer.parseInt(stdin.readLine());
        final var matrices = new int[N][2];
        for (var i = 0; i < N; ++i) {
            final var tokens = stdin.readLine().split(" ");
            matrices[i][0] = Integer.parseInt(tokens[0]);
            matrices[i][1] = Integer.parseInt(tokens[1]);
        }

        stdout.write(OptimalMatMul.find(matrices) + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
