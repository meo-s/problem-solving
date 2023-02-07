// https://www.acmicpc.net/problem/15649

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.util.Deque;

public class BJ_15649_N과_M_1 {
    private static Deque<Integer> seq = new ArrayDeque<>();

    public static void permutation(final BufferedWriter stdout, final int N, final int M, final int bitmask)
            throws IOException {
        if (seq.size() == M) {
            for (final int n : seq) {
                stdout.write(n + " ");
            }
            stdout.write("\n");
        } else {
            for (int i = 0; i < N; ++i) {
                if ((bitmask & (1 << i)) != 0) {
                    seq.addLast(i + 1);
                    permutation(stdout, N, M, bitmask ^ (1 << i));
                    seq.pollLast();
                }
            }
        }
    }

    public static void permutation(final BufferedWriter stdout, final int N, final int M) throws IOException {
        seq.clear();
        final int bitmask = (int) (Math.pow(2, N) - 1);
        permutation(stdout, N, M, bitmask);
    }

    public static void main(final String[] args) throws IOException {
        final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        final int N, M;
        {
            final String[] tokens = stdin.readLine().split(" ");
            N = Integer.parseInt(tokens[0]);
            M = Integer.parseInt(tokens[1]);
        }

        permutation(stdout, N, M);

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
