// https://www.acmicpc.net/problem/2839

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_2839_설탕_배달 {
    public static void main(final String[] args) throws IOException {
        try (final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int N = Integer.parseInt(stdin.readLine());

            int minDeliveries = Integer.MAX_VALUE;
            for (int i = N / 5; 0 <= i; --i) {
                if (5 * i + 3 * ((N - 5 * i) / 3) == N) {
                    minDeliveries = i + (N - 5 * i) / 3;
                    break;
                }
            }

            stdout.write((minDeliveries == Integer.MAX_VALUE ? -1 : minDeliveries) + "\n");
            stdout.flush();
        }
    }
}