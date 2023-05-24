// https://www.acmicpc.net/problem/18429

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_18429_근손실 {

    private static int N, K;
    private static int[] A;
    private static int[] orders;

    private static int readInt(final BufferedReader br) throws IOException {
        int n = 0;
        for (;;) {
            final int b = br.read();
            if (b == ' ' || b == '\n' || b == -1) {
                return n;
            }
            n = n * 10 + (b - '0');
        }
    }

    private static void init() throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            N = readInt(stdin);
            K = readInt(stdin);

            A = new int[N];
            orders = new int[N];
            for (int i = 0; i < N; ++i) {
                A[i] = readInt(stdin);
            }
        }
    }

    private static int dfs(int depth, final int bitmask) {
        if (depth == N) {
            int dmuscle = 0;
            for (int i = 0; i < N && 0 <= dmuscle; ++i) {
                dmuscle += A[orders[i]] - K;
            }
            return 0 <= dmuscle ? 1 : 0;
        } else {
            int count = 0;
            for (int i = 0; i < N; ++i) {
                if ((bitmask & (1 << i)) == 0) {
                    orders[depth] = i;
                    count += dfs(depth + 1, bitmask | (1 << i));
                }
            }
            return count;
        }
    }

    private static void solve() {
        System.out.println(dfs(0, 0));
    }

    public static void main(final String[] args) throws IOException {
        init();
        solve();
    }

}

