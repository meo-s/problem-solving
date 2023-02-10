// https://www.acmicpc.net/problem/1062

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_1062_가르침 {
    private static int NUM_DEFAULT_KNOWNS = 5;
    private static int DEFAULT_KNOWNS = 0;
    static {
        DEFAULT_KNOWNS |= 1 << ('a' - 'a');
        DEFAULT_KNOWNS |= 1 << ('c' - 'a');
        DEFAULT_KNOWNS |= 1 << ('i' - 'a');
        DEFAULT_KNOWNS |= 1 << ('n' - 'a');
        DEFAULT_KNOWNS |= 1 << ('t' - 'a');
    }

    public static class PolarWord {
        public static int asBitmask(final String word) {
            int bitmask = 0;
            for (int i = 0; i < word.length(); ++i) {
                bitmask |= 1 << (word.charAt(i) - 'a');
            }

            return bitmask;
        }
    }

    public static class Teacher {
        public Teacher(final int[] polarWords) {
            this.polarWords = polarWords;
        }

        public int countReadablePolarWords(int bitmask) {
            int numReadables = 0;
            for (final var polarWord : polarWords) {
                numReadables += ((polarWord & bitmask) == polarWord ? 1 : 0);
            }

            return numReadables;
        }

        public int pickOptimalAlphabets(final int lb, final int r, final int bitmask) {
            if (r <= 0 || lb == 'z' - 'a' + 1) {
                return countReadablePolarWords(bitmask);
            }

            int maxReadables = 0;
            for (int i = lb; i < 'z' - 'a' + 1; ++i) {
                if ((DEFAULT_KNOWNS & (1 << i)) == 0) {
                    final int newKnowns = bitmask | (1 << i);
                    maxReadables = Math.max(maxReadables, pickOptimalAlphabets(i + 1, r - 1, newKnowns));
                }
            }

            return maxReadables;
        }

        public int findMaximumReadbles(final int r) {
            if (r < NUM_DEFAULT_KNOWNS) {
                return 0;
            }
            return pickOptimalAlphabets(0, r - NUM_DEFAULT_KNOWNS, DEFAULT_KNOWNS);
        }

        private int[] polarWords;
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int N, K;
            {
                final String[] tokens = stdin.readLine().split(" ");
                N = Integer.parseInt(tokens[0]);
                K = Integer.parseInt(tokens[1]);
            }

            final int[] polarWords = new int[N];
            for (int i = 0; i < N; ++i) {
                polarWords[i] = PolarWord.asBitmask(stdin.readLine());
            }

            stdout.write(new Teacher(polarWords).findMaximumReadbles(K) + "\n");

            stdout.flush();
        }
    }
}
