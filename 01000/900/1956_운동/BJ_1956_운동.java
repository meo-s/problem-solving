// https://www.acmicpc.net/problem/1956

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class BJ_1956_운동 {
    public static void main(final String[] args) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int V, E;
            {
                final var tokens = stdin.readLine().split(" ");
                V = Integer.parseInt(tokens[0]);
                E = Integer.parseInt(tokens[1]);
            }

            final var dists = new int[V + 1][V + 1];
            for (final var distRow : dists) {
                Arrays.fill(distRow, Integer.MAX_VALUE);
            }
            for (var i = 0; i <= V; ++i) {
                dists[i][i] = 0;
            }
            for (var i = 0; i < E; ++i) {
                final var tokens = stdin.readLine().split(" ");
                final var u = Integer.parseInt(tokens[0]);
                final var v = Integer.parseInt(tokens[1]);
                final var w = Integer.parseInt(tokens[2]);
                dists[u][v] = w;
            }

            for (var k = 1; k <= V; ++k) {
                for (var i = 1; i <= V; ++i) {
                    if (dists[i][k] < Integer.MAX_VALUE) {
                        for (var j = 1; j < V; ++j) {
                            if (dists[k][j] < Integer.MAX_VALUE) {
                                dists[i][j] = Math.min(dists[i][j], dists[i][k] + dists[k][j]);
                            }
                        }
                    }
                }
            }

            int minCycleDist = Integer.MAX_VALUE;
            for (var i = 1; i <= V; ++i) {
                for (var j = 1; j <= V; ++j) {
                    if (i != j && dists[i][j] != Integer.MAX_VALUE && dists[j][i] != Integer.MAX_VALUE) {
                        minCycleDist = Math.min(minCycleDist, dists[i][j] + dists[j][i]);
                    }
                }
            }

            stdout.write((minCycleDist < Integer.MAX_VALUE ? minCycleDist : -1) + "\n");
            stdout.flush();
        }
    }
}
