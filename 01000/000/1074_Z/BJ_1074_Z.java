// https://www.acmicpc.net/problem/1074

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_1074_Z {
    public static class Z {
        public static int find(final int N, final int y, final int x) {
            if (y + x == 0) {
                return 0;
            }

            final int halfSize = (int) Math.pow(2, N) / 2;
            final int numCells = halfSize * halfSize;
            if (y < halfSize && x < halfSize) {
                return Z.find(N - 1, y, x);
            }
            if (y < halfSize && halfSize <= x) {
                return numCells + Z.find(N - 1, y, x - halfSize);
            }
            if (halfSize <= y && x < halfSize) {
                return 2 * numCells + Z.find(N - 1, y - halfSize, x);
            }
            {
                return 3 * numCells + Z.find(N - 1, y - halfSize, x - halfSize);
            }
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int N, Y, X;
            {
                final String[] tokens = stdin.readLine().split(" ");
                N = Integer.parseInt(tokens[0]);
                Y = Integer.parseInt(tokens[1]);
                X = Integer.parseInt(tokens[2]);
            }

            System.out.print(Z.find(N, Y, X) + "\n");
        }
    }
}
