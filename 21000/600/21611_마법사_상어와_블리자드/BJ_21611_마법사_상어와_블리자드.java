// https://www.acmicpc.net/problem/21611

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class BJ_21611_마법사_상어와_블리자드 {
    private static final int[] dy = { -1, 0, 1, 0 };
    private static final int[] dx = { 0, 1, 0, -1 };

    private static final byte EMPTY = 0;
    private static List<Point> snailPoints;

    public static class Point {
        public final int y;
        public final int x;

        public Point(final int y, final int x) {
            this.y = y;
            this.x = x;
        }
    }

    public static class Bead {
        public final Point pos;
        public final byte no;

        public Bead(final Point pos, final byte no) {
            this.pos = pos;
            this.no = no;
        }
    }

    public static List<Point> getSnailPoints(final int N) {
        final var pts = new ArrayList<Point>();
        var ny = N / 2;
        var nx = N / 2;
        var dir = 0;
        var len = 1;
        outerLoop: for (var i = 0;; ++i) {
            if (i != 0 && i % 2 == 0) {
                ++len;
            }

            dir = (dir + dy.length - 1) % dy.length;
            for (var j = 0; j < len; ++j) {
                ny += dy[dir];
                nx += dx[dir];
                if (nx < 0) {
                    break outerLoop;
                }

                pts.add(new Point(ny, nx));
            }
        }

        return pts;
    }

    public static void bombBeads(final byte[][] board, final int[] scores) {
        final var beads = new ArrayDeque<Bead>();
        final var count = new ArrayDeque<Integer>();

        var notBombed = false;
        while (!notBombed) {
            notBombed = true;

            for (var i = 0;; ++i) {
                for (; i < snailPoints.size(); ++i) {
                    final var pt = snailPoints.get(i);
                    if (board[pt.y][pt.x] != EMPTY) {
                        break;
                    }
                }

                final var pt = (i < snailPoints.size() ? snailPoints.get(i) : null);
                if (0 < beads.size() && (pt == null || beads.peekLast().no != board[pt.y][pt.x])) {
                    if (4 <= count.peekLast()) {
                        notBombed = false;

                        for (var j = count.peekLast(); 0 < j; --j) {
                            ++scores[beads.peekLast().no - 1];
                            count.pollLast();
                            beads.pollLast();
                        }
                    }

                    if (pt != null) {
                        count.add(1);
                        beads.add(new Bead(pt, board[pt.y][pt.x]));
                    }
                } else if (pt != null) {
                    count.add(0 < beads.size() && beads.peekLast().no == board[pt.y][pt.x] ? count.peekLast() + 1 : 1);
                    beads.add(new Bead(pt, board[pt.y][pt.x]));
                }

                if (pt == null) {
                    break;
                }
            }

            for (var i = 0; i < snailPoints.size(); ++i) {
                final var pt = snailPoints.get(i);
                board[pt.y][pt.x] = (0 < beads.size() ? beads.pollFirst().no : EMPTY);
            }

            count.clear();
        }
    }

    public static void reproduceBeads(final byte[][] board) {
        final var beads = new ArrayDeque<Bead>();
        final var count = new ArrayDeque<Integer>();

        for (var i = 0; i < snailPoints.size(); ++i) {
            final var pt = snailPoints.get(i);
            if (board[pt.y][pt.x] == EMPTY) {
                break;
            }

            final var beadNo = board[pt.y][pt.x];
            count.addLast(0 < beads.size() && beads.peekLast().no == beadNo ? count.peekLast() + 1 : 1);
            beads.addLast(new Bead(pt, beadNo));
        }

        for (var i = 0; i < snailPoints.size();) {
            if (beads.size() == 0) {
                final var pt = snailPoints.get(i++);
                board[pt.y][pt.x] = EMPTY;
            } else {
                final var bead = beads.pollFirst();
                var groupSize = count.pollFirst();
                while (0 < count.size() && groupSize + 1 == count.peekFirst()) {
                    beads.pollFirst();
                    groupSize = count.pollFirst();
                }

                for (final var v : new byte[] { groupSize.byteValue(), (byte) bead.no }) {
                    if (i < snailPoints.size()) {
                        final var pt = snailPoints.get(i++);
                        board[pt.y][pt.x] = v;
                    }
                }
            }
        }
    }

    public static int redirect(final int direction) {
        switch (direction) {
            case 1:
                return 0;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 1;
        }
        return -1;
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int N, M;
            {
                final var tokens = stdin.readLine().split(" ");
                N = Integer.parseInt(tokens[0]);
                M = Integer.parseInt(tokens[1]);
                snailPoints = getSnailPoints(N);
            }

            final var board = new byte[N][N];
            for (var y = 0; y < N; ++y) {
                final var tokens = stdin.readLine().split(" ");
                for (var x = 0; x < N; ++x) {
                    board[y][x] = Byte.parseByte(tokens[x]);
                }
            }

            final var scores = new int[3];
            for (var i = 0; i < M; ++i) {
                final var tokens = stdin.readLine().split(" ");
                final var dir = redirect(Integer.parseInt(tokens[0]));
                final var radius = Integer.parseInt(tokens[1]);
                final var y = N / 2;
                final var x = N / 2;
                for (var dt = 1; dt <= radius; ++dt) {
                    board[y + dy[dir] * dt][x + dx[dir] * dt] = EMPTY;
                }

                bombBeads(board, scores);
                reproduceBeads(board);
            }

            final var score = scores[0] + 2 * scores[1] + 3 * scores[2];
            System.out.print(score + "\n");
        }
    }
}
