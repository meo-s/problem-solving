// https://www.acmicpc.net/problem/13335

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.ArrayDeque;

public class BJ_13335_트럭 {

    private static int N, L, W;
    private static int[] trucks;

    private static int readInt(final BufferedReader br) throws IOException {
        int n = 0;
        for (;;) {
            int b = br.read();
            if (b == ' ' || b == '\n' || b == -1) {
                return n;
            }
            n = n * 10 + (b - '0');
        }
    }

    private static void init() throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            N = readInt(stdin);
            L = readInt(stdin);
            W = readInt(stdin);
            trucks = new int[N];
            for (int i = 0; i < N; ++i) {
                trucks[i] = readInt(stdin);
            }
        }
    }

    private static void solve() {
        int s = 0;
        int e = 0;
        int w = 0;
        int now = 0;
        final Deque<Integer> times = new ArrayDeque<>();
        while (e < N) {
            ++now;
            while (!times.isEmpty() && (W < w + trucks[e] || L == now - times.peekFirst())) {
                now += L - (now - times.pollFirst());
                w -= trucks[s++];
            }

            times.addLast(now);
            w += trucks[e++];
        }

        now += L - (now - times.pollLast());
        System.out.println(now);
    }

    public static void main(final String[] args) throws IOException {
        init();
        solve();
    }

}
