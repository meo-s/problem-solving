// https://www.acmicpc.net/problem/1256

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.util.StringTokenizer;

public class BJ_1256_사전 {
    public static class nCr {
        public static BigInteger nr(final int n, final int r) {
            final var mr = Math.min(n - r, r);
            switch (mr) {
                case 0:
                    return BigInteger.ONE;
                case 1:
                    return BigInteger.valueOf(n);
                default:
                    if (dp[n][r] == null) {
                        dp[n][r] = nr(n - 1, mr).add(nr(n - 1, mr - 1));
                    }
                    return dp[n][r];
            }
        }

        private static BigInteger[][] dp = new BigInteger[201][201];
    }

    public static class nHr {
        public static BigInteger nr(final int n, final int r) {
            return nCr.nr(n + r - 1, r);
        }
    }

    public static class Dictionary {
        public static String search(final int k, final int a, final int z) {
            final var maxIndex = nHr.nr(Math.max(a, z) + 1, Math.min(a, z));
            if (a + z == 0 || maxIndex.compareTo(BigInteger.valueOf(k)) < 0) {
                return "";
            }

            final var subIndex = (0 < a ? nHr.nr(Math.max(a - 1, z) + 1, Math.min(a - 1, z)) : BigInteger.ZERO);
            if (subIndex.compareTo(BigInteger.valueOf(k)) >= 0) {
                return "a" + search(k, a - 1, z);
            } else {
                return "z" + search(k - subIndex.intValue(), a, z - 1);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        final StringTokenizer tokens = new StringTokenizer(stdin.readLine());
        final int a = Integer.parseInt(tokens.nextToken());
        final int z = Integer.parseInt(tokens.nextToken());
        final int k = Integer.parseInt(tokens.nextToken());
        final String word = Dictionary.search(k, a, z);
        stdout.write((0 < word.length() ? word : "-1") + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
