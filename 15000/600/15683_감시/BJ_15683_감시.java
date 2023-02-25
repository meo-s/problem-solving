// https://www.acmicpc.net/problem/15683

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BJ_15683_감시 {
    public static class CCTV {
        public static final int[] DIRECTIONS = { 0b0001, 0b0101, 0b0011, 0b1011, 0b1111 };
        private static final int[] dy = { -1, 0, 1, 0 };
        private static final int[] dx = { 0, 1, 0, -1 };

        public final int y;
        public final int x;
        public int direction;

        public CCTV(final int y, final int x, final int type) {
            this.y = y;
            this.x = x;
            this.direction = DIRECTIONS[type - 1];
        }

        public void rotate() {
            direction = ((direction << 1) & 0b1111) | (direction >> 3);
        }

        private void updateRoomState(final int[][] room, final int[][] state, final int w) {
            final var H = room.length;
            final var W = room[0].length;

            state[y][x] += w;

            for (var i = 0; i < dy.length; ++i) {
                if ((direction & (1 << i)) == 0) {
                    continue;
                }

                var ny = y + dy[i];
                var nx = x + dx[i];
                while (0 <= ny && ny < H && 0 <= nx && nx < W) {
                    if (room[ny][nx] == 6) {
                        break;
                    }

                    state[ny][nx] += w;
                    ny += dy[i];
                    nx += dx[i];
                }
            }
        }

        public void setup(final int[][] room, final int[][] state) {
            updateRoomState(room, state, 1);
        }

        public CCTV dismantle(final int[][] room, final int[][] state) {
            updateRoomState(room, state, -1);
            return this;
        }
    }

    public static class CCTVSimulator {
        private int H;
        private int W;
        private int[][] room;
        private int[][] state;
        private List<CCTV> cctvs;
        private int minBlindSpotCount;

        private CCTVSimulator(final int H, final int W) {
            this.H = H;
            this.W = W;
            this.room = new int[H][W];
            this.state = new int[H][W];
            this.cctvs = new ArrayList<>();
            this.minBlindSpotCount = Integer.MAX_VALUE;
        }

        private void updateBlindSpotCount() {
            var blindSpotCount = 0;
            for (var y = 0; y < H; ++y) {
                for (var x = 0; x < W; ++x) {
                    if (room[y][x] != 6 && state[y][x] == 0) {
                        ++blindSpotCount;
                    }
                }
            }

            minBlindSpotCount = Math.min(minBlindSpotCount, blindSpotCount);
        }

        private void dfs(final int depth) {
            if (depth == cctvs.size()) {
                updateBlindSpotCount();
                return;
            }

            for (int i = 0; i < 4; ++i) {
                final var cctv = cctvs.get(depth);
                cctv.setup(room, state);
                dfs(depth + 1);
                cctv.dismantle(room, state).rotate();
            }
        }

        public int findOptimalCase() {
            dfs(0);
            return minBlindSpotCount;
        }

        public static CCTVSimulator from(final BufferedReader br) throws IOException {
            final int H, W;
            {
                final var tokens = br.readLine().split(" ");
                H = Integer.parseInt(tokens[0]);
                W = Integer.parseInt(tokens[1]);
            }

            final var simul = new CCTVSimulator(H, W);
            for (var y = 0; y < H; ++y) {
                final var tokens = br.readLine().split(" ");
                for (var x = 0; x < W; ++x) {
                    simul.room[y][x] = Integer.parseInt(tokens[x]);
                    if (0 < simul.room[y][x] && simul.room[y][x] < 6) {
                        simul.cctvs.add(new CCTV(y, x, simul.room[y][x]));
                    }
                }
            }

            return simul;
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println(CCTVSimulator.from(stdin).findOptimalCase() + "\n");
        }
    }
}
