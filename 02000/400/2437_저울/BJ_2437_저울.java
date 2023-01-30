// https://www.acmicpc.net/problem/2437

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BJ_2437_저울 {
    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = Integer.parseInt(stdin.readLine());
        final var weights = new int[N];
        {
            final var tokens = new StringTokenizer(stdin.readLine());
            for (var i = 0; i < N; ++i) {
                weights[i] = Integer.parseInt(tokens.nextToken());
            }
        }

        Arrays.sort(weights);

        var lastVisited = 0;
        for (final var w : weights) {
            if (lastVisited + 1 < w) {
                break;
            }
            lastVisited += w;
        }

        stdout.write((lastVisited + 1) + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
