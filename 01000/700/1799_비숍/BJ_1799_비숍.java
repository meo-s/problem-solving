// https://www.acmicpc.net/problem/1799

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_1799_비숍 {
    public static class BishopSimulator {
        private final int VALID_BITMASK;
        private int N;
        private int[] bmask;
        private boolean[][][] visited;

        private BishopSimulator(final int N) {
            VALID_BITMASK = (int) Math.pow(2, N) - 1;

            this.N = N;
            this.bmask = new int[N];
            this.visited = new boolean[N][VALID_BITMASK + 1][VALID_BITMASK + 1];
        }

        public int simulate(final int y, final int x, final int numBishops, int lmask, int rmask, int cmask) {
            if (y == N) {
                return numBishops;
            }

            var maxBishops = numBishops;
            for (var i = y * N + x; i < N * N; ++i) {
                if (i % N == 0) {
                    lmask = ((lmask << 1) | (cmask << 1)) & VALID_BITMASK;
                    rmask = (rmask >> 1) | (cmask >> 1);
                    cmask = 0;

                    if (!visited[i / N][lmask][rmask]) {
                        visited[i / N][lmask][rmask] = true;
                    } else {
                        break;
                    }
                }

                var here = 1 << (i % N);
                if (((bmask[i / N] | lmask | rmask) & here) == 0) {
                    final var ny = (i + 1) / N;
                    final var nx = (i + 1) % N;
                    maxBishops = Math.max(maxBishops, simulate(ny, nx, numBishops + 1, lmask, rmask, cmask | here));
                }
            }

            return maxBishops;
        }

        public int simulate() {
            return simulate(0, 0, 0, 0, 0, 0);
        }

        public static BishopSimulator from(final BufferedReader br) throws IOException {
            final var N = Integer.parseInt(br.readLine());
            final var simulator = new BishopSimulator(N);
            for (var y = 0; y < N; ++y) {
                final var tokens = br.readLine().split(" ");
                for (var x = 0; x < N; ++x) {
                    simulator.bmask[y] |= (Integer.parseInt(tokens[x]) ^ 1) << x;
                }
            }

            return simulator;
        }

    }

    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print(BishopSimulator.from(stdin).simulate() + "\n");
            stdin.close();
        }
    }
}
