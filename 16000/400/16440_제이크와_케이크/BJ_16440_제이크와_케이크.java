// https://www.acmicpc.net/problem/16440

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_16440_제이크와_케이크 {
    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final var N = Integer.parseInt(stdin.readLine());
            final var cake = stdin.readLine();

            final var sk = new int[2];
            for (var i = 0; i < N / 2; ++i) {
                ++sk[cake.charAt(i) == 's' ? 0 : 1];
            }

            if (sk[0] == sk[1]) {
                System.out.printf("1\n%d\n", N / 2);
                return;
            }

            for (var i = N / 2; i < N; ++i) {
                --sk[cake.charAt(i - N / 2) == 's' ? 0 : 1];
                ++sk[cake.charAt(i) == 's' ? 0 : 1];
                if (sk[0] == sk[1]) {
                    System.out.printf("2\n%d %d\n", i - N / 2 + 1, i + 1);
                    break;
                }
            }
        }
    }
}
