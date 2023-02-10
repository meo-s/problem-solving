// https://www.acmicpc.net/problem/12100

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BJ_12100_2048_Easy {

    public static class Board {
        private Board(final int boardSize) {
            this.boardSize = boardSize;
            this.state = new int[boardSize][boardSize];
        }

        public Board clone() {
            final var clone = new Board(boardSize);
            for (var i = 0; i < boardSize; ++i) {
                System.arraycopy(state[i], 0, clone.state[i], 0, boardSize);
            }

            return clone;
        }

        public int getMaximum() {
            var maximum = 0;
            for (final var stateRow : state) {
                for (final var n : stateRow) {
                    maximum = Math.max(maximum, n);
                }
            }

            return maximum;
        }

        public int moveLeft() {
            final var freezed = new boolean[boardSize];
            var numMovements = 0;

            for (var y = 0; y < boardSize; ++y) {
                Arrays.fill(freezed, false);
                for (var xi = 1; xi < boardSize; ++xi) {
                    for (var x = xi; 0 < x; --x) {
                        if (state[y][x - 1] == 0 || state[y][x - 1] == state[y][x]) {
                            if (state[y][x - 1] != 0) {
                                if (freezed[x] || freezed[x - 1]) {
                                    continue;
                                }
                                freezed[x] = true;
                            }

                            freezed[x - 1] = freezed[x];
                            freezed[x] = false;
                            state[y][x - 1] += state[y][x];
                            state[y][x] = 0;
                            ++numMovements;
                        }
                    }
                }
            }

            return numMovements;
        }

        public int moveRight() {
            final var freezed = new boolean[boardSize];
            var numMovements = 0;

            for (var y = 0; y < boardSize; ++y) {
                Arrays.fill(freezed, false);
                for (var xi = boardSize - 2; 0 <= xi; --xi) {
                    for (var x = xi; x < boardSize - 1; ++x) {
                        if (state[y][x + 1] == 0 || state[y][x + 1] == state[y][x]) {
                            if (state[y][x + 1] != 0) {
                                if (freezed[x] || freezed[x + 1]) {
                                    continue;
                                }
                                freezed[x] = true;
                            }

                            freezed[x + 1] = freezed[x];
                            freezed[x] = false;
                            state[y][x + 1] += state[y][x];
                            state[y][x] = 0;
                            ++numMovements;
                        }
                    }
                }
            }

            return numMovements;
        }

        public int moveUp() {
            final var freezed = new boolean[boardSize];
            var numMovements = 0;

            for (var x = 0; x < boardSize; ++x) {
                Arrays.fill(freezed, false);
                for (var yi = 1; yi < boardSize; ++yi) {
                    for (var y = yi; 0 < y; --y) {
                        if (state[y - 1][x] == 0 || state[y - 1][x] == state[y][x]) {
                            if (state[y - 1][x] != 0) {
                                if (freezed[y] || freezed[y - 1]) {
                                    continue;
                                }
                                freezed[y] = true;
                            }

                            freezed[y - 1] = freezed[y];
                            freezed[y] = false;
                            state[y - 1][x] += state[y][x];
                            state[y][x] = 0;
                            ++numMovements;
                        }
                    }
                }
            }

            return numMovements;
        }

        public int moveDown() {
            final var freezed = new boolean[boardSize];
            var numMovements = 0;

            for (int x = 0; x < boardSize; ++x) {
                Arrays.fill(freezed, false);
                for (int yi = boardSize - 2; 0 <= yi; --yi) {
                    for (int y = yi; y < boardSize - 1; ++y) {
                        if (state[y + 1][x] == 0 || state[y + 1][x] == state[y][x]) {
                            if (state[y + 1][x] != 0) {
                                if (freezed[y] || freezed[y + 1]) {
                                    continue;
                                }
                                freezed[y] = true;
                            }

                            freezed[y + 1] = freezed[y];
                            freezed[y] = false;
                            state[y + 1][x] += state[y][x];
                            state[y][x] = 0;
                            ++numMovements;
                        }
                    }
                }
            }

            return numMovements;
        }

        public int move(int direction) {
            switch (direction) {
                case 0:
                    return moveUp();
                case 1:
                    return moveDown();
                case 2:
                    return moveLeft();
                case 3:
                    return moveRight();
                default:
                    return 0;
            }
        }

        public static Board from(final BufferedReader br) throws IOException {
            final var N = Integer.parseInt(br.readLine());
            final var board = new Board(N);
            for (var y = 0; y < N; ++y) {
                final var boardRow = new StringTokenizer(br.readLine());
                for (var x = 0; x < N; ++x) {
                    board.state[y][x] = Integer.parseInt(boardRow.nextToken());
                }
            }

            return board;
        }

        private int boardSize;
        private int[][] state;
    }

    public static int dfs(final Board board, final int depth) {
        if (depth == 5) {
            return board.getMaximum();
        }

        var globalMaximum = 0;
        for (var boardOpIndex = 0; boardOpIndex < 4; ++boardOpIndex) {
            final var nextBoard = board.clone();
            if (0 < nextBoard.move(boardOpIndex)) {
                globalMaximum = Math.max(globalMaximum, dfs(nextBoard, depth + 1));
            } else {
                globalMaximum = Math.max(globalMaximum, nextBoard.getMaximum());
            }
        }

        return globalMaximum;
    }

    public static int dfs(final Board board) {
        return dfs(board, 0);
    }

    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        stdout.write(dfs(Board.from(stdin)) + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
