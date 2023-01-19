// https://www.acmicpc.net/problem/11003

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.util.StringTokenizer;

public class BJ_11003_최솟값_찾기_DEQUE {
    public static class NumInfo {
        public NumInfo(final int value_, final int offset_) {
            value = value_;
            offset = offset_;
        }

        public int value;
        public int offset;
    }

    public static void main(String[] args) throws IOException {
        final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        int N, L;
        {
            final StringTokenizer tokens = new StringTokenizer(stdin.readLine());
            N = Integer.parseInt(tokens.nextToken());
            L = Integer.parseInt(tokens.nextToken());
        }

        final StringTokenizer tokens = new StringTokenizer(stdin.readLine());
        final ArrayDeque<NumInfo> numStorage = new ArrayDeque<>();
        numStorage.add(new NumInfo(Integer.parseInt(tokens.nextToken()), 0));

        for (int i = 0; i < N; ++i) {
            if (0 < i) {
                final NumInfo numInfo = new NumInfo(Integer.parseInt(tokens.nextToken()), i);
                if (numInfo.value <= numStorage.peekFirst().value) {
                    numStorage.clear();
                    numStorage.add(numInfo);
                } else {
                    while (0 < numStorage.size() && numStorage.peekFirst().offset <= i - L) {
                        numStorage.pollFirst();
                    }
                    while (0 < numStorage.size() && numInfo.value <= numStorage.peekLast().value) {
                        numStorage.pollLast();
                    }
                    numStorage.addLast(numInfo);
                }
            }

            stdout.write(numStorage.peekFirst().value + " ");
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
