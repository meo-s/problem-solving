// https://www.acmicpc.net/problem/3860

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BJ_3860_할로윈_묘지 {
    private static int[] dy = { 1, -1, 0, 0 };
    private static int[] dx = { 0, 0, 1, -1 };

    public static class Graph extends ArrayList<ArrayList<Graph.Edge>> {
        public static class Edge {
            public int to;
            public int cost;

            public Edge(final int to, final int cost) {
                this.to = to;
                this.cost = cost;
            }
        }
    }

    public static class Graveyard {
        public static class Wormhole {
            public int gy;
            public int gx;
            public int dtime;
            public int latestUsedTime;
            public boolean hasNegativeCycle;

            public Wormhole(final int gy, final int gx, final int dtime) {
                this.gy = gy;
                this.gx = gx;
                this.dtime = dtime;
            }

            public void use(final int time) {
                hasNegativeCycle = hasNegativeCycle || time < latestUsedTime;
                latestUsedTime = time;
            }
        }

        private int height;
        private int width;
        public int[][] map;
        private Map<Integer, Wormhole> wormholes;

        private Graveyard(final int H, final int W) {
            height = H;
            width = W;
            map = new int[H][W];
            wormholes = new HashMap<>();
        }

        public boolean isWormhole(final int y, final int x) {
            return 0 < map[y][x];
        }

        public boolean isTombstone(final int y, final int x) {
            return map[y][x] == -1;
        }

        public Wormhole getWormholeAt(final int y, final int x) {
            return wormholes.get(map[y][x]);
        }

        public Graph toGraph() {
            final var graph = new Graph();
            final var numCells = height * width;
            for (var i = 0; i < numCells; ++i) {
                graph.add(new ArrayList<>());
            }

            for (var y = 0; y < height; ++y) {
                for (var x = 0; x < width; ++x) {
                    if (isTombstone(y, x)) {
                        continue;
                    }

                    if (isWormhole(y, x)) {
                        final var wormhole = getWormholeAt(y, x);
                        graph.get(y * width + x).add(new Graph.Edge(wormhole.gy * width + wormhole.gx, wormhole.dtime));
                        continue;
                    }

                    for (var i = 0; i < dy.length; ++i) {
                        if (y + dy[i] < 0 || height <= y + dy[i] || x + dx[i] < 0 || width <= x + dx[i]) {
                            continue;
                        }

                        if (!isTombstone(y + dy[i], x + dx[i])) {
                            graph.get(y * width + x).add(new Graph.Edge((y + dy[i]) * width + (x + dx[i]), 1));
                        }
                    }
                }
            }

            return graph;
        }

        public static Graveyard from(final BufferedReader br) throws IOException {
            final int H, W;
            {
                final var tokens = br.readLine().split(" ");
                W = Integer.parseInt(tokens[0]);
                H = Integer.parseInt(tokens[1]);
            }

            if (H + W == 0) {
                return null;
            }

            final var cemetery = new Graveyard(H, W);

            var numTombs = Integer.parseInt(br.readLine());
            while (0 < numTombs--) {
                final var tokens = br.readLine().split(" ");
                final var x = Integer.parseInt(tokens[0]);
                final var y = Integer.parseInt(tokens[1]);
                cemetery.map[y][x] = -1;
            }

            var numWormholes = Integer.parseInt(br.readLine());
            while (0 < numWormholes--) {
                final var tokens = br.readLine().split(" ");
                final var x = Integer.parseInt(tokens[0]);
                final var y = Integer.parseInt(tokens[1]);
                final var gx = Integer.parseInt(tokens[2]);
                final var gy = Integer.parseInt(tokens[3]);
                final var dtime = Integer.parseInt(tokens[4]);
                final var wormholeIndex = cemetery.wormholes.size() + 1;
                cemetery.map[y][x] = wormholeIndex;
                cemetery.wormholes.put(wormholeIndex, new Wormhole(gy, gx, dtime));
            }

            return cemetery;
        }
    }

    public static class BellmanFord {
        public static class Result {
            private int[] dists;
            private boolean hasNegativeCycle;

            private Result(final int N) {
                dists = new int[N];
                Arrays.fill(dists, Integer.MAX_VALUE);
            }

            public int distanceOf(final int v) {
                return dists[v];
            }

            public boolean hasNegativeCycle() {
                return hasNegativeCycle;
            }
        }

        public static Result perform(final Graph g, final int start) {
            final var V = g.size();
            final var r = new Result(V);
            r.dists[start] = 0;

            for (var i = 1; i <= V; ++i) {
                for (var u = 0; u < g.size() - 1; ++u) {
                    if (r.dists[u] == Integer.MAX_VALUE) {
                        continue;
                    }

                    for (final var edge : g.get(u)) {
                        if (r.dists[u] + edge.cost < r.dists[edge.to]) {
                            r.dists[edge.to] = r.dists[u] + edge.cost;
                            r.hasNegativeCycle = r.hasNegativeCycle || i == V;
                        }
                    }
                }
            }

            return r;
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            Graveyard graveyard;
            while ((graveyard = Graveyard.from(stdin)) != null) {
                final var bf = BellmanFord.perform(graveyard.toGraph(), 0);
                if (bf.hasNegativeCycle()) {
                    stdout.write("Never\n");
                } else {
                    final var goal = graveyard.height * graveyard.width - 1;
                    if (bf.distanceOf(goal) == Integer.MAX_VALUE) {
                        stdout.write("Impossible\n");
                    } else {
                        stdout.write(bf.distanceOf(goal) + "\n");
                    }
                }
            }

            stdout.flush();
        }
    }
}
