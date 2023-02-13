// https://www.acmicpc.net/problem/1158

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.util.Deque;

public class BJ_1158_요세푸스_문제 {
    public static void main(final String[] args) throws IOException {
        try (final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int N, K;
            {
                final String[] tokens = stdin.readLine().split(" ");
                N = Integer.parseInt(tokens[0]);
                K = Integer.parseInt(tokens[1]);
            }

            final Deque<String> nums = new ArrayDeque<>();
            for (int i = 1; i <= N; ++i) {
                nums.addLast(Integer.toString(i));
            }

            final Deque<String> seq = new ArrayDeque<>();
            while (0 < nums.size()) {
                for (int j = 1; j < K; ++j) {
                    nums.addLast(nums.pollFirst());
                }

                seq.addLast(nums.pollFirst());
            }

            stdout.write(String.format("<%s>\n", String.join(", ", seq)));
            stdout.flush();
        }
    }
}
