// https://www.acmicpc.net/problem/20058

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.stream.IntStream;

public class BJ_20058_마법사_상어와_파이어스톰 {
    private static int[] dy = { 1, -1, 0, 0 };
    private static int[] dx = { 0, 0, 1, -1 };

    public static class ArrayTurner {
        private int[][] buf;

        public ArrayTurner(int N) {
            N = (int) Math.pow(2, N);
            buf = new int[N][N];
        }

        private void turn(final int[][] a, final int L, final int y_lb, final int y_ub, final int x_lb,
                final int x_ub) {
            final var N = y_ub - y_lb;
            if (L == 0) {
                for (var y = 0; y < N; ++y) {
                    for (var x = 0; x < N; ++x) {
                        a[y_lb + x][x_lb + (N - 1) - y] = buf[y_lb + y][x_lb + x];
                    }
                }
            } else {
                turn(a, L - 1, y_lb, y_ub - N / 2, x_lb, x_ub - N / 2);
                turn(a, L - 1, y_lb, y_ub - N / 2, x_lb + N / 2, x_ub);
                turn(a, L - 1, y_lb + N / 2, y_ub, x_lb, x_ub - N / 2);
                turn(a, L - 1, y_lb + N / 2, y_ub, x_lb + N / 2, x_ub);
            }
        }

        public void turn(final int[][] a, final int L) {
            for (var i = 0; i < a.length; ++i) {
                System.arraycopy(a[i], 0, buf[i], 0, a[i].length);
            }

            turn(a, L, 0, a.length, 0, a.length);
        }
    }

    public static void melt(final int[][] ice) {
        final var N = ice.length;
        final var prevIce = new int[N][N];
        for (var i = 0; i < N; ++i) {
            System.arraycopy(ice[i], 0, prevIce[i], 0, N);
        }

        for (var y = 0; y < N; ++y) {
            for (var x = 0; x < N; ++x) {
                if (prevIce[y][x] == 0) {
                    continue;
                }

                int numAdjacentIces = 0;
                for (var i = 0; i < dy.length; ++i) {
                    if (y + dy[i] < 0 || N <= y + dy[i] || x + dx[i] < 0 || N <= x + dx[i]) {
                        continue;
                    }
                    if (0 < prevIce[y + dy[i]][x + dx[i]]) {
                        ++numAdjacentIces;
                    }
                }

                if (numAdjacentIces < 3) {
                    --ice[y][x];
                }
            }
        }
    }

    private static int dfs(final int[][] ice, final boolean[][] visited, final int y, final int x) {
        visited[y][x] = true;
        final var N = ice.length;

        var numVisits = 1;
        for (var i = 0; i < dy.length; ++i) {
            final var ny = y + dy[i];
            final var nx = x + dx[i];
            if (0 <= ny && ny < N && 0 <= nx && nx < N) {
                if (0 < ice[ny][nx] && !visited[ny][nx]) {
                    numVisits += dfs(ice, visited, ny, nx);
                }
            }
        }

        return numVisits;
    }

    private static int findLargestIcyArea(final int[][] ice) {
        final var N = ice.length;
        final var visited = new boolean[N][N];
        var maxIcyArea = 0;
        for (var y = 0; y < N; ++y) {
            for (var x = 0; x < N; ++x) {
                if (0 < ice[y][x] && !visited[y][x]) {
                    maxIcyArea = Math.max(maxIcyArea, dfs(ice, visited, y, x));
                }
            }
        }

        return maxIcyArea;
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {

            final int N, Q;
            {
                final String[] tokens = stdin.readLine().split(" ");
                N = Integer.parseInt(tokens[0]);
                Q = Integer.parseInt(tokens[1]);
            }

            final int HW = (int) Math.pow(2, N);
            final var ice = new int[HW][HW];
            for (var y = 0; y < HW; ++y) {
                final var tokens = stdin.readLine().split(" ");
                for (var x = 0; x < HW; ++x) {
                    ice[y][x] = Integer.parseInt(tokens[x]);
                }
            }

            final var arrayTurner = new ArrayTurner(N);
            final var turnLevels = (0 < Q ? stdin.readLine().split(" ") : null);
            for (var turnIndex = 0; turnIndex < Q; ++turnIndex) {
                arrayTurner.turn(ice, N - Integer.parseInt(turnLevels[turnIndex]));
                melt(ice);
            }

            final var totalIces = IntStream.range(0, HW).mapToLong(i -> Arrays.stream(ice[i]).sum()).sum();
            stdout.write(totalIces + "\n");
            stdout.write(findLargestIcyArea(ice) + "\n");
            stdout.flush();
        }
    }
}
