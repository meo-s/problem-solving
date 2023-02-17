// https://www.acmicpc.net/problem/16435

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class BJ_16435_스네이크버드 {
    public static void main(final String[] args) throws IOException {
        try (final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {

            final int N, L;
            {
                final String[] tokens = stdin.readLine().split(" ");
                N = Integer.parseInt(tokens[0]);
                L = Integer.parseInt(tokens[1]);
            }

            final int[] fruits = new int[N];
            {
                final String[] tokens = stdin.readLine().split(" ");
                for (int i = 0; i < N; ++i) {
                    fruits[i] = Integer.parseInt(tokens[i]);
                }
            }

            Arrays.sort(fruits);
            int dL = 0;
            while (dL < N && fruits[dL] <= L + dL) {
                ++dL;
            }

            stdout.write((L + dL) + "\n");
            stdout.flush();
        }
    }
}