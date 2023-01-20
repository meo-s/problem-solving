import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.PriorityQueue;

public class BJ_1655_가운데를_말해요 {
    public static void main(String[] args) throws IOException {
        final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        final int N = Integer.parseInt(stdin.readLine());
        final PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        final PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        int mid = Integer.parseInt(stdin.readLine());
        for (int i = 0; i < N; ++i) {
            if (0 < i) {
                final int n = Integer.parseInt(stdin.readLine());
                (n <= mid ? maxHeap : minHeap).add(n);
                if (minHeap.size() < maxHeap.size()) {
                    minHeap.add(mid);
                    mid = maxHeap.poll();
                } else if (maxHeap.size() + 1 < minHeap.size()) {
                    maxHeap.add(mid);
                    mid = minHeap.poll();
                }
            }

            stdout.write(mid + "\n");
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
