// https://www.acmicpc.net/problem/16935

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_16935_배열_돌리기_3 {
    public static class ArrayRotator {
        @FunctionalInterface
        static interface ArrayOp {
            void apply();
        }

        private int arrayIndex;
        private int[][][] array;
        private int[] height;
        private int[] width;

        private ArrayRotator(final int H, final int W) {
            arrayIndex = 0;
            array = new int[2][Math.max(H, W)][Math.max(H, W)];
            height = new int[] { H, H };
            width = new int[] { W, W };
        }

        private int[][] getSrcArray() {
            return array[arrayIndex];
        }

        private int[][] getDstArray() {
            return array[arrayIndex ^ 1];
        }

        public int getHeight() {
            return height[arrayIndex];
        }

        public int getWidth() {
            return width[arrayIndex];
        }

        public int[][] getArray() {
            return getSrcArray();
        }

        public void op1() {
            final int[][] in = getSrcArray();
            final int[][] out = getDstArray();

            final int H = height[arrayIndex];
            final int W = width[arrayIndex];
            for (int y = 0; y < H; ++y) {
                for (int x = 0; x < W; ++x) {
                    out[H - (y + 1)][x] = in[y][x];
                }
            }

            arrayIndex ^= 1;
            height[arrayIndex] = H;
            width[arrayIndex] = W;
        }

        public void op2() {
            final int[][] in = getSrcArray();
            final int[][] out = getDstArray();

            final int H = height[arrayIndex];
            final int W = width[arrayIndex];
            for (int y = 0; y < H; ++y) {
                for (int x = 0; x < W; ++x) {
                    out[y][W - (x + 1)] = in[y][x];
                }
            }

            arrayIndex ^= 1;
            height[arrayIndex] = H;
            width[arrayIndex] = W;
        }

        public void op3() {
            final int[][] in = getSrcArray();
            final int[][] out = getDstArray();

            final int H = height[arrayIndex];
            final int W = width[arrayIndex];
            for (int y = 0; y < H; ++y) {
                for (int x = 0; x < W; ++x) {
                    out[x][H - (y + 1)] = in[y][x];
                }
            }

            arrayIndex ^= 1;
            height[arrayIndex] = W;
            width[arrayIndex] = H;
        }

        public void op4() {
            final int[][] in = getSrcArray();
            final int[][] out = getDstArray();

            final int H = height[arrayIndex];
            final int W = width[arrayIndex];
            for (int y = 0; y < H; ++y) {
                for (int x = 0; x < W; ++x) {
                    out[W - (x + 1)][y] = in[y][x];
                }
            }

            arrayIndex ^= 1;
            height[arrayIndex] = W;
            width[arrayIndex] = H;
        }

        private int getQuadrant(final int y, final int x) {
            final int H = getHeight();
            final int W = getWidth();
            if (y < H / 2 && x < W / 2) {
                return 1;
            } else if (y < H / 2 && W / 2 <= x) {
                return 2;
            } else if (H / 2 <= y && W / 2 <= x) {
                return 3;
            } else {
                return 4;
            }
        }

        public void op5() {
            final int[][] in = getSrcArray();
            final int[][] out = getDstArray();

            final int H = height[arrayIndex];
            final int W = width[arrayIndex];
            final int halfH = H / 2;
            final int halfW = W / 2;
            final int[] dy = { 0, halfH, 0, -halfH };
            final int[] dx = { halfW, 0, -halfW, 0 };
            for (int y = 0; y < H; ++y) {
                for (int x = 0; x < W; ++x) {
                    final int quadrant = getQuadrant(y, x) - 1;
                    out[y + dy[quadrant]][x + dx[quadrant]] = in[y][x];
                }
            }

            arrayIndex ^= 1;
            height[arrayIndex] = H;
            width[arrayIndex] = W;
        }

        public void op6() {
            final int[][] in = getSrcArray();
            final int[][] out = getDstArray();

            final int H = height[arrayIndex];
            final int W = width[arrayIndex];
            final int halfH = H / 2;
            final int halfW = W / 2;
            final int[] dy = { halfH, 0, -halfH, 0 };
            final int[] dx = { 0, -halfW, 0, halfW };
            for (int y = 0; y < H; ++y) {
                for (int x = 0; x < W; ++x) {
                    final int quadrant = getQuadrant(y, x) - 1;
                    out[y + dy[quadrant]][x + dx[quadrant]] = in[y][x];
                }
            }

            arrayIndex ^= 1;
            height[arrayIndex] = H;
            width[arrayIndex] = W;
        }

        public static ArrayRotator from(final BufferedReader br) throws IOException {
            final int H, W, R;
            {
                final String[] tokens = br.readLine().split(" ");
                H = Integer.parseInt(tokens[0]);
                W = Integer.parseInt(tokens[1]);
                R = Integer.parseInt(tokens[2]);
            }

            final ArrayRotator ar = new ArrayRotator(H, W);

            final int[][] a = ar.getArray();
            for (int y = 0; y < ar.getHeight(); ++y) {
                final String[] tokens = br.readLine().split(" ");
                for (int x = 0; x < ar.getWidth(); ++x) {
                    a[y][x] = Integer.parseInt(tokens[x]);
                }
            }

            final ArrayOp[] arrayOps = { ar::op1, ar::op2, ar::op3, ar::op4, ar::op5, ar::op6 };
            final String[] operationIndices = br.readLine().split(" ");
            for (int i = 0; i < R; ++i) {
                arrayOps[Integer.parseInt(operationIndices[i]) - 1].apply();
            }

            return ar;
        }
    }

    public static void main(final String[] args) throws IOException {

        try (final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final ArrayRotator arrayRotator = ArrayRotator.from(stdin);
            final int[][] a = arrayRotator.getArray();
            for (int y = 0; y < arrayRotator.getHeight(); ++y) {
                for (int x = 0; x < arrayRotator.getWidth(); ++x) {
                    stdout.write(a[y][x] + " ");
                }
                stdout.write("\n");
            }

            stdout.flush();
        }
    }
}