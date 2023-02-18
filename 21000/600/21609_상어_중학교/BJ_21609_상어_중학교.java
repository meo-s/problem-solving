// https://www.acmicpc.net/problem/21609

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class BJ_21609_상어_중학교 {
    public static <T> T swap(final T lhs, final T rhs) {
        return lhs;
    }

    public static class ArrayTurner {
        private static byte[][] buf;

        public static byte[][] turn(final byte[][] a) {
            assert a.length == a[0].length;
            final int N = a.length;
            if (buf == null || buf.length != N) {
                buf = new byte[N][N];
            }

            for (var y = 0; y < N; ++y) {
                System.arraycopy(a[y], 0, buf[y], 0, N);
            }

            for (var y = 0; y < N; ++y) {
                for (var x = 0; x < N; ++x) {
                    a[(N - 1) - x][y] = buf[y][x];
                }
            }

            return a;
        }
    }

    public static class BrickGame {
        private static final byte EMPTY = Byte.MIN_VALUE;
        private static final int[] dy = { 1, -1, 0, 0 };
        private static final int[] dx = { 0, 0, 1, -1 };

        public static class BrickGroup {
            public final int index;
            public final int y;
            public final int x;
            public int area;
            public int numRainbowBricks;

            public BrickGroup(final int index, final int y, final int x) {
                this.index = index;
                this.y = y;
                this.x = x;
                this.area = 0;
                this.numRainbowBricks = 0;
            }
        }

        private byte[][] board;
        private int score;

        public BrickGame(final byte[][] board) {
            this.board = board;
            this.score = 0;
        }

        public void applyGravity() {
            final var N = board.length;
            for (var y = N - 1; 1 <= y; --y) {
                for (var x = 0; x < N; ++x) {
                    if (board[y][x] != EMPTY) {
                        continue;
                    }

                    for (var dy = -1; 0 <= y + dy; --dy) {
                        if (board[y + dy][x] == -1) {
                            break;
                        } else if (board[y + dy][x] != EMPTY) {
                            board[y][x] = swap(board[y + dy][x], board[y + dy][x] = board[y][x]);
                            break;
                        }
                    }
                }
            }
        }

        private BrickGroup gatherBricks(final int brickGroupIndex, final int[][] visited, int y, int x) {
            final var N = board.length;
            final var brickGroup = new BrickGroup(brickGroupIndex, y, x);

            final var pendings = new ArrayDeque<Integer>();
            pendings.add(y * 100 + x);
            while (0 < pendings.size()) {
                y = pendings.peekFirst() / 100;
                x = pendings.pollFirst() % 100;

                ++brickGroup.area;
                brickGroup.numRainbowBricks += (board[y][x] == 0 ? 1 : 0);

                for (var i = 0; i < dy.length; ++i) {
                    final var ny = y + dy[i];
                    final var nx = x + dx[i];
                    if (ny < 0 || N <= ny || nx < 0 || N <= nx) {
                        continue;
                    }
                    if (board[ny][nx] < 0 || visited[ny][nx] == brickGroupIndex) {
                        continue;
                    }
                    if (board[ny][nx] == 0 || board[ny][nx] == board[brickGroup.y][brickGroup.x]) {
                        visited[ny][nx] = brickGroupIndex;
                        pendings.addLast(ny * 100 + nx);
                    }
                }
            }

            return brickGroup;
        }

        private void breakBrickGroup(final BrickGroup brickGroup) {
            final var N = board.length;

            var y = brickGroup.y;
            var x = brickGroup.x;
            final var color = board[y][x];
            board[y][x] = EMPTY;

            final var pendings = new ArrayDeque<Integer>();
            pendings.add(y * 100 + x);
            while (0 < pendings.size()) {
                y = pendings.peekFirst() / 100;
                x = pendings.pollFirst() % 100;

                for (var i = 0; i < dy.length; ++i) {
                    final var ny = y + dy[i];
                    final var nx = x + dx[i];
                    if (ny < 0 || N <= ny || nx < 0 || N <= nx) {
                        continue;
                    }
                    if (board[ny][nx] < 0 || (board[ny][nx] != 0 && board[ny][nx] != color)) {
                        continue;
                    }

                    board[ny][nx] = EMPTY;
                    pendings.add(ny * 100 + nx);
                }
            }
        }

        public int breakLargestBrickGroup() {
            final var N = board.length;
            final var visited = new int[N][N];

            var brickGroups = new ArrayList<BrickGroup>();
            var largestBrickGroupIndex = -1;
            for (var y = 0; y < N; ++y) {
                for (var x = 0; x < N; ++x) {
                    if (board[y][x] <= 0 || visited[y][x] != 0) {
                        continue;
                    }

                    visited[y][x] = brickGroups.size() + 1;
                    final var brickGroup = gatherBricks(brickGroups.size() + 1, visited, y, x);
                    if (1 < brickGroup.area) {
                        brickGroups.add(brickGroup);
                        if (largestBrickGroupIndex == -1) {
                            largestBrickGroupIndex = 1;
                            continue;
                        }

                        final var largestBrickGroup = brickGroups.get(largestBrickGroupIndex - 1);
                        if (largestBrickGroup.area < brickGroup.area) {
                            largestBrickGroupIndex = brickGroup.index;
                        } else if (largestBrickGroup.area == brickGroup.area) {
                            if (largestBrickGroup.numRainbowBricks < brickGroup.numRainbowBricks) {
                                largestBrickGroupIndex = brickGroup.index;
                                continue;
                            } else if (largestBrickGroup.numRainbowBricks == brickGroup.numRainbowBricks) {
                                if (largestBrickGroup.y < brickGroup.y) {
                                    largestBrickGroupIndex = brickGroup.index;
                                } else if (largestBrickGroup.y == brickGroup.y) {
                                    if (largestBrickGroup.x < brickGroup.x) {
                                        largestBrickGroupIndex = brickGroup.index;
                                    }
                                }
                            }
                        } // if (largestBrickGroup.area < brickGroup.area) ...
                    }
                }
            }

            if (largestBrickGroupIndex < 0) {
                return 0;
            }

            final var largestBrickGroup = brickGroups.get(largestBrickGroupIndex - 1);
            breakBrickGroup(largestBrickGroup);
            applyGravity();
            return largestBrickGroup.area;
        }

        public boolean step() {
            final var largestBrickgroupArea = breakLargestBrickGroup();
            if (0 < largestBrickgroupArea) {
                score += largestBrickgroupArea * largestBrickgroupArea;
            }

            ArrayTurner.turn(board);
            applyGravity();

            return 0 < largestBrickgroupArea;
        }

        public int getScore() {
            return score;
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {

            final int N;
            {
                final var tokens = stdin.readLine().split(" ");
                N = Integer.parseInt(tokens[0]);
            }

            final var board = new byte[N][N];
            for (var y = 0; y < N; ++y) {
                final var tokens = stdin.readLine().split(" ");
                for (var x = 0; x < N; ++x) {
                    board[y][x] = Byte.parseByte(tokens[x]);
                }
            }

            final var game = new BrickGame(board);
            while (game.step()) {
                ;
            }

            stdout.write(game.getScore() + "\n");
            stdout.flush();
        }
    }
}
