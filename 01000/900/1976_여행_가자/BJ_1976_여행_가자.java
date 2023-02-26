// https://www.acmicpc.net/problem/1976

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BJ_1976_여행_가자 {
    public static class UnionFind {
        private int[] parents;

        public UnionFind(final int N) {
            parents = IntStream.range(0, N + 1).toArray();
        }

        public int find(final int u) {
            if (parents[u] != u) {
                parents[u] = find(parents[u]);
            }
            return parents[u];
        }

        public void union(final int v, final int u) {
            parents[find(v)] = find(u);
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final var N = Integer.parseInt(stdin.readLine());
            final var M = Integer.parseInt(stdin.readLine());

            final var uf = new UnionFind(N);

            for (var i = 0; i < N; ++i) {
                final var tokens = stdin.readLine().split(" ");
                for (var j = 0; j < N; ++j) {
                    if (i != j && tokens[j].charAt(0) - '0' != 0) {
                        uf.union(j + 1, i + 1);
                    }
                }
            }

            final var plan = Arrays.stream(stdin.readLine().split(" "))
                    .mapToInt(Integer::parseInt)
                    .map(uf::find)
                    .boxed()
                    .collect(Collectors.toSet());
            System.out.print((plan.size() == 1 ? "YES" : "NO") + "\n");
        }
    }
}
