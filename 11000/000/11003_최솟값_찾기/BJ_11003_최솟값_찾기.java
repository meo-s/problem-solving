// https://www.acmicpc.net/problem/11003

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class BJ_11003_최솟값_찾기 {
    public static class NumInfo implements Comparable<NumInfo> {
        public NumInfo(final int value_, final int offset_) {
            value = value_;
            offset = offset_;
        }

        @Override
        public int compareTo(final NumInfo rhs) {
            return value <= rhs.value ? -1 : 1;
        }

        public int value;
        public int offset;
    }

    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        int N, L;
        {
            final var tokens = new StringTokenizer(stdin.readLine());
            N = Integer.parseInt(tokens.nextToken());
            L = Integer.parseInt(tokens.nextToken());
        }

        final var nums = new NumInfo[N];
        {
            final var tokens = new StringTokenizer(stdin.readLine());
            for (var i = 0; i < N; ++i) {
                nums[i] = new NumInfo(Integer.parseInt(tokens.nextToken()), i);
            }
        }

        var numIndex = 1;
        final var heap = new PriorityQueue<NumInfo>();
        heap.add(nums[0]);
        for (int i = 0; i < N; ++i) {
            if (0 < i) {
                heap.add(nums[numIndex++]);
            }

            while (heap.peek().offset <= i - L) {
                heap.poll();
            }

            stdout.write(heap.peek().value + " ");
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
