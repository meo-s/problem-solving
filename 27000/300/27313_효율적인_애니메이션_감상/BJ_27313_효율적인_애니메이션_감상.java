// https://www.acmicpc.net/problem/27313

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class BJ_27313_효율적인_애니메이션_감상 {
    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int N, M, K;
            {
                final var tokens = stdin.readLine().split(" ");
                N = Integer.parseInt(tokens[0]);
                M = Integer.parseInt(tokens[1]);
                K = Integer.parseInt(tokens[2]);
            }

            final var animations = new int[N];
            {
                final var tokens = stdin.readLine().split(" ");
                for (var i = 0; i < N; ++i) {
                    animations[i] = Integer.parseInt(tokens[i]);
                }
            }

            Arrays.sort(animations);

            final var optimalTimes = new int[N];
            for (var i = 0; i < Math.min(K, N); ++i) {
                optimalTimes[i] = animations[i];
            }
            for (var i = K; i < N; ++i) {
                optimalTimes[i] = optimalTimes[i - K] + animations[i];
            }

            var maxWatchables = 0;
            while (maxWatchables < N && optimalTimes[maxWatchables] <= M) {
                ++maxWatchables;
            }

            System.out.print(maxWatchables + "\n");
        }
    }
}
