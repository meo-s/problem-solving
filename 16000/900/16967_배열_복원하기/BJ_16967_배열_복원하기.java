// https://www.acmicpc.net/problem/16967

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_16967_배열_복원하기 {

    private static int H, W, Y, X;
    private static int[][] B;
    private static int[][] A;

    private static int readInt(final BufferedReader br) throws IOException {
        int n = 0;
        for (;;) {
            final int b = br.read();
            if (b == ' ' || b == '\n' || b == -1) {
                return n;
            }
            n = n * 10 + (b - '0');
        }
    }

    private static void init() throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            H = readInt(stdin);
            W = readInt(stdin);
            Y = readInt(stdin);
            X = readInt(stdin);
            B = new int[H + Y][W + X];
            for (int y = 0; y < B.length; ++y) {
                for (int x = 0; x < B[y].length; ++x) {
                    B[y][x] = readInt(stdin);
                }
            }
        }
    }

    private static void findA() {
        A = new int[H][W];
        for (int y = 0; y < H; ++y) {
            for (int x = 0; x < W; ++x) {
                if (0 <= y - Y && 0 <= x - X) {
                    A[y][x] = B[y][x] - A[y - Y][x - X];
                } else {
                    A[y][x] = B[y][x];
                }
            }
        }
    }

    private static void printA() {
        final StringBuilder sb = new StringBuilder();
        for (int y = 0; y < H; ++y) {
            for (int x = 0; x < W; ++x) {
                sb.append(A[y][x]);
                sb.append(' ');
            }
            sb.append('\n');
        }

        System.out.print(sb.toString());
    }

    public static void main(final String[] args) throws IOException {
        init();
        findA();
        printA();
    }

}
