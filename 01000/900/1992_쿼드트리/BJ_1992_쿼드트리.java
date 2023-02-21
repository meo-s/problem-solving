// https://www.acmicpc.net/problem/1992

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_1992_쿼드트리 {
    public static class QuadTree {
        private StringBuilder compressed;
        private int[][] img;

        public QuadTree(final int[][] img) {
            this.compressed = new StringBuilder();
            this.img = img;
        }

        public QuadTree compress(final int y_lb, final int y_ub, final int x_lb, final int x_ub) {
            boolean needToDivide = false;
            outerLoop: for (int y = y_lb; y < y_ub; ++y) {
                for (int x = x_lb; x < x_ub; ++x) {
                    if (img[y][x] != img[y_lb][x_lb]) {
                        needToDivide = true;
                        break outerLoop;
                    }
                }
            }

            if (needToDivide) {
                final int y_mid = (y_lb + y_ub) / 2;
                final int x_mid = (x_lb + x_ub) / 2;
                compressed.append('(');
                compress(y_lb, y_mid, x_lb, x_mid);
                compress(y_lb, y_mid, x_mid, x_ub);
                compress(y_mid, y_ub, x_lb, x_mid);
                compress(y_mid, y_ub, x_mid, x_ub);
                compressed.append(')');
            } else {
                compressed.append(img[y_lb][x_lb]);
            }

            return this;
        }

        public String toString() {
            return compressed.toString();
        }

        public static String compress(final int[][] img) {
            return new QuadTree(img).compress(0, img.length, 0, img.length).toString();
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int N = Integer.parseInt(stdin.readLine());
            final int[][] img = new int[N][N];
            for (int y = 0; y < N; ++y) {
                final String row = stdin.readLine();
                for (int x = 0; x < N; ++x) {
                    img[y][x] = row.charAt(x) - '0';
                }
            }

            System.out.println(QuadTree.compress(img));
        }
    }
}
