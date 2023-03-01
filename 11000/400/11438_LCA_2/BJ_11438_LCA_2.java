// https://www.acmicpc.net/problem/11438

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BJ_11438_LCA_2 {
    public static class Graph extends ArrayList<List<Integer>> {
        public Graph(final int V) {
            for (var i = 0; i <= V; ++i) {
                add(new ArrayList<>());
            }
        }

        public static Graph from(final BufferedReader br) throws IOException {
            final var V = Integer.parseInt(br.readLine());
            final var g = new Graph(V);
            for (var i = 0; i < V - 1; ++i) {
                final var tokens = br.readLine().split(" ");
                final var u = Integer.parseInt(tokens[0]);
                final var v = Integer.parseInt(tokens[1]);
                g.get(u).add(v);
                g.get(v).add(u);
            }

            return g;
        }
    }

    public static class DFSTree {
        private int root;
        private int[][] ancestors;
        private int[] parents;
        private int[] begTimes;
        private int[] endTimes;
        private int depth;

        private DFSTree(final int V) {
            root = 0;
            ancestors = null;
            parents = new int[V + 1];
            begTimes = new int[V + 1];
            endTimes = new int[V + 1];
            depth = 0;
        }

        private int dfs(final Graph g, final boolean[] visited, final int u, final int begTime, int depth) {
            visited[u] = true;
            this.depth = Math.max(this.depth, depth);

            begTimes[u] = begTime;
            var endTime = begTime;
            for (final var v : g.get(u)) {
                if (!visited[v]) {
                    parents[v] = u;
                    endTime = dfs(g, visited, v, endTime + 1, depth + 1);
                }
            }

            return endTimes[u] = endTime + 1;
        }

        private void cacheLogAncestors(final Graph g, final int u) {
            if (u == root) {
                Arrays.fill(ancestors[root], root);
            } else {
                ancestors[u][0] = parents[u];
                for (var i = 1; i < ancestors[u].length; ++i) {
                    ancestors[u][i] = ancestors[ancestors[u][i - 1]][i - 1];
                }
            }

            for (final var v : g.get(u)) {
                if (v != parents[u]) {
                    cacheLogAncestors(g, v);
                }
            }
        }

        private void cacheLogAncestors(final Graph g) {
            final var N = (int) Math.floor(Math.log(depth) / Math.log(2));
            ancestors = new int[parents.length][N + 1];
            cacheLogAncestors(g, root);
        }

        private DFSTree createTree(final Graph g, final int root) {
            parents[root] = (this.root = root);
            dfs(g, new boolean[g.size()], root, 1, 1);
            cacheLogAncestors(g);
            return this;
        }

        public boolean isAncestorOf(final int parent, final int child) {
            return parent == child || (begTimes[parent] < begTimes[child] && endTimes[child] < endTimes[parent]);
        }

        public int getLowestCommonAncestorOf(int nodeA, final int nodeB) {
            var latestAncestor = nodeA;
            if (!isAncestorOf(nodeA, nodeB)) {
                for (var i = ancestors[nodeA].length - 1; 0 <= i; --i) {
                    if (!isAncestorOf(ancestors[nodeA][i], nodeB)) {
                        nodeA = ancestors[nodeA][i];
                    } else {
                        latestAncestor = ancestors[nodeA][i];
                    }
                }
            }

            return latestAncestor;
        }

        public static DFSTree from(final Graph g, final int root) {
            return new DFSTree(g.size() - 1).createTree(g, root);
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {

            final var g = Graph.from(stdin);
            final var tree = DFSTree.from(g, 1);

            var Q = Integer.parseInt(stdin.readLine());
            while (0 <= --Q) {
                final var tokens = stdin.readLine().split(" ");
                final var nodeA = Integer.parseInt(tokens[0]);
                final var nodeB = Integer.parseInt(tokens[1]);
                stdout.write(tree.getLowestCommonAncestorOf(nodeA, nodeB) + "\n");
            }

            stdout.flush();
        }
    }
}
