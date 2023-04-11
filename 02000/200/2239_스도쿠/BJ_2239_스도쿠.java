// https://www.acmicpc.net/problem/2239

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_2239_스도쿠 {
    private static boolean dfs(int[][] board, int[] rmask, int[] cmask, int[] smask, int y, int x) {
        if (y == 9)
            return true;
        if (x == 9)
            return dfs(board, rmask, cmask, smask, y + 1, 0);
        if (board[y][x] != 0)
            return dfs(board, rmask, cmask, smask, y, x + 1);

        int bitmask = rmask[y] | cmask[x] | smask[(y / 3) * 3 + x / 3];
        for (int n = 1; n <= 9; ++n) {
            if ((bitmask & (1 << n)) == 0) {
                board[y][x] = n;
                rmask[y] |= 1 << n;
                cmask[x] |= 1 << n;
                smask[(y / 3) * 3 + x / 3] |= 1 << n;
                if (dfs(board, rmask, cmask, smask, y, x + 1))
                    return true;
                rmask[y] ^= 1 << n;
                cmask[x] ^= 1 << n;
                smask[(y / 3) * 3 + x / 3] ^= 1 << n;
            }
        }

        board[y][x] = 0;
        return false;
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int[][] board = new int[9][9];
            for (int y = 0; y < 9; ++y) {
                final String row = stdin.readLine();
                for (int x = 0; x < 9; ++x) {
                    board[y][x] = row.charAt(x) - '0';
                }
            }

            final int[] rmask = new int[9];
            final int[] cmask = new int[9];
            final int[] smask = new int[9];
            for (int y = 0; y < 9; ++y) {
                for (int x = 0; x < 9; ++x) {
                    if (board[y][x] != 0) {
                        rmask[y] |= 1 << board[y][x];
                        cmask[x] |= 1 << board[y][x];
                        smask[(y / 3) * 3 + x / 3] |= 1 << board[y][x];
                    }
                }
            }

            dfs(board, rmask, cmask, smask, 0, 0);

            final StringBuilder sb = new StringBuilder();
            for (final int[] row : board) {
                for (final int v : row)
                    sb.append(v);
                sb.append('\n');
            }

            System.out.println(sb.toString());
        }
    }
}
