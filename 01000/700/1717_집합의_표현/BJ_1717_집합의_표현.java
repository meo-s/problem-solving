// https://www.acmicpc.net/problem/1717

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_1717_집합의_표현 {
    public static class UnionFind {
        private int[] parents;

        public UnionFind(final int N) {
            parents = new int[N + 1];
            for (var i = 0; i < parents.length; ++i) {
                parents[i] = i;
            }
        }

        public int find(final int child) {
            if (parents[child] != child) {
                parents[child] = find(parents[child]);
            }
            return parents[child];
        }

        public void union(final int u, final int v) {
            parents[find(u)] = find(v);
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int N, Q;
            {
                final var tokens = stdin.readLine().split(" ");
                N = Integer.parseInt(tokens[0]);
                Q = Integer.parseInt(tokens[1]);
            }

            final var uf = new UnionFind(N + 1);
            for (int i = 0; i < Q; ++i) {
                final int queryType, u, v;
                {
                    final var tokens = stdin.readLine().split(" ");
                    queryType = Integer.parseInt(tokens[0]);
                    u = Integer.parseInt(tokens[1]);
                    v = Integer.parseInt(tokens[2]);
                }

                switch (queryType) {
                    case 0:
                        uf.union(u, v);
                        break;
                    case 1:
                        stdout.write((uf.find(u) == uf.find(v) ? "YES" : "NO") + "\n");
                        break;
                }
            }

            stdout.flush();
        }
    }
}
