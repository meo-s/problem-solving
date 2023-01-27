// https://www.acmicpc.net/problem/2011

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class BJ_2011_암호코드 {
    private static final int M = 1_000_000;

    public static class Decrypt {
        public Decrypt(final int maxLen) {
            dp = new int[maxLen];
        }

        public Decrypt init() {
            Arrays.fill(dp, -1);
            return this;
        }

        public int decrypt(final byte[] cryptogram, final int offset) {
            if (offset == cryptogram.length) {
                return 1;
            }

            if (dp[offset] == -1) {
                if (cryptogram[offset] == 0) {
                    dp[offset] = 0;
                } else {
                    dp[offset] = decrypt(cryptogram, offset + 1);
                    if (offset + 1 < cryptogram.length && cryptogram[offset] * 10 + cryptogram[offset + 1] <= 26) {
                        dp[offset] += decrypt(cryptogram, offset + 2);
                        dp[offset] %= M;
                    }
                }
            }

            return dp[offset];
        }

        public int decrypt(final byte[] cryptogram) {
            return decrypt(cryptogram, 0);
        }

        private int[] dp;
    }

    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var data = stdin.readLine();
        final var cryptogram = new byte[data.length()];
        for (var i = 0; i < data.length(); ++i) {
            cryptogram[i] = (byte) (data.charAt(i) - '0');
        }

        stdout.write(new Decrypt(cryptogram.length).init().decrypt(cryptogram) + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
