// https://www.acmicpc.net/problem/2164

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.util.Deque;

public class BJ_2164_카드2 {
    public static void main(final String[] args) throws IOException {
        try (final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int N = Integer.parseInt(stdin.readLine());
            final Deque<Integer> cards = new ArrayDeque<>();
            for (int i = 1; i <= N; ++i) {
                cards.addLast(i);
            }

            while (1 < cards.size()) {
                cards.pollFirst();
                cards.addLast(cards.pollFirst());
            }

            stdout.write(cards.peekFirst() + "\n");
            stdout.flush();
        }
    }
}
