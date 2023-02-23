// https://www.acmicpc.net/problem/13913

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class BJ_13913_숨바꼭질_4 {
    public static class BFSTracer {
        public static String trace(final int[] timestamps, final int start) {
            final Deque<Integer> trace = new ArrayDeque<>();
            trace.addLast(start);

            int x = start;
            int now = timestamps[start];
            while (0 <= --now) {
                final int[] nx = { x - 1, x + 1, x / 2 };
                for (int i = 0; i < nx.length; ++i) {
                    if (nx[i] < 0 || timestamps.length <= nx[i]) {
                        continue;
                    }

                    if (timestamps[nx[i]] == now) {
                        x = nx[i];
                        trace.addLast(nx[i]);
                        break;
                    }
                }
            }

            final StringBuilder traceString = new StringBuilder();
            while (0 < trace.size()) {
                traceString.append(trace.pollLast());
                traceString.append(' ');
            }

            return traceString.toString().trim();
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {

            final int S, G;
            {
                final String[] tokens = stdin.readLine().split(" ");
                S = Integer.parseInt(tokens[0]);
                G = Integer.parseInt(tokens[1]);
            }

            final int[] timestamps = new int[Math.max(S, G) * 2 + 1];
            Arrays.fill(timestamps, Integer.MAX_VALUE);
            timestamps[S] = 0;

            final Deque<Integer> waypoints = new ArrayDeque<>();
            waypoints.addFirst(S);
            while (timestamps[G] == Integer.MAX_VALUE) {
                final int x = waypoints.pollFirst();

                final int[] nx = { x - 1, x + 1, 2 * x };
                for (int i = 0; i < nx.length; ++i) {
                    if (nx[i] < 0 || timestamps.length <= nx[i]) {
                        continue;
                    }

                    if (timestamps[x] + 1 < timestamps[nx[i]]) {
                        timestamps[nx[i]] = timestamps[x] + 1;
                        waypoints.addLast(nx[i]);
                    }
                }
            }

            stdout.write(timestamps[G] + "\n");
            stdout.write(BFSTracer.trace(timestamps, G) + "\n");

            stdout.flush();
        }
    }
}
