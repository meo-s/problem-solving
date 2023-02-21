// https://www.acmicpc.net/problem/6987

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BJ_6987_월드컵 {
    private static final int NUM_TEAMS = 6;

    public static class Combination {
        private List<Integer> elements;
        private int n;

        public Combination(final int n, final int r) {
            this.n = n;

            elements = new ArrayList<>();
            for (int i = 0; i < r - 1; ++i) {
                elements.add(0);
            }
            elements.add(-1);
        }

        public List<Integer> get() {
            return elements;
        }

        public boolean next() {
            if (0 < elements.size()) {
                final int tailIndex = elements.size() - 1;
                elements.set(tailIndex, elements.get(tailIndex) + 1);
                if (elements.get(tailIndex) == n) {
                    elements.remove(tailIndex);
                    if (next()) {
                        elements.add(0);
                    }
                }
            }

            return 0 < elements.size();
        }
    }

    public static class ScoreBoard {
        private static final int WIN = 0;
        private static final int DRAW = 1;
        private static final int LOSE = 2;

        public byte[][] scores = new byte[NUM_TEAMS][3];

        public boolean isValid() {
            for (int i = 0; i < NUM_TEAMS; ++i) {
                int total = 0;
                for (int j = 0; j < 3; ++j) {
                    total += scores[i][j];
                }

                if (total != NUM_TEAMS - 1) {
                    return false;
                }
            }

            return true;
        }

        public int compareTo(final ScoreBoard other) {
            int relation = 0;
            outerLoop: for (int i = 0; i < NUM_TEAMS; ++i) {
                for (int j = 0; j < 3; ++j) {
                    if (scores[i][j] < other.scores[i][j]) {
                        relation = -1;
                    } else if (other.scores[i][j] < scores[i][j]) {
                        relation = 1;
                        break outerLoop;
                    }
                }
            }

            return relation;
        }

        public static ScoreBoard from(final BufferedReader br) throws IOException {
            final ScoreBoard ret = new ScoreBoard();
            final String[] tokens = br.readLine().split(" ");
            for (int i = 0; i < NUM_TEAMS; ++i) {
                for (int j = 0; j < 3; ++j) {
                    ret.scores[i][j] = Byte.parseByte(tokens[i * 3 + j]);
                }
            }
            return ret;
        }
    }

    public static class ScoreBoardUpdater {
        @FunctionalInterface
        interface Update {
            void apply(final ScoreBoard scoreBoard, final int team1, final int team2);
        }

        public static void win(final ScoreBoard scoreBoard, final int winner, final int loser) {
            ++scoreBoard.scores[winner][ScoreBoard.WIN];
            ++scoreBoard.scores[loser][ScoreBoard.LOSE];
        }

        public static void rollbackWin(final ScoreBoard scoreBoard, final int winner, final int loser) {
            --scoreBoard.scores[winner][ScoreBoard.WIN];
            --scoreBoard.scores[loser][ScoreBoard.LOSE];
        }

        public static void lose(final ScoreBoard scoreBoard, final int loser, final int winner) {
            win(scoreBoard, winner, loser);
        }

        public static void rollbackLose(final ScoreBoard scoreBoard, final int loser, final int winner) {
            rollbackWin(scoreBoard, winner, loser);
        }

        public static void draw(final ScoreBoard scoreBoard, final int team1, final int team2) {
            ++scoreBoard.scores[team1][ScoreBoard.DRAW];
            ++scoreBoard.scores[team2][ScoreBoard.DRAW];
        }

        public static void rollbackDraw(final ScoreBoard scoreBoard, final int team1, final int team2) {
            --scoreBoard.scores[team1][ScoreBoard.DRAW];
            --scoreBoard.scores[team2][ScoreBoard.DRAW];
        }
    }

    public static class Simulator {
        public static boolean find(final ScoreBoard goal, final ScoreBoard now, final int pivot) {
            final int relation = now.compareTo(goal);
            if (0 <= relation) {
                return relation == 0;
            }

            final ScoreBoardUpdater.Update[] updater = {
                    ScoreBoardUpdater::win,
                    ScoreBoardUpdater::draw,
                    ScoreBoardUpdater::lose };
            final ScoreBoardUpdater.Update[] rollback = {
                    ScoreBoardUpdater::rollbackWin,
                    ScoreBoardUpdater::rollbackDraw,
                    ScoreBoardUpdater::rollbackLose };

            final Combination combi = new Combination(updater.length, (NUM_TEAMS - 1) - pivot);
            boolean wasFound = false;
            while (!wasFound && combi.next()) {
                for (int i = pivot + 1; i < NUM_TEAMS; ++i) {
                    updater[combi.get().get(i - (pivot + 1))].apply(now, pivot, i);
                }
                wasFound = find(goal, now, pivot + 1);
                for (int i = pivot + 1; i < NUM_TEAMS; ++i) {
                    rollback[combi.get().get(i - (pivot + 1))].apply(now, pivot, i);
                }
            }

            return wasFound;
        }

        public static boolean find(final ScoreBoard goal) {
            return find(goal, new ScoreBoard(), 0);
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            for (int i = 0; i < 4; ++i) {
                final ScoreBoard scoreBoard = ScoreBoard.from(stdin);
                final boolean wasFound = scoreBoard.isValid() && Simulator.find(scoreBoard);
                System.out.print((wasFound ? 1 : 0) + " ");
            }
        }

        System.out.print('\n');
    }
}
