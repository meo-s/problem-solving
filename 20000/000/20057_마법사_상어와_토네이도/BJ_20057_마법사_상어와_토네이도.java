// https://www.acmicpc.net/problem/20057

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.util.Deque;

public class BJ_20057_마법사_상어와_토네이도 {

    private static final int[] dy = { 0, 1, 0, -1 };
    private static final int[] dx = { 1, 0, -1, 0 };

    private static final int[][][] w = {
            { // E
                    { 0, 0, 2, 0, 0 },
                    { 0, 1, 7, 10, 0 },
                    { 0, 0, 0, 0, 5 },
                    { 0, 1, 7, 10, 0 },
                    { 0, 0, 2, 0, 0 },
            },
            { // S
                    { 0, 0, 0, 0, 0 },
                    { 0, 1, 0, 1, 0 },
                    { 2, 7, 0, 7, 2 },
                    { 0, 10, 0, 10, 0 },
                    { 0, 0, 5, 0, 0 },
            },
            { // W
                    { 0, 0, 2, 0, 0 },
                    { 0, 10, 7, 1, 0 },
                    { 5, 0, 0, 0, 0 },
                    { 0, 10, 7, 1, 0 },
                    { 0, 0, 2, 0, 0 },
            },
            { // N
                    { 0, 0, 5, 0, 0 },
                    { 0, 10, 0, 10, 0 },
                    { 2, 7, 0, 7, 2 },
                    { 0, 1, 0, 1, 0 },
                    { 0, 0, 0, 0, 0 },
            },
    };

    public static class SnailPoint {
        public int y;
        public int x;
        public int dir;

        public SnailPoint(final int y, final int x, final int dir) {
            this.y = y;
            this.x = x;
            this.dir = dir;
        }

        public static Deque<SnailPoint> get(final int N) {
            final var pts = new ArrayDeque<SnailPoint>();
            int y_lb = 0, y_ub = N, x_lb = 0, x_ub = N;
            int y = 0, x = 0;
            int dir = 0;
            for (;;) {
                pts.addFirst(new SnailPoint(y, x, (dir + 2) % 4));

                if (y == N / 2 && x == N / 2) {
                    break;
                }

                if (y + dy[dir] < y_lb || y_ub <= y + dy[dir] || x + dx[dir] < x_lb || x_ub <= x + dx[dir]) {
                    dir = (dir + 1) % dy.length;
                    switch (dir) {
                        case 0:
                            ++x_lb;
                            break;
                        case 1:
                            ++y_lb;
                            break;
                        case 2:
                            --x_ub;
                            break;
                        case 3:
                            --y_ub;
                            break;
                    }
                }

                y += dy[dir];
                x += dx[dir];
            }

            return pts;
        }
    }

    public static int deposit(final int[][] ground, final int y, final int x, final int dir, final int amount) {
        final var N = ground.length;

        var overflows = 0;
        var totalDeltaAmount = 0;
        for (var ny = y - 2; ny < y + 3; ++ny) {
            for (var nx = x - 2; nx < x + 3; ++nx) {
                final var deltaAmount = amount * w[dir][ny - y + 2][nx - x + 2] / 100;
                totalDeltaAmount += deltaAmount;
                if (ny < 0 || N <= ny || nx < 0 || N <= nx) {
                    overflows += deltaAmount;
                } else {
                    ground[ny][nx] += deltaAmount;
                }
            }
        }

        if (y + dy[dir] < 0 || N <= y + dy[dir] || x + dx[dir] < 0 || N <= x + dx[dir]) {
            overflows += amount - totalDeltaAmount;
        } else {
            ground[y + dy[dir]][x + dx[dir]] += amount - totalDeltaAmount;
        }

        return overflows;
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {

            final var N = Integer.parseInt(stdin.readLine());
            final var ground = new int[N][N];
            for (var y = 0; y < N; ++y) {
                final var tokens = stdin.readLine().split(" ");
                for (var x = 0; x < N; ++x) {
                    ground[y][x] = Integer.parseInt(tokens[x]);
                }
            }

            var totalOverflows = 0;
            for (final var pt : SnailPoint.get(N)) {
                final var ny = pt.y + dy[pt.dir];
                final var nx = pt.x + dx[pt.dir];
                if (0 <= ny && ny < N && 0 <= nx && nx < N) {
                    totalOverflows += deposit(ground, ny, nx, pt.dir, ground[ny][nx]);
                    ground[ny][nx] = 0;
                }
            }

            stdout.write(totalOverflows + "\n");
            stdout.flush();
        }
    }
}
