// https://www.acmicpc.net/problem/6198

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.util.Arrays;

public class BJ_6198_옥상_정원_꾸미기 {
    public static void main(final String[] args) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {

            final var N = Integer.parseInt(stdin.readLine());
            final var heights = new int[N + 1];
            heights[N] = Integer.MAX_VALUE;
            for (var i = 0; i < N; ++i) {
                heights[i] = Integer.parseInt(stdin.readLine());
            }

            final var viewables = new int[N];
            final var st = new ArrayDeque<Integer>();
            for (var i = 0; i <= N; ++i) {
                while (0 < st.size() && heights[st.peekLast()] <= heights[i]) {
                    final var j = st.pollLast();
                    viewables[j] = i - (j + 1);
                }

                st.addLast(i);
            }

            stdout.write(Arrays.stream(viewables).asLongStream().sum() + "\n");
            stdout.flush();
        }
    }
}
