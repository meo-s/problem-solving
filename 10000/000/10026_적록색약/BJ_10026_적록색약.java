// https://www.acmicpc.net/problem/10026

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_10026_적록색약 {
    private static final int[] dy = { 1, -1, 0, 0 };
    private static final int[] dx = { 0, 0, 1, -1 };

    private static final byte RED = 0;
    private static final byte GREEN = 1;
    private static final byte BLUE = 2;

    @FunctionalInterface
    interface ColorComparator {
        boolean isSame(final byte c1, final byte c2);
    }

    public static class RGBCmp {
        public static boolean R_G_B(final byte c1, final byte c2) {
            return c1 == c2;
        }

        public static boolean RG_B(final byte c1, final byte c2) {
            return (c1 + c2 == GREEN) || (c1 == c2);
        }
    }

    public static void dfs(final byte[][] screen, final boolean[][] visited, final int y, final int x,
            final ColorComparator cmp) {
        final int H = screen.length;
        final int W = screen[0].length;

        visited[y][x] = true;

        for (int i = 0; i < dy.length; ++i) {
            final int ny = y + dy[i];
            final int nx = x + dx[i];
            if (0 <= ny && ny < H && 0 <= nx && nx < W) {
                if (visited[ny][nx]) {
                    continue;
                }
                if (cmp.isSame(screen[y][x], screen[ny][nx])) {
                    dfs(screen, visited, ny, nx, cmp);
                }
            }
        }
    }

    public static int distinguish(final byte[][] screen, final ColorComparator cmp) {
        final int H = screen.length;
        final int W = screen[0].length;

        int numRegions = 0;
        final boolean[][] visited = new boolean[H][W];
        for (int y = 0; y < H; ++y) {
            for (int x = 0; x < W; ++x) {
                if (!visited[y][x]) {
                    visited[y][x] = true;
                    dfs(screen, visited, y, x, cmp);
                    ++numRegions;
                }
            }
        }

        return numRegions;
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int N = Integer.parseInt(stdin.readLine());
            final byte[][] screen = new byte[N][N];
            for (int y = 0; y < N; ++y) {
                final String row = stdin.readLine();
                for (int x = 0; x < N; ++x) {
                    screen[y][x] = row.charAt(x) == 'R' ? RED : (row.charAt(x) == 'G' ? GREEN : BLUE);
                }
            }

            System.out.printf("%d %d\n", distinguish(screen, RGBCmp::R_G_B), distinguish(screen, RGBCmp::RG_B));
        }
    }
}