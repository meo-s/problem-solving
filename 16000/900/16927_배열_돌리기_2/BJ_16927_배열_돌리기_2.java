// https://www.acmicpc.net/problem/16927

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class BJ_16927_배열_돌리기_2 {
    public static class TurnnedArray {
        private static final int[] dy = { 0, 1, 0, -1 };
        private static final int[] dx = { 1, 0, -1, 0 };

        private static List<Integer> disclose(final int[][] a, final int level) {
            final int x_lb = level;
            final int y_lb = level;
            final int y_ub = a.length - level;
            final int x_ub = a[0].length - level;

            int x = x_lb;
            int y = y_lb;
            int i = 0;
            final List<Integer> elements = new ArrayList<>();
            for (;;) {
                elements.add(a[y][x]);
                if (y + dy[i] < y_lb || y_ub <= y + dy[i] || x + dx[i] < x_lb || x_ub <= x + dx[i]) {
                    if (++i == 4) {
                        elements.remove(elements.size() - 1);
                        break;
                    }
                }

                y += dy[i];
                x += dx[i];
            }

            return elements;
        }

        private static void enclose(final int[][] a, final int level, final List<Integer> elements, final int offset) {
            final int x_lb = level;
            final int y_lb = level;
            final int y_ub = a.length - level;
            final int x_ub = a[0].length - level;

            int x = x_lb;
            int y = y_lb;
            int i = 0;
            int elementIndex = 0;
            for (;;) {
                a[y][x] = elements.get((offset + elementIndex++) % elements.size());
                if (y + dy[i] < y_lb || y_ub <= y + dy[i] || x + dx[i] < x_lb || x_ub <= x + dx[i]) {
                    if (++i == 4) {
                        break;
                    }
                }

                y += dy[i];
                x += dx[i];
            }
        }

        public static int[][] from(final BufferedReader br) throws IOException {
            final int H, W, R;
            {
                final String[] tokens = br.readLine().split(" ");
                H = Integer.parseInt(tokens[0]);
                W = Integer.parseInt(tokens[1]);
                R = Integer.parseInt(tokens[2]);
            }

            final int[][] a = new int[H][W];
            for (int y = 0; y < H; ++y) {
                final String[] tokens = br.readLine().split(" ");
                for (int x = 0; x < W; ++x) {
                    a[y][x] = Integer.parseInt(tokens[x]);
                }
            }

            int numProcesseds = 0;
            for (int level = 0; numProcesseds < H * W; ++level) {
                final List<Integer> elements = disclose(a, level);
                enclose(a, level, elements, R % elements.size());
                numProcesseds += elements.size();
            }

            return a;
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {

            for (final int[] row : TurnnedArray.from(stdin)) {
                for (final int element : row) {
                    stdout.write(element + " ");
                }
                stdout.write("\n");
            }

            stdout.flush();
        }
    }
}
