// https://www.acmicpc.net/problem/8061

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class BJ_8061_Bitmap {

    private static int H, W;
    private static int[][] bitmap;
    private static int[][] dists;
    private static int[] dy = {-1, 0, 1, 0};
    private static int[] dx = {0, 1, 0, -1};

    private static void initBitmap() throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            {
                final String[] tokens = stdin.readLine().split(" ");
                H = Integer.parseInt(tokens[0]);
                W = Integer.parseInt(tokens[1]);
            }

            bitmap = new int[H][W];
            for (int y = 0; y < H; ++y) {
                final String line = stdin.readLine();
                for (int x = 0; x < W; ++x) {
                    bitmap[y][x] = line.charAt(x) - '0';
                }
            }
        }
    }

    private static void solve() throws IOException {
        dists = new int[H][W];
        for (int y = 0; y < H; ++y) {
            Arrays.fill(dists[y], Integer.MAX_VALUE);
        }

        final Deque<int[]> pixels = new ArrayDeque<>();
        for (int y = 0; y < H; ++y) {
            for (int x = 0; x < W; ++x) {
                if (bitmap[y][x] == 1) {
                    dists[y][x] = 0;
                    pixels.addLast(new int[]{y, x});
                }
            }
        }

        while (!pixels.isEmpty()) {
            final int[] pixel = pixels.pollFirst();
            for (int i = 0; i < dy.length; ++i) {
                final int ny = pixel[0] + dy[i];
                final int nx = pixel[1] + dx[i];
                if (ny < 0 || H <= ny || nx < 0 || W <= nx || dists[ny][nx] != Integer.MAX_VALUE) {
                    continue;
                }

                dists[ny][nx] = dists[pixel[0]][pixel[1]] + 1;
                pixels.addLast(new int[]{ny, nx});
            }
        }
    }

    private static String getDistMap() {
        final StringBuilder distMap = new StringBuilder();
        for (int y = 0; y < H; ++y) {
            for (int x = 0; x < W; ++x) {
                distMap.append(dists[y][x]);
                distMap.append(' ');
            }
            distMap.append('\n');
        }
        return distMap.toString();
    }

    public static void main(final String[] args) throws IOException {
        initBitmap();
        solve();
        System.out.println(getDistMap());
    }

}

