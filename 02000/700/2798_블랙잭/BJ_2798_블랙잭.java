// https://www.acmicpc.net/problem/2798

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_2798_블랙잭 {
    private static final int MAX_PICKS = 3;
    private static int N;
    private static int M;
    private static int[] cards;

    public static void main(final String[] args) throws IOException {
        try (final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            {
                final String[] tokens = stdin.readLine().split(" ");
                N = Integer.parseInt(tokens[0]);
                M = Integer.parseInt(tokens[1]);
            }

            cards = new int[N];
            {
                final String[] tokens = stdin.readLine().split(" ");
                for (int i = 0; i < N; ++i) {
                    cards[i] = Integer.parseInt(tokens[i]);
                }
            }

            stdout.write(combination(0, 0, 0) + "\n");
            stdout.flush();
        }
    }

    public static int combination(final int offset, final int depth, final int score) {
        if (M < score) {
            return 0;
        }
        if (depth == MAX_PICKS) {
            return score;
        }

        int maxScore = 0;
        for (int i = offset; i < N; ++i) {
            maxScore = Math.max(maxScore, combination(i + 1, depth + 1, score + cards[i]));
        }

        return maxScore;
    }
}