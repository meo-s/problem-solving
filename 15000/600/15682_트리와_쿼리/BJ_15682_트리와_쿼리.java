// https://www.acmicpc.net/problem/15682

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class BJ_15682_트리와_쿼리 {

    public static class Graph extends HashMap<Integer, ArrayList<Integer>> {
    }

    public static class SubtreeSizeCacher {
        public SubtreeSizeCacher(final int N) {
            dp = new int[N + 1];
        }

        private int prepare(final Graph graph, final int rootNode, final boolean[] visited) {
            dp[rootNode] = 1;
            for (final var childNode : graph.get(rootNode)) {
                if (!visited[childNode]) {
                    visited[childNode] = true;
                    dp[rootNode] += prepare(graph, childNode, visited);
                }
            }

            return dp[rootNode];
        }

        public void cache(final Graph graph, final int rootNode) {
            final var visited = new boolean[dp.length];
            visited[rootNode] = true;
            prepare(graph, rootNode, visited);
        }

        public int querySubtreeSize(final int subtreeRootNode) {
            return dp[subtreeRootNode];
        }

        private int[] dp;
    }

    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final int N, R, Q;
        {
            final var tokens = stdin.readLine().split(" ");
            N = Integer.parseInt(tokens[0]);
            R = Integer.parseInt(tokens[1]);
            Q = Integer.parseInt(tokens[2]);
        }

        final var graph = new Graph();
        for (var i = 1; i <= N; ++i) {
            graph.put(i, new ArrayList<>());
        }

        for (var i = 0; i < N - 1; ++i) {
            final var tokens = stdin.readLine().split(" ");
            final var u = Integer.parseInt(tokens[0]);
            final var v = Integer.parseInt(tokens[1]);
            graph.get(u).add(v);
            graph.get(v).add(u);
        }

        final var subtreeSizeCacher = new SubtreeSizeCacher(N);
        subtreeSizeCacher.cache(graph, R);

        for (var i = 0; i < Q; ++i) {
            stdout.write(subtreeSizeCacher.querySubtreeSize(Integer.parseInt(stdin.readLine())) + "\n");
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
