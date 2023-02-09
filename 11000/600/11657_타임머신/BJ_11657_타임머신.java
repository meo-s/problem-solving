// https://www.acmicpc.net/problem/11657

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BJ_11657_타임머신 {

    public static class BellmanFord {
        private BellmanFord(final int V) {
            dists = new long[V + 1];
        }

        public boolean hasNegativeCycle() {
            return hasNegativeCycle;
        }

        public long distOf(final int v) {
            return dists[v];
        }

        public static BellmanFord perform(final ArrayList<HashMap<Integer, Integer>> graph, final int V,
                final int start) {
            final var bf = new BellmanFord(V);
            Arrays.fill(bf.dists, Long.MAX_VALUE);
            bf.dists[start] = 0;

            for (int i = 1; i <= V && !bf.hasNegativeCycle; ++i) {
                for (int u = 1; u <= V; ++u) {
                    if (bf.dists[u] == Long.MAX_VALUE) {
                        continue;
                    }

                    for (final var edge : graph.get(u).entrySet()) {
                        final var v = edge.getKey();
                        final var cost = edge.getValue();
                        if (bf.dists[u] + cost < bf.dists[v]) {
                            if (i < V) {
                                bf.dists[v] = bf.dists[u] + cost;
                            } else {
                                bf.hasNegativeCycle = true;
                                break;
                            }
                        }
                    }
                }
            }

            return bf;
        }

        private long[] dists;
        private boolean hasNegativeCycle;
    }

    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final int V, M;
        {
            final var tokens = stdin.readLine().split(" ");
            V = Integer.parseInt(tokens[0]);
            M = Integer.parseInt(tokens[1]);
        }

        final var graph = new ArrayList<HashMap<Integer, Integer>>();
        for (var i = 0; i < V + 1; ++i) {
            graph.add(new HashMap<>());
        }

        for (var i = 0; i < M; ++i) {
            final var tokens = stdin.readLine().split(" ");
            final var u = Integer.parseInt(tokens[0]);
            final var v = Integer.parseInt(tokens[1]);
            final var cost = Integer.parseInt(tokens[2]);
            graph.get(u).put(v, Math.min(graph.get(u).getOrDefault(v, Integer.MAX_VALUE), cost));
        }

        final var bf = BellmanFord.perform(graph, V, 1);
        if (!bf.hasNegativeCycle()) {
            for (var i = 2; i <= V; ++i) {
                stdout.write((bf.distOf(i) < Long.MAX_VALUE ? bf.distOf(i) : -1) + "\n");
            }
        } else {
            stdout.write("-1\n");
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
