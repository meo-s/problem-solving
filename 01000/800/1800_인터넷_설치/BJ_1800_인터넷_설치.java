// https://www.acmicpc.net/problem/1800

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class BJ_1800_인터넷_설치 {

    private static int V, E, K;
    private static int[][] dists;
    private static final List<List<int[]>> g = new ArrayList<>();

    private static int readInt(final BufferedReader br) throws IOException {
        int n = 0;
        for (;;) {
            final int b = br.read();
            if (b == ' ' || b == '\n' || b == -1) {
                return n;
            }
            n = n * 10 + (b - '0');
        }
    }

    private static void initGraph() throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            V = readInt(stdin);
            E = readInt(stdin);
            K = readInt(stdin);
            for (int i = 0; i < V; ++i) {
                g.add(new ArrayList<>());
            }
            for (int i = 0; i < E; ++i) {
                final int u = readInt(stdin) - 1;
                final int v = readInt(stdin) - 1;
                final int w = readInt(stdin);
                g.get(u).add(new int[] { v, w });
                g.get(v).add(new int[] { u, w });
            }
        }
    }

    public static int dijkstra(final int maxCost) {
        if (dists == null) {
            dists = new int[V][K + 1];
        }
        for (int i = 0; i < V; ++i) {
            Arrays.fill(dists[i], Integer.MAX_VALUE);
        }
        dists[0][0] = 0;

        final PriorityQueue<int[]> waypoints = new PriorityQueue<int[]>((x, y) -> {
            return Integer.compare(x[0], y[0]);
        });

        waypoints.add(new int[] { 0, 0, 0 }); // cost, vertex, k
        while (!waypoints.isEmpty()) {
            final int[] waypoint = waypoints.poll();
            final int w0 = waypoint[0];
            final int u = waypoint[1];
            final int k = waypoint[2];
            if (dists[u][k] != w0) {
                continue;
            }

            for (final int[] edge : g.get(u)) {
                final int v = edge[0];
                final int w = edge[1];
                if (w <= maxCost) {
                    if (Math.max(w, w0) < dists[v][k]) {
                        dists[v][k] = Math.max(w, w0);
                        waypoints.add(new int[] { dists[v][k], v, k });
                    }
                }
                if (k + 1 <= K) {
                    if (w0 < dists[v][k + 1]) {
                        dists[v][k + 1] = w0;
                        waypoints.add(new int[] { dists[v][k + 1], v, k + 1 });
                    }
                }
            }
        }

        return Arrays.stream(dists[V - 1]).min().getAsInt();
    }

    private static void solve() {
        int lb = 1, ub = 1_000_001;
        int optimalCost = Integer.MAX_VALUE;
        while (lb < ub) {
            final int mid = (lb + ub) / 2;
            final int mostExpensive = dijkstra(mid);
            optimalCost = Math.min(optimalCost, mostExpensive);
            if (mostExpensive == Integer.MAX_VALUE) {
                lb = mid + 1;
            } else {
                ub = Math.min(optimalCost, mid);
            }
        }

        System.out.println(optimalCost == Integer.MAX_VALUE ? -1 : optimalCost);
    }

    public static void main(final String[] args) throws IOException {
        initGraph();
        solve();
    }

}
