// https://www.acmicpc.net/problem/2749

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_2749_피보나치_수_3 {
    private static int M = 1_000_000;

    public static class Mat {
        public Mat(final int r, final int c) {
            this.r = r;
            this.c = c;
            m = new long[r][c];
        }

        public Mat(final long[] data, final int r, final int c) {
            this.r = r;
            this.c = c;
            m = new long[r][c];
            for (var i = 0; i < r * c; ++i) {
                m[i / c][i % c] = data[i];
            }
        }

        public Mat mul(final Mat rhs) {
            final var p = this.r;
            final var q = rhs.c;
            final var r = rhs.r;
            final var ret = new Mat(p, q);
            for (var i = 0; i < p; ++i) {
                for (var j = 0; j < q; ++j) {
                    for (var k = 0; k < r; ++k) {
                        ret.m[i][j] += (m[i][k] * rhs.m[k][j]) % M;
                        ret.m[i][j] %= M;
                    }
                }
            }

            return ret;
        }

        public Mat pow(final long k) {
            assert r == c;
            if (k == 0) {
                return eye(r);
            }
            if (k == 1) {
                return this;
            }
            if (k == 2) {
                return mul(this);
            }
            final var mxm = pow(k / 2).pow(2);
            return k % 2 == 1 ? mxm.mul(this) : mxm;
        }

        public long at(final int r, final int c) {
            return m[r][c];
        }

        public static Mat from(final long[] data, final int r, final int c) {
            return new Mat(data, r, c);
        }

        public static Mat eye(final int rc) {
            final var ret = new Mat(rc, rc);
            for (var i = 0; i < rc; ++i) {
                ret.m[i][i] = 1;
            }
            return ret;
        }

        private long[][] m;
        private int r;
        private int c;
    }

    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final long ans;
        final var N = Long.parseLong(stdin.readLine());
        if (N == 0) {
            ans = 0;
        } else {
            final var m = Mat.from(new long[] { 1, 1, 1, 0 }, 2, 2);
            final var fib = Mat.from(new long[] { 1, 0 }, 2, 1);
            ans = m.pow(N - 1).mul(fib).at(0, 0);
        }

        stdout.write(ans + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
