// https://www.acmicpc.net/problem/5430

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringJoiner;
import java.util.StringTokenizer;

public class BJ_5430_AC {
    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        var t = Integer.parseInt(stdin.readLine());
        while (0 < t--) {
            final var commands = stdin.readLine();

            final var N = Integer.parseInt(stdin.readLine());
            final var array = new int[N];
            {
                final var initialState = stdin.readLine();
                final var tokens = new StringTokenizer(initialState.substring(1, initialState.length() - 1));
                for (var i = 0; i < N; ++i) {
                    array[i] = Integer.parseInt(tokens.nextToken(","));
                }
            }

            var isReversed = false;
            var lb = 0;
            var ub = N;
            for (var i = 0; i < commands.length(); ++i) {
                switch (commands.charAt(i)) {
                    case 'R':
                        isReversed = !isReversed;
                        break;
                    case 'D':
                        if (isReversed) {
                            --ub;
                        } else {
                            ++lb;
                        }
                        break;
                }
            }

            if (ub < lb) {
                stdout.write("error\n");
            } else {
                final var values = new StringJoiner(",");
                for (var i = lb; i < ub; ++i) {
                    final var j = (isReversed ? (ub - 1) - (i - lb) : i);
                    values.add(Integer.toString(array[j]));
                }

                stdout.write("[" + values + "]\n");
            }
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
