// https://www.acmicpc.net/problem/21610

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.stream.IntStream;

public class BJ_21610_마법사_상어와_비바라기 {
    private static final int dy[] = { 0, -1, -1, -1, 0, 1, 1, 1 };
    private static final int dx[] = { -1, -1, 0, 1, 1, 1, 0, -1 };
    private static final int copyDy[] = { -1, -1, 1, 1 };
    private static final int copyDx[] = { -1, 1, 1, -1 };

    public static class Point {
        public int y;
        public int x;

        public Point(final int y, final int x) {
            this.y = y;
            this.x = x;
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {

            final int N, M;
            {
                final String[] tokens = stdin.readLine().split(" ");
                N = Integer.parseInt(tokens[0]);
                M = Integer.parseInt(tokens[1]);
            }

            final var waters = new int[N][N];
            for (var y = 0; y < N; ++y) {
                final var tokens = stdin.readLine().split(" ");
                for (var x = 0; x < N; ++x) {
                    waters[y][x] = Integer.parseInt(tokens[x]);
                }
            }

            final var clouds = new ArrayDeque<Point>();
            for (var i = 0; i < 4; ++i) {
                clouds.add(new Point((N - 1) - i / 2, i % 2));
            }

            final var wasRained = new boolean[N][N];
            for (int m = 0; m < M; ++m) {
                final int d, s;
                {
                    final var tokens = stdin.readLine().split(" ");
                    d = Integer.parseInt(tokens[0]) - 1;
                    s = Integer.parseInt(tokens[1]);
                }

                for (final var cloud : clouds) {
                    final var cy = (N + cloud.y + (s % N) * dy[d]) % N;
                    final var cx = (N + cloud.x + (s % N) * dx[d]) % N;
                    wasRained[cy][cx] = true;
                    waters[cy][cx] += 1;
                }

                while (0 < clouds.size()) {
                    final var y = (N + clouds.peekLast().y + (s % N) * dy[d]) % N;
                    final var x = (N + clouds.pollLast().x + (s % N) * dx[d]) % N;
                    for (var i = 0; i < copyDy.length; ++i) {
                        final var yy = y + copyDy[i];
                        final var xx = x + copyDx[i];
                        if (0 <= yy && yy < N && 0 <= xx && xx < N) {
                            if (0 < waters[yy][xx]) {
                                waters[y][x] += 1;
                            }
                        }
                    }
                }

                for (var y = 0; y < N; ++y) {
                    for (var x = 0; x < N; ++x) {
                        if (!wasRained[y][x] && 2 <= waters[y][x]) {
                            waters[y][x] -= 2;
                            clouds.add(new Point(y, x));
                        }

                        wasRained[y][x] = false;
                    }
                }
            }

            stdout.write(IntStream.range(0, N).map(i -> Arrays.stream(waters[i]).sum()).sum() + "\n");
            stdout.flush();
        }
    }
}
