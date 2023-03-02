// https://www.acmicpc.net/problem/15481

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class BJ_15481_그래프와_MST {
    public static class Graph extends ArrayList<List<Graph.Edge>> {
        public static class Edge implements Comparable<Edge> {
            public final int from;
            public final int to;
            public final int cost;

            public Edge(final int u, final int v, final int cost) {
                this.from = u;
                this.to = v;
                this.cost = cost;
            }

            @Override
            public int compareTo(final Edge other) {
                return cost != other.cost ? Integer.compare(cost, other.cost) : Integer.compare(to, other.to);
            }
        }

        public Graph(final int V) {
            for (var i = 0; i < V; ++i) {
                add(new ArrayList<>());
            }
        }

        public Graph toMinimumSpanningTree() {
            final var visited = new boolean[size()];
            visited[1] = true;

            final var minEdgeCosts = new int[size()];
            Arrays.fill(minEdgeCosts, Integer.MAX_VALUE);
            minEdgeCosts[1] = 0;

            final var cuts = new PriorityQueue<Graph.Edge>();
            for (final var edge : get(1)) {
                minEdgeCosts[edge.to] = edge.cost;
                cuts.add(edge);
            }

            final var mst = new Graph(size());
            var numConnections = 1;
            while (numConnections < size() - 1) {
                final var cut = cuts.poll();
                if (!visited[cut.to]) {
                    visited[cut.to] = true;
                    ++numConnections;

                    mst.get(cut.from).add(cut);
                    mst.get(cut.to).add(new Edge(cut.to, cut.from, cut.cost));

                    for (final var edge : get(cut.to)) {
                        if (!visited[edge.to] && edge.cost < minEdgeCosts[edge.to]) {
                            minEdgeCosts[edge.to] = edge.cost;
                            cuts.add(edge);
                        }
                    }
                }
            }

            return mst;
        }
    }

    public static class DfsTree {
        private static class Range {
            public final int beg;
            public final int end;

            public Range(final int beg, final int end) {
                this.beg = beg;
                this.end = end;
            }

            public boolean contains(final Range other) {
                return beg <= other.beg && other.end <= end;
            }
        }

        private final Graph g;
        private final Graph.Edge[] parents;
        private final Range[] ranges;
        private final int[][] ancestors;
        private final long[][] maxWeights;
        private int root;
        private int depth;
        private long totalTreeWeight;

        public DfsTree(final Graph g) {
            this.g = g;
            ranges = new Range[g.size()];
            parents = new Graph.Edge[g.size()];
            depth = 0;
            composeTree(1);

            final var N = ((int) Math.floor(Math.log(depth) / Math.log(2))) + 1;
            ancestors = new int[g.size()][N];
            maxWeights = new long[g.size()][N];
            cacheAncestors(root);
        }

        private int composeTree(final int u, final int beg, final int depth) {
            this.depth = Math.max(this.depth, depth);

            var end = beg + 1;
            for (final var edge : g.get(u)) {
                if (parents[edge.to] == null) {
                    totalTreeWeight += edge.cost;
                    parents[edge.to] = edge;
                    end = composeTree(edge.to, end, depth + 1);
                }
            }

            ranges[u] = new Range(beg, end);
            return end + 1;
        }

        private void composeTree(final int u) {
            root = u;
            depth = 0;
            totalTreeWeight = 0;
            parents[u] = new Graph.Edge(u, u, 0);
            composeTree(u, 1, 1);
        }

        private void cacheAncestors(final int u) {
            if (u == root) {
                Arrays.fill(ancestors[u], u);
            } else {
                ancestors[u][0] = parents[u].from;
                maxWeights[u][0] = parents[u].cost;
                for (var i = 1; i < ancestors[u].length; ++i) {
                    final var ancestor = ancestors[u][i - 1];
                    ancestors[u][i] = ancestors[ancestor][i - 1];
                    maxWeights[u][i] = Math.max(maxWeights[u][i - 1], maxWeights[ancestor][i - 1]);
                }
            }

            for (final var edge : g.get(u)) {
                if (edge.to != parents[u].from) {
                    cacheAncestors(edge.to);
                }
            }
        }

        public boolean isAncestorOf(final int parent, final int child) {
            return ranges[parent].contains(ranges[child]);
        }

        public long queryMaxWeightBetween(final int a, final int b) {
            var maxWeight = 0L;
            for (final var picks : new int[][] { { a, b }, { b, a } }) {
                var u = picks[0];
                final var v = picks[1];
                if (!isAncestorOf(u, v)) {
                    final var N = ancestors[u].length;
                    for (var i = N - 1; 0 <= i; --i) {
                        if (!isAncestorOf(ancestors[u][i], v)) {
                            maxWeight = Math.max(maxWeight, maxWeights[u][i]);
                            u = ancestors[u][i];
                        }
                    }

                    maxWeight = (u != v ? Math.max(maxWeight, maxWeights[u][0]) : maxWeight);
                }
            }

            return maxWeight;
        }

        public long getTotalTreeWeight() {
            return totalTreeWeight;
        }

        public static DfsTree of(final Graph g) {
            return new DfsTree(g);
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {

            final int V, E;
            {
                final var tokens = stdin.readLine().split(" ");
                V = Integer.parseInt(tokens[0]);
                E = Integer.parseInt(tokens[1]);
            }

            final var g = new Graph(V + 1);
            final var edges = new Graph.Edge[E];
            for (var i = 0; i < E; ++i) {
                final var tokens = stdin.readLine().split(" ");
                final var u = Integer.parseInt(tokens[0]);
                final var v = Integer.parseInt(tokens[1]);
                final var w = Integer.parseInt(tokens[2]);
                edges[i] = new Graph.Edge(u, v, w);
                g.get(u).add(edges[i]);
                g.get(v).add(new Graph.Edge(v, u, w));
            }

            final var tree = DfsTree.of(g.toMinimumSpanningTree());
            for (var i = 0; i < E; ++i) {
                final var w = tree.queryMaxWeightBetween(edges[i].from, edges[i].to);
                stdout.write(tree.getTotalTreeWeight() - w + edges[i].cost + "\n");
            }

            stdout.flush();
        }
    }
}
