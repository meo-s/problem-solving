// https://www.acmicpc.net/problem/21608

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class BJ_21608_상어_초등학교 {
    private static final int[] dy = { 1, -1, 0, 0 };
    private static final int[] dx = { 0, 0, 1, -1 };
    private static final int NUM_RELATIONS = 4;

    private static class SeatInfo {
        public int y;
        public int x;
        public int numAdjacentVacancies;
        public int numAdjacentFriends;

        public SeatInfo(final int y, final int x) {
            this.y = y;
            this.x = x;
            this.numAdjacentVacancies = 0;
            this.numAdjacentFriends = 0;
        }
    }

    public static SeatInfo evaluateSeat(int[][] room, int y, int x, int[][] relations, int student) {
        final var N = room.length;
        final var seat = new SeatInfo(y, x);
        for (var i = 0; i < dy.length; ++i) {
            final var ny = y + dy[i];
            final var nx = x + dx[i];
            if (0 <= ny && ny < N && 0 <= nx && nx < N) {
                if (room[ny][nx] == -1) {
                    ++seat.numAdjacentVacancies;
                } else {
                    for (var j = 1; j <= NUM_RELATIONS; ++j) {
                        if (relations[room[ny][nx]][0] == relations[student][j]) {
                            ++seat.numAdjacentFriends;
                            break;
                        }
                    }
                }
            }
        }

        return seat;
    }

    public static int satisfactionOf(final int[][] room, final int y, final int x, final int[][] relations) {
        final var N = room.length;
        var numAdjacentFriends = 0;
        for (var i = 0; i < dy.length; ++i) {
            final var ny = y + dy[i];
            final var nx = x + dx[i];
            if (0 <= ny && ny < N && 0 <= nx && nx < N) {
                for (var j = 1; j <= NUM_RELATIONS; ++j) {
                    if (relations[room[ny][nx]][0] == relations[room[y][x]][j]) {
                        ++numAdjacentFriends;
                    }
                }
            }
        }

        return 0 < numAdjacentFriends ? (int) Math.pow(10, numAdjacentFriends - 1) : 0;
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final var N = Integer.parseInt(stdin.readLine());
            final var relations = new int[N * N][NUM_RELATIONS + 1];
            for (var i = 0; i < N * N; ++i) {
                final var tokens = stdin.readLine().split(" ");
                for (var j = 0; j <= NUM_RELATIONS; ++j) {
                    relations[i][j] = Integer.parseInt(tokens[j]) - 1;
                }
            }

            final var room = new int[N][N];
            for (final var roomRow : room) {
                Arrays.fill(roomRow, -1);
            }

            for (var i = 0; i < N * N; ++i) {
                SeatInfo bestSeat = null;
                for (var y = 0; y < N; ++y) {
                    for (var x = 0; x < N; ++x) {
                        if (room[y][x] != -1) {
                            continue;
                        }

                        final var seat = evaluateSeat(room, y, x, relations, i);
                        if (bestSeat == null) {
                            bestSeat = seat;
                        } else {
                            if (bestSeat.numAdjacentFriends < seat.numAdjacentFriends) {
                                bestSeat = seat;
                            } else if (bestSeat.numAdjacentFriends == seat.numAdjacentFriends) {
                                if (bestSeat.numAdjacentVacancies < seat.numAdjacentVacancies) {
                                    bestSeat = seat;
                                }
                            }
                        } // if (bestSeat == null) ...
                    }
                }

                room[bestSeat.y][bestSeat.x] = i;
            }

            var totalSatisfaction = 0;
            for (var y = 0; y < N; ++y) {
                for (var x = 0; x < N; ++x) {
                    totalSatisfaction += satisfactionOf(room, y, x, relations);
                }
            }

            System.out.print(totalSatisfaction + "\n");
            stdin.close();
        }
    }
}
