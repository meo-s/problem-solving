// https://www.acmicpc.net/problem/3109

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class BJ_3109 {
    public static class Point2<T> {
        public Point2(final T y_, final T x_) {
            y = y_;
            x = x_;
        }

        public Point2<T> clone() {
            return new Point2<>(y, x);
        }

        public T y;
        public T x;
    }

    public static class PipelineBuilder {
        public static boolean build(final char[][] map, final boolean[][] visited, final int rootRow) {
            return search(map, visited, rootRow, 0);
        }

        private static boolean search(final char[][] map, final boolean[][] visited, final int r, final int c) {
            visited[r][c] = true;

            if (c + 1 == map[0].length) {
                return true;
            }

            for (var i = 0; i < dr.length; ++i) {
                if (r + dr[i] < 0 || map.length <= r + dr[i]) {
                    continue;
                }

                if (map[r + dr[i]][c + 1] == 'x') {
                    continue;
                }

                if (!visited[r + dr[i]][c + 1]) {
                    if (search(map, visited, r + dr[i], c + 1)) {
                        return true;
                    }
                }
            }

            return false;
        }

        private static int[] dr = { -1, 0, 1 };
    }

    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        int R, C;
        {
            var tokens = new StringTokenizer(stdin.readLine());
            R = Integer.parseInt(tokens.nextToken());
            C = Integer.parseInt(tokens.nextToken());
        }

        final char[][] map = new char[R][C];
        for (var h = 0; h < R; ++h) {
            final var row = stdin.readLine();
            for (var w = 0; w < C; ++w) {
                map[h][w] = row.charAt(w);
            }
        }

        var n_pipelines = 0;
        final boolean[][] visited = new boolean[R][C];
        for (var h = 0; h < R; ++h) {
            if (PipelineBuilder.build(map, visited, h)) {
                ++n_pipelines;
            }
        }

        stdout.write(n_pipelines + "");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
