// https://www.acmicpc.net/problem/23290

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.stream.IntStream;

public class BJ_23290_마법사_상어와_복제 {

    public static class Combination {
        final int n;
        final int r;
        final int[] picks;
        boolean hasNext;

        public Combination(final int n, final int r) {
            this.n = n;
            this.r = r;
            this.picks = new int[r];
            this.hasNext = true;
        }

        public boolean hasNext() {
            return hasNext;
        }

        public int[] get() {
            return picks;
        }

        public boolean next(final int i) {
            if (n <= ++picks[i]) {
                if (0 < i && next(i - 1)) {
                    picks[i] = 0;
                } else {
                    hasNext = false;
                }
            }
            return hasNext;
        }

        public boolean next() {
            return next(r - 1);
        }
    }

    public static class Point {
        public int y;
        public int x;

        public Point(final int y, final int x) {
            this.y = y;
            this.x = x;
        }
    }

    public static class Fish extends Point {
        private static final int[] dy = { 0, -1, -1, -1, 0, 1, 1, 1 };
        private static final int[] dx = { -1, -1, 0, 1, 1, 1, 0, -1 };
        public int dir;

        public Fish(final int y, final int x, final int dir) {
            super(y, x);
            this.dir = dir;
        }

        public Fish clone() {
            return new Fish(y, x, dir);
        }

        public void update(final Sea sea) {
            for (var i = 0; i < dy.length; ++i) {
                final var ny = y + dy[dir];
                final var nx = x + dx[dir];
                if (0 <= ny && ny < Sea.H && 0 <= nx && nx < Sea.W) {
                    if (!sea.hasSmell(ny, nx) && !sea.isSharkAt(ny, nx)) {
                        y = ny;
                        x = nx;
                        break;
                    }
                }
                dir = (dir + dy.length - 1) % dy.length;
            }
        }
    }

    public static class Shark extends Point {
        private static final int dy[] = { -1, 0, 1, 0 };
        private static final int dx[] = { 0, -1, 0, 1 };

        public Shark(final int y, final int x) {
            super(y, x);
        }

        private int[] findOptimalPath(final Sea sea) {
            final var optimalPath = new int[] { 0, 0, 0 };
            var optimalPathScore = -1;

            final var visited = new boolean[Sea.H][Sea.W];
            final var combi = new Combination(dy.length, 3);
            while (combi.hasNext()) {
                for (var y = 0; y < Sea.H; ++y) {
                    Arrays.fill(visited[y], false);
                }

                var isValidPath = true;
                var pathScore = 0;
                var ny = y;
                var nx = x;
                for (final var dir : combi.get()) {
                    ny += dy[dir];
                    nx += dx[dir];
                    if (ny < 0 || Sea.H <= ny || nx < 0 || Sea.W <= nx) {
                        isValidPath = false;
                        break;
                    }

                    if (!visited[ny][nx]) {
                        visited[ny][nx] = true;
                        pathScore += sea.getFishCountAt(ny, nx);
                    }
                }

                if (isValidPath && optimalPathScore < pathScore) {
                    System.arraycopy(combi.get(), 0, optimalPath, 0, optimalPath.length);
                    optimalPathScore = pathScore;
                }

                combi.next();
            }

            return optimalPath;
        }

        public void update(final Sea sea) {
            final var path = findOptimalPath(sea);

            for (final var dir : path) {
                y += dy[dir];
                x += dx[dir];
                sea.killFishsAt(y, x);
            }
        }
    }

    public static class Sea {
        private static final int H = 4;
        private static final int W = 4;

        private final Shark shark;
        private final int[][] smells;
        private final List<List<Deque<Fish>>> fishes;

        public Sea() {
            shark = new Shark(-1, -1);
            smells = new int[H][W];
            fishes = new ArrayList<>();
            for (var y = 0; y < H; ++y) {
                fishes.add(new ArrayList<>());
                for (var x = 0; x < W; ++x) {
                    fishes.get(y).add(new ArrayDeque<>());
                }
            }
        }

        public boolean hasSmell(final int y, final int x) {
            return 0 < smells[y][x];
        }

        public boolean isSharkAt(final int y, final int x) {
            return shark.y == y && shark.x == x;
        }

        public int getFishCountAt(final int y, final int x) {
            return fishes.get(y).get(x).size();
        }

        public int getFishCount() {
            return IntStream.range(0, H * W).map(i -> getFishCountAt(i / W, i % W)).sum();
        }

        public void killFishsAt(final int y, final int x) {
            final var localFishes = fishes.get(y).get(x);
            if (0 < localFishes.size()) {
                localFishes.clear();
                smells[y][x] = 3;
            }
        }

        public void update() {
            final var clonedFishes = new ArrayDeque<Fish>();
            final var fishPendings = new ArrayDeque<Fish>();
            for (var y = 0; y < H; ++y) {
                for (var x = 0; x < W; ++x) {
                    var localFishes = fishes.get(y).get(x);
                    while (0 < localFishes.size()) {
                        final var fish = localFishes.poll();
                        clonedFishes.add(fish.clone());

                        fish.update(this);
                        fishPendings.add(fish);
                    }
                }
            }

            while (0 < fishPendings.size()) {
                final var fish = fishPendings.poll();
                fishes.get(fish.y).get(fish.x).add(fish);
            }

            shark.update(this);

            for (final var smellRow : smells) {
                for (var x = 0; x < smellRow.length; ++x) {
                    smellRow[x] = Math.max(smellRow[x] - 1, 0);
                }
            }

            while (0 < clonedFishes.size()) {
                final var fish = clonedFishes.poll();
                fishes.get(fish.y).get(fish.x).add(fish);
            }
        }

        public static Sea from(final BufferedReader br, final int M) throws IOException {
            final var sea = new Sea();
            for (var i = 0; i < M; ++i) {
                final var tokens = br.readLine().split(" ");
                final var y = Integer.parseInt(tokens[0]) - 1;
                final var x = Integer.parseInt(tokens[1]) - 1;
                final var dir = Integer.parseInt(tokens[2]) - 1;
                sea.fishes.get(y).get(x).add(new Fish(y, x, dir));
            }

            {
                final var tokens = br.readLine().split(" ");
                sea.shark.y = Integer.parseInt(tokens[0]) - 1;
                sea.shark.x = Integer.parseInt(tokens[1]) - 1;
            }

            return sea;
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int M, S;
            {
                final var tokens = stdin.readLine().split(" ");
                M = Integer.parseInt(tokens[0]);
                S = Integer.parseInt(tokens[1]);
            }

            final var sea = Sea.from(stdin, M);
            for (var i = 0; i < S; ++i) {
                sea.update();
            }

            System.out.print(sea.getFishCount() + "\n");
        }
    }
}
