// https://www.acmicpc.net/problem/8872

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BJ_8872_빌라봉 {

    public static class Edge {
        public final int to, cost;

        public Edge(final int to, final int cost) {
            this.to = to;
            this.cost = cost;
        }
    }

    public static class Pair<T, U> {
        public T x;
        public U y;

        public Pair(final T x, final U y) {
            this.x = x;
            this.y = y;
        }
    }

    private static int N, M, L;
    private static List<List<Edge>> g = new ArrayList<>();
    private static int[] dists;
    private static boolean[] visited;

    public static int readInt(final BufferedReader br) throws IOException {
        int n = 0;
        for (;;) {
            final int b = br.read();
            if (b == ' ' || b == '\n' || b == -1) {
                return n;
            }
            n = n * 10 + (b - '0');
        }
    }

    public static void initGraph(final BufferedReader br) throws IOException {
        N = readInt(br);
        M = readInt(br);
        L = readInt(br);
        dists = new int[N];
        visited = new boolean[N];
        for (int i = 0; i < N; ++i) {
            g.add(new ArrayList<>());
        }

        for (int i = 0; i < M; ++i) {
            final int u = readInt(br);
            final int v = readInt(br);
            final int w = readInt(br);
            g.get(u).add(new Edge(v, w));
            g.get(v).add(new Edge(u, w));
        }
    }

    public static int dfs(final int u, final int p) {
        visited[u] = true;
        for (final Edge e : g.get(u)) {
            if (e.to != p) {
                dists[u] = Math.max(dists[u], dfs(e.to, u) + e.cost);
            }
        }
        return dists[u];
    }

    public static Pair<Integer, Integer> diameterAndRadiusOf(final int u, final int p, final int prevDist) {
        Edge toTop1 = null;
        int top1Dist = prevDist;
        int top2Dist = -1;
        for (final Edge e : g.get(u)) {
            if (e.to != p) {
                final int cost = dists[e.to] + e.cost;
                if (top1Dist <= cost) {
                    top2Dist = top1Dist;
                    top1Dist = cost;
                    toTop1 = e;
                } else {
                    top2Dist = Math.max(top2Dist, cost);
                }
            }
        }

        if (toTop1 == null) {
            return new Pair<>(top1Dist, top1Dist);
        }
        if (top1Dist < Math.max(top1Dist - toTop1.cost, top2Dist + toTop1.cost)) {
            return new Pair<>(top1Dist + top2Dist, top1Dist);
        }

        final Pair<Integer, Integer> dr = diameterAndRadiusOf(toTop1.to, u, top2Dist + toTop1.cost);
        dr.x = Math.max(dr.x, top1Dist + top2Dist);
        return dr;
    }

    public static int findInfimumOfMaximumDiameter() {
        int maxDiameter = 0;
        final List<Integer> radiuses = new ArrayList<>();
        for (int i = 0; i < N; ++i) {
            if (!visited[i]) {
                dfs(i, -1);
                final Pair<Integer, Integer> dr = diameterAndRadiusOf(i, -1, 0);
                maxDiameter = Math.max(maxDiameter, dr.x);
                radiuses.add(dr.y);
            }
        }

        int infimum = maxDiameter;
        if (1 < radiuses.size()) {
            Collections.sort(radiuses, Collections.reverseOrder());
            infimum = Math.max(infimum, radiuses.get(0) + radiuses.get(1) + L);
            if (2 < radiuses.size()) {
                infimum = Math.max(infimum, radiuses.get(1) + radiuses.get(2) + 2 * L);
            }
        }

        return infimum;
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            initGraph(stdin);
        }
        System.out.println(findInfimumOfMaximumDiameter());
    }

}