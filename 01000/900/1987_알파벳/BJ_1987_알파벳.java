// https://www.acmicpc.net/problem/1987

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_1987_알파벳 {
    private static final int[] dy = { 1, -1, 0, 0 };
    private static final int[] dx = { 0, 0, 1, -1 };

    public static class AlphabetBoard {
        private char[][] board;
        private int H;
        private int W;

        public AlphabetBoard(final int H, final int W) {
            this.board = new char[H][W];
            this.H = H;
            this.W = W;
        }

        public int dfs(final int y, final int x, int depth, int visited) {
            int maxLen = depth;
            visited |= 1 << (int) (board[y][x] - 'A');
            for (int i = 0; i < dy.length; ++i) {
                final int ny = y + dy[i];
                final int nx = x + dx[i];
                if (ny < 0 || H <= ny || nx < 0 || W <= nx) {
                    continue;
                }

                final int alphamask = 1 << (int) (board[ny][nx] - 'A');
                if ((visited & alphamask) == 0) {
                    maxLen = Math.max(maxLen, dfs(ny, nx, depth + 1, visited));
                }
            }

            return maxLen;
        }

        public int findMaximumLength() {
            return dfs(0, 0, 1, 0);
        }

        public static AlphabetBoard from(final BufferedReader br) throws IOException {
            final int H, W;
            {
                final String[] tokens = br.readLine().split(" ");
                H = Integer.parseInt(tokens[0]);
                W = Integer.parseInt(tokens[1]);
            }

            final AlphabetBoard ret = new AlphabetBoard(H, W);
            for (int y = 0; y < H; ++y) {
                final String boardRow = br.readLine();
                for (int x = 0; x < W; ++x) {
                    ret.board[y][x] = boardRow.charAt(x);
                }
            }

            return ret;
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print(AlphabetBoard.from(stdin).findMaximumLength() + "\n");
        }
    }
}
