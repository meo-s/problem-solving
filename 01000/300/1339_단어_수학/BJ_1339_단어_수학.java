// https://www.acmicpc.net/problem/1339

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;

public class BJ_1339_단어_수학 {
    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final var N = Integer.parseInt(stdin.readLine());
            final var weights = new HashMap<Character, Integer>();
            for (var i = 0; i < N; ++i) {
                final var word = stdin.readLine();
                var digit = 1;
                for (var j = word.length() - 1; 0 <= j; --j) {
                    final var c = word.charAt(j);
                    weights.put(c, weights.getOrDefault(c, 0) + digit);
                    digit *= 10;
                }
            }

            final var values = weights.values().stream().toArray(Integer[]::new);
            Arrays.sort(values);

            var largest = 0;
            for (int i = 0; i < values.length; ++i) {
                largest += values[(values.length - 1) - i] * (9 - i);
            }

            System.out.print(largest + "\n");
            stdin.close();
        }
    }
}
