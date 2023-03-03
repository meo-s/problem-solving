// https://www.acmicpc.net/problem/19237

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class BJ_19237_어른_상어 {
    private static final int[] dy = { -1, 1, 0, 0 };
    private static final int[] dx = { 0, 0, -1, 1 };

    private static final int EMPTY = -1;

    public static class Point {
        public int y;
        public int x;

        public Point(final int y, final int x) {
            this.y = y;
            this.x = x;
        }
    }

    public static class Shark extends Point {
        private final int index;
        private final int[][] priors;
        private int direction;

        public Shark(final int sharkIndex, final int y, final int x) {
            super(y, x);
            index = sharkIndex;
            priors = new int[4][4];
            direction = -1;
        }

        public Shark update(final int[][] smellMap) {
            final var N = smellMap.length;

            outerLoop: for (final var target : new int[] { EMPTY, index }) {
                for (var i = 0; i < dy.length; ++i) {
                    final var ny = y + dy[priors[direction][i]];
                    final var nx = x + dx[priors[direction][i]];
                    if (0 <= ny && ny < N && 0 <= nx && nx < N) {
                        if (smellMap[ny][nx] == target) {
                            direction = priors[direction][i];
                            y = ny;
                            x = nx;
                            break outerLoop;
                        }
                    }
                }
            }

            return this;
        }

        public static Shark from(final int sharkIndex, final BufferedReader br) throws IOException {
            final var shark = new Shark(sharkIndex, 0, 0);
            for (var direction = 0; direction < dy.length; ++direction) {
                final var tokens = br.readLine().split(" ");
                for (int i = 0; i < dy.length; ++i) {
                    shark.priors[direction][i] = Integer.parseInt(tokens[i]) - 1;
                }
            }

            return shark;
        }
    }

    public static class Sea {
        private static class Smell {
            public Point pos;
            public int sharkIndex;
            public int at;

            public Smell(final int y, final int x, final int sharkIndex, final int at) {
                this.pos = new Point(y, x);
                this.sharkIndex = sharkIndex;
                this.at = at;
            }

            public boolean isExpired(final int now, final int K) {
                return K <= (now - at);
            }
        }

        private final int N;
        private final int K;

        private final Shark[][][] sharkMap;
        private int currentSharkMapIndex;

        private final Deque<Smell> smells;
        private final int[][] smellMap;
        private final int[][] latestSmellTimestamps;

        public Sea(final int N, final int K) {
            this.N = N;
            this.K = K;

            sharkMap = new Shark[2][N][N];
            currentSharkMapIndex = 0;

            smells = new ArrayDeque<>();
            smellMap = new int[N][N];
            latestSmellTimestamps = new int[N][N];
            for (final var smellMapRow : smellMap) {
                Arrays.fill(smellMapRow, EMPTY);
            }
        }

        public Shark[][] getSharkMap() {
            return sharkMap[currentSharkMapIndex];
        }

        public int[][] getSmellMap() {
            return smellMap;
        }

        public boolean update(final int now) {
            final var cmap = sharkMap[currentSharkMapIndex];
            final var nmap = sharkMap[currentSharkMapIndex ^ 1];

            for (final var nmapRow : nmap) {
                Arrays.fill(nmapRow, null);
            }

            var numSharks = 0;
            final var newSmells = new ArrayDeque<Smell>();
            for (var y = 0; y < N; ++y) {
                for (var x = 0; x < N; ++x) {
                    if (cmap[y][x] != null) {
                        final var shark = cmap[y][x].update(smellMap);
                        if (nmap[shark.y][shark.x] == null || shark.index < nmap[shark.y][shark.x].index) {
                            newSmells.addLast(new Smell(shark.y, shark.x, shark.index, now));
                            if (nmap[shark.y][shark.x] == null) {
                                ++numSharks;
                                smells.add(newSmells.peekLast());
                                latestSmellTimestamps[shark.y][shark.x] = now;
                            }

                            nmap[shark.y][shark.x] = shark;
                        }
                    }
                }
            }

            while (0 < smells.size() && smells.peekFirst().isExpired(now, K)) {
                final var smell = smells.pollFirst();
                if (latestSmellTimestamps[smell.pos.y][smell.pos.x] == smell.at) {
                    smellMap[smell.pos.y][smell.pos.x] = EMPTY;
                }
            }

            for (final var newSmell : newSmells) {
                smellMap[newSmell.pos.y][newSmell.pos.x] = newSmell.sharkIndex;
            }

            currentSharkMapIndex ^= 1;
            return 1 < numSharks;
        }

        public static Sea from(final BufferedReader br) throws IOException {
            final int N, M, K;
            {
                final var tokens = br.readLine().split(" ");
                N = Integer.parseInt(tokens[0]);
                M = Integer.parseInt(tokens[1]);
                K = Integer.parseInt(tokens[2]);
            }

            final var sharkPositions = new Point[M];
            for (var y = 0; y < N; ++y) {
                final var tokens = br.readLine().split(" ");
                for (var x = 0; x < N; ++x) {
                    final var sharkIndex = Integer.parseInt(tokens[x]) - 1;
                    if (0 <= sharkIndex) {
                        sharkPositions[sharkIndex] = new Point(y, x);
                    }
                }
            }

            final var sea = new Sea(N, K);
            final var sharkDirections = br.readLine().split(" ");
            for (var sharkIndex = 0; sharkIndex < M; ++sharkIndex) {
                final var shark = Shark.from(sharkIndex, br);
                shark.y = sharkPositions[sharkIndex].y;
                shark.x = sharkPositions[sharkIndex].x;
                shark.direction = Integer.parseInt(sharkDirections[sharkIndex]) - 1;
                sea.getSharkMap()[shark.y][shark.x] = shark;
                sea.getSmellMap()[shark.y][shark.x] = sharkIndex;
                sea.smells.addLast(new Smell(shark.y, shark.x, sharkIndex, 0));
            }

            return sea;
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            var now = 0;
            var done = false;
            final var sea = Sea.from(stdin);
            while (now < 1000 && !done) {
                done = !sea.update(++now);
            }

            System.out.print((done ? now : -1) + "\n");
        }
    }
}
