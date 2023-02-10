// https://www.acmicpc.net/problem/1941

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BJ_1941_소문난_칠공주 {
    private static final int H = 5;
    private static final int W = 5;

    private static final int[] dy = { 1, -1, 0, 0 };
    private static final int[] dx = { 0, 0, 1, -1 };

    private static final int MAX_GROUP_SIZE = 7;
    private static final int MIN_DASOMS = 4; 

    public static class Visitmask {
        public static int on(final int bitmask, final int y, final int x) {
            return bitmask | (1 << (H * y + x));
        }

        public static int off(final int bitmask, final int y, final int x) {
            return bitmask & ~(1 << (H * y + x));
        }

        public static boolean isVisited(final int bitmask, final int flatYX) {
            return (bitmask & (1 << flatYX)) != 0;
        }
    }

    public static class SevenPrincess {
        private static final ArrayList<Integer> dasoms = new ArrayList<>();
        private static final Set<Integer> validGroups = new HashSet<>();
        private static final Set<Integer> canGrouping = new HashSet<>();
        private static final int[] leverages = new int[H * W];

        public static void settle(final int y, final int x) {
            for (var i = 0; i < dy.length; ++i) {
                if (y + dy[i] < 0 || H <= y + dy[i] || x + dx[i] < 0 || W <= x + dx[i]) {
                    continue;
                }

                final var flatYX = (y + dy[i]) * H + x + dx[i];
                if (leverages[flatYX]++ == 0) {
                    canGrouping.add(flatYX);
                }
            }
        }

        public static void leave(final int y, final int x) {
            for (var i = 0; i < dy.length; ++i) {
                if (y + dy[i] < 0 || H <= y + dy[i] || x + dx[i] < 0 || W <= x + dx[i]) {
                    continue;
                }

                final var flatYX = (y + dy[i]) * H + x + dx[i];
                if (--leverages[flatYX] == 0) {
                    canGrouping.remove(flatYX);
                }
            }
        }

        private static boolean isValidGroup(final int visitmask) {
            var numDasoms = 0;
            for (final var dasom : dasoms) {
                if (Visitmask.isVisited(visitmask, dasom)) {
                    if (MIN_DASOMS <= ++numDasoms) {
                        return true;
                    }
                }
            }

            return false;
        }

        private static void search(final int visitmask, final int depth) {
            if (depth == MAX_GROUP_SIZE) {
                if (isValidGroup(visitmask)) {
                    validGroups.add(visitmask);
                }
                return;
            }

            for (final var flatYX : canGrouping.toArray(new Integer[canGrouping.size()])) {
                if (Visitmask.isVisited(visitmask, flatYX)) {
                    continue;
                }

                final var y = flatYX / H;
                final var x = flatYX % H;
                settle(y, x);
                search(Visitmask.on(visitmask, y, x), depth + 1);
                leave(y, x);
            }
        }

        public static int gather(final char[][] room) {
            dasoms.clear();
            validGroups.clear();
            canGrouping.clear();
            Arrays.fill(leverages, 0);

            for (var y = 0; y < H; ++y) {
                for (var x = 0; x < W; ++x) {
                    if (room[y][x] == 'S') {
                        dasoms.add(y * H + x);
                    }
                }
            }

            for (final var flatYX : dasoms) {
                final int y = flatYX / H;
                final int x = flatYX % H;
                settle(y, x);
                search(Visitmask.on(0, y, x), 1);
                leave(y, x);
            }

            return validGroups.size();
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final var room = new char[H][W];
            for (var y = 0; y < H; ++y) {
                final var line = stdin.readLine();
                for (var x = 0; x < W; ++x) {
                    room[y][x] = line.charAt(x);
                }
            }

            stdout.write(SevenPrincess.gather(room) + "\n");
            stdout.flush();
        }
    }
}