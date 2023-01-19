// https://www.acmicpc.net/problem/1083

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class BJ_1083_소트 {
    public static void main(String[] args) throws IOException {
        final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        final int N = Integer.parseInt(stdin.readLine());

        final int[] seq = new int[N];
        {
            final StringTokenizer tokens = new StringTokenizer(stdin.readLine());
            for (int i = 0; i < N; ++i) {
                seq[i] = Integer.parseInt(tokens.nextToken());
            }
        }

        int n_swappables = Integer.parseInt(stdin.readLine());
        for (int i = 0; 0 < n_swappables && i < N - 1; ++i) {
            int maxIndex = i;
            for (int j = 1; j <= n_swappables && i + j < N; ++j) {
                if (seq[maxIndex] < seq[i + j]) {
                    maxIndex = i + j;
                }
            }

            n_swappables -= maxIndex - i;
            while (0 < (maxIndex - i)) {
                seq[maxIndex] ^= seq[maxIndex - 1];
                seq[maxIndex - 1] ^= seq[maxIndex];
                seq[maxIndex] ^= seq[maxIndex - 1];
                --maxIndex;
            }
        }

        for (final int n : seq) {
            stdout.write(n + " ");
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
