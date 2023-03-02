// https://www.acmicpc.net/problem/1753

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class BJ_1753_최단경로 {
    public static class Graph extends ArrayList<Map<Integer, Graph.Edge>> {
        public static class Edge implements Comparable<Edge> {
            public int to;
            public int cost;

            public Edge(final int to, final int cost) {
                this.to = to;
                this.cost = cost;
            }

            @Override
            public int compareTo(final Edge other) {
                return Integer.compare(cost, other.cost);
            }
        }

        public Graph(final int V) {
            for (int i = 0; i < V; ++i) {
                add(new HashMap<>());
            }
        }

        public void addEdge(final int from, final Edge edge) {
            final Edge other = get(from).getOrDefault(edge.to, null);
            if (other == null || edge.compareTo(other) < 0) {
                get(from).put(edge.to, edge);
            }
        }

        public int[] dijkstra(final int S) {
            final int[] dists = new int[size()];
            Arrays.fill(dists, Integer.MAX_VALUE);
            dists[S] = 0;

            final PriorityQueue<Edge> candidates = new PriorityQueue<>();
            candidates.add(new Edge(S, 0));

            while (0 < candidates.size()) {
                final Edge path = candidates.poll();
                if (path.cost == dists[path.to]) {
                    for (final Edge edge : get(path.to).values()) {
                        if (dists[path.to] + edge.cost < dists[edge.to]) {
                            dists[edge.to] = dists[path.to] + edge.cost;
                            candidates.add(new Edge(edge.to, dists[edge.to]));
                        }
                    }
                }
            }

            return dists;
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {

            final int V, E;
            {
                final String[] tokens = stdin.readLine().split(" ");
                V = Integer.parseInt(tokens[0]);
                E = Integer.parseInt(tokens[1]);
            }

            final int S = Integer.parseInt(stdin.readLine()) - 1;

            final Graph g = new Graph(V);
            for (int i = 0; i < E; ++i) {
                final String[] tokens = stdin.readLine().split(" ");
                final int u = Integer.parseInt(tokens[0]) - 1;
                final int v = Integer.parseInt(tokens[1]) - 1;
                final int w = Integer.parseInt(tokens[2]);
                g.addEdge(u, new Graph.Edge(v, w));
            }

            for (final int dist : g.dijkstra(S)) {
                stdout.write((dist == Integer.MAX_VALUE ? "INF" : dist) + "\n");
            }

            stdout.flush();
        }
    }
}
