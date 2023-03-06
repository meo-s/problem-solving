// https://www.acmicpc.net/problem/3025

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BJ_3025_돌_던지기 {
    private static final char EMPTY = '.';
    private static final char STONE = 'O';
    private static final char WALL = 'X';

    public static class PointStack {
        final int[][] st;
        int size;

        public PointStack(final int maxSize) {
            st = new int[maxSize][2];
            size = 0;
        }

        public int[] top() {
            return st[size - 1];
        }

        public void push(final int y, final int x) {
            st[size][0] = y;
            st[size++][1] = x;
        }

        public int[] pop() {
            return st[--size];
        }

        public int size() {
            return size;
        }
    }

    public static class Board {
        public final int H;
        public final int W;
        public final char[][] state;

        private Board(final short H, final short W) {
            this.H = H;
            this.W = W;
            this.state = new char[H][W];
        }

        public boolean isInBoard(final int y, final int x) {
            return 0 <= x && x < W && 0 <= y && y < H;
        }

        public boolean isEmpty(final int y, final int x) {
            return isInBoard(y, x) && state[y][x] == EMPTY;
        }

        public boolean isWall(final int y, final int x) {
            return isInBoard(y, x) && state[y][x] == WALL;
        }

        public boolean isStone(final int y, final int x) {
            return isInBoard(y, x) && state[y][x] == STONE;
        }

        public String toString() {
            final var sb = new StringBuilder();
            for (var y = H - 1; 0 <= y; --y) {
                for (var x = 0; x < W; ++x) {
                    sb.append(state[y][x]);
                }
                sb.append('\n');
            }

            return sb.toString();
        }

        public static Board from(final BufferedReader br) throws IOException {
            final short H, W;
            {
                final var tokens = br.readLine().split(" ");
                H = Short.parseShort(tokens[0]);
                W = Short.parseShort(tokens[1]);
            }

            final var board = new Board(H, W);
            for (var y = H - 1; 0 <= y; --y) {
                final var boardRow = br.readLine();
                for (var x = 0; x < W; ++x) {
                    board.state[y][x] = boardRow.charAt(x);
                }
            }

            return board;
        }
    }

    public static class StoneThrower {
        private final Board board;
        private final int[][] districts;
        private final List<Integer> heights;
        private final PointStack[] traces;

        public StoneThrower(final Board board) {
            this.board = board;
            this.districts = new int[board.H][board.W];
            this.heights = new ArrayList<>();

            this.traces = new PointStack[board.W];
            for (var i = 0; i < board.W; ++i) {
                this.traces[i] = new PointStack(board.H);
            }

            initHeights();
        }

        private void increaseHeightOf(final int districtIndex) {
            heights.set(districtIndex, heights.get(districtIndex) + 1);
        }

        public void initHeights() {
            for (var y = 0; y < districts.length; ++y) {
                Arrays.fill(districts[y], -1);
            }

            final var st = new ArrayDeque<Integer>();
            for (var x = 0; x < board.W; ++x) {
                for (var y = board.H - 1; 0 <= y; --y) {
                    if (board.state[y][x] == EMPTY) {
                        st.add(y);
                    }

                    if ((y - 1 < 0 || board.state[y - 1][x] == WALL) && 0 < st.size()) {
                        heights.add(st.peekLast());
                        for (var dy = 0; dy < st.size(); ++dy) {
                            districts[y + dy][x] = heights.size() - 1;
                        }

                        st.clear();
                    }
                }
            } // for (var x = 0; ...) {
        }

        public void throwStoneAt(int y, int x, final int x0) {
            final var trace = traces[x0];
            if (y == board.H - 1) {
                while (0 < trace.size() && !board.isEmpty(trace.top()[0], trace.top()[1])) {
                    trace.pop();
                }

                if (trace.size() == 0) {
                    trace.push(y, x);
                } else {
                    y = trace.top()[0];
                    x = trace.top()[1];
                }
            } else {
                trace.push(y, x);
            }

            y = heights.get(districts[y][x]);
            if (y - 1 < 0 || board.isWall(y - 1, x)) {
                board.state[y][x] = STONE;
                increaseHeightOf(districts[y][x]);
            } else {
                if (board.isEmpty(y, x - 1) && board.isEmpty(y - 1, x - 1)) {
                    throwStoneAt(y - 1, (short) (x - 1), x0);
                    return;
                }
                if (board.isEmpty(y, x + 1) && board.isEmpty(y - 1, x + 1)) {
                    throwStoneAt(y - 1, x + 1, x0);
                    return;
                }

                board.state[y][x] = STONE;
                increaseHeightOf(districts[y][x]);
            }
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final var board = Board.from(stdin);
            final var stoneThrower = new StoneThrower(board);
            var numStoneRemains = Integer.parseInt(stdin.readLine());
            while (0 <= --numStoneRemains) {
                final var x = Integer.parseInt(stdin.readLine()) - 1;
                stoneThrower.throwStoneAt((short) (board.H - 1), (short) x, (short) x);
            }

            System.out.print(board);
        }
    }
}
