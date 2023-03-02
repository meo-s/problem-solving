// https://www.acmicpc.net/problem/17143

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BJ_17143_낚시왕 {
    public static class Point {
        final int y;
        final int x;

        public Point(final int y, final int x) {
            this.y = y;
            this.x = x;
        }
    }

    public static class Shark {
        private static final int[] dy = { -1, 0, 1, 0 };
        private static final int[] dx = { 0, 1, 0, -1 };

        public int y;
        public int x;
        public int size;
        public int speed;
        public int direction;

        public Shark(final int y, final int x, final int size, final int speed, final int direction) {
            this.y = y;
            this.x = x;
            this.size = size;
            this.speed = speed;
            this.direction = direction;
        }

        public void update(final int H, final int W) {
            if ((direction & 1) == 0) {
                y += dy[direction] * (speed % (2 * (H - 1)));
                while (y < 0 || H <= y) {
                    direction = (direction + 2) % 4;
                    if (y < 0) {
                        y = y * -1;
                    } else {
                        y = (H - 1) - (y - (H - 1));
                    }
                }
            } else {
                x += dx[direction] * (speed % (2 * (W - 1)));
                while (x < 0 || W <= x) {
                    direction = (direction + 2) % 4;
                    if (x < 0) {
                        x = x * -1;
                    } else {
                        x = (W - 1) - (x - (W - 1));
                    }
                }
            }

        }

        public static Shark from(final BufferedReader br) throws IOException {
            final String[] tokens = br.readLine().split(" ");
            final int r = Integer.parseInt(tokens[0]) - 1;
            final int c = Integer.parseInt(tokens[1]) - 1;
            final int s = Integer.parseInt(tokens[2]);
            final int z = Integer.parseInt(tokens[4]);
            int d = Integer.parseInt(tokens[3]) - 1;
            d = (d == 1 ? 2 : (d == 2 ? 1 : d)); // 방향값 변경
            return new Shark(r, c, z, s, d);
        }
    }

    public static class Sea {
        public final int height;
        public final int width;
        public final Set<Shark> sharks;
        public final Shark[][][] map;
        public int currentMapIndex;

        public Sea(final int H, final int W) {
            height = H;
            width = W;
            sharks = new HashSet<>();

            map = new Shark[2][H][W];
            currentMapIndex = 0;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public Shark at(final int y, final int x) {
            return map[currentMapIndex][y][x];
        }

        public int killShark(final int y, final int x) {
            final Shark shark = map[currentMapIndex][y][x];
            map[currentMapIndex][y][x] = null;
            sharks.remove(shark);
            return shark.size;
        }

        public void update() {
            final Shark[][] nmap = map[currentMapIndex ^ 1];

            for (final Shark[] nmapRow : nmap) {
                Arrays.fill(nmapRow, null);
            }

            for (final Shark shark : sharks.toArray(new Shark[sharks.size()])) {
                shark.update(height, width);
                if (nmap[shark.y][shark.x] == null) {
                    nmap[shark.y][shark.x] = shark;
                } else {
                    final Shark other = nmap[shark.y][shark.x];
                    if (shark.size < other.size) {
                        sharks.remove(shark);
                    } else {
                        nmap[shark.y][shark.x] = shark;
                        sharks.remove(other);
                    }
                }
            }

            currentMapIndex ^= 1;
        }

        public static Sea from(final BufferedReader br) throws IOException {
            final int H, W, M;
            {
                final String[] tokens = br.readLine().split(" ");
                H = Integer.parseInt(tokens[0]);
                W = Integer.parseInt(tokens[1]);
                M = Integer.parseInt(tokens[2]);
            }

            final Sea sea = new Sea(H, W);
            for (int i = 0; i < M; ++i) {
                final Shark shark = Shark.from(br);
                sea.map[sea.currentMapIndex][shark.y][shark.x] = shark;
                sea.sharks.add(shark);
            }

            return sea;
        }
    }

    public static class Fisher {
        public int x = -1;
        public int score = 0;

        public void update(final Sea sea) {
            ++x;
            for (int y = 0; y < sea.getHeight(); ++y) {
                if (sea.at(y, x) != null) {
                    score += sea.killShark(y, x);
                    break;
                }
            }
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final Sea sea = Sea.from(stdin);
            final Fisher fisher = new Fisher();
            for (int i = 0; i < sea.getWidth(); ++i) {
                fisher.update(sea);
                sea.update();
            }

            System.out.print(fisher.score + "\n");
        }
    }
}
