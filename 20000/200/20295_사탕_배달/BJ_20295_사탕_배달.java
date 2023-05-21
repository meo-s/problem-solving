// https://www.acmicpc.net/problem/20295

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BJ_20295_사탕_배달 {
    public static class Graph extends ArrayList<List<Integer>> {
        public Graph(final int V) {
            for (int i = 0; i < V; ++i) {
                add(new ArrayList<>());
            }
        }
    }

    public static class DFSTree {
        public final int root;
        public int depth;
        public final int[] vbeg;
        public final int[] vend;
        public final int[] parents;
        public final int[][] ancestors;
        public final int[][] candies;

        public DFSTree(final Graph g, final int[] stores, final int root) {
            this.root = root;
            this.depth = 0;
            this.vbeg = new int[g.size()];
            this.vend = new int[g.size()];
            this.parents = new int[g.size()];
            this.parents[root] = root;
            composeTree(g, root, 1, 1);

            final var log2D = (int) Math.floor(Math.log(depth) / Math.log(2)) + 1;
            this.ancestors = new int[g.size()][log2D];
            this.candies = new int[g.size()][log2D];
            cacheTree(g, stores, root);
        }

        private int composeTree(final Graph g, final int u, final int beg, final int depth) {
            this.depth = Math.max(this.depth, depth);

            var end = beg + 1;
            for (final var v : g.get(u)) {
                if (v != parents[u]) {
                    parents[v] = u;
                    end = composeTree(g, v, end, depth + 1);
                }
            }

            vbeg[u] = beg;
            vend[u] = end;
            return end + 1;
        }

        private void cacheTree(final Graph g, final int[] stores, final int u) {
            if (u == root) {
                Arrays.fill(ancestors[u], u);
                Arrays.fill(candies[u], stores[u]);
            } else {
                ancestors[u][0] = parents[u];
                candies[u][0] = stores[u];
                for (var i = 1; i < ancestors[u].length; ++i) {
                    final var ancestor = ancestors[u][i - 1];
                    ancestors[u][i] = ancestors[ancestor][i - 1];
                    candies[u][i] = candies[u][i - 1] | candies[ancestor][i - 1];
                }
            }

            for (final var v : g.get(u)) {
                if (v != parents[u]) {
                    cacheTree(g, stores, v);
                }
            }
        }

        public boolean isAncestorOf(final int parent, final int child) {
            return vbeg[parent] <= vbeg[child] && vend[child] <= vend[parent];
        }

        public int query(final int vertexA, final int vertexB) {
            var candy = candies[vertexB][0];  // vertexA == vertexB인 경우 목적지를 포함해야 한다.
            for (final var picks : new int[][] { { vertexA, vertexB }, { vertexB, vertexA } }) {
                var u = picks[0];
                var v = picks[1];
                if (isAncestorOf(u, v)) {
                    continue;
                }
                
                var latestAncestor = v;
                for (var i = ancestors[u].length - 1; 0 <= i; --i) {
                    if (!isAncestorOf(ancestors[u][i], v)) {
                        candy |= candies[u][i];
                        u = ancestors[u][i];
                    } else {
                        latestAncestor = ancestors[u][i];
                    }
                }

                candy |= candies[latestAncestor][0] | (u != v ? candies[u][0] : 0);
            }

            return candy;
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {

            final var N = Integer.parseInt(stdin.readLine());

            final var stores = new int[N];
            {
                final var tokens = stdin.readLine().split(" ");
                for (var i = 0; i < N; ++i) {
                    stores[i] = 1 << (Integer.parseInt(tokens[i]) - 1);
                }
            }

            final var g = new Graph(N);
            for (var i = 0; i < N - 1; ++i) {
                final var tokens = stdin.readLine().split(" ");
                final var u = Integer.parseInt(tokens[0]) - 1;
                final var v = Integer.parseInt(tokens[1]) - 1;
                g.get(u).add(v);
                g.get(v).add(u);
            }

            final var dfsTree = new DFSTree(g, stores, 0);
            final var M = Integer.parseInt(stdin.readLine());
            var latestVertex = -1;
            for (var i = 0; i < M; ++i) {
                final int v, p;
                {
                    final var tokens = stdin.readLine().split(" ");
                    v = Integer.parseInt(tokens[0]) - 1;
                    p = 1 << (Integer.parseInt(tokens[1]) - 1);
                }

                var isFriend = false;
                if (i == 0) {
                    for (final var candy : stores) {
                        if (isFriend = (candy == p)) {
                            break;
                        }
                    }
                } else {
                    isFriend = (dfsTree.query(latestVertex, v) & p) != 0;
                }

                stdout.write(isFriend ? "PLAY\n" : "CRY\n");
                latestVertex = v;
            }

            stdout.flush();
        }
    }
}
