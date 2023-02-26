// https://www.acmicpc.net/problem/3197

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.IntStream;

public class BJ_3197_백조와_호수 {

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

        public void union(final int u, final int v) {
            final var pu = find(u);
            final var pv = find(v);
            if (pu != pv) {
                parents[pu] = parents[pv];
            }
        }
    }

    public static class Ice {
        final int y;
        final int x;
        final int region;

        public Ice(final int y, final int x, final int region) {
            this.y = y;
            this.x = x;
            this.region = region;
        }
    }

    public static class Lake {
        private static final char ICE = 'X';
        private static final char WATER = '.';

        public final int H;
        public final int W;
        public final char[][] map;
        public final List<Integer> swans;

        private Lake(final int H, final int W) {
            this.H = H;
            this.W = W;
            this.map = new char[H][W];
            this.swans = new ArrayList<>();
        }

        public static Lake from(final BufferedReader br) throws IOException {
            final int H, W;
            {
                final var tokens = br.readLine().split(" ");
                H = Integer.parseInt(tokens[0]);
                W = Integer.parseInt(tokens[1]);
            }

            final var lake = new Lake(H, W);
            for (var y = 0; y < H; ++y) {
                final var lakeRow = br.readLine();
                for (var x = 0; x < W; ++x) {
                    lake.map[y][x] = lakeRow.charAt(x);
                    if (lake.map[y][x] == 'L') {
                        lake.map[y][x] = '.';
                        lake.swans.add(y * W + x);
                    }
                }
            }

            return lake;
        }
    }

    public static class LakeSimulator {
        private static int[] dy = { 1, -1, 0, 0 };
        private static int[] dx = { 0, 0, 1, -1 };

        private final Lake lake;
        private final Deque<Ice> ices;
        private final UnionFind regions;
        private final boolean[][] visited;

        private LakeSimulator(final Lake lake) {
            this.lake = lake;
            this.ices = new ArrayDeque<>();
            this.regions = new UnionFind(lake.H * lake.W);
            this.visited = new boolean[lake.H][lake.W];

            init();
        }

        private void dfs(final int y, final int x) {
            visited[y][x] = true;

            for (var i = 0; i < dy.length; ++i) {
                final var ny = y + dy[i];
                final var nx = x + dx[i];
                if (ny < 0 || lake.H <= ny || nx < 0 || lake.W <= nx) {
                    continue;
                }

                if (lake.map[ny][nx] != Lake.ICE) {
                    regions.union(ny * lake.W + nx, y * lake.W + x);
                }

                if (!visited[ny][nx]) {
                    visited[ny][nx] = true;
                    if (lake.map[ny][nx] == Lake.ICE) {
                        ices.addLast(new Ice(ny, nx, regions.find(y * lake.W + x)));
                    } else {
                        dfs(ny, nx);
                    }
                }
            }
        }

        private void init() {
            for (var y = 0; y < lake.H; ++y) {
                for (var x = 0; x < lake.W; ++x) {
                    if (lake.map[y][x] == Lake.WATER && !visited[y][x]) {
                        dfs(y, x);
                    }
                }
            }
        }

        public void update() {
            var numSteps = ices.size();
            while (0 <= --numSteps) {
                final var ice = ices.pollFirst();
                lake.map[ice.y][ice.x] = Lake.WATER;
                regions.union(ice.y * lake.W + ice.x, ice.region);
                dfs(ice.y, ice.x);
            }
        }

        public boolean canSwansMeet() {
            return regions.find(lake.swans.get(0)) == regions.find(lake.swans.get(1));
        }

        public static LakeSimulator of(final Lake lake) {
            return new LakeSimulator(lake);
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final var lake = Lake.from(stdin);
            final var lakeSimulator = LakeSimulator.of(lake);

            var day = 0;
            while (!lakeSimulator.canSwansMeet()) {
                ++day;
                lakeSimulator.update();
            }

            System.out.print(day + "\n");
        }
    }
}
