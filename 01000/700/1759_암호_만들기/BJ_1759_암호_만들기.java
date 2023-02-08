// https://www.acmicpc.net/problem/1759

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BJ_1759_암호_만들기 {
    private static int VOWEL;

    static {
        VOWEL |= (1 << ('a' - 'a'));
        VOWEL |= (1 << ('e' - 'a'));
        VOWEL |= (1 << ('i' - 'a'));
        VOWEL |= (1 << ('o' - 'a'));
        VOWEL |= (1 << ('u' - 'a'));
    }

    public static class Cryptographer {
        public Cryptographer(final int[] alphas) {
            this.alphas = alphas;
        }

        private String bitmaskToStrig(final int picks) {
            if ((picks & VOWEL) == 0) {
                return null;
            }

            var numConsonants = 0;
            final var cryptogram = new StringBuilder();
            for (int i = 0; i < 'Z' - 'A' + 1; ++i) {
                if ((picks & (1 << i)) != 0) {
                    numConsonants += (~VOWEL & (1 << i)) >> i;
                    cryptogram.append((char) ('a' + i));
                }
            }

            return 2 <= numConsonants ? cryptogram.toString() : null;
        }

        private void make(final List<String> cryptograms, final int lb, final int r, final int picks) {
            if (r == 0) {
                final var cryptogram = bitmaskToStrig(picks);
                if (cryptogram != null) {
                    cryptograms.add(cryptogram);
                }
                return;
            }

            for (int i = lb; i <= alphas.length - r; ++i) {
                make(cryptograms, i + 1, r - 1, picks | (1 << alphas[i]));
            }
        }

        public List<String> make(final int length) {
            final var cryptograms = new ArrayList<String>();
            make(cryptograms, 0, length, 0);
            return cryptograms;
        }

        private int[] alphas;
    }

    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final int L, C;
        {
            final var tokens = stdin.readLine().split(" ");
            L = Integer.parseInt(tokens[0]);
            C = Integer.parseInt(tokens[1]);
        }

        final var alphas = new int[C];
        {
            final var tokens = stdin.readLine().split(" ");
            for (var i = 0; i < C; ++i) {
                alphas[i] = tokens[i].charAt(0) - 'a';
            }
        }

        Arrays.sort(alphas);

        for (final var cryptogram : new Cryptographer(alphas).make(L)) {
            stdout.write(cryptogram + "\n");
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
