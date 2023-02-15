// https://www.acmicpc.net/problem/11286

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.PriorityQueue;

public class BJ_11286_절댓값_힙 {
    public static void main(final String[] args) throws IOException {
        try (final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {

            final PriorityQueue<Integer> pq = new PriorityQueue<>((lhs, rhs) -> {
                if (Math.abs(lhs) < Math.abs(rhs)) {
                    return -1;
                }
                if (Math.abs(rhs) < Math.abs(lhs)) {
                    return 1;
                }
                return Integer.compare(lhs, rhs);
            });

            int Q = Integer.parseInt(stdin.readLine());
            while (0 < Q--) {
                final int n = Integer.parseInt(stdin.readLine());
                if (n == 0) {
                    stdout.write((pq.size() == 0 ? 0 : pq.poll()) + "\n");
                } else {
                    pq.add(n);
                }
            }

            stdout.flush();
        }
    }
}
