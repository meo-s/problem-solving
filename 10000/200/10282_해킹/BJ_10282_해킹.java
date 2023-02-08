// https://www.acmicpc.net/problem/10292

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class BJ_10292_해킹 {
    public static class Edge {
        public Edge(final int to, final int cost) {
            this.to = to;
            this.cost = cost;
        }

        public int to;
        public int cost;
    }

    public static class VisitState implements Comparable<VisitState> {
        public VisitState(final int u, final int dist) {
            this.u = u;
            this.dist = dist;
        }

        @Override
        public int compareTo(final VisitState rhs) {
            if (dist < rhs.dist) {
                return -1;
            }
            if (rhs.dist < dist) {
                return 1;
            }
            return 0;
        }

        public int u;
        public int dist;
    }

    public static class Graph extends ArrayList<ArrayList<Edge>> {
        public int[] dijkstra(final int start) {
            var dists = new int[size()];
            Arrays.fill(dists, Integer.MAX_VALUE);
            dists[start] = 0;

            final var pendings = new PriorityQueue<VisitState>();
            pendings.add(new VisitState(start, 0));
            while (0 < pendings.size()) {
                final var state = pendings.poll();
                if (dists[state.u] < state.dist) {
                    continue;
                }

                for (final var edge : get(state.u)) {
                    if (state.dist + edge.cost < dists[edge.to]) {
                        dists[edge.to] = state.dist + edge.cost;
                        pendings.add(new VisitState(edge.to, dists[edge.to]));
                    }
                }
            }

            return dists;
        }
    }

    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        var T = Integer.parseInt(stdin.readLine());
        while (0 < T--) {
            final int N, D, C;
            {
                final var tokens = stdin.readLine().split(" ");
                N = Integer.parseInt(tokens[0]);
                D = Integer.parseInt(tokens[1]);
                C = Integer.parseInt(tokens[2]);
            }

            final var graph = new Graph();
            for (var i = 0; i <= N; ++i) {
                graph.add(new ArrayList<>());
            }

            for (var i = 0; i < D; ++i) {
                final var tokens = stdin.readLine().split(" ");
                final int u = Integer.parseInt(tokens[1]);
                final int v = Integer.parseInt(tokens[0]);
                final int cost = Integer.parseInt(tokens[2]);
                graph.get(u).add(new Edge(v, cost));
            }

            var numInfected = 0;
            var maxInfectionTime = 0;
            for (final var dist : graph.dijkstra(C)) {
                if (dist != Integer.MAX_VALUE) {
                    ++numInfected;
                    maxInfectionTime = Math.max(maxInfectionTime, dist);
                }
            }

            stdout.write(String.format("%d %d\n", numInfected, maxInfectionTime));
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
