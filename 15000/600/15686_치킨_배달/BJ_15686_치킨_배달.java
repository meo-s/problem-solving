// https://www.acmicpc.net/problem/15686

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class BJ_15686_치킨_배달 {
    public static class Point {
        public int y;
        public int x;

        public Point(final int y, final int x) {
            this.y = y;
            this.x = x;
        }
    }

    public static class Combination {
        @FunctionalInterface
        public static interface Callback {
            void consume(final int bitmask);
        }

        private final int n;
        private final int r;
        private final Callback callback;

        private Combination(final int n, final int r, final Callback callback) {
            this.n = n;
            this.r = r;
            this.callback = callback;
        }

        public void pick(final int offset, final int depth, final int bitmask) {
            if (depth == r) {
                callback.consume(bitmask);
            } else {
                for (int i = offset; i < n; ++i) {
                    pick(i + 1, depth + 1, bitmask | (1 << i));
                }
            }
        }

        public void pick() {
            pick(0, 0, 0);
        }
    }

    public static class ChickenFranchise {
        private List<Point> houses;
        private List<Point> chickenStores;
        private int optimalChickenDist;

        public ChickenFranchise(final List<Point> houses, final List<Point> chickenStores) {
            this.houses = houses;
            this.chickenStores = chickenStores;
            this.optimalChickenDist = Integer.MAX_VALUE;
        }

        private void measureChickenDistance(final int bitmask) {
            int totalChickenDist = 0;
            for (final Point house : houses) {
                int minChickenDist = Integer.MAX_VALUE;
                for (int i = 0; i < chickenStores.size(); ++i) {
                    if ((bitmask & (1 << i)) == 0) {
                        continue;
                    }

                    final int cy = chickenStores.get(i).y;
                    final int cx = chickenStores.get(i).x;
                    minChickenDist = Math.min(minChickenDist, Math.abs(cy - house.y) + Math.abs(cx - house.x));
                }
                
                totalChickenDist += minChickenDist;
            }

            optimalChickenDist = Math.min(optimalChickenDist, totalChickenDist);
        }

        public int plan(final int numAvailables) {
            new Combination(chickenStores.size(), numAvailables, this::measureChickenDistance).pick();
            return optimalChickenDist;
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {

            final int N, M;
            {
                final String[] tokens = stdin.readLine().split(" ");
                N = Integer.parseInt(tokens[0]);
                M = Integer.parseInt(tokens[1]);
            }

            final List<Point> houses = new ArrayList<>();
            final List<Point> chickenStores = new ArrayList<>();
            for (int y = 0; y < N; ++y) {
                final String row = stdin.readLine();
                for (int x = 0; x < N; ++x) {
                    switch (row.charAt(x * 2)) {
                        case '1':
                            houses.add(new Point(y, x));
                            break;
                        case '2':
                            chickenStores.add(new Point(y, x));
                            break;
                    }
                }
            }

            stdout.write(new ChickenFranchise(houses, chickenStores).plan(M) + "\n");
            stdout.flush();
        }
    }
}