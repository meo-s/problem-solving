// https://www.acmicpc.net/problem/15644

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;


public class BJ_15644 {
    public static class Point2<T> {
        public Point2(final T y_, final T x_) {
            y = y_;
            x = x_;
        }

        public String toString() {
            return String.format("%d,%d;", y, x);
        }

        public T y;
        public T x;
    }

    public static class Ball extends Point2<Integer> {
        public Ball(final int y, final int x) {
            super(y, x);
        }

        public Ball clone() {
            return new Ball(y, x);
        }
    }

    public static class Pair<T, U> {
        public Pair(final T one_, final U two_) {
            one = one_;
            two = two_;
        }

        public T one;
        public U two;
    }

    public static class BoardState extends Pair<Integer, Pair<Ball, Ball>> {
        public BoardState(final int turnIndex, Pair<Ball, Ball> ballState) {
            super(turnIndex, ballState);
            log = "";
        }

        private boolean isBallGoalIn(final char[][] board, Ball ball) {
            return board[ball.y][ball.x] == 'O';
        }

        private void moveBall(final char[][] board, Ball ball, Ball other, final int dy, final int dx) {
            final var H = board.length;
            final var W = board[0].length;

            while (board[ball.y][ball.x] != 'O') {
                if (ball.y + dy < 0 || H <= ball.y + dy || ball.x + dx < 0 || W <= ball.x + dx) {
                    break;
                }

                if (board[ball.y + dy][ball.x + dx] == '#') {
                    break;
                }

                if (!isBallGoalIn(board, other) && ball.y + dy == other.y && ball.x + dx == other.x) {
                    break;
                }

                ball.y += dy;
                ball.x += dx;
            }
        }

        public boolean isRedBallGoalIn(final char[][] board) {
            return isBallGoalIn(board, two.one);
        }

        public boolean isBlueBallGoalIn(final char[][] board) {
            return isBallGoalIn(board, two.two);
        }

        public BoardState tilt(final char[][] board, final int dy, final int dx) {
            switch (dy * 10 + dx) {
                case -10:
                    log += "U";
                    break;
                case +10:
                    log += "D";
                    break;
                case -1:
                    log += "L";
                    break;
                case +1:
                    log += "R";
                    break;
            }

            moveBall(board, two.one, two.two, dy, dx);
            moveBall(board, two.two, two.one, dy, dx);
            moveBall(board, two.one, two.two, dy, dx);
            return this;
        }

        public BoardState clone() {
            final var newState = new BoardState(one, new Pair<>(two.one.clone(), two.two.clone()));
            newState.log = this.log;
            return newState;
        }

        public String toString() {
            return two.one.toString() + two.two.toString();
        }

        public String log;
    }

    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        int H, W;
        {
            final var tokens = new StringTokenizer(stdin.readLine());
            H = Integer.parseInt(tokens.nextToken());
            W = Integer.parseInt(tokens.nextToken());
        }

        final var balls = new HashMap<Integer, Ball>();
        final var board = new char[H][W];
        for (var h = 0; h < H; ++h) {
            final var row = stdin.readLine();
            for (var w = 0; w < W; ++w) {
                switch (row.charAt(w)) {
                    case 'R':
                    case 'B':
                        balls.put((int) row.charAt(w), new Ball(h, w));

                    default:
                        board[h][w] = row.charAt(w);
                }
            }
        }

        BoardState clearState = null;
        final var dy = new int[] { 1, -1, 0, 0 };
        final var dx = new int[] { 0, 0, 1, -1 };
        final var visited = new HashSet<String>();
        final var pendings = new ArrayDeque<BoardState>();
        pendings.add(new BoardState(0, new Pair<>(balls.get((int) 'R'), balls.get((int) 'B'))));
        while (clearState == null && 0 < pendings.size()) {
            final var rootState = pendings.pollFirst();
            if (10 <= rootState.one) {
                break;
            }

            for (var i = 0; i < dy.length; ++i) {
                final var newState = rootState.clone().tilt(board, dy[i], dx[i]);
                newState.one += 1;

                final var newStateKey = newState.toString();
                if (!visited.contains(newStateKey)) {
                    visited.add(newStateKey);

                    if (!newState.isBlueBallGoalIn(board)) {
                        if (newState.isRedBallGoalIn(board)) {
                            clearState = newState;
                            break;
                        }

                        pendings.addLast(newState);
                    }
                }
            }
        }

        if (clearState == null) {
            stdout.write("-1\n");
        }
        else {
            stdout.write(clearState.one + "\n");
            stdout.write(clearState.log + "\n");
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
