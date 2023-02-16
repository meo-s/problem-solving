// https://www.acmicpc.net/problem/16472

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;

public class BJ_16472_고냥이 {
    public static void main(final String[] args) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {

            final var N = Integer.parseInt(stdin.readLine());
            final var catLang = stdin.readLine();

            var lb = 0;
            var longestTranslatableLength = 0;
            final var alphas = new HashSet<Character>();
            final var latestIndices = new int['Z' - 'A' + 1];
            for (var i = 0; i < catLang.length(); ++i) {
                final var c = catLang.charAt(i);
                latestIndices[c - 'a'] = i;

                alphas.add(c);
                while (N < alphas.size()) {
                    if (lb == latestIndices[catLang.charAt(lb) - 'a']) {
                        alphas.remove(catLang.charAt(lb));
                    }
                    ++lb;
                }

                longestTranslatableLength = Math.max(longestTranslatableLength, i - lb + 1);
            }

            stdout.write(longestTranslatableLength + "\n");
            stdout.flush();
        }
    }
}