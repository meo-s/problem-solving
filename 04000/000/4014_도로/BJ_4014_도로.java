// https://www.acmicpc.net/problem/4014

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.ArrayList;

public class BJ_4014_도로 {

    public static class DisjointSet {

        private int remains;
        private int[] parents;

        public DisjointSet(final int N) {
            parents = new int[N];
            clear();
        }

        public void clear() {
            remains = parents.length;
            for (int i = 0; i < parents.length; ++i) {
                parents[i] = i;
            }
        }

        public int remains() {
            return remains;
        }

        public int find(final int u) {
            return parents[u] != u ? (parents[u] = find(parents[u])) : parents[u];
        }

        public boolean merge(final int u, final int v) {
            final int up = find(u);
            final int vp = find(v);
            if (up != vp) {
                parents[vp] = up;
                --remains;
            }
            return up != vp;
        }

    }

    private static int V, E, K;
    private static final List<List<int[]>> edges;

    static {
        edges = new ArrayList<>();
        for (int i = 0; i < 2; ++i) {
            edges.add(new ArrayList<>());
        }
    }

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
        V = readInt(br);
        E = readInt(br);
        K = readInt(br);
        for (int i = 0; i < E; ++i) {
            final int u = readInt(br) - 1;
            final int v = readInt(br) - 1;
            final int w = readInt(br);
            edges.get(w).add(new int[] { u, v });
        }
    }

    public static void solve() throws IOException {
        final DisjointSet set = new DisjointSet(V);
        for (final int[] edge : edges.get(1)) {
            set.merge(edge[0], edge[1]);
        }

        final boolean[] isEssential = new boolean[edges.get(0).size()];
        for (int i = 0; 1 < set.remains() && 0 < K && i < edges.get(0).size(); ++i) {
            final int[] edge = edges.get(0).get(i);
            if (set.merge(edge[0], edge[1])) {
                --K;
                isEssential[i] = true;
            }
        }

        set.clear();
        final List<int[]> picks = new ArrayList<>();
        for (int i = 0; 1 < set.remains() && i < edges.get(0).size(); ++i) {
            final int[] edge = edges.get(0).get(i);
            if (0 < K || isEssential[i]) {
                if (set.merge(edge[0], edge[1])) {
                    picks.add(new int[] { edge[0], edge[1], 0 });
                    if (!isEssential[i]) {
                        --K;
                    }
                }
            }
        }
        for (int i = 0; 1 < set.remains() && i < edges.get(1).size(); ++i) {
            final int[] edge = edges.get(1).get(i);
            if (set.merge(edge[0], edge[1])) {
                picks.add(new int[] { edge[0], edge[1], 1 });
            }
        }

        try (final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out))) {
            if (1 < set.remains() || 0 < K) {
                stdout.write("no solution\n");
            } else {
                for (final int[] pick : picks) {
                    stdout.write(String.format("%d %d %d\n", pick[0] + 1, pick[1] + 1, pick[2]));
                }
            }
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            initGraph(stdin);
        }
        solve();
    }

}
