// https://www.acmicpc.net/problem/3109

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.IntStream;

public class BJ_3109_빵집 {

    public static class PipelineBuilder {
        private static final int[] dy = { -1, 0, 1 };

        private final byte[][] map;

        public PipelineBuilder(final byte[][] map) {
            this.map = map;
        }

        private boolean dfs(final int y, final int x) {
            map[y][x] = 1;

            if (x == map[0].length - 1) {
                return true;
            }

            for (int i = 0; i < dy.length; ++i) {
                if (0 <= y + dy[i] && y + dy[i] < map.length) {
                    if (map[y + dy[i]][x + 1] == 0) {
                        if (dfs(y + dy[i], x + 1)) {
                            return true;
                        }
                    }
                }
            }

            return false;
        }

        public int build(final int y) {
            return map[y][0] == 0 && dfs(y, 0) ? 1 : 0;
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int H, W;
            {
                final String[] tokens = stdin.readLine().split(" ");
                H = Integer.parseInt(tokens[0]);
                W = Integer.parseInt(tokens[1]);
            }

            final byte[][] map = new byte[H][W];
            for (int y = 0; y < H; ++y) {
                final String row = stdin.readLine();
                for (int x = 0; x < W; ++x) {
                    map[y][x] = (byte) (row.charAt(x) == '.' ? 0 : -1);
                }
            }

            final PipelineBuilder pipelineBuilder = new PipelineBuilder(map);
            System.out.print(IntStream.range(0, H).mapToLong(pipelineBuilder::build).sum() + "\n");
        }
    }
}