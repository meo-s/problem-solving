// https://www.acmicpc.net/problem/2493

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.util.Deque;

public class BJ_2493_탑 {
    public static class Top {
        public int index;
        public int height;

        public Top(final int index, final int height) {
            this.index = index;
            this.height = height;
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {

            final int N = Integer.parseInt(stdin.readLine());
            final String[] tokens = stdin.readLine().split(" ");
            final Deque<Top> bottlenecks = new ArrayDeque<>();
            final Deque<Integer> receivers = new ArrayDeque<>();
            for (int i = 0; i < N; ++i) {
                final Top top = new Top(i + 1, Integer.parseInt(tokens[i]));
                while (0 < bottlenecks.size() && bottlenecks.peekLast().height < top.height) {
                    bottlenecks.pollLast();
                }

                receivers.addLast(0 < bottlenecks.size() ? bottlenecks.peekLast().index : 0);
                bottlenecks.addLast(top);
            }

            for (final int receiver : receivers) {
                stdout.write(receiver + " ");
            }

            stdout.write("\n");
            stdout.flush();
        }
    }
}
