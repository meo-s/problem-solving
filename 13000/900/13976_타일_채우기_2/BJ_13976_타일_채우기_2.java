// https://www.acmicpc.net/problem/13976

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;

class BJ_13976_타일_채우기_2 {
    private static int M = 1_000_000_007;
    private static BigInteger BI_M = BigInteger.valueOf(M);

    public static class Mat {
        public Mat(final int rows, final int cols) {
            m = new long[rows][cols];
            r = rows;
            c = cols;
        }

        public Mat mul(Mat rhs) {
            final int r = this.r;
            final int t = this.c;
            final int c = rhs.c;
            final Mat ret = Mat.zero(r, c);
            for (var i = 0; i < r; ++i) {
                for (var j = 0; j < c; ++j) {
                    for (var k = 0; k < t; ++k) {
                        final var lhs_ik = BigInteger.valueOf(m[i][k]);
                        final var rhs_kj = BigInteger.valueOf(rhs.m[k][j]);
                        ret.m[i][j] += lhs_ik.multiply(rhs_kj).mod(BI_M).longValue();
                        ret.m[i][j] %= M;
                    }
                }
            }

            return ret;
        }

        public Mat pow(final BigInteger k) {
            if (k.equals(BigInteger.ZERO)) {
                return eye(this.r);
            }
            if (k.equals(BigInteger.ONE)) {
                return this;
            }
            if (k.equals(BigInteger.TWO)) {
                return this.mul(this);
            }

            final var mxm = pow(k.divide(BigInteger.TWO)).pow(BigInteger.TWO);
            return k.mod(BigInteger.TWO).equals(BigInteger.ONE) ? mxm.mul(this) : mxm;
        }

        public static Mat zero(final int rows, final int cols) {
            return new Mat(rows, cols);
        }

        public static Mat eye(final int rc) {
            final var ret = zero(rc, rc);
            for (var i = 0; i < rc; ++i) {
                ret.m[i][i] = 1;
            }
            return ret;
        }

        public static Mat from(final long[] data, final int r, final int c) {
            final var ret = zero(r, c);
            for (var i = 0; i < r * c; ++i) {
                ret.m[i / c][i % c] = data[i];
            }
            return ret;
        }

        public long[][] m;
        public int r, c;
    }

    public static long count(final BigInteger N) {
        if (N.mod(BigInteger.TWO).equals(BigInteger.ONE)) {
            return 0;
        }

        if (N.equals(BigInteger.TWO)) {
            return 3;
        }

        final var m = Mat.from(new long[] { 4, -1, 1, 0 }, 2, 2);
        final var k = N.divide(BigInteger.TWO).subtract(BigInteger.ONE);
        var dp = Mat.from(new long[] { 3, 1 }, 2, 1);
        dp = m.pow(k).mul(dp);

        return dp.m[0][0];
    }

    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = new BigInteger(stdin.readLine());
        stdout.write(count(N) + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
