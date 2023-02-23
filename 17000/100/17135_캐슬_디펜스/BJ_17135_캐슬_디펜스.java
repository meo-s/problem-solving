// https://www.acmicpc.net/problem/17135

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BJ_17135_캐슬_디펜스 {
    public static class Combination {
        @FunctionalInterface
        public static interface CombinationCallback {
            void consume(final int[] picks);
        }

        private CombinationCallback callback;
        private int[] picks;
        private int n;

        public Combination(final int n, final int r, final CombinationCallback callback) {
            this.callback = callback;
            this.picks = new int[r];
            this.n = n;
        }

        private void pick(final int offset, final int depth) {
            if (depth == this.picks.length) {
                callback.consume(picks);
                return;
            }

            for (int i = offset; i < n; ++i) {
                picks[depth] = i;
                pick(i + 1, depth + 1);
            }
        }

        public void pick() {
            pick(0, 0);
        }
    }

    public static class Point {
        public int y;
        public int x;

        public Point(final int y, final int x) {
            this.y = y;
            this.x = x;
        }

        public Point clone() {
            return new Point(y, x);
        }

        public int distanceOf(final Point other) {
            return Math.abs(other.y - y) + Math.abs(other.x - x);
        }
    }

    public static class Game {
        private List<Point> initialEnemyStates;
        private int H;
        private int W;
        private int D;
        private int maxScore;

        public Game(final int H, final int W, final int D) {
            this.initialEnemyStates = new ArrayList<>();
            this.H = H;
            this.W = W;
            this.D = D;
            this.maxScore = 0;
        }

        public int simulate(final Point[] archers) {
            final Point[] enemies = new Point[initialEnemyStates.size()];
            for (int i = 0; i < initialEnemyStates.size(); ++i) {
                enemies[i] = initialEnemyStates.get(i).clone();
            }

            int numKills = 0;
            int numEnemies = enemies.length;
            int[] targets = new int[archers.length];
            while (0 < numEnemies) {
                Arrays.fill(targets, -1);

                for (int enemyIndex = 0; enemyIndex < enemies.length; ++enemyIndex) {
                    if (enemies[enemyIndex] == null) {
                        continue;
                    }

                    for (int archerIndex = 0; archerIndex < archers.length; ++archerIndex) {
                        final int dist = archers[archerIndex].distanceOf(enemies[enemyIndex]);
                        if (dist <= D) {
                            final int targetIndex = targets[archerIndex];
                            if (targetIndex == -1) {
                                targets[archerIndex] = enemyIndex;
                                continue;
                            }

                            final int targetDist = archers[archerIndex].distanceOf(enemies[targetIndex]);
                            if (dist < targetDist) {
                                targets[archerIndex] = enemyIndex;
                            } else if (dist == targetDist) {
                                if (enemies[enemyIndex].x < enemies[targetIndex].x) {
                                    targets[archerIndex] = enemyIndex;
                                }
                            }
                        }
                    }
                }

                for (int archerIndex = 0; archerIndex < archers.length; ++archerIndex) {
                    if (0 <= targets[archerIndex]) {
                        if (enemies[targets[archerIndex]] != null) {
                            ++numKills;
                            --numEnemies;
                            enemies[targets[archerIndex]] = null;
                        }
                    }
                }

                for (int enemyIndex = 0; enemyIndex < enemies.length; ++enemyIndex) {
                    if (enemies[enemyIndex] != null) {
                        if (H <= ++enemies[enemyIndex].y) {
                            --numEnemies;
                            enemies[enemyIndex] = null;
                        }
                    }
                }
            } // while (0 < numEnemies) { ... }

            return numKills;
        }

        public void updateMaximumScore(final int[] archerX) {
            final Point[] archers = new Point[3];
            for (int i = 0; i < archerX.length; ++i) {
                archers[i] = new Point(H, archerX[i]);
            }

            maxScore = Math.max(maxScore, simulate(archers));
        }

        public int getMaximumScore() {
            maxScore = 0;
            final Combination combi = new Combination(W, 3, this::updateMaximumScore);
            combi.pick();
            return maxScore;
        }

        public static Game from(final BufferedReader br) throws IOException {
            final int H, W, D;
            {
                final String[] tokens = br.readLine().split(" ");
                H = Integer.parseInt(tokens[0]);
                W = Integer.parseInt(tokens[1]);
                D = Integer.parseInt(tokens[2]);
            }

            final Game game = new Game(H, W, D);
            for (int y = 0; y < H; ++y) {
                final String[] tokens = br.readLine().split(" ");
                for (int x = 0; x < W; ++x) {
                    if (tokens[x].charAt(0) == '1') {
                        game.initialEnemyStates.add(new Point(y, x));
                    }
                }
            }

            return game;
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print(Game.from(stdin).getMaximumScore() + "\n");
        }
    }
}