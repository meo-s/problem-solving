// https://www.acmicpc.net/problem/1080

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_1080_행렬 {

    public static void flip(final byte[][] mat, final int y, final int x) {
        for (int dy = 0; dy < 3; ++dy) {
            for (int dx = 0; dx < 3; ++dx) {
                mat[y + dy][x + dx] ^= 1;
            }
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int H, W;
            {
                final String[] tokens = stdin.readLine().split(" ");
                H = Integer.parseInt(tokens[0]);
                W = Integer.parseInt(tokens[1]);
            }

            final byte[][][] mat = new byte[2][H][W];
            for (int i = 0; i < 2; ++i) {
                for (int y = 0; y < H; ++y) {
                    final String row = stdin.readLine();
                    for (int x = 0; x < W; ++x) {
                        mat[i][y][x] = (byte) (row.charAt(x) - '0');
                    }
                }
            }

            int numFlips = 0;
            for (int y = 0; y < H - 2; ++y) {
                for (int x = 0; x < W - 2; ++x) {
                    if (mat[0][y][x] != mat[1][y][x]) {
                        flip(mat[0], y, x);
                        ++numFlips;
                    }
                }
            }

            boolean isSame = true;
            outerLoop: for (int y = 0; y < H; ++y) {
                for (int x = 0; x < W; ++x) {
                    if (mat[0][y][x] != mat[1][y][x]) {
                        isSame = false;
                        break outerLoop;
                    }
                }
            }

            stdout.write((isSame ? numFlips : -1) + "\n");
            stdout.flush();
        }
    }
}