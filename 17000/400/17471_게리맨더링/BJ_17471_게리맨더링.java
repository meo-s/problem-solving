// https://www.acmicpc.net/problem/17471

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class BJ_17471_게리맨더링 {
    public static class UnionFind {
        private int[] parents;

        public UnionFind(final int N) {
            parents = IntStream.range(0, N + 1).toArray();
        }

        public int find(final int u) {
            if (parents[u] != u) {
                parents[u] = find(parents[u]);
            }
            return parents[u];
        }

        public void union(final int v, final int u) {
            parents[find(v)] = parents[find(u)];
        }

        public int count() {
            final Set<Integer> unique = new HashSet<>();
            for (int i = 1; i < parents.length; ++i) {
                unique.add(find(i));
            }

            return unique.size();
        }
    }

    public static class Graph extends ArrayList<ArrayList<Integer>> {
        public Graph(final int V) {
            for (int i = 0; i <= V; ++i) {
                add(new ArrayList<>());
            }
        }
    }

    public static class Combination {
        private final int n;
        private final int r;
        private final int[] picks;
        private boolean hasNext;

        public Combination(final int n, final int r) {
            this.n = n;
            this.r = r;
            this.picks = new int[r];
            this.hasNext = true;
        }

        public boolean hasNext() {
            return hasNext;
        }

        public void next(final int pivot) {
            if (pivot < 0) {
                hasNext = false;
                return;
            }

            if (n <= ++picks[pivot]) {
                next(pivot - 1);
                picks[pivot] = 0;
            }
        }

        public void next() {
            next(r - 1);
        }

        public int[] get() {
            return picks;
        }
    }

    public static class Solver {
        public static int solve(final Graph g, final int[] populations) {
            final int N = populations.length;

            int minPopulationDiff = Integer.MAX_VALUE;
            final Combination combi = new Combination(2, N);
            while (combi.hasNext()) {
                final UnionFind districts = new UnionFind(N);
                final int[] picks = combi.get();

                for (int u = 1; u <= N; ++u) {
                    for (final int v : g.get(u)) {
                        if (picks[u - 1] == picks[v - 1]) {
                            districts.union(u, v);
                        }
                    }
                }

                if (districts.count() == 2) {
                    int populationDiff = 0;
                    for (int i = 0; i < N; ++i) {
                        populationDiff += populations[i] * (picks[i] == 1 ? 1 : -1);
                    }

                    minPopulationDiff = Math.min(minPopulationDiff, Math.abs(populationDiff));
                }

                combi.next();
            }

            return minPopulationDiff < Integer.MAX_VALUE ? minPopulationDiff : -1;
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int N = Integer.parseInt(stdin.readLine());

            final int[] populations = new int[N];
            {
                final String[] tokens = stdin.readLine().split(" ");
                for (int i = 0; i < N; ++i) {
                    populations[i] = Integer.parseInt(tokens[i]);
                }
            }

            final Graph g = new Graph(N);
            for (int u = 1; u <= N; ++u) {
                final String[] tokens = stdin.readLine().split(" ");
                final int E = Integer.parseInt(tokens[0]);
                for (int i = 1; i <= E; ++i) {
                    final int v = Integer.parseInt(tokens[i]);
                    g.get(u).add(v);
                    g.get(v).add(u);
                }
            }

            System.out.print(Solver.solve(g, populations) + "\n");
        }
    }
}
